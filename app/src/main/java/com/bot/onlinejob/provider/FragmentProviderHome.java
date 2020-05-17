package com.bot.onlinejob.provider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bot.onlinejob.R;
import com.bot.onlinejob.bean.PostJobBean;
import com.bot.onlinejob.provider.adapters.ProviderShowAllLatestWorkRVAdapter;
import com.bot.onlinejob.provider.adapters.ViewPagerAdapter;
import com.bot.onlinejob.provider.newWorks.ProviderNewWorkSemiSkilledActivity;
import com.bot.onlinejob.provider.newWorks.ProviderNewWorkSkilledActivity;
import com.bot.onlinejob.provider.newWorks.ProviderNewWorkUnSkilledActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentProviderHome extends Fragment {
    Activity referenceActivity;
    View parentHolder;
    RecyclerView recyclerViewShowAllNewWorks;
    ProviderShowAllLatestWorkRVAdapter providerShowAllLatestWorkRVAdapter;
    DatabaseReference databaseReferenceToAllActiveWorkPost;
    List<PostJobBean> activePostJobBeansList;
    //categories card
    CardView skilledCard,unSkilledCard,semiSkilledCard;


    //image flipper


    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;



    //required empty constructor
    public FragmentProviderHome(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        referenceActivity=getActivity();
        parentHolder = inflater.inflate(R.layout.provider_home_fragment, container, false);
        Log.e("Frag : ","Home");

        skilledCard = parentHolder.findViewById(R.id.provider_home_skilled_card);
        semiSkilledCard = parentHolder.findViewById(R.id.provider_home_semi_skilled_card);
        unSkilledCard = parentHolder.findViewById(R.id.provider_home_un_skilled_card);

        //all latest active jobs
        activePostJobBeansList = new ArrayList<>();
        recyclerViewShowAllNewWorks = parentHolder.findViewById(R.id.provider_show_all_new_jobs_recycler_view);
        recyclerViewShowAllNewWorks.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        //all users posted job
        databaseReferenceToAllActiveWorkPost = FirebaseDatabase.getInstance().getReference("work-posted");
        // Adding Add Value Event Listener to databaseReference.
                databaseReferenceToAllActiveWorkPost.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        //to solve repeating data
                        if(activePostJobBeansList!=null || !activePostJobBeansList.isEmpty()){
                            activePostJobBeansList.clear();
                        }

                        for (DataSnapshot jobPostSnapshot : snapshot.getChildren()) {
                            Log.e("Data snapshot : ",jobPostSnapshot.toString());

                            Log.e("Job Post :::: ",jobPostSnapshot.getValue().toString());
                            PostJobBean postJobBean = jobPostSnapshot.getValue(PostJobBean.class);
                                    //add the post to active job list
                                    if(postJobBean.getStatus().matches("Active")){
                                        activePostJobBeansList.add(postJobBean);

                            }
                        }

                        //call the adapter and initialize recycler view
                        //after finding current user posted job
                        providerShowAllLatestWorkRVAdapter = new ProviderShowAllLatestWorkRVAdapter(getContext(),activePostJobBeansList);
                        recyclerViewShowAllNewWorks.setAdapter(providerShowAllLatestWorkRVAdapter);

                    }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Hiding the progress bar.
                Toast.makeText(referenceActivity, "Database Cancelled", Toast.LENGTH_SHORT).show();

            }
        });// end of database reference

        //click handles on category
        skilledCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(referenceActivity, ProviderNewWorkSkilledActivity.class);
                startActivity(intent);
            }
        });

       semiSkilledCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(referenceActivity, ProviderNewWorkSemiSkilledActivity.class);
                startActivity(intent);
            }
        });

        unSkilledCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(referenceActivity, ProviderNewWorkUnSkilledActivity.class);
                startActivity(intent);
            }
        });


//======
        viewPager = parentHolder.findViewById(R.id.viewPager);

        sliderDotspanel = parentHolder.findViewById(R.id.SliderDots);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity());

        viewPager.setAdapter(viewPagerAdapter);

        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for(int i = 0; i < dotscount; i++){

            dots[i] = new ImageView(getContext());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.non_active_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


//=======






        //return parentHolder;
        return parentHolder;
    }
}
