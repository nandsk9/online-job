package com.bot.onlinejob.provider.status;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bot.onlinejob.R;
import com.bot.onlinejob.bean.PostJobBean;
import com.bot.onlinejob.provider.status.adapter.ProviderAcceptedJobAdapter;
import com.bot.onlinejob.provider.status.adapter.ProviderActiveJobAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProviderStatusJobAcceptedFragment extends Fragment {
    Activity referenceActivity;
    View parentHolder;
    DatabaseReference databaseReferenceToCurrentUserPostedJobId;
    DatabaseReference databaseReferenceToAllUserPostedJobId;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    //list that contain job id
    List<String> jobIDList;
    //list that contains jobPostObject
    List<PostJobBean> postJobBeansAccepted;
    //recycler view and its adapter
    RecyclerView recyclerViewAcceptedJob;
    ProviderAcceptedJobAdapter providerAcceptedJobAdapter;
    public ProviderStatusJobAcceptedFragment() {
        super();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        referenceActivity=getActivity();
        parentHolder = inflater.inflate(R.layout.provider_status_job_accepted_fragment, container,
                false);
        Log.e("Frag : ","Status Accepted");


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        jobIDList = new ArrayList<>();
        postJobBeansAccepted = new ArrayList<>();
        //recycler view
        recyclerViewAcceptedJob = parentHolder.findViewById(R.id.provider_status_accepted_job_rv);
        //connect to database
        //current user posted work id reference, to get job id posted
        //by current user
        databaseReferenceToCurrentUserPostedJobId = FirebaseDatabase.getInstance().getReference("users/"+mUser.getUid()).child("work-posted");
        //all users posted job
        databaseReferenceToAllUserPostedJobId = FirebaseDatabase.getInstance().getReference("work-posted");


        Log.e("Uid :: ",mUser.getUid());
        // Adding Add Value Event Listener to databaseReference.
        databaseReferenceToCurrentUserPostedJobId.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //to solve repeating data
                if(jobIDList!=null || !jobIDList.isEmpty()){
                    jobIDList.clear();
                }

                for (DataSnapshot jobIdSnapshot : snapshot.getChildren()) {
                    Log.e("Data snapshot : ",jobIdSnapshot.toString());

                    Log.e("Job Id :::: ",jobIdSnapshot.getValue().toString());
                    //list contains job ids
                    jobIDList.add(jobIdSnapshot.getValue().toString());
                }


                //after reading all id posted by current user
                databaseReferenceToAllUserPostedJobId.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        //to solve repeating data
                        if(postJobBeansAccepted!=null || !postJobBeansAccepted.isEmpty()){
                            postJobBeansAccepted.clear();
                        }

                        for (DataSnapshot jobPostSnapshot : snapshot.getChildren()) {
                            Log.e("Data snapshot : ",jobPostSnapshot.toString());

                            Log.e("Job Post :::: ",jobPostSnapshot.getValue().toString());
                            PostJobBean postJobBean = jobPostSnapshot.getValue(PostJobBean.class);
                            for(int i=0;i<jobIDList.size();i++){
                                if(jobIDList.get(i).matches(postJobBean.getJobId())){
                                    //add the post to accepted job list
                                    if(postJobBean.getStatus().matches("Accepted")){
                                        postJobBeansAccepted.add(postJobBean);
                                        //once in a loop
                                        //if it matches
                                        //break the loop and try with next snapshot
                                        break;
                                    }

                                }
                            }
                        }

                        //call the adapter and initialize recycler view
                        //after finding current user posted job
                        providerAcceptedJobAdapter = new ProviderAcceptedJobAdapter(getContext(),postJobBeansAccepted);
                        recyclerViewAcceptedJob.setAdapter(providerAcceptedJobAdapter);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Hiding the progress bar.
                        Toast.makeText(referenceActivity, "Database Cancelled", Toast.LENGTH_SHORT).show();

                    }
                });// end of inner database reference




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Hiding the progress bar.
                Toast.makeText(referenceActivity, "Database Cancelled", Toast.LENGTH_SHORT).show();

            }
        });// end of outer database reference




        //return parentHolder;
        return parentHolder;
    }
}
