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

public class ProviderNewWorkSemiSkilledActivity extends AppCompatActivity {
    Button backto_semihome;
    List<providerwork> lstProviderwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider_new_work_semiskilled_activity);

        backto_semihome = (Button) findViewById(R.id.backto_semihome);
        backto_semihome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProviderNewWorkSemiSkilledActivity.this, ProviderHomeActivity.class);
                startActivity(intent);
            }
        });

        lstProviderwork = new ArrayList<>();
        lstProviderwork.add(new providerwork("Account/Clerk","SemiSkilled", R.drawable.employer));
        lstProviderwork.add(new providerwork("Agriculture","SemiSkilled", R.drawable.employer));
        lstProviderwork.add(new providerwork("Drawing Teacher","SemiSkilled", R.drawable.employer));
        lstProviderwork.add(new providerwork("Home Tuition-Primary","SemiSkilled", R.drawable.employer));
        lstProviderwork.add(new providerwork("Home Tuition (UP-CBSE)","SemiSkilled", R.drawable.employer));
        lstProviderwork.add(new providerwork("Home Tuition-Upper Primary","SemiSkilled", R.drawable.employer));
        lstProviderwork.add(new providerwork("Music Teacher","SemiSkilled", R.drawable.employer));
        lstProviderwork.add(new providerwork("Painter","SemiSkilled", R.drawable.employer));
        lstProviderwork.add(new providerwork("Receptionist","SemiSkilled", R.drawable.employer));
        lstProviderwork.add(new providerwork("Salesperson","SemiSkilled", R.drawable.employer));
        lstProviderwork.add(new providerwork("Sports Coach","SemiSkilled", R.drawable.employer));
        lstProviderwork.add(new providerwork("Telecaller","SemiSkilled", R.drawable.employer));
        lstProviderwork.add(new providerwork("Tyre Mechanic","SemiSkilled", R.drawable.employer));
        lstProviderwork.add(new providerwork("Yoga Teacher","SemiSkilled", R.drawable.employer));


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
