package com.example.mainactivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class Fragment_event_info extends Fragment {
    private String searchString="서울";
    public String key = "PPq%2FP8f%2FbAJ4oRgKDDoa3Zt7BLBOwjHUD7Sh1wFd27lA739KV54sunuwkXPjq7pUWr4yFlDewA18fEIl4d2c2g%3D%3D";
    ArrayList<Travel_item> list = null;
    Travel_item bus = null;
    public String arrage="A";
    RecyclerView recyclerView;
    private String lan,contypeid;
    private static Fragment_event_info instance;
public boolean visible=false;
    public static synchronized Fragment_event_info getInstance() {
        // Required empty public constructor
        return instance;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        instance=this;
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_travel_main, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


        com.example.mainactivity.Fragment_event_info.MyAsyncTask myAsyncTask = new com.example.mainactivity.Fragment_event_info.MyAsyncTask();
        myAsyncTask.execute();


        return rootView;
    }







public void  search(){

            Fragment_event_info.MyAsyncTask myAsyncTask = new Fragment_event_info.MyAsyncTask();
            myAsyncTask.execute();


    }


    public class MyAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            StringBuffer buffer=new StringBuffer();
            arrage=((MainActivity)getActivity()).get_arrange();
            searchString =((MainActivity)getActivity()).get_search_event();
            searchString = URLEncoder.encode(searchString);//한글의 경우 인식이 안되기에 utf-8 방식으로 encoding     //지역 검색 위한 변수


            lan = getString(R.string.service);
            contypeid = getString(R.string.event_contypeid);
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



