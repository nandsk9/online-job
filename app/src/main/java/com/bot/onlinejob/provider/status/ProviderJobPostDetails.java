package com.bot.onlinejob.provider.status;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bot.onlinejob.R;
import com.bot.onlinejob.bean.PostJobBean;
import com.bumptech.glide.Glide;

public class ProviderJobPostDetails extends AppCompatActivity {
    Intent intentData;
    PostJobBean postJobBean;

    //image view
    ImageView imageViewPostedJob;
    //text view
    TextView tvName,tvType,tvCategory,tvLocation,tvWage,tvQualification,tvDate,tvTime,tvPostedOn,tvAcceptedBy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // make the activity on full screen

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_provider_job_post_details);

        // read the PostJobBean object from the intent
        intentData = getIntent();
        postJobBean = (PostJobBean)intentData.getSerializableExtra("statusPostJobBean");
        //initialize the view
        imageViewPostedJob = findViewById(R.id.provider_job_post_details_img);
        tvName = findViewById(R.id.provider_job_post_job_name);
        tvType = findViewById(R.id.provider_job_post_job_type);
        tvCategory = findViewById(R.id.provider_job_post_job_category);
        tvLocation = findViewById(R.id.provider_job_post_job_location);
        tvWage = findViewById(R.id.provider_job_post_job_wage);
        tvQualification = findViewById(R.id.provider_job_post_job_qualification);
        tvDate = findViewById(R.id.provider_job_post_job_date);
        tvTime = findViewById(R.id.provider_job_post_job_time);
        tvPostedOn = findViewById(R.id.provider_job_post_job_posted_on);
        tvAcceptedBy = findViewById(R.id.provider_job_post_job_accepted_by);

        Glide.with(getApplicationContext()).load(postJobBean.getJobImgUrl()).into(imageViewPostedJob);
        tvName.setText("Job Name : " +postJobBean.getJobName());
        tvType.setText("Job Type : " +postJobBean.getJobType());
        tvCategory.setText("Job Category : " +postJobBean.getJobCategory());
        tvLocation.setText("Job Location : " +postJobBean.getJobLocation());
        tvWage.setText("Job Wage : " +postJobBean.getJobWage());
        tvQualification.setText("Job Qualification : " +postJobBean.getJobQualification());
        tvDate.setText("Job Date : " +postJobBean.getJobDate());
        tvTime.setText("Job Time : " +postJobBean.getJobTime());
        tvPostedOn.setText("Job Posted On  : " +postJobBean.getPostedOnDate());
        tvAcceptedBy.setText("Job Accepted By : " +postJobBean.getSeekerUserId());
        //also display all details about the seeker who accepted the job
        //except confidential data

    }
}
