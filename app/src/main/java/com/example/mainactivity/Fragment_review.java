package com.example.mainactivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Fragment_review extends Fragment {
    SwipeRefreshLayout mSwipeRefreshLayout;
    private static String TAG = "phptest";
    private EditText mEditTexttitle;
    private EditText mEditTextDate;
    private EditText mEditTextSearchKeyword;
    private ArrayList<communityData> mArrayList;
    private communityadapter mAdapter;
    private RecyclerView mRecyclerView;
    private String mJsonString;
    private String mTextViewResult;
    public String contentid;
    SharedPreferences sharedPreferences;
    public String rew1;
    public Fragment_review () {

        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_review, container, false);


        contentid=((Travel_info)getActivity()).publicMethod();
        Context context = getActivity();
        sharedPreferences =context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.listView_main_list);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //mLayoutManager.setReverseLayout(true);
        //mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swifeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mArrayList.clear();
                Fragment_review.GetData task = new Fragment_review.GetData();
                task.execute("http://ahtj1234.dothome.co.kr/Community.php",contentid);
                mSwipeRefreshLayout.setRefreshing(false);

            }
        });
        mArrayList = new ArrayList<>();

        mAdapter = new communityadapter(getActivity(), mArrayList);
        mRecyclerView.setAdapter(mAdapter);

        mArrayList.clear();
        mAdapter.notifyDataSetChanged();



        Fragment_review.GetData task = new Fragment_review.GetData();
        task.execute( "http://ahtj1234.dothome.co.kr/Community.php", contentid);
        Button button4 = (Button)rootView.findViewById(R.id.write);
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                rew1 = getString(R.string.login_check);



                String loginStatus = sharedPreferences.getString(getResources().getString(R.string.prefLoginState), "");
                if (loginStatus.equals("loggedout")) {

                    Toast.makeText(getActivity(),rew1,Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(getActivity(), Login.class);

                    startActivity(intent2);
                }
                else {
                    Intent intent = new Intent(getActivity(), Write.class);

                    startActivity(intent);
                }
            }
        });
        return rootView;
    }
    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(getContext(),
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            mTextViewResult=(result);
            Log.d(TAG, "response - " + result);

            if (result == null){

                Toast.makeText(getContext(), errorString, Toast.LENGTH_SHORT).show();
            }
            else {

                mJsonString = result;
                showResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String nu=(String)params[1];
            String postParameters = "content_id="+nu;

            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "GetData : Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }


    private void showResult(){

        String TAG_JSON="webnautes";
        String TAG_row_number = "row_number";
        String TAG_title = "title";
        String TAG_Date ="date";
        String TAG_Context ="context";
        String TAG_Id ="id";

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String row_number = item.getString(TAG_row_number);
                String title = item.getString(TAG_title);
                String Date = item.getString(TAG_Date);
                String context = item.getString(TAG_Context);
                String id = item.getString(TAG_Id);
                communityData personalData = new communityData();

                personalData.setMember_row_number(row_number);
                personalData.setMember_title(title);
                personalData.setMember_Date(Date);
                personalData.setMember_context(context);
                personalData.setMember_id(id);


                mArrayList.add(personalData);
                mAdapter.notifyDataSetChanged();
            }



        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }

}