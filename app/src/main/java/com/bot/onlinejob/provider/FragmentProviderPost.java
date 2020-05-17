package com.bot.onlinejob.provider;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.bot.onlinejob.R;
import com.bot.onlinejob.bean.PostJobBean;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class FragmentProviderPost extends Fragment implements AdapterView.OnItemSelectedListener  {
    Activity referenceActivity;
    View parentHolder;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    Button ChooseImageButton,postJobBtn;
    ImageView SelectImage;
    Spinner spinnerJobType,spinnerJobPostQualification,spinnerJobPostCategories;
    //date and time format setting...
    EditText inputSelectDate, inputSelectTime,inputJobName,inputJobLocation,inputContactNumber,inputWage;
    //image selected or not flag
    boolean isImageSelected;
    //image uploaded or not
    boolean isImageUploaded;
    private int mYear, mMonth, mDay, mHour, mMinute;
    // Creating URI for image
    Uri FilePathUri;
    private int Image_Request_Code = 8;
    //ArrayList to store the validation errors
    ArrayList<String> validationErrors;
    //firebase storage
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    AlertDialog alertDialog,alertDialogValidationErrors;
    //url of uploaded image
    String itemImageUrl;
    //array adapter for category spinner
    ArrayAdapter<String> spinnerJobPostCategoriesAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        referenceActivity = getActivity();
        parentHolder = inflater.inflate(R.layout.provider_post_fragment, container,
                false);
        Log.e("Frag : ", "Post");
        //initialize
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        postJobBtn = parentHolder.findViewById(R.id.btn_provider_post_job);
        ChooseImageButton = parentHolder.findViewById(R.id.provider_post_job_btn_choose_img);
        spinnerJobType = parentHolder.findViewById(R.id.provider_spinner_type);
        spinnerJobPostQualification = parentHolder.findViewById(R.id.provider_spinner_qualification);
        spinnerJobPostCategories = parentHolder.findViewById(R.id.provider_spinner_catagories);
        SelectImage = parentHolder.findViewById(R.id.provider_post_job_image);
        inputSelectDate=parentHolder.findViewById(R.id.provider_post_job_day);
        inputSelectTime=parentHolder.findViewById(R.id.provider_post_job_time);
        inputJobName = parentHolder.findViewById(R.id.provider_post_job_name);
        inputJobLocation=parentHolder.findViewById(R.id.provider_post_job_location);
        inputContactNumber = parentHolder.findViewById(R.id.provider_post_job_mob_num);
        inputWage = parentHolder.findViewById(R.id.provider_post_job_wage);
        //initialize error array list
        validationErrors = new ArrayList<>();

        //dropdown
        ArrayAdapter<String> myAdapter1 = new ArrayAdapter<String>(referenceActivity,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.job_type));
        myAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJobType.setAdapter(myAdapter1);
        spinnerJobType.setOnItemSelectedListener(this);
        ArrayAdapter<String> myAdapter3 = new ArrayAdapter<String>(referenceActivity,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.job_qualification));
        myAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJobPostQualification.setAdapter(myAdapter3);

        //post the job
        postJobBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //read the user input
                String jobType = spinnerJobType.getSelectedItem().toString();
                String jobCategory = spinnerJobPostCategories.getSelectedItem().toString();
                String jobName = inputJobName.getText().toString().trim();
                String jobLocation = inputJobLocation.getText().toString().trim();
                String jobContactNumber = inputContactNumber.getText().toString();
                String jobWage = inputWage.getText().toString().trim();
                String jobQualification = spinnerJobPostQualification.getSelectedItem().toString();
                String jobDate = inputSelectDate.getText().toString();
                String jobTime = inputSelectTime.getText().toString();

                //validate the input
                if (validateUserPostInput()){
                    //post the job to the database
                    //first upload the image
                    uploadImage();
                }
                else{
                    //display a error dialog
                    Toast.makeText(referenceActivity, "Not valid input", Toast.LENGTH_SHORT).show();
                    //show error dialog
                    showCustomErrorDialog(validationErrors);
                }
            }
        });

        //showing image in activity
        ChooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Creating intent.
                Intent intent = new Intent();
                // Setting intent type as image to select image from phone storage.
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);
            }
        });

        //select date and time................
        inputSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == inputSelectDate) {
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(referenceActivity,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    inputSelectDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            }
        });
       inputSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == inputSelectTime) {
                    // Get Current Time
                    final Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);
                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(referenceActivity,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {
                                    inputSelectTime.setText(hourOfDay + ":" + minute);
                                }
                            }, mHour, mMinute, false);
                    timePickerDialog.show();
                }
            }
        });

        //return parentHolder;
        return parentHolder;
    }

    //accessing image from gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {
            FilePathUri = data.getData();
            try {
                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), FilePathUri);
                // Setting up bitmap selected image into ImageView.
                SelectImage.setImageBitmap(bitmap);
                //if image selected
                isImageSelected =true;
                // After selecting image change choose button above text.
                ChooseImageButton.setText("Change Image");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        switch (position) {
            case 0:
                // Whatever you want to happen when the first item gets selected
                //Spinner spinnerJobPostCategories = parentHolder.findViewById(R.id.provider_spinner_catagories);

                spinnerJobPostCategoriesAdapter = new ArrayAdapter<String>(referenceActivity,
                        android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.job_catagories_skilled));
                spinnerJobPostCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerJobPostCategories.setAdapter(spinnerJobPostCategoriesAdapter);
                break;
            case 1:
                // Whatever you want to happen when the second item gets selected
                //Spinner spinnerJobPostCategories = parentHolder.findViewById(R.id.provider_spinner_catagories);

                spinnerJobPostCategoriesAdapter = new ArrayAdapter<String>(referenceActivity,
                        android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.job_catagories_semiskilled));
                spinnerJobPostCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerJobPostCategories.setAdapter(spinnerJobPostCategoriesAdapter);
                break;
            case 2:
                // Whatever you want to happen when the thrid item gets selected
                //Spinner spinner23 = parentHolder.findViewById(R.id.provider_spinner_catagories);

                spinnerJobPostCategoriesAdapter = new ArrayAdapter<String>(referenceActivity,
                        android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.job_catagories_unskilled));
                spinnerJobPostCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerJobPostCategories.setAdapter(spinnerJobPostCategoriesAdapter);
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }


    //////
    public String createJobId(){
        String jobId = "job-";
        long timeInMillis = System.currentTimeMillis();
        jobId+=timeInMillis;
        return jobId;
    }
    //function to validate data entered
    public boolean validateUserPostInput(){
        //read the user input
        String jobType = spinnerJobType.getSelectedItem().toString();
        String jobCategory = spinnerJobPostCategories.getSelectedItem().toString();
        String jobName = inputJobName.getText().toString().trim();
        String jobLocation = inputJobLocation.getText().toString().trim();
        String jobContactNumber = inputContactNumber.getText().toString();
        String jobWage = inputWage.getText().toString().trim();
        String jobQualification = spinnerJobPostQualification.getSelectedItem().toString();
        String jobDate = inputSelectDate.getText().toString();
        String jobTime = inputSelectTime.getText().toString();
        if(jobType.isEmpty()||jobType.length()==0){
            validationErrors.add("Not a valid job type");
            return false;
        }
        if(jobCategory.isEmpty()||jobCategory.length()==0){
            validationErrors.add("Not a valid job category");
            return false;
        }
        if(jobName.isEmpty()||jobName.length()==0){
            validationErrors.add("Not a valid job name");
            return false;
        }
        if(jobLocation.isEmpty()||jobLocation.length()==0){
            validationErrors.add("Not a valid job location");
            return false;
        }
        if(jobContactNumber.isEmpty()||jobContactNumber.length()==0||jobContactNumber.length()<10){
            validationErrors.add("Not a valid contact number");
            return false;
        }
        if(jobWage.isEmpty()||jobWage.length()==0){
            validationErrors.add("Not a valid job wage");
            return false;
        }
        if(jobQualification.isEmpty()||jobQualification.length()==0){
            validationErrors.add("Not a valid qualification");
            return false;
        }
        if(jobDate.isEmpty()||jobDate.length()==0){
            validationErrors.add("Not a valid job date");
            return false;
        }
        if(jobTime.isEmpty()||jobTime.length()==0){
            validationErrors.add("Not a valid job time");
            return false;
        }
        //finally if everything is valid return true
        return true;

    }

    //upload file
    public void uploadImage(){
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("posted-job-images/").child(String.valueOf(System.currentTimeMillis()));
        // Get the data from an ImageView as bytes
        //imageViewSelectPic.setDrawingCacheEnabled(true);
        //imageViewSelectPic.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) SelectImage.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] data = baos.toByteArray();
        Log.e("Pic Size :: ",String.valueOf((double)(data.length/1024)));
        double imageSize = (data.length/1024);
        if(imageSize<500.00){
            showCustomLoaderDialog("Uploading ..");
            UploadTask uploadTask = storageReference.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    isImageUploaded = false;
                    // Handle unsuccessful uploads
                    Toast.makeText(getContext(), "Upload Failed", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    isImageUploaded = true;
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.e("image url ",uri.toString());
                            itemImageUrl = uri.toString();
                            dismissLoadingDialog();
                            //after successful image upload
                            //store the details to db
                            postJobToDataBase(itemImageUrl);
                        }
                    });
                    Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                }
            });
        }
        else{
            //greater than 500kb
            isImageUploaded = false;
            Toast.makeText(getContext(), "Image Size should be less than 500kb", Toast.LENGTH_SHORT).show();
        }

    }
    private void showCustomLoaderDialog(String message) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = parentHolder.findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.custom_loading_dialog, viewGroup, false);
        TextView loaderMessage = dialogView.findViewById(R.id.custom_loading_dialog_text);
        loaderMessage.setText(message);
        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        //finally creating the alert dialog and displaying it
        alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);

    }
    private void dismissLoadingDialog(){
        alertDialog.dismiss();
    }


    //post job to db
    public void postJobToDataBase(String imageUrl){
        //read the user input
        String jobType = spinnerJobType.getSelectedItem().toString();
        String jobCategory = spinnerJobPostCategories.getSelectedItem().toString();
        String jobName = inputJobName.getText().toString().trim();
        String jobLocation = inputJobLocation.getText().toString().trim();
        String jobContactNumber = inputContactNumber.getText().toString();
        String jobWage = inputWage.getText().toString().trim();
        String jobQualification = spinnerJobPostQualification.getSelectedItem().toString();
        String jobDate = inputSelectDate.getText().toString();
        String jobTime = inputSelectTime.getText().toString();
        //loading dialog
        showCustomLoaderDialog("Posting .. ");
        String jobId = createJobId();
        //create a job post bean object
        //add seeker user id only when someone accept the job
        PostJobBean postJobBean = new PostJobBean(jobId,jobType,
                jobCategory,
                jobName,
                jobLocation,
                jobContactNumber,
                jobWage,
                jobQualification,
                jobDate,
                jobTime,
                imageUrl,"Active",getCurrentDate(),mUser.getUid(),null);//currently active job

        //adding to database -> work-posted
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReferenceToItems = firebaseDatabase.getReference("work-posted").child(postJobBean.getJobId());
        databaseReferenceToItems.setValue(postJobBean);
        //adding to database -> users
        DatabaseReference databaseReferenceToUsersWorkPosted = firebaseDatabase.getReference("users/"+mUser.getUid()).child("work-posted");
        databaseReferenceToUsersWorkPosted.child(postJobBean.getJobId()).setValue(postJobBean.getJobId());
        //after completion
        dismissLoadingDialog();
        Toast.makeText(referenceActivity, "Job posted Successfully", Toast.LENGTH_SHORT).show();

    }

    //custom validation error dialog
    private void showCustomErrorDialog(ArrayList<String> errors) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = parentHolder.findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogViewErrors = LayoutInflater.from(getContext()).inflate(R.layout.custom_validation_error_dialog, viewGroup, false);

        //add text view dynamically
        final LinearLayout linearLayout = dialogViewErrors.findViewById(R.id.linear_layout_validation_errors);
        Button btnDismissErrorDialog = dialogViewErrors.findViewById(R.id.btn_close_validation_error_dialog);
        for(int i=0;i<errors.size();i++){
            TextView textView = new TextView(getContext());
            textView.setText(errors.get(i));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(params);
            linearLayout.addView(textView);
        }
        btnDismissErrorDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clear all previous errors
                validationErrors.clear();
                alertDialogValidationErrors.dismiss();

            }
        });

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogViewErrors);
        //finally creating the alert dialog and displaying it
        alertDialogValidationErrors = builder.create();
        alertDialogValidationErrors.show();
        alertDialogValidationErrors.setCanceledOnTouchOutside(false);

    }
    private void dismissCustomErrorDialog(){
        alertDialogValidationErrors.dismiss();
    }

    public String getCurrentDate(){
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");

        String formattedDate = format.format(date);

        return formattedDate;
    }

}