package com.manacher.hammer.fragments;

import static android.app.Activity.RESULT_OK;

import static com.manacher.hammer.Utils.Util.getResizedBitmap;


import android.app.Activity;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.manacher.fireauthservice.FireAuthService;
import com.manacher.firestorageservice.FireStorageService;
import com.manacher.hammer.Activities.LoginActivity;
import com.manacher.hammer.Activities.SplashActivity;
import com.manacher.hammer.R;
import com.manacher.hammer.Utils.Util;
import com.manacher.hammer.models.User;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import java.io.InputStream;

import java.util.Objects;

public class SetupProfileFragment extends Fragment {

    private Activity activity;
    private ImageView dp;
    private EditText editText;
    private EditText age;
    private FireAuthService fireAuthService;
    private FireStorageService storageService;
    private Button continueButton;

    private ProgressBar progressBar;
    private TextView pleaseWait;

    private Button getImageButton;

    private boolean isMen = true;

    private CheckBox maleCheck;
    private CheckBox femaleCheck;
    private CheckBox otherCheck;

    private String gender;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setup_profile, container, false);
        this.initialized(view);
        this.listener();
        return view;
    }

    private void initialized(View view) {
        activity = getActivity();

        fireAuthService = new FireAuthService();
        storageService = new FireStorageService();

        dp = view.findViewById(R.id.profileImage);
        continueButton = view.findViewById(R.id.setContinue);
        editText = view.findViewById(R.id.editText);
        progressBar = view.findViewById(R.id.progressBar);
        pleaseWait = view.findViewById(R.id.pleaseWait);

        getImageButton = view.findViewById(R.id.get_button);

        maleCheck = view.findViewById(R.id.maleCheck);
        femaleCheck = view.findViewById(R.id.femaleCheck);
        otherCheck = view.findViewById(R.id.otherCheck);

        age = view.findViewById(R.id.age);
    }


    private void listener() {
        getImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCropper();
            }
        });

        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCropper();
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                continueButton.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                pleaseWait.setVisibility(View.VISIBLE);

                updateFireUser();
                continueButton.setEnabled(false);
            }
        });

        maleCheck.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                if (maleCheck.isChecked()) {
                    isMen = true;
                    gender = getString(R.string.male);
                    femaleCheck.setChecked(false);
                    otherCheck.setChecked(false);

                } else if (femaleCheck.isChecked()) {
                    isMen = false;
                    gender = getString(R.string.female);
                    maleCheck.setChecked(false);
                    otherCheck.setChecked(false);

                } else if (otherCheck.isChecked()) {
                    isMen = true;
                    gender = getString(R.string.other);
                    femaleCheck.setChecked(false);
                    maleCheck.setChecked(false);

                }
            }

            @Override
            public void onViewDetachedFromWindow(View v) {

            }
        });

        femaleCheck.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                if (maleCheck.isChecked()) {
                    isMen = true;
                    gender = getString(R.string.male);
                    femaleCheck.setChecked(false);
                    otherCheck.setChecked(false);

                } else if (femaleCheck.isChecked()) {
                    isMen = false;
                    gender = getString(R.string.female);
                    maleCheck.setChecked(false);
                    otherCheck.setChecked(false);

                } else if (otherCheck.isChecked()) {
                    isMen = true;
                    gender = getString(R.string.other);
                    femaleCheck.setChecked(false);
                    maleCheck.setChecked(false);

                }
            }

            @Override
            public void onViewDetachedFromWindow(View v) {

            }
        });

        otherCheck.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                if (maleCheck.isChecked()) {
                    isMen = true;
                    gender = getString(R.string.male);
                    femaleCheck.setChecked(false);
                    otherCheck.setChecked(false);

                } else if (femaleCheck.isChecked()) {
                    isMen = false;
                    gender = getString(R.string.female);
                    maleCheck.setChecked(false);
                    otherCheck.setChecked(false);

                } else if (otherCheck.isChecked()) {
                    isMen = true;
                    gender = getString(R.string.other);
                    femaleCheck.setChecked(false);
                    maleCheck.setChecked(false);

                }
            }

            @Override
            public void onViewDetachedFromWindow(View v) {

            }
        });
    }

    private void openCropper() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .setFixAspectRatio(true)
                .start(activity, this);

    }

    private void updateFireUser() {
        String name = String.valueOf(editText.getText());
        String ageText = String.valueOf(age.getText());

        if (name.isEmpty()) {
            editText.setError("enter your name");
            return;
        }

        if (ageText.isEmpty()) {
            age.setError("enter your age");
            return;
        }

        User user = new User();
        user.setName(name);
        user.setDpUrl(isMen ? (Util.MEN_DP) : (Util.WOMEN_DP));
        user.setGender(gender);
        user.setAge(Integer.parseInt(ageText));
        user.setCountry(SplashActivity.COUNTRY);
        user.setChips(SplashActivity.APP_INFO.getDefaultChips());

        Util.USER = user;

        ((LoginActivity) activity).authentication.updateFireUser(name, isMen ? Uri.parse(Util.MEN_DP) : Uri.parse(Util.MEN_DP));
    }

    private void uploadImage(Bitmap bitmap) {
        pleaseWait.setVisibility(View.VISIBLE);
        continueButton.setEnabled(false);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if (bitmap != null) {
            bitmap = getResizedBitmap(bitmap, 1000);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        }

        byte[] byteArray = stream.toByteArray();
        storageService.uploadFile("profilePictures", fireAuthService.getUserId(), byteArray)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {

                            task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    continueButton.setEnabled(true);
                                    pleaseWait.setVisibility(View.GONE);
                                    if (isMen) {
                                        Util.MEN_DP = String.valueOf(uri);
                                    } else {
                                        Util.WOMEN_DP = String.valueOf(uri);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(activity, "Something went wrong, Please try again",
                                    Toast.LENGTH_LONG).show();
                            FirebaseAuth.getInstance().signOut();
                        }
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        progressBar.setProgress((int) progress);

                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Uri resultUri = null;
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
            try {

                InputStream inputStream = null;
                if (data != null && resultUri != null) {
                    inputStream = activity.getContentResolver().openInputStream(Objects.requireNonNull(resultUri));
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    dp.setImageBitmap(bitmap);
                    continueButton.setVisibility(View.VISIBLE);
                    uploadImage(bitmap);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}