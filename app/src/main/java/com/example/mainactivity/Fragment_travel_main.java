package com.example.mainactivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


public class Fragment_travel_main extends Fragment {
    private String lan,contypeid,distance,mapx,mapy;
    private String searchString="서울";
    private double mymapx,mymapy,mapx2,mapy2;
    public String arrage="A";
        public String key = "PPq%2FP8f%2FbAJ4oRgKDDoa3Zt7BLBOwjHUD7Sh1wFd27lA739KV54sunuwkXPjq7pUWr4yFlDewA18fEIl4d2c2g%3D%3D";
        ArrayList<Travel_item> list = null;
        Travel_item bus = null;
        RecyclerView recyclerView;
    private LocationManager locationManager;
    private static final int REQUEST_CODE_LOCATION = 2;
    private static Fragment_travel_main instance;
    public static synchronized Fragment_travel_main getInstance() {
            return instance;
        }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        instance=this;


        locationManager = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
        Location userLocation = getMyLocation();
        if( userLocation != null ) {
            double latitude = userLocation.getLatitude();
            double longitude = userLocation.getLongitude();

          mymapy=latitude;
          mymapx=longitude;
        }


        View rootView = inflater.inflate(R.layout.fragment_travel_main, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);



        com.example.mainactivity.Fragment_travel_main.MyAsyncTask myAsyncTask = new com.example.mainactivity.Fragment_travel_main.MyAsyncTask();
        myAsyncTask.execute();

        return rootView;
    }


   public Double  get_mymapx(){

       getMyLocation();
       return mymapx;


   }
    public Double  get_mymapy(){

        getMyLocation();
        return mymapy;


    }
    public void  search(){

        Fragment_travel_main.MyAsyncTask myAsyncTask = new Fragment_travel_main.MyAsyncTask();
        myAsyncTask.execute();


    }

    private Location getMyLocation() {
        Location currentLocation = null;
        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("////////////사용자에게 권한을 요청해야함");
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, this.REQUEST_CODE_LOCATION);
            getMyLocation(); //이건 써도되고 안써도 되지만, 전 권한 승인하면 즉시 위치값 받아오려고 썼습니다!
        }
        else {
            System.out.println("////////////권한요청 안해도됨");

            // 수동으로 위치 구하기
            String locationProvider = LocationManager.GPS_PROVIDER;
            currentLocation = locationManager.getLastKnownLocation(locationProvider);
            if (currentLocation != null) {
                mymapx = currentLocation.getLongitude();
                mymapy = currentLocation.getLatitude();
            }
        }
        return currentLocation;
    }






        public class MyAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... strings) {
                StringBuffer buffer=new StringBuffer();
                arrage=((MainActivity)getActivity()).get_arrange();
                searchString =((MainActivity)getActivity()).get_search_travel();
                searchString = URLEncoder.encode(searchString);//한글의 경우 인식이 안되기에 utf-8 방식으로 encoding     //지역 검색 위한 변수
                lan = getString(R.string.service);
                contypeid = getString(R.string.m_contypeid);
                String requestUrl="http://api.visitkorea.or.kr/openapi/service/rest/" +
                        lan+"/searchKeyword?"
                        +"&serviceKey="+key
                        +"&numOfRows=100&pageNo=1&MobileOS=ETC&MobileApp=AppTest" +
                        "&arrange="+arrage
                        +"&contentTypeId="
                        + contypeid
                        +"&keyword="+searchString;


                try {
                    boolean b_locationNo1 = false;
                    boolean b_plateNo1 = false;
                    boolean b_routeId = false;
                    boolean b_mapx = false;
                    boolean b_mapy = false;
                    boolean b_firstimage = false;
                    URL url = new URL(requestUrl);
                    InputStream is = url.openStream();
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = factory.newPullParser();
                    parser.setInput(new InputStreamReader(is, "UTF-8"));

                    String tag;
                    int eventType = parser.getEventType();

                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        switch (eventType) {
                            case XmlPullParser.START_DOCUMENT:
                                list = new ArrayList<Travel_item>();
                                break;
                            case XmlPullParser.END_DOCUMENT:
                                break;
                            case XmlPullParser.END_TAG:
                                if (parser.getName().equals("item") && bus != null) {
                                    list.add(bus);
                                }
                                break;
                            case XmlPullParser.START_TAG:
                                if (parser.getName().equals("item")) {
                                    bus = new Travel_item();
                                }
                                if (parser.getName().equals("addr1")) b_locationNo1 = true;
                                if (parser.getName().equals("title")) b_plateNo1 = true;
                                if (parser.getName().equals("contentid")) b_routeId = true;
                                if (parser.getName().equals("mapx")) b_mapx = true;
                                if (parser.getName().equals("mapy")) b_mapy = true;
                                if (parser.getName().equals("firstimage")) b_firstimage = true;
                                break;
                            case XmlPullParser.TEXT:
                                if (b_locationNo1) {
                                    bus.setLocationNo1(parser.getText());
                                    b_locationNo1 = false;
                                } else if (b_plateNo1) {
                                    bus.setPlateNo1(parser.getText());
                                    b_plateNo1 = false;
                                } else if (b_routeId) {
                                    bus.setRouteId(parser.getText());
                                    b_routeId = false;
                                } else if (b_mapx) {
                                    bus.setMapx(parser.getText());
                                    b_mapx = false;
                                } else if (b_mapy) {
                                    bus.setMapy(parser.getText());
                                    b_mapy = false;
                                }
                                else if (b_firstimage) {
                                    bus.setFirstimage(parser.getText());
                                    b_firstimage = false;
                                }
                                break;
                            default:
                                throw new IllegalStateException("Unexpected value: " + eventType);
                        }

                        eventType = parser.next();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                //어답터 연결
                Travel_adapter adapter = new Travel_adapter(getContext(), list);
                recyclerView.setAdapter(adapter);

            }
        }
    }

