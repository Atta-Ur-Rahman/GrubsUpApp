package com.techease.grubsup.views.ui.signup;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.techease.grubsup.R;
import com.techease.grubsup.utils.GeneralUtills;
import com.techease.grubsup.views.activities.PreferencesActivity;
import com.techease.grubsup.views.ui.login.LoginFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;

public class SignUpFragment extends Fragment implements View.OnClickListener {


    private SignUpViewModel mViewModel;

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }


    View parentView;

    final int CAMERA_CAPTURE = 1;
    final int RESULT_LOAD_IMAGE = 2;

    public static File sourceFile = null;
    boolean valid = true;


    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;


    //    @BindView(R.id.iv_back)
//    ImageView ivBack;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_email)
    EditText etEmailAddress;

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_confirm_password)
    EditText etConfirmPassword;

    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_create)
    TextView tvCreate;

    @BindView(R.id.profile_image)
    CircleImageView ivProfile;
    Dialog alertDialog;

    public static String strFullName, strEmailAddress, strPhone, strPassword, strConfirmPassword, strDeviceType, strDeviceToken;


    @BindView(R.id.radioRemember)
    RadioButton radioRemember;

    public static boolean isRemember;

    @SuppressLint("HardwareIds")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.sign_up_fragment, container, false);
        ButterKnife.bind(this, parentView);


        strDeviceToken = Settings.Secure.getString(Objects.requireNonNull(getActivity()).getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        ivProfile.setOnClickListener(this);
        tvCreate.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
//        ivBack.setOnClickListener(this);

        GeneralUtills.putBooleanValueInEditor(getActivity(), "setting_screen", false);


        radioRemember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRemember == true) {
                    radioRemember.setChecked(false);
                    isRemember = false;

                } else {
                    radioRemember.setChecked(true);
                    isRemember = true;

                }
            }
        });


        return parentView;
    }

    private boolean validate() {

        valid = true;
        strFullName = etName.getText().toString();
        strEmailAddress = etEmailAddress.getText().toString();
        strPhone = etPhone.getText().toString();
        strPassword = etPassword.getText().toString();
        strConfirmPassword = etConfirmPassword.getText().toString();


        if (strFullName.isEmpty()) {
            etName.setError(getResources().getString(R.string.enter_vlid_name));
            valid = false;
        }

        if (strEmailAddress.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(strEmailAddress).matches()) {
            etEmailAddress.setError(getResources().getString(R.string.enter_valid_email));
            valid = false;
        }
        if (strPhone.isEmpty()) {
            etPhone.setError(getResources().getString(R.string.enter_valid_phone));
            valid = false;
        }
        if (strPassword.isEmpty() || strPassword.length() < 6 || strPassword.length() > 12) {
            etPassword.setError(getResources().getString(R.string.password_between));
            valid = false;
        } else if (!strPassword.equals(strConfirmPassword)) {
            etConfirmPassword.setError(getResources().getString(R.string.password_dosnot_match));
            valid = false;
        }

        return valid;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
        // TODO: Use the ViewModel
    }

    public void cameraBuilder() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Open");
        String[] pictureDialogItems = {
                "\tGallery",
                "\tCamera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                galleryIntent();

                                break;
                            case 1:
                                cameraIntent();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void cameraIntent() {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(captureIntent, CAMERA_CAPTURE);
    }

    public void galleryIntent() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && null != data) {
            Uri selectedImageUri = data.getData();
            String imagepath = getPath(selectedImageUri);
            sourceFile = new File(imagepath);
            try {
                sourceFile = new Compressor(getActivity()).compressToFile(sourceFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
//            updateProfile();

        } else if (resultCode == RESULT_OK && requestCode == CAMERA_CAPTURE && data != null) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();

            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            sourceFile = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");
            FileOutputStream fo;
            try {
                sourceFile.createNewFile();
//
                fo = new FileOutputStream(sourceFile);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                sourceFile = new Compressor(getActivity()).compressToFile(sourceFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
//            tvUploadPic.setTextColor(Color.RED);
//            tvUploadPic.setText("Change Picture");
            ivProfile.setImageBitmap(thumbnail);
//            updateProfile();
        }

    }

    @SuppressLint("SetTextI18n")
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(projection[0]);
        String filePath = cursor.getString(columnIndex);
        ivProfile.setImageBitmap(BitmapFactory.decodeFile(filePath));

//        tvUploadPic.setTextColor(Color.RED);
//        tvUploadPic.setText("Change Picture");
        return cursor.getString(column_index);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.profile_image:

                // Check whether this app has write external storage permission or not.
                int writeExternalStoragePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                // If do not grant write external storage permission.
                if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    // Request user to grant write external storage permission.
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
                }
                cameraBuilder();

                break;

            case R.id.tv_create:

                if (validate()) {
//                    if (sourceFile == null) {
//                        Toast.makeText(getActivity(), "upload profile picture", Toast.LENGTH_SHORT).show();
//                    } else {

                    GeneralUtills.putBooleanValueInEditor(getActivity(), "isSetting", false);
                    GeneralUtills.putStringValueInEditor(getActivity(), "adultsSize", "1");
                    GeneralUtills.putStringValueInEditor(getActivity(), "kidsUnder14Size", "1");

                    getActivity().startActivity(new Intent(getActivity(), PreferencesActivity.class));
//                    }

                }
                break;

            case R.id.tv_login:

                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, LoginFragment.newInstance())
                        .commitNow();

                break;
//            case R.id.iv_back:
//                GeneralUtills.withOutBackStackConnectFragment(getActivity(), new LoginSignupFragment());
        }
    }


}
