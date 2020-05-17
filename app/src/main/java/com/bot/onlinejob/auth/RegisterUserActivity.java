package com.bot.onlinejob.auth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.bot.onlinejob.R;
import com.bot.onlinejob.bean.UserBean;
import com.bot.onlinejob.provider.ProviderHomeActivity;
import com.bot.onlinejob.seeker.SeekerHomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUserActivity extends AppCompatActivity {
    Button btnRegisterUser;
    //user input
    EditText inputUserName,inputEmail;
    TextView inputServiceType;
    String userName,email,serviceType;

    //mauth
    FirebaseAuth mAuth;
    //current user
    FirebaseUser currentUser;
    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // make the activity on full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        //initialize
        btnRegisterUser = findViewById(R.id.btn_register_user);
        inputUserName = findViewById(R.id.et_reg_username);
        inputEmail = findViewById(R.id.et_reg_email);
        inputServiceType = findViewById(R.id.tv_reg_type);

        //initialize mAuth
        mAuth = FirebaseAuth.getInstance();
        //current user
        currentUser = mAuth.getCurrentUser();
        currentUserId = currentUser.getUid();
        //TODO try checking a user is verified or not
        //handle click on service type
        inputServiceType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //load the dialog
                //and select the service
                loadNSetServiceTypeDialog();
            }
        });
        //handle click on register user btn
        btnRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //read the data from input fields
                userName = inputUserName.getText().toString().trim();
                email = inputEmail.getText().toString().trim();
                serviceType = inputServiceType.getText().toString().trim();
                //validate the inputs
                if(validateInput(userName,email,serviceType)){
                    //if inputs are valid register the user
                    //save data to shared pref
                    //and database
                    saveRegPrefsData();
                    saveDataToDb(userName,email,serviceType,currentUser.getPhoneNumber());
                    //goto dashboard
                    gotoDashboard(serviceType);
                }
            }
        });

    }

    //function that load the dialog
    //and set the service type
    public void loadNSetServiceTypeDialog(){
        Button btnSelectTypeProvider,btnSelectTypeSeeker;
        final  AlertDialog alertDialog;
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.select_service_dialog, viewGroup, false);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        //finally creating the alert dialog and displaying it
        alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);

        //initialize btns
        btnSelectTypeProvider = dialogView.findViewById(R.id.dialog_btn_provider);
        btnSelectTypeSeeker = dialogView.findViewById(R.id.dialog_btn_worker);
        //handle the clicks
        btnSelectTypeProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputServiceType.setText("Provider");
                alertDialog.dismiss();
            }
        });
        btnSelectTypeSeeker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputServiceType.setText("Worker");
                alertDialog.dismiss();
            }
        });

    }

    //validate input
    //return true if every i/p is valid
    //else false
    public boolean validateInput(String userName,String email,String serviceType){
        if(userName.isEmpty()||userName.length()==0)
            return false;
        if(serviceType.isEmpty()||serviceType.length()==0)
            return false;
        if (!isValidEmail(email))
            return false;
        return true;
    }

    //valid email or not
    public  boolean isValidEmail(CharSequence email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    //after register save
    //flags :: logged in or not
    //  :: service type
    private void saveRegPrefsData(){
        SharedPreferences pref =getApplicationContext().getSharedPreferences("loggedInOrNot",MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        editor.putBoolean("isLoggedInAlready",true);
        //seeker or provider
        //for in app navigation
        editor.putString("serviceType",serviceType);
        editor.putString("currentUserId",currentUserId);
        editor.commit();
    }

    private void saveDataToDb(String userName,String email,String serviceType,String mob){
            //create a user object
        final UserBean user = new UserBean(userName,email,serviceType,mob);
        //database instance
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        //reference to db
        //store user type seperate
        //path and value will be Worker or Provide -> userId-8888 : 8888
        DatabaseReference databaseReferenceToUserType = database.getReference(serviceType+"/userId-"+currentUserId);
        databaseReferenceToUserType.setValue(currentUserId);

        //store profile details
        DatabaseReference databaseReferenceToUserProfile = database.getReference("users/"+currentUserId+"/profile");
        databaseReferenceToUserProfile.child("/").setValue(user);
    }

    //goto dashboard of seeker or provider
    public void gotoDashboard(String serviceType){
        Intent intentToDashboard = null;
        //if provider
        if (serviceType.matches("Provider"))
            intentToDashboard = new Intent(RegisterUserActivity.this, ProviderHomeActivity.class);
        //if worker
        if (serviceType.matches("Worker"))
            intentToDashboard = new Intent(RegisterUserActivity.this, SeekerHomeActivity.class);
        //when opening the home activity clear the all top activities
        intentToDashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentToDashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentToDashboard);
        finish();

    }
}
