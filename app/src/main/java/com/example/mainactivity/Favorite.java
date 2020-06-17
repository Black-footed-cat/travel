package com.example.mainactivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


public class Favorite extends AppCompatActivity {
    private static String TAG = "phptest";
    private  String favorites;
    private EditText mEditTexttitle;
    private EditText mEditTextDate;
    private EditText mEditTextSearchKeyword;
    private String mTextViewResult;
    private ArrayList<FavoriteData> mArrayList;
    private FavoriteAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private String mJsonString;
    private String ran1;

    SharedPreferences sharedPreferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite);


        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE); // onCreate
        final String id=sharedPreferences.getString(getResources().getString(R.string.IdState), "");

        mRecyclerView = (RecyclerView) findViewById(R.id.listView_main_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        NetworkUtil.setNetworkPolicy();

        mArrayList = new ArrayList<>();

        mAdapter = new FavoriteAdapter(this, mArrayList);

        mRecyclerView.setAdapter(mAdapter);

        mArrayList.clear();
        mAdapter.notifyDataSetChanged();

        Favorite.GetData task = new Favorite.GetData();
        task.execute( "http://ahtj1234.dothome.co.kr/favorites.php",id);
        String loginStatus = sharedPreferences.getString(getResources().getString(R.string.prefLoginState), "");
        final String Idstate=sharedPreferences.getString(getResources().getString(R.string.IdState), "");

        if (loginStatus.equals("loggedout")) {
            ran1 = getString(R.string.login_check);
            Toast.makeText(getApplication(), ran1 ,Toast.LENGTH_SHORT).show();
            finish();

        }
    }
    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = "잘못됨";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Favorite.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            mTextViewResult=result;
            Log.d(TAG, "response - " + result);

            if (result == null){

                Toast.makeText(Favorite.this, errorString, Toast.LENGTH_SHORT).show();
            }
            else {

                mJsonString = result;
                showResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String id=(String)params[1];
            String postParameters = "id="+id;


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

        String TAG_favorite = "favorites";



        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);


                String context = item.getString(TAG_favorite);


                //Toast.makeText(ShowContext.this, , Toast.LENGTH_SHORT).show();
                FavoriteData personalData = new FavoriteData();


                personalData.setFavor_id(context);



                mArrayList.add(personalData);
                mAdapter.notifyDataSetChanged();
            }



        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }

}
