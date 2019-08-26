package com.techease.grubsup.views.fragments.profileFragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.techease.grubsup.R;
import com.techease.grubsup.model.getProfileModel.GetProfileResponseModel;
import com.techease.grubsup.model.profileUpdateModel.ProfileUpdateResponseModel;
import com.techease.grubsup.networking.APIClient;
import com.techease.grubsup.networking.APIServices;
import com.techease.grubsup.networking.BaseNetworking;
import com.techease.grubsup.utils.AlertUtils;
import com.techease.grubsup.utils.GeneralUtills;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {


    View parentView;

    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    String strFullName, strPhoneNumber, strDateOfBirth, strAdults, strKidsUnder14, strLocation, strPostalCode, strGender;

    @BindView(R.id.iv_profile)
    CircleImageView ivProfileImage;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.tv_change_photo)
    TextView tvChangePhoto;
    @BindView(R.id.tv_name)
    TextView tvFullName;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.et_full_name)
    EditText etFullName;
    @BindView(R.id.et_edit_phone_number)
    EditText etPhoneNumber;
    @BindView(R.id.tv_edit_date_of_birth)
    TextView tvDateOfBirth;
    @BindView(R.id.et_adults)
    EditText etAdults;
    @BindView(R.id.et_kids_under14)
    EditText etKidsUnder14;
    @BindView(R.id.et_location)
    EditText etLocation;
    @BindView(R.id.et_postal_code)
    EditText etPostalCode;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.rbtn_male)
    RadioButton radioMale;
    @BindView(R.id.rbtn_female)
    RadioButton radioFemale;

    @BindView(R.id.tv_adults_title)
    TextView tvFamilySizeTitle;


    final int CAMERA_CAPTURE = 1;
    final int RESULT_LOAD_IMAGE = 2;

    File sourceFile = null;
    boolean valid = true;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    Dialog alertDialog, adultDailog;

    Button btnYes, btnNO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        ButterKnife.bind(this, parentView);
        alertDialog = AlertUtils.createProgressDialog(getActivity());

        getProfileData();
        DatePickerClass();
        initiateListener();

        return parentView;
    }

    private void initiateListener() {
        tvDateOfBirth.setOnClickListener(this);
        tvChangePhoto.setOnClickListener(this);
        tvSave.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(this);
        tvFamilySizeTitle.setOnClickListener(this);
        etAdults.setOnClickListener(this);
    }

    private void DatePickerClass() {
        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
    }

    private void updateLabel() {
        String myFormat = "MM-dd-yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tvDateOfBirth.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getId()) {
            case R.id.radioGroup:
                if (radioMale.isChecked()) {
                    strGender = "Male";
                } else if (radioFemale.isChecked()) {
                    strGender = "Female";
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_edit_date_of_birth:

                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.tv_change_photo:

                // Check whether this app has write external storage permission or not.
                int writeExternalStoragePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                // If do not grant write external storage permission.
                if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
                }
                cameraBuilder();
                break;

            case R.id.tv_save:

                if (sourceFile == null) {
                } else {
                    uploadProfilePic();
                }
                if (validate()) {
                    updateProfile();
                }
                break;


            case R.id.iv_back:

                getActivity().finish();

                break;

            case R.id.tv_adults_title:


                break;

            case R.id.et_adults:

                break;

        }
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
        } else if (resultCode == RESULT_OK && requestCode == CAMERA_CAPTURE && data != null) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();

            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            sourceFile = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");
            FileOutputStream fo;
            try {
                sourceFile.createNewFile();

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

            ivProfileImage.setImageBitmap(thumbnail);
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
        ivProfileImage.setImageBitmap(BitmapFactory.decodeFile(filePath));

        return cursor.getString(column_index);
    }


    private boolean validate() {

        strFullName = etFullName.getText().toString();
        strPhoneNumber = etPhoneNumber.getText().toString();
        strDateOfBirth = tvDateOfBirth.getText().toString();
        strAdults = etAdults.getText().toString();
        strKidsUnder14 = etKidsUnder14.getText().toString();
        strLocation = etLocation.getText().toString();
        strPostalCode = etPostalCode.getText().toString();


        valid = true;

        if (strGender == null) {
            valid = false;
        }
        if (strDateOfBirth == null) {
            valid = false;
        }
        if (strFullName.isEmpty()) {
            etFullName.setError("enter a valid name");
            valid = false;
        }

        if (strPhoneNumber.isEmpty()) {
            etPhoneNumber.setError("enter a valid phone number");
            valid = false;
        }
        if (strDateOfBirth.isEmpty()) {
            etPhoneNumber.setError("enter a date of birth");
            valid = false;
        }
        if (strAdults.isEmpty()) {
            etAdults.setError("enter adult size");
            valid = false;
        }
        if (strKidsUnder14.isEmpty()) {
            etKidsUnder14.setError("enter kids under 14 size");
        }
        if (strPostalCode.isEmpty()) {
            etPostalCode.setError("enter a postal");
            valid = false;
        }


        return valid;

    }


    private void updateProfile() {
        alertDialog.show();
        Call<ProfileUpdateResponseModel> updateResponse = BaseNetworking.apiServices(GeneralUtills.getApiToken(getActivity()))
                .updateProfile(strFullName, strPhoneNumber, strLocation, strPostalCode, strGender, strAdults, strKidsUnder14);
        updateResponse.enqueue(new Callback<ProfileUpdateResponseModel>() {
            @Override
            public void onResponse(Call<ProfileUpdateResponseModel> call, Response<ProfileUpdateResponseModel> response) {
                alertDialog.dismiss();
                if (response.isSuccessful()) {

                    GeneralUtills.putStringValueInEditor(getActivity(), "postal_code", String.valueOf(response.body().getData().getPostalCode()));
                    GeneralUtills.putStringValueInEditor(getActivity(), "notification_status", response.body().getData().getNotificationStatus());
                    GeneralUtills.putStringValueInEditor(getActivity(), "email", response.body().getData().getEmail());
                    GeneralUtills.putStringValueInEditor(getActivity(), "name", response.body().getData().getName());
                    GeneralUtills.putStringValueInEditor(getActivity(), "location", String.valueOf(response.body().getData().getLocation()));
                    GeneralUtills.putStringValueInEditor(getActivity(), "gender", String.valueOf(response.body().getData().getGender()));
                    GeneralUtills.putStringValueInEditor(getActivity(), "adultsSize", String.valueOf(response.body().getData().getAdults()));
                    GeneralUtills.putStringValueInEditor(getActivity(), "kidsUnder14Size", String.valueOf(response.body().getData().getAdults()));
                    GeneralUtills.putStringValueInEditor(getActivity(), "planId", String.valueOf(response.body().getData().getPlanId()));

                    GeneralUtills.withOutBackStackConnectFragment(getActivity(), new EditProfileFragment());
                    GeneralUtills.hideKeyboardFrom(getActivity(), parentView);
                    GeneralUtills.hideKeyboard(getActivity());
                } else {
                    Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileUpdateResponseModel> call, Throwable t) {
                alertDialog.dismiss();
            }
        });
    }

    private void uploadProfilePic() {

        alertDialog.show();
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), sourceFile);
        final MultipartBody.Part profileImage = MultipartBody.Part.createFormData("profilePicture", sourceFile.getName(), requestFile);
        RequestBody fileName = RequestBody.create(MediaType.parse("text/plain"), "upload_test");
        Call<ProfileUpdateResponseModel> call = BaseNetworking.apiServices(GeneralUtills.getApiToken(getActivity())).updateProfilePic(profileImage, fileName);
        call.enqueue(new Callback<ProfileUpdateResponseModel>() {
            @Override
            public void onResponse(Call<ProfileUpdateResponseModel> call, Response<ProfileUpdateResponseModel> response) {
                alertDialog.dismiss();
                if (response.isSuccessful()) {
                    GeneralUtills.hideKeyboardFrom(getActivity(), parentView);
                    GeneralUtills.hideKeyboard(getActivity());
                } else {
                    Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileUpdateResponseModel> call, Throwable t) {
                alertDialog.dismiss();

            }
        });

    }

    private void getProfileData() {

        alertDialog.show();
        APIServices services = APIClient.getApiClient(GeneralUtills.getApiToken(getActivity())).create(APIServices.class);
        Call<GetProfileResponseModel> getProfileResponseModelCall = services.profile();
        getProfileResponseModelCall.enqueue(new Callback<GetProfileResponseModel>() {
            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<GetProfileResponseModel> call, Response<GetProfileResponseModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    alertDialog.dismiss();

                    Glide.with(getActivity()).load(response.body().getData().getProfilePictureLink()).into(ivProfileImage);
                    etFullName.setText(response.body().getData().getName());
                    tvEmail.setText(response.body().getData().getEmail());
                    tvFullName.setText(response.body().getData().getName());
                    etPhoneNumber.setText(response.body().getData().getPhoneNumber());
                    etAdults.setText(String.valueOf(response.body().getData().getAdults()));
                    etKidsUnder14.setText(String.valueOf(response.body().getData().getKidsUnder14()));
                    etPostalCode.setText(String.valueOf(response.body().getData().getPostalCode()));
                    strGender = String.valueOf(response.body().getData().getGender());
                    etLocation.setText(String.valueOf(response.body().getData().getLocation()));
                    if (strGender != null) {
                        if (strGender.equals("Male")) {
                            radioMale.setChecked(true);
                            strGender = "Male";
                        } else if (strGender.equals("Female")) {
                            radioFemale.setChecked(true);
                            strGender = "Female";
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GetProfileResponseModel> call, Throwable t) {
                alertDialog.dismiss();
                Toast.makeText(getActivity(), String.valueOf(t), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void adultDialog() {


        adultDailog = new Dialog(getActivity());
        adultDailog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        adultDailog.setCancelable(true);
        adultDailog.setContentView(R.layout.custom_privacy_policy_dialog);


        btnYes = adultDailog.findViewById(R.id.btn_yes);
        btnNO = adultDailog.findViewById(R.id.btn_no);


        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (adultDailog != null) {
                    adultDailog.dismiss();
                }

            }
        });

        btnNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adultDailog != null) {
                    adultDailog.dismiss();
//                    getActivity().finish();
                }
            }
        });


        adultDailog.show();

    }

}
