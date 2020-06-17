package com.example.mainactivity;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import androidx.fragment.app.Fragment;


public class Fragment_map extends Fragment {
    public String mapx;
    public String mapy;
    private Context context;
    public double mapx2;
    public double mapy2;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        ViewGroup mapViewContainer = (ViewGroup) v.findViewById(R.id.map_view);
        MapView mapView = new MapView(getActivity());
        mapViewContainer.addView(mapView);
        context = container.getContext();
       // initDataset();
        // double d_mapx = Double.valueOf(mapx);
       //  double d_mapy = Double.valueOf(mapy);
        mapx=((Travel_info)getActivity()).publicMethod2();
        mapy=((Travel_info)getActivity()).publicMethod3();

        mapx2 = Double.valueOf(mapx);
        mapy2 = Double.valueOf(mapy);
      //  Toast.makeText(context,mapx2+"/"+mapy2,Toast.LENGTH_SHORT).show();


        mapView.setDaumMapApiKey("2f1148e460b2b3c775a295badc75299b");
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(mapy2,mapx2);
        mapView.setMapCenterPoint(mapPoint, true);
        mapView.setZoomLevel(1, true);

        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("관광지");
        marker.setTag(0);
        marker.setMapPoint(mapPoint);
        // 기본으로 제공하는 BluePin 마커 모양.
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
        mapView.addPOIItem(marker);
        return v;
    }





    private void initDataset() {



    }

}




