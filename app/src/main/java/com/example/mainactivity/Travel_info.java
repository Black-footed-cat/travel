package com.example.mainactivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
public class Travel_info extends AppCompatActivity {
    String contentid;
    String mapx, mapy;
    public double mapx2;
    public double mapy2;
    public static Context mContext;
    private String st_info,st_location,st_review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_info);
        mContext = this;
        Intent intent = getIntent();
        String contentid2 =intent.getStringExtra("a");
        mapx=intent.getStringExtra("b");
        mapy=intent.getStringExtra("c");
        double mapx2 = Double.valueOf(mapx);
        double mapy2 = Double.valueOf(mapy);

        contentid= contentid2;


        //TabLayout
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        st_info = getString(R.string.st_info);
        st_location = getString(R.string.st_location);
        st_review = getString(R.string.st_review);
        tabs.addTab(tabs.newTab().setText(st_info));
        tabs.addTab(tabs.newTab().setText(st_location));
        tabs.addTab(tabs.newTab().setText(st_review));
        tabs.setTabGravity(tabs.GRAVITY_FILL);

        //어답터설정
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        final Tab_adapter myPagerAdapter = new Tab_adapter(getSupportFragmentManager(), 3);
        viewPager.setAdapter(myPagerAdapter);

        //탭메뉴를 클릭하면 해당 프래그먼트로 변경-싱크화
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
    }
    public String publicMethod() { return contentid; }
    public  String publicMethod2() { return mapx; }
    public String publicMethod3() { return mapy; }
    public String publicMethod4() { return contentid; }
    public  String publicMethod5() { return mapx; }
    public String publicMethod6() { return mapy; }
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



        setContentView(R.layout.activity_m2);
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