package com.bot.onlinejob.provider.adapters;

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
import java.util.List;

public class ProviderShowAllLatestWorkRVAdapter extends RecyclerView.Adapter<ProviderShowAllLatestWorkRVAdapter.ViewHolder> {
    private static final String TAG = "ProviderShowAllLatestWorkRVAdapter";
    Context context;
    List<PostJobBean> jobList;


    public ProviderShowAllLatestWorkRVAdapter(Context context, List<PostJobBean> jobList) {
        this.context = context;
        this.jobList = jobList;

    }

    @NonNull
    @Override
    public ProviderShowAllLatestWorkRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.provider_show_all_latest_job_rv_item, parent, false);
        ProviderShowAllLatestWorkRVAdapter.ViewHolder viewHolder = new ProviderShowAllLatestWorkRVAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostJobBean postJobBean = jobList.get(position);
        //bind the data to view
        //loading image to recycler view using glide library
        Glide.with(context).load(postJobBean.getJobImgUrl()).into(holder.imageViewJobImg);
        holder.textViewName.setText(postJobBean.getJobName());
        holder.textViewLocation.setText(postJobBean.getJobLocation());
        holder.textViewDate.setText(postJobBean.getJobDate());
        holder.textViewWage.setText(" ₹ "+postJobBean.getJobWage());

    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //declare the views
        ImageView imageViewJobImg;
        TextView textViewName, textViewLocation, textViewWage, textViewDate;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            //initialize the views
            imageViewJobImg = itemView.findViewById(R.id.p_s_a_l_w_r_v_job_image);
            textViewName = itemView.findViewById(R.id.p_s_a_l_w_r_v_job_name);
            textViewLocation = itemView.findViewById(R.id.p_s_a_l_w_r_v_job_location);
            textViewDate = itemView.findViewById(R.id.p_s_a_l_w_r_v_job_date);
            textViewWage = itemView.findViewById(R.id.p_s_a_l_w_r_v_job_wage);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            Intent intentToPostedJobDetails = new Intent(context, ProviderJobPostDetails.class);
            PostJobBean postJobBeanTemp = jobList.get(getAdapterPosition());
            intentToPostedJobDetails.putExtra("statusPostJobBean",postJobBeanTemp);
            context.startActivity(intentToPostedJobDetails);


        }
    }
}