package com.example.mainactivity;
import android.content.Context;
import android.content.Intent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ShowContext extends AppCompatActivity {
    private static String TAG = "phptest";
private  String row_num;
    private EditText mEditTexttitle;
    private EditText mEditTextDate;
    private EditText mEditTextSearchKeyword;
    private String mTextViewResult;
    private ArrayList<ContextData> mArrayList;
    private ShowContextadapter mAdapter;
    private RecyclerView mRecyclerView;
    private String mJsonString;
    public String picture;
    private Button btn_delete;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showcontext);
        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        mRecyclerView = (RecyclerView) findViewById(R.id.listView_main_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        NetworkUtil.setNetworkPolicy();
        Intent intent = getIntent();
        final String row_num =intent.getStringExtra("a");

        mArrayList = new ArrayList<>();

        mAdapter = new ShowContextadapter(this, mArrayList);

        mRecyclerView.setAdapter(mAdapter);

        mArrayList.clear();
        mAdapter.notifyDataSetChanged();

        ShowContext.GetData task = new ShowContext.GetData();
        task.execute( "http://ahtj1234.dothome.co.kr/Context.php",row_num);
        String loginStatus = sharedPreferences.getString(getResources().getString(R.string.prefLoginState), "");
        final String Idstate=sharedPreferences.getString(getResources().getString(R.string.IdState), "");

        if (loginStatus.equals("loggedout")) {
            Toast.makeText(getApplication(),"로그인이 필요합니다.",Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(getApplication(), Login.class);
            startActivity(intent2);
            finish();
        }

        //post>>delete.php검사>>삭제
        //db ID값추가 글작성할때 자동으로 ID값 전송
        btn_delete = (Button)findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    PHPDelete request = new PHPDelete("http://ahtj1234.dothome.co.kr/delete.php");
                    String result = request.PhPDel(row_num,Idstate);
                    if(result.equals("1")){
                        Toast.makeText(getApplication(),"완료되었습니다",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplication(),"글 작성자가 아닙니다.",Toast.LENGTH_SHORT).show();
                    }
                }catch (MalformedURLException e){
                    e.printStackTrace();
                }
            }
        });





    }




        private class GetData extends AsyncTask<String, Void, String> {

            ProgressDialog progressDialog;
            String errorString = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(ShowContext.this,
                        "Please Wait", null, true, true);
            }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            mTextViewResult=result;
            Log.d(TAG, "response - " + result);

            if (result == null){

                Toast.makeText(ShowContext.this, errorString, Toast.LENGTH_SHORT).show();
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
            String postParameters = "num="+nu;


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
    public String publicMethod() { return picture; }

    private void showResult(){

        String TAG_JSON="webnautes";
        String TAG_context = "context";
        String TAG_picture = "picture";


        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);


                String context = item.getString(TAG_context);
                picture = item.getString(TAG_picture);

                ContextData personalData = new ContextData();


                personalData.setMember_context(context);




                mArrayList.add(personalData);
                mAdapter.notifyDataSetChanged();
            }



        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }

}
