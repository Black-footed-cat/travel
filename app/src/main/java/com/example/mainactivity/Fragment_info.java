package com.example.mainactivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Fragment_info extends Fragment {
    public static ArrayList<Travel_info_item> items = new ArrayList<>();
    public static Travel_info_adapter adapter;
    public String key = "PPq%2FP8f%2FbAJ4oRgKDDoa3Zt7BLBOwjHUD7Sh1wFd27lA739KV54sunuwkXPjq7pUWr4yFlDewA18fEIl4d2c2g%3D%3D";
    ArrayList<Travel_info_item> list = null;
    Travel_info_item bus = null;
    RecyclerView recyclerView;
    public String contentid;
    private Button favoritebtn;
    public String Service_Country="KorService";
    SharedPreferences sharedPreferences;
    public String title;
    private String fan1;
    public Fragment_info () {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        NetworkUtil.setNetworkPolicy();
        View rootView = inflater.inflate(R.layout.fragment_info, container, false);
        Context context = getActivity();
        sharedPreferences = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE); // onCreate
        final String id=sharedPreferences.getString(getResources().getString(R.string.IdState), "");
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        favoritebtn = (Button)rootView.findViewById(R.id.favoritebtn);
        favoritebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginStatus = sharedPreferences.getString(getResources().getString(R.string.prefLoginState), "");

                if (loginStatus.equals("loggedout")) {
                    fan1 = getString(R.string.login_check);
                    Toast.makeText(getActivity(),fan1,Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(getActivity(), Login.class);

                    startActivity(intent2);
                }
                else {
                    try {
                        PHPFavorite request = new PHPFavorite("http://ahtj1234.dothome.co.kr/insert_favorite.php");
                        String result = request.PhPtest(title, id);
                        if (result.equals("1")) {
                            Toast.makeText(getActivity(), "즐겨찾기에 저장되었습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            Toast.makeText(getActivity(), "이미 존재하는 즐겨찾기 입니다.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        com.example.mainactivity.Fragment_info.MyAsyncTask myAsyncTask = new com.example.mainactivity.Fragment_info.MyAsyncTask();
        myAsyncTask.execute();

        return rootView;
    }





    public class MyAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {

            contentid=((Travel_info)getActivity()).publicMethod();
           Service_Country=getString(R.string.service);
            String requestUrl="http://api.visitkorea.or.kr/openapi/service/rest/"+Service_Country+"/detailCommon?"
                    +"&serviceKey="+key
                    +"&numOfRows=10&pageNo=1&MobileOS=ETC&MobileApp=AppTest&contentTypeId=&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&catcodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y"

                    +"&contentId="+contentid;


            try {
                boolean b_locationNo1 = false;
                boolean b_plateNo1 = false;
                boolean b_routeId = false;
                boolean b_stationId = false;

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
                            list = new ArrayList<Travel_info_item>();
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
                                bus = new Travel_info_item();
                            }
                            if (parser.getName().equals("addr1")) b_locationNo1 = true;
                            if (parser.getName().equals("title")) b_plateNo1 = true;
                            if (parser.getName().equals("firstimage")) b_routeId = true;
                            if (parser.getName().equals("overview")) b_stationId = true;
                            break;
                        case XmlPullParser.TEXT:
                            if (b_locationNo1) {
                                bus.setLocationNo1(parser.getText());
                                b_locationNo1 = false;
                            } else if (b_plateNo1) {
                                bus.setPlateNo1(parser.getText());
                                title = bus.getPlateNo1();
                                b_plateNo1 = false;
                            } else if (b_routeId) {
                                bus.setRouteId(parser.getText());
                                b_routeId = false;
                            } else if (b_stationId) {
                                bus.setStationId(parser.getText());
                                b_stationId = false;
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
            Travel_info_adapter adapter = new Travel_info_adapter(getContext(), list);
            recyclerView.setAdapter(adapter);
            //어답터 연결
        }
    }

}


/* public String contentid;
    public String key = "%2Bnv760op%2BarsgrvnuO9HN49TLg%2Fy2IceS%2B0uZw2kJbxTkpZud5jpAn4f2gvTTI6RIasycoqjesa2AwJ3NWsslQ%3D%3D";
    private String requestUrl;
    ArrayList<Item3> list = null;
    Item3 bus = null;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);




        Intent intent = getIntent();
        String contentid2 =intent.getStringExtra("a");
        contentid= contentid2;
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


//        AsyncTask
        com.example.mainactivity.M2.MyAsyncTask myAsyncTask = new com.example.mainactivity.M2.MyAsyncTask();
        myAsyncTask.execute();



    }







    public class MyAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {



            String requestUrl="http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailCommon?"
                    +"&serviceKey="+key
                    +"&numOfRows=10&pageNo=1&MobileOS=ETC&MobileApp=AppTest&contentTypeId=&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&catcodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y"

                    +"&contentId="+contentid;


            try {
                boolean b_locationNo1 = false;
                boolean b_plateNo1 = false;
                boolean b_routeId = false;
                boolean b_stationId = false;

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
                            list = new ArrayList<Item3>();
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
                                bus = new Item3();
                            }
                            if (parser.getName().equals("addr1")) b_locationNo1 = true;
                            if (parser.getName().equals("title")) b_plateNo1 = true;
                            if (parser.getName().equals("firstimage")) b_routeId = true;
                            if (parser.getName().equals("overview")) b_stationId = true;
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
                            } else if (b_stationId) {
                                bus.setStationId(parser.getText());
                                b_stationId = false;
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
            MyAdapter2 adapter = new MyAdapter2(getApplicationContext(), list);
            recyclerView.setAdapter(adapter);
        }
    }
}
*/