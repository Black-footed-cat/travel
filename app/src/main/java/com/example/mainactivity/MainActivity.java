package com.example.mainactivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    public static Context mContext;
   public String frag_status ="1";
    private EditText editText;
    private String searchStr_travel="서울";
    private String searchStr_hotel="서울";
    private String searchStr_event="서울";
    public String arrange="A";
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter sortAdapter = ArrayAdapter.createFromResource(this, R.array.sort, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(sortAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
           if(position==0){
                    arrange="A";
                    sort();
                }
                if(position==1){
                    arrange="B";
                    sort();
                }
                if(position==2){
                    arrange="C";
                    sort();
                }

            } //이 오버라이드 메소드에서 position은 몇번째 값이 클릭됬는지 알 수 있습니다.
            //getItemAtPosition(position)를 통해서 해당 값을 받아올수있습니다.

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });


        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        mContext = this;
        //TabLayout
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);

        tabs.addTab(tabs.newTab().setText(getString(R.string.travel_info)));
        tabs.addTab(tabs.newTab().setText(getString(R.string.hotel_info)));
        tabs.addTab(tabs.newTab().setText(getString(R.string.event_info)));
        tabs.setTabGravity(tabs.GRAVITY_FILL);

        //어답터설정
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        Main_Tab_adapter myPagerAdapter = new Main_Tab_adapter(getSupportFragmentManager(), 3);
        viewPager.setAdapter(myPagerAdapter);

        //탭메뉴를 클릭하면 해당 프래그먼트로 변경-싱크화
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs){



            @Override
            public void onPageSelected(int position) {
                if(position==0)
                {
                    frag_status="1";

                }
                if(position == 1){
                   frag_status="2";

                }
                if(position==2)
                {
                    frag_status="3";

                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }












    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        View v = menu.findItem(R.id.search_travel).getActionView(); //검색 메뉴를 뷰 객체로 참조
        if (v != null) {
            editText = (EditText) v.findViewById(R.id.editText); //검색 메뉴 아이템 안에 입력상자 객체 참조

            if (editText != null) {
                editText.setOnEditorActionListener(onSearchListener); //입력상자 객체에 리스너 설정
            }
        } else {
            Toast.makeText(getApplicationContext(), "값을 입력해주세요.", Toast.LENGTH_SHORT).show();
        }

        return true;
    }


    private TextView.OnEditorActionListener onSearchListener = new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (event == null || event.getAction() == KeyEvent.ACTION_UP) {
                // 검색 메소드 호출
                search();
                // 키패드 닫기
                InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }

            return (true);
        }
    };
    private void search() {
        if (frag_status=="2")
        {
            searchStr_hotel= editText.getEditableText().toString();
            Fragment_hotel_info.getInstance().search();
        }
        if (frag_status=="1")
        {
            searchStr_travel= editText.getEditableText().toString();
            Fragment_travel_main.getInstance().search();
        }
        if (frag_status=="3")
        {
            searchStr_event = editText.getEditableText().toString();
            Fragment_event_info.getInstance().search();
        }


    }

    private void sort() {
        if (frag_status=="2")
        {
            Fragment_hotel_info.getInstance().search();

        }
        if (frag_status=="1")
        {
            Fragment_travel_main.getInstance().search();
        }
        if (frag_status=="3")
        {
            Fragment_event_info.getInstance().search();
        }


    }
    public String get_search_hotel() { return searchStr_travel; }
    public String get_search_travel() { return searchStr_hotel; }
    public String get_search_event() { return searchStr_event; }
    public String get_arrange() { return arrange; }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {


        String loginStatus = sharedPreferences.getString(getResources().getString(R.string.prefLoginState), "");
        if (loginStatus.equals("loggedin")) {
            menu.getItem(3).setEnabled(true);
            menu.getItem(2).setEnabled(false);
        }else{ // 로그 아웃 한 상태 : 로그인 보이게, 로그아웃은 안보이게
            menu.getItem(3).setEnabled(false);
            menu.getItem(2).setEnabled(true);
        }



        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, Login.class);
        Intent intent2 = new Intent(MainActivity.this, RegisterActivity.class);
        Intent intent3 = new Intent(MainActivity.this, Favorite.class);
        switch(item.getItemId()){

            case R.id.register:
                startActivity(intent2);
                return true;
            case R.id.login:

                startActivity(intent);
                return true;

            case R.id.favorite:
                startActivity(intent3);
                return true;


            case R.id.logout:

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getResources().getString(R.string.prefLoginState), "loggedout");
                editor.apply();
                return true;

         //   case R.id.search_travel:

               // return true;
        }
           return super.onOptionsItemSelected(item);


        }

}




