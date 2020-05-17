package com.bot.onlinejob.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bot.onlinejob.R;
import com.bot.onlinejob.bean.UserBean;
import com.bot.onlinejob.provider.ProviderHomeActivity;
import com.bot.onlinejob.seeker.SeekerHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class EnterMobileActivity extends AppCompatActivity {
    //service type of already registered user
    String serviceTypeAlreadyReg;
  Button btnSendOtp;
  EditText inputMobileNumber;
  //verification id
  String mVerificationId;
  //token to resend otp
  PhoneAuthProvider.ForceResendingToken mResendToken;
  //firebase auth
    FirebaseAuth mAuth;
    FirebaseUser user;
  //call back
  private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // make the activity on full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        //When this activity is about to be launch we need to check if previously logged in or not
        //Default value is given false,when logged in once it changes to true
        //also check type of user
        //seeker or provider
        mAuth = FirebaseAuth.getInstance();
        //find if a user logged in or not
        user = mAuth.getCurrentUser();
        if(restoreLoginPrefData()&&user!=null&&restoreServiceTypePref()!=null){
            Intent mainActivity = null;
            //check seeker or provider
            if(restoreServiceTypePref().matches("Worker"))
                mainActivity =new Intent(getApplicationContext(), SeekerHomeActivity.class);
            if(restoreServiceTypePref().matches("Provider"))
                mainActivity =new Intent(getApplicationContext(), ProviderHomeActivity.class);
            startActivity(mainActivity);
            finish();
        }


        setContentView(R.layout.activity_enter_mobile);
        //reference
        btnSendOtp = findViewById(R.id.button_send_otp);
        inputMobileNumber = findViewById(R.id.login_enter_mob);
        btnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(EnterMobileActivity.this,VerifyOtpActivity.class));
                //read the mobile number
                String mobileNum = inputMobileNumber.getText().toString().trim();
                //check valid or not
                if (mobileNum.isEmpty()||mobileNum.length()<10){
                    Toast.makeText(EnterMobileActivity.this, "Please enter valid mobile number....", Toast.LENGTH_SHORT).show();
                }
                else{
                    //if valid
                    mobileNum = "+91"+inputMobileNumber.getText().toString().trim();
                    Log.e("Mob :: ",mobileNum);
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            mobileNum,// Phone number to verify
                            60,// Timeout duration
                            TimeUnit.SECONDS,// Unit of timeout
                            EnterMobileActivity.this,// Activity (for callback binding)
                            mCallbacks);// OnVerificationStateChangedCallbacks

                }
            }
        });

        //initializing callbacks
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                    // This callback will be invoked in two situations:
                    // 1 - Instant verification. In some cases the phone number can be instantly
                    //     verified without needing to send or enter a verification code.
                    // 2 - Auto-retrieval. On some devices Google Play services can automatically
                    //     detect the incoming verification SMS and perform verification without
                    //     user action.
                    Log.d(" OTP Status", "onVerificationCompleted:" + credential);

                    //newly added to solve the problem
                //of not going to otp activity
                //because account is verified without otp
                    signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w(" OTP Status", "onVerificationFailed", e);

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request

            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded

            }

            // Show a message and update the UI
            //verification failed

        }

        @Override
        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken resendToken) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(" OTP Status", "onCodeSent:" + verificationId);
            super.onCodeSent(verificationId,resendToken);
            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId;
            mResendToken = resendToken;
            //after sending otp
            //goto VerifyOtpActivity
            Intent intentToVerifyOtp = new Intent(EnterMobileActivity.this,VerifyOtpActivity.class);
            //also send the required credentials
            intentToVerifyOtp.putExtra("verificationId",mVerificationId);
            //intentToVerifyOtp.putExtra("otpResendToken",mResendToken);
           intentToVerifyOtp.putExtra("otpResendToken",resendToken);
            intentToVerifyOtp.putExtra("mobileNum","+91"+inputMobileNumber.getText().toString().trim());
            //goto verification activity
            startActivity(intentToVerifyOtp);
            finish();


        }
    };

}

    //code for not opening login screen again
    //after login is success
    private boolean restoreLoginPrefData(){
        SharedPreferences pref=getApplicationContext().getSharedPreferences("loggedInOrNot",MODE_PRIVATE);
        Boolean isLoggedInBefore =pref.getBoolean("isLoggedInAlready",false);//Default is false
        return isLoggedInBefore;
    }

    //restore service type
    private String restoreServiceTypePref(){
        SharedPreferences pref=getApplicationContext().getSharedPreferences("loggedInOrNot",MODE_PRIVATE);
        String serviceType = pref.getString("serviceType",null);
        //return service type for current user
        //who already logged in
        return serviceType;
    }

    //to solve the problem
    //of not going to verify otp
    //because credential is already created
    //without otp
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(EnterMobileActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SignInStatus ", "signInWithCredential:success");

                    FirebaseUser user = task.getResult().getUser();
                    //current user id
                    final String currentUserId = user.getUid();
                    Toast.makeText(EnterMobileActivity.this, "Sign In Success", Toast.LENGTH_SHORT).show();
                    //goto home of seeker or provider
                    //if already registered

                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users/" + currentUserId).child("/profile");
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                //data exist
                                //read the serviceType that already registered
                                UserBean user = dataSnapshot.getValue(UserBean.class);
                                serviceTypeAlreadyReg = user.getServiceType();
                                //also user
                                Log.e("user : ", "Exist");
                                Log.e("Data : ", dataSnapshot.toString());
                                Log.e("serviceTypeAlreadyReg :: ", user.getServiceType());
                                //save to shared pref
                                saveReLoginPrefsData(serviceTypeAlreadyReg, currentUserId);
                                gotoDashboard(serviceTypeAlreadyReg);
                            } else {
                                Log.e("Exist Status :: ", "User not exist");
                                //here is a bug
                                //tried to solve
                                gotoRegisterUser();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(EnterMobileActivity.this, "Some error occurred", Toast.LENGTH_LONG).show();
                        }
                    });

                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w("SignInStatus ", "signInWithCredential:failure", task.getException());
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(EnterMobileActivity.this, "Invalid Otp", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    //goto register activity
    public void gotoRegisterUser(){
        Intent intentToRegister = new Intent(EnterMobileActivity.this,RegisterUserActivity.class);
        startActivity(intentToRegister);
        finish();
    }

    //goto home activity
    public void gotoDashboard(String serviceType){
        Intent intentToDashboard = null;
        //if provider
        if (serviceType.matches("Provider"))
            intentToDashboard = new Intent(EnterMobileActivity.this, ProviderHomeActivity.class);
        //if worker
        if (serviceType.matches("Worker"))
            intentToDashboard = new Intent(EnterMobileActivity.this, SeekerHomeActivity.class);
        //when opening the home activity clear the all top activities
        intentToDashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentToDashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentToDashboard);
        finish();
    }
    private void saveReLoginPrefsData(String serviceType,String currentUserId){
        SharedPreferences pref =getApplicationContext().getSharedPreferences("loggedInOrNot",MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        editor.putBoolean("isLoggedInAlready",true);
        //seeker or provider
        //for in app navigation
        editor.putString("serviceType",serviceType);
        editor.putString("currentUserId",currentUserId);
        editor.commit();
    }
}
