package com.example.mainactivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Travel_adapter extends RecyclerView.Adapter<Travel_adapter.MyViewHolder> {
    String contentid;
    String contentid2,distance;
    String mapx,mapy;
    Double mapx2,mapy2;
    Double mymapx,mymapy;
    private ArrayList<Travel_item> mList;
    private LayoutInflater mInflate;
    private Context mContext;

    public Travel_adapter(Context context, ArrayList<Travel_item> itmes) {
        this.mList = itmes;
        this.mInflate = LayoutInflater.from(context);
        this.mContext = context;
    }


    @NonNull
    @Override
    public com.example.mainactivity.Travel_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflate.inflate(R.layout.travel_item, parent, false);
        com.example.mainactivity.Travel_adapter.MyViewHolder viewHolder = new com.example.mainactivity.Travel_adapter.MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.mainactivity.Travel_adapter.MyViewHolder holder, final int position) {
        //binding
        mapx=mList.get(position).mapx;
        mapy=mList.get(position).mapy;
        holder.plateNo1.setText(mList.get(position).plateNo1);
        Glide.with(holder.itemView.getContext())
                .load(mList.get(position).getFirstimage())
                .override(1440, 1440)
                .error(R.drawable.noimg)
                .into(holder.ivImage);
        result_location();
        holder.locationNo1.setText(distance);


        //Click event


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();

                contentid = mList.get(position).routeId;
                mapx = mList.get(position).mapx;
                mapy = mList.get(position).mapy;

                Intent intent = new Intent(mContext, Travel_info.class);
                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("a",contentid);
                intent.putExtra("b",mapx);
                intent.putExtra("c",mapy);
                mContext.startActivity(intent);


            }
        });

    }
    public String result_location(){

        mymapx=((Fragment_travel_main)Fragment_travel_main.getInstance()).get_mymapx();
        mymapy=((Fragment_travel_main)Fragment_travel_main.getInstance()).get_mymapy();
        mapx2 = Double.valueOf(mapx);
        mapy2 = Double.valueOf(mapy);
        distance= getDistance(mymapy,mymapx,mapy2,mapx2);
        return distance;
    }

    public static String getDistance(double lat1 , double lng1 , double lat2 , double lng2 ){
        double EARTH_R, Rad, radLat1, radLat2, radDist;
        double distance, ret;

        EARTH_R = 6371000.0;
        Rad = Math.PI/180;
        radLat1 = Rad * lat1;
        radLat2 = Rad * lat2;
        radDist = Rad * (lng1 - lng2);

        distance = Math.sin(radLat1) * Math.sin(radLat2);
        distance = distance + Math.cos(radLat1) * Math.cos(radLat2) * Math.cos(radDist);
        ret = EARTH_R * Math.acos(distance);

        double rslt = Math.round(Math.round(ret) / 1000);
        String result = rslt + " km";
        if(rslt == 0) result = Math.round(ret) +" m";

        return result;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    //ViewHolder
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView plateNo1;
        public TextView locationNo1;
        ImageView ivImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_item_image);
            plateNo1 = itemView.findViewById(R.id.travel_title);
            locationNo1 = itemView.findViewById(R.id.tv_locationNo1);
        }
    }
}