package com.bot.onlinejob.provider.newWorks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bot.onlinejob.R;
import com.bot.onlinejob.provider.ProviderHomeActivity;

import java.util.ArrayList;
import java.util.List;

public class ProviderNewWorkSkilledActivity extends AppCompatActivity {
    Button backto_home;
    //for recycler view
    List<providerwork> lstProviderwork;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider_new_work_skilled_activity);

        backto_home = (Button) findViewById(R.id.backto_home);
        backto_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProviderNewWorkSkilledActivity.this, ProviderHomeActivity.class);
                startActivity(intent);
            }
        });


        lstProviderwork = new ArrayList<>();
        lstProviderwork.add(new providerwork("Carpenter","Skilled", R.drawable.employer));
        lstProviderwork.add(new providerwork("Dance Teacher","Skilled", R.drawable.employer));
        lstProviderwork.add(new providerwork("Driver","Skilled", R.drawable.employer));
        lstProviderwork.add(new providerwork("Electrician","Skilled", R.drawable.employer));
        lstProviderwork.add(new providerwork("Grass cutting with machine","Skilled", R.drawable.employer));
        lstProviderwork.add(new providerwork("Home Tuition - High School","Skilled", R.drawable.employer));
        lstProviderwork.add(new providerwork("Home Tuition (HS-CBSE)","Skilled", R.drawable.employer));
        lstProviderwork.add(new providerwork("Mansion","Skilled", R.drawable.employer));
        lstProviderwork.add(new providerwork("Motor Mechanic","Skilled", R.drawable.employer));
        lstProviderwork.add(new providerwork("Plumber","Skilled", R.drawable.employer));
        lstProviderwork.add(new providerwork("Teaching (Others-Higher levels)","Skilled", R.drawable.employer));
        lstProviderwork.add(new providerwork("Tile Fixing","Skilled", R.drawable.employer));
        lstProviderwork.add(new providerwork("TV Technician","Skilled", R.drawable.employer));
        lstProviderwork.add(new providerwork("Welder","Skilled", R.drawable.employer));



        ///for recycler view

        RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerview_id);
        ProviderRecyclerViewAdapter myAdapter = new ProviderRecyclerViewAdapter(this, lstProviderwork);
        myrv.setLayoutManager(new GridLayoutManager(this,3));
        myrv.setAdapter(myAdapter);




    }
    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }
}
