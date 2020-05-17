package com.bot.onlinejob.provider.newWorks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bot.onlinejob.R;
import com.bot.onlinejob.provider.ProviderHomeActivity;

import java.util.ArrayList;
import java.util.List;

public class ProviderNewWorkUnSkilledActivity extends AppCompatActivity {
    List<providerwork> lstProviderwork;
    Button backto_unhome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider_new_work_unskilled_activity);

        backto_unhome = (Button) findViewById(R.id.backto_unhome);
        backto_unhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProviderNewWorkUnSkilledActivity.this, ProviderHomeActivity.class);
                startActivity(intent);
            }
        });

        lstProviderwork = new ArrayList<>();
        lstProviderwork.add(new providerwork("Cooking/helper (Non-Veg)","UnSkilled", R.drawable.employer));
        lstProviderwork.add(new providerwork("Cooking/helper (Veg)","UnSkilled", R.drawable.employer));
        lstProviderwork.add(new providerwork("Helper (construction)","UnSkilled", R.drawable.employer));
        lstProviderwork.add(new providerwork("Helper(general)","UnSkilled", R.drawable.employer));
        lstProviderwork.add(new providerwork("Hotel and functions table cleaning","UnSkilled", R.drawable.employer));
        lstProviderwork.add(new providerwork("Hotel Kitchen helper","UnSkilled", R.drawable.employer));
        lstProviderwork.add(new providerwork("Indoor cleaning","UnSkilled", R.drawable.employer));
        lstProviderwork.add(new providerwork("Lifting/moving/packing","UnSkilled", R.drawable.employer));
        lstProviderwork.add(new providerwork("Parking attendants","UnSkilled", R.drawable.employer));
        lstProviderwork.add(new providerwork("Serving food at functions","UnSkilled", R.drawable.employer));
        lstProviderwork.add(new providerwork("Sales helper","UnSkilled", R.drawable.employer));



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

