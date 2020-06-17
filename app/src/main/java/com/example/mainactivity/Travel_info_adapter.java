package com.example.mainactivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
public class Travel_info_adapter extends RecyclerView.Adapter<Travel_info_adapter.MyViewHolder> {
    String contentid;
    private ArrayList<Travel_info_item> mList;
    private LayoutInflater mInflate;
    private Context mContext;

    public Travel_info_adapter(Context context, ArrayList<Travel_info_item> itmes) {
        this.mList = itmes;
        this.mInflate = LayoutInflater.from(context);
        this.mContext = context;
    }


    @NonNull
    @Override
    public Travel_info_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflate.inflate(R.layout.travel_info_item, parent, false);
        Travel_info_adapter.MyViewHolder viewHolder = new Travel_info_adapter.MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Travel_info_adapter.MyViewHolder holder, final int position) {
        //binding
        holder.locationNo1.setText(mList.get(position).locationNo1);
        holder.plateNo1.setText(mList.get(position).plateNo1);
        holder.stationId.setText(mList.get(position).stationId);

        //Click event


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    //ViewHolder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView locationNo1;
        public TextView plateNo1;
        public TextView routeId;
        public TextView stationId;

        public MyViewHolder(View itemView) {
            super(itemView);

            locationNo1 = itemView.findViewById(R.id.tv_locationNo1);
            plateNo1 = itemView.findViewById(R.id.tv_plateNo1);
            stationId = itemView.findViewById(R.id.tv_stationId);

        }
    }

}
