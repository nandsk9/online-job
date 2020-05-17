package com.bot.onlinejob.provider.status.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bot.onlinejob.R;
import com.bot.onlinejob.bean.PostJobBean;
import com.bot.onlinejob.provider.status.ProviderJobPostDetails;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.List;

public class ProviderAcceptedJobAdapter extends RecyclerView.Adapter<ProviderAcceptedJobAdapter.ViewHolder>{
    FirebaseUser currentUser;
    FirebaseAuth firebaseAuth;
    private static final String TAG = "ProviderAcceptedJobAdapter";
    Context context;
    List<PostJobBean> acceptedJobList;



    public ProviderAcceptedJobAdapter(Context context,List<PostJobBean> acceptedJobList) {
        this.context = context;
        this.acceptedJobList=acceptedJobList;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.provider_job_status_accepted_rv_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostJobBean postJobBean = acceptedJobList.get(position);
        //bind the data to view
        //loading image to recycler view using glide library
        Glide.with(context).load(postJobBean.getJobImgUrl()).into(holder.imageViewJobImg);
        holder.textViewName.setText("Job Name : "+postJobBean.getJobName());
        holder.textViewLocation.setText("Job Location : "+postJobBean.getJobLocation());
        holder.textViewCategory.setText("Job Category : "+postJobBean.getJobCategory());
        holder.textViewDate.setText("Job Date : "+postJobBean.getJobDate());

    }

    @Override
    public int getItemCount() {
        return acceptedJobList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //declare the views
        ImageView imageViewJobImg;
        TextView textViewName,textViewLocation,textViewCategory,textViewDate;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            //initialize the views
            imageViewJobImg= itemView.findViewById(R.id.provider_rv_accepted_job_img);
            textViewName = itemView.findViewById(R.id.provider_rv_accepted_job_tv_job_name);
            textViewLocation = itemView.findViewById(R.id.provider_rv_accepted_job_tv_job_location);
            textViewCategory = itemView.findViewById(R.id.provider_rv_accepted_job_tv_job_category);
            textViewDate = itemView.findViewById(R.id.provider_rv_accepted_job_tv_job_date);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            Intent intentToPostedJobDetails = new Intent(context, ProviderJobPostDetails.class);
            PostJobBean postJobBeanTemp = acceptedJobList.get(getAdapterPosition());
            intentToPostedJobDetails.putExtra("statusPostJobBean",postJobBeanTemp);
            context.startActivity(intentToPostedJobDetails);


        }
    }
}
