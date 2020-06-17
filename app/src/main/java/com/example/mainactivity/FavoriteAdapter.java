package com.example.mainactivity;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.CustomViewHolder> {

    private ArrayList<FavoriteData> mList = null;
    private Activity context = null;
    String text;
    private Context mContext;

    public FavoriteAdapter(Activity context, ArrayList<FavoriteData> list) {
        this.context = context;
        this.mList = list;
        this.mContext = context;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        //protected TextView row_number;
        protected TextView favor;
        public CustomViewHolder(View view) {
            super(view);
           //this.row_number = (TextView) view.findViewById(R.id.textView_list_row_number);
            this.favor = (TextView) view.findViewById(R.id.textView_list_favorite);
        }
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.favoriteitem, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, final int position) {

        //viewholder.row_number.setText(mList.get(position).getMember_row_number());
        viewholder.favor.setText(mList.get(position).getFavor_id());


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