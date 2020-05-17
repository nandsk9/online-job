package com.bot.onlinejob.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class VerifyOtpActivity extends AppCompatActivity {
    Button btnVerifyOtp,btnResendOtp;
    EditText inputOtp;
    //user already exist or not
   // boolean existOrNot;
    String solveFlag;
    //service type of already registered user
    String serviceTypeAlreadyReg;
    //verification id,resendToken,mobileNum
    String mVerificationId,mobileNum;
    PhoneAuthProvider.ForceResendingToken resendToken;
    //storing otp
    String otp;
    //resend otp counter
    int resendCounter = 60;
    //firebase auth
    private FirebaseAuth mAuth;
    //user id
    String currentUserId=null;
    //call back
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // make the activity on full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        btnVerifyOtp = findViewById(R.id.button_verify_otp);
        inputOtp = findViewById(R.id.login_enter_otp);
        //read the intent data
        mVerificationId = getIntent().getStringExtra("verificationId");
        //mobile num
        mobileNum = getIntent().getStringExtra("mobileNum");
        btnResendOtp = findViewById(R.id.button_resend_otp);
        //otp resend token
        resendToken  = (PhoneAuthProvider.ForceResendingToken)getIntent().getParcelableExtra("otpResendToken");
        //initialize mAuth
        mAuth = FirebaseAuth.getInstance();
        //giving a 30 sec delay for activating resend button before click
        resendDelayedBtn();
        //control onclick on resend button
        btnResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //giving a 60 sec delay for activating resend button after click
                resendDelayedBtn();
                //resend otp
                resendVerificationCode(mobileNum,resendToken);
            }
        });
        btnVerifyOtp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //read the otp
                otp = inputOtp.getText().toString().trim();
                //if not valid otp
                if (otp.isEmpty()||otp.length()<6){
                    Toast.makeText(VerifyOtpActivity.this, "Enter a valid otp", Toast.LENGTH_SHORT).show();
                }
                //if valid otp
                else{
                    //try to verify
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
                    //call the below method to sign in user
                    signInWithPhoneAuthCredential(credential);
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
                Log.d(" OTP RS Status", "onVerificationCompleted:" + credential);

                //signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(" OTP RS Status", "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request

                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded

                }

                // Show a message and update the UI
                //verification failed

            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken otpResendToken) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(" OTP RS Status", "onCodeSent:" + verificationId);
                super.onCodeSent(verificationId,resendToken);
                // update verification id and resendToken
                //after resending otp
                mVerificationId = verificationId;
                resendToken = otpResendToken;
            }
        };


    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyOtpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SignInStatus ", "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            //current user id
                            currentUserId = user.getUid();
                            Toast.makeText(VerifyOtpActivity.this, "Sign In Success", Toast.LENGTH_SHORT).show();
                            //goto home of seeker or provider
                            //if already registered
                            /*if(checkUserAlreadyRegistered(currentUserId)){
                                Log.e("Exist Status :: ","User exist");
                                        //this parameter is assigned by
                                //method checkUserAlreadyRegistered(currentUserId)
                                        gotoDashboard(serviceTypeAlreadyReg);
                            }
                            else{
                                Log.e("Exist Status :: ","User not exist");
                                //register the user if not already registered
                                gotoRegisterUser();
                            }*/
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users/"+currentUserId).child("/profile");
                            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        //data exist
                                        //read the serviceType that already registered
                                        UserBean user = dataSnapshot.getValue(UserBean.class);
                                        serviceTypeAlreadyReg = user.getServiceType();
                                        //also user
                                        Log.e("user : ","Exist");
                                        Log.e("Data : ",dataSnapshot.toString());
                                        Log.e("serviceTypeAlreadyReg :: ",user.getServiceType());
                                        //save to shared pref
                                        saveReLoginPrefsData(serviceTypeAlreadyReg);
                                        gotoDashboard(serviceTypeAlreadyReg);
                                    }else{
                                        Log.e("Exist Status :: ","User not exist");
                                        //register the user if not already registered
                                        gotoRegisterUser();
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(VerifyOtpActivity.this,"Some error occurred",Toast.LENGTH_LONG).show();
                                }
                            });

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("SignInStatus ", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(VerifyOtpActivity.this, "Invalid Otp", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }



    //goto home activity
    public void gotoDashboard(String serviceType){
        Intent intentToDashboard = null;
        //if provider
        if (serviceType.matches("Provider"))
            intentToDashboard = new Intent(VerifyOtpActivity.this, ProviderHomeActivity.class);
        //if worker
        if (serviceType.matches("Worker"))
            intentToDashboard = new Intent(VerifyOtpActivity.this, SeekerHomeActivity.class);
        //when opening the home activity clear the all top activities
        intentToDashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentToDashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentToDashboard);
        finish();
    }
    //goto register activity
    public void gotoRegisterUser(){
        Intent intentToRegister = new Intent(VerifyOtpActivity.this,RegisterUserActivity.class);
        startActivity(intentToRegister);
        finish();
    }

    //function for resend otp delay
    public void resendDelayedBtn(){
        //reset counter value on each call
        resendCounter = 60;
        //disable the button
        //when it is clicked
        btnResendOtp.setEnabled(false);
        //start delay for 60sec
        new CountDownTimer(60000, 1000){
            public void onTick(long millisUntilFinished){
                btnResendOtp.setText("Resend OTP in "+String.valueOf(resendCounter)+" Sec");
                resendCounter--;
            }
            public  void onFinish(){
                btnResendOtp.setText("Resend OTP");
                btnResendOtp.setEnabled(true);
            }
        }.start();
    }
    //resend otp
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private void saveReLoginPrefsData(String serviceType){
        SharedPreferences pref =getApplicationContext().getSharedPreferences("loggedInOrNot",MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        editor.putBoolean("isLoggedInAlready",true);
        //seeker or provider
        //for in app navigation
        editor.putString("serviceType",serviceType);
        editor.putString("currentUserId",currentUserId);
        editor.commit();
    }

    /*//function to check if user already completed profile
    //if completed go directly to home
    //no need of registration ):
    //TODO ):
    //here an issue always returns false
    private boolean checkUserAlreadyRegistered(String currentUserId){
        final boolean existOrNot;
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users/"+currentUserId).child("/profile");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                       //data exist
                       //read the serviceType that already registered
                    UserBean user = dataSnapshot.getValue(UserBean.class);
                    serviceTypeAlreadyReg = user.getServiceType();
                    //also user
                    //existOrNot = true;
                    solveFlag = "yes";
                    Log.e("Data : ",dataSnapshot.toString());
                    Log.e("serviceTypeAlreadyReg :: ",user.getServiceType());
                }else{
                    //existOrNot =false;
                    Toast.makeText(VerifyOtpActivity.this,"User Not Exists",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(VerifyOtpActivity.this,"Some error occurred",Toast.LENGTH_LONG).show();
            }
        });
        //true if exist
        //else false
        Log.e("Return :: ",String.valueOf(solveFlag));
        if (solveFlag.matches("yes"))
            return true;
        Log.e("Return :: ",String.valueOf(solveFlag));
        return false;
    }*/
}
