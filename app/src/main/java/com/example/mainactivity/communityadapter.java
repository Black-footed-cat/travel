package com.example.mainactivity;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class communityadapter extends RecyclerView.Adapter<communityadapter.CustomViewHolder> {

    private ArrayList<communityData> mList = null;
    private Activity context = null;
    String text;
    private Context mContext;


    public communityadapter(Activity context, ArrayList<communityData> list) {
        this.context = context;
        this.mList = list;
        this.mContext = context;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        //protected TextView row_number;
        protected TextView title;
        protected TextView Date;
        protected TextView context;
        protected TextView id;
        protected ImageView ivimage;

        public CustomViewHolder(View view) {
            super(view);

            //this.row_number = (TextView) view.findViewById(R.id.textView_list_row_number);
            this.title = (TextView) view.findViewById(R.id.textView_list_title);
            this.Date = (TextView) view.findViewById(R.id.textView_list_Date);
            this.context = (TextView) view.findViewById(R.id.textView_list_context);
            this.id = (TextView) view.findViewById(R.id.textView_list_id);
            this.ivimage = (ImageView)view.findViewById(R.id.iv_item_image);
        }
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.communityitem, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, final int position) {

        //viewholder.row_number.setText(mList.get(position).getMember_row_number());
        viewholder.title.setText(mList.get(position).getMember_title());
        viewholder.Date.setText(mList.get(position).getMember_Date());
        viewholder.context.setText(mList.get(position).getMember_context());
        viewholder.id.setText(mList.get(position).getMember_id());
        Glide.with(viewholder.itemView.getContext())
                .load("")
                .apply(new RequestOptions().circleCrop())


                .error(R.drawable.default_profile)
                .into(viewholder.ivimage);
        //Click event


        viewholder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                Context context = v.getContext();
                text = mList.get(position).member_address;


               Intent intent = new Intent(mContext, ShowContext.class);
               intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("a",mList.get(position).getMember_row_number());
                mContext.startActivity(intent);
            }
        });

    }
    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}