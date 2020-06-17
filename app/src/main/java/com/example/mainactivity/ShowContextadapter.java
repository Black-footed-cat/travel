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

public class ShowContextadapter extends RecyclerView.Adapter<ShowContextadapter.CustomViewHolder> {

    private ArrayList<ContextData> mList = null;
    private Activity context = null;
    private Context mContext;

    public ShowContextadapter(Activity context, ArrayList<ContextData> list) {
        this.context = context;
        this.mList = list;
        this.mContext = context;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        protected TextView context1;
        protected ImageView ivimage;

        public CustomViewHolder(View view) {
            super(view);
            this.ivimage = (ImageView)view.findViewById(R.id.iv_item_image);
            this.context1 = (TextView) view.findViewById(R.id.textView_list_context);

        }
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contextitem, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, final int position) {


        viewholder.context1.setText(mList.get(position).getMember_context());
        String picture = ((ShowContext) mContext).publicMethod();
        Glide.with(viewholder.itemView.getContext())
                .load("http://ahtj1234.dothome.co.kr/uploads/"+picture)
                .override(1440, 1440)
                .into(viewholder.ivimage);

        //Click event


        viewholder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                Context context = v.getContext();


            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}