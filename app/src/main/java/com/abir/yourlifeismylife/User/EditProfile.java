package com.abir.yourlifeismylife.User;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.abir.yourlifeismylife.DataModels.UserDataModel;
import com.abir.yourlifeismylife.R;
import com.abir.yourlifeismylife.Utils.AESCrypt;
import com.abir.yourlifeismylife.Utils.Common;
import com.abir.yourlifeismylife.Utils.CustomProgress;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class EditProfile extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, View.OnClickListener {

    Button mUpdate;
    EditText mFName, mLName, mEmail, mPhoneNumber, mPassword, mConfirmPassword;

    final static int Gallery_Pick = 1;
    CircleImageView mProfileImage;
    TextView mUploadImage;

    String currentUserID, image = "", profileImageURL;

    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference UsersRef;
    StorageReference mUserProfileImageRef;
    AuthCredential credential;
    UserDataModel dataModel;

    CustomProgress mCustomProgress = CustomProgress.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initViews();

    }

    private void initViews() {

        mCustomProgress = CustomProgress.getInstance();

        mUpdate = findViewById(R.id.update_data_btn);
        mUpdate.setOnClickListener(this);

        //Edit Text
        mFName = findViewById(R.id.signup_first_name_et);
        mLName = findViewById(R.id.signup_last_name_et);
        mEmail = findViewById(R.id.signup_email_et);
        mPhoneNumber = findViewById(R.id.signup_phone_et);
        mPassword = findViewById(R.id.signup_password_et);
        mConfirmPassword = findViewById(R.id.signup_confirm_password_et);

        mProfileImage = findViewById(R.id.user_image);
        mUploadImage = findViewById(R.id.choose_image_text);
        mProfileImage.setOnClickListener(this);
        mUploadImage.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        currentUserID = user.getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference(Common.USERS_INFORMATION).child(currentUserID);
        mUserProfileImageRef = FirebaseStorage.getInstance().getReference().child("UserImages");

        getData();


    }


    private void getData() {
        mCustomProgress.showProgress(this, "Please Wait... Loading!!!", true);


        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    dataModel = snapshot.getValue(UserDataModel.class);
                    mFName.setText(dataModel.getFirstName());
                    mLName.setText(dataModel.getLastName());
                    mEmail.setText(dataModel.getEmail());
                    mEmail.setEnabled(false);
                    mPhoneNumber.setText(dataModel.getPhoneNumber());
                    try {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (image.equals("none") || image.equals("") || image == null)
                                    Glide.with(getApplicationContext()).load(R.drawable.user).into(mProfileImage);
                                else
                                    Glide.with(getApplicationContext()).load(image).into(mProfileImage);
                                mCustomProgress.hideProgress();
                            }
                        }, 100);
                    } catch (Exception e) {
                        Log.e("EditProfile", "onDataChange: getData : " + e.getMessage());
                    }
                    mCustomProgress.hideProgress();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void saveData() {
        final String newPassword = mConfirmPassword.getText().toString();
        String oldPassword = mPassword.getText().toString();

        mCustomProgress.showProgress(this, "Please Wait... Loading!!!", true);


        if (!(oldPassword.equals("")) && !(newPassword.equals(""))) {
            final String email = user.getEmail();
            credential = EmailAuthProvider.getCredential(email, oldPassword);

            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        updatePassword(newPassword);

                    } else {
                        String message = task.getException().toString();
                        Toast.makeText(EditProfile.this, "Cannot change Password, The old password is incorrect", Toast.LENGTH_LONG).show();
                        mCustomProgress.hideProgress();
                        Log.e("EditProfile", "onComplete: Error Occurred On Changing Password " + message);
                    }
                }
            });
        } else {
            UsersRef.child("firstName").setValue(mFName.getText().toString());
            UsersRef.child("lastName").setValue(mLName.getText().toString());
            UsersRef.child("phoneNumber").setValue(mPhoneNumber.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mCustomProgress.hideProgress();
                                Toast.makeText(EditProfile.this, "Data Updated!!", Toast.LENGTH_SHORT).show();
                                finish();
                                Log.e("EditProfile", "onComplete: Done Saving Data !");
                            } else
                                Log.e("EditProfile", "onComplete: Error on Saving Data " + task.getException().toString());
                        }
                    });
        }


    }


    private void updatePassword(final String newPassword) {

        String passwordEncrypted;
        try {
            passwordEncrypted = AESCrypt.encrypt(newPassword);
        } catch (Exception e) {
            passwordEncrypted = newPassword;
            Log.e("EditProfile", "updatePassword: " + e.getMessage());
        }


        final String finalPasswordEncrypted = passwordEncrypted;
        user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    UsersRef.child("password").setValue(finalPasswordEncrypted);
                    Toast.makeText(EditProfile.this, "Password Changed!!", Toast.LENGTH_SHORT).show();
                    UsersRef.child("firstName").setValue(mFName.getText().toString());
                    UsersRef.child("lastName").setValue(mLName.getText().toString());
                    UsersRef.child("phoneNumber").setValue(mPhoneNumber.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        mCustomProgress.hideProgress();
                                        Toast.makeText(EditProfile.this, "Data Updated!!", Toast.LENGTH_SHORT).show();
                                        finish();
                                        Log.e("EditProfile", "onComplete: Done Saving Data !");
                                    } else
                                        Log.e("EditProfile", "onComplete: Error on Saving Data " + task.getException().toString());
                                }
                            });
                    Log.e("EditProfile", "onComplete: Change Password Successfully");
                } else {
                    Log.e("EditProfile", "onComplete: Failed To change Password ");
                }
            }
        });
    }

    @AfterPermissionGranted(101)
    private void choosePhotoFromGallery() {
        String[] galleryPermission = new String[0];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            galleryPermission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                    , Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (EasyPermissions.hasPermissions(this, galleryPermission)) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, Gallery_Pick);
        } else {
            EasyPermissions.requestPermissions(this, "Access for Storage",
                    101, galleryPermission);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        {
            if (grantResults.length > 0) {
                if (grantResults.toString().equals(Gallery_Pick)) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, Gallery_Pick);
                }
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == Gallery_Pick && resultCode == RESULT_OK && data != null) {
            Uri ImageUri = data.getData();

            CropImage.activity(ImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                mCustomProgress.showProgress(this, "Uploading your image...", true);


                Uri resUri = result.getUri();

                final StorageReference filePath = mUserProfileImageRef.child(currentUserID + ".jpg");

                final UploadTask uploadTask = filePath.putFile(resUri);


                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String message = e.toString();
                        Toast.makeText(EditProfile.this, "Error Occurred : " + message, Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(EditProfile.this, "Updated Successfully..", Toast.LENGTH_SHORT).show();
                        Log.e("EditProfile", "Uploaded Successfully..");

                        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }
                                profileImageURL = filePath.getDownloadUrl().toString();
                                return filePath.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    profileImageURL = task.getResult().toString();

                                    UsersRef.child("profileImage").setValue(profileImageURL)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.e("Profile Image", "Profile url is " + profileImageURL);
                                                        Picasso.with(EditProfile.this).load(profileImageURL).placeholder(R.drawable.user).into(mProfileImage);
                                                    } else {
                                                        String message = task.getException().getMessage();
                                                        Toast.makeText(EditProfile.this, "Error Occurred" + message, Toast.LENGTH_SHORT).show();
                                                    }
                                                    mCustomProgress.hideProgress();
                                                }
                                            });

                                    Log.e("EditProfile", "The url Image Saved ..");
                                }
                            }
                        });

                    }
                });

            } else {
                Toast.makeText(EditProfile.this, "Error Occurred : image cannot be cropped. Try Again ", Toast.LENGTH_SHORT).show();
                mCustomProgress.hideProgress();
            }


        }
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.update_data_btn:
                if (!mPassword.getText().toString().isEmpty() && !mConfirmPassword.getText().toString().isEmpty()) {
                    if (mPassword.getText().toString().length() < 8) {
                        mPassword.setError("Password < 8");
                        Toast.makeText(this, "Your password cannot be less than 8 characters", Toast.LENGTH_LONG).show();
                    } else if (mConfirmPassword.getText().toString().length() < 8) {
                        mConfirmPassword.setError("Password < 8");
                        Toast.makeText(this, "Your password cannot be less than 8 characters", Toast.LENGTH_LONG).show();
                    } else {
                        saveData();
                    }
                } else {
                    saveData();
                }
                break;

            case R.id.choose_image_text:
            case R.id.user_image:
                choosePhotoFromGallery();
                break;
        }
    }


}