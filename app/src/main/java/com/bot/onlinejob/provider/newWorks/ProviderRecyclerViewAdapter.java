package com.bot.onlinejob.provider.newWorks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bot.onlinejob.R;

import java.util.List;

/**
 * Created by Aws on 28/01/2018.
 */

public class ProviderRecyclerViewAdapter extends RecyclerView.Adapter<ProviderRecyclerViewAdapter.MyViewHolder> {

    private Context mContext ;
    private List<providerwork> mData ;


    public ProviderRecyclerViewAdapter(Context mContext, List<providerwork> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.provider_cardview_item_work,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.tv_work_title.setText(mData.get(position).getTitle());
        holder.img_work_thumbnail.setImageResource(mData.get(position).getThumbnail());
        /* holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext,NewWorkSkilledActivity.class);

                // passing data to the book activity
                intent.putExtra("Title",mData.get(position).getTitle());
                intent.putExtra("Thumbnail",mData.get(position).getThumbnail());

                // start the activity
                mContext.startActivity(intent);

            }
        });


         */



    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_work_title;
        ImageView img_work_thumbnail;
        CardView cardView ;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_work_title = (TextView) itemView.findViewById(R.id.work_title_id) ;
            img_work_thumbnail = (ImageView) itemView.findViewById(R.id.work_img_id);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);


        }
    }


}
