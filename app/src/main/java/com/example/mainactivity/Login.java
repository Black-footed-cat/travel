package com.example.mainactivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    MaterialEditText email, password;
    Button login, register;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(Login.this, RegisterActivity.class));

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String txtEmail = email.getText().toString();
                String txtPassword = password.getText().toString();
                if (TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword)) {
                    Toast.makeText(Login.this, "빈칸을 전부 채워주세요", Toast.LENGTH_SHORT).show();
                } else {
                    login(txtEmail, txtPassword);
                }
            }
        });

        String loginStatus = sharedPreferences.getString(getResources().getString(R.string.prefLoginState), "");


        if (loginStatus.equals("loggedin")) {
            Toast.makeText(getApplication(),"이미 로그인상태입니다..",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();

        }

    }

    private void login(final String email, final String password) {
        final ProgressDialog progressDialog = new ProgressDialog(Login.this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("로그인 중");
        progressDialog.show();

        String uRl = "http://ahtj1234.dothome.co.kr/Login.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                if (response.equals("로그인을 성공되었습니다")) {
                    progressDialog.dismiss();
                    Toast.makeText(Login.this, response, Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(getResources().getString(R.string.prefLoginState), "loggedin");
                    editor.putString(getResources().getString(R.string.IdState), email);
                    editor.apply();




                    startActivity(new Intent(Login.this, MainActivity.class));
                    finish();
                }

                else {
                    progressDialog.dismiss();
                    Toast.makeText(Login.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(Login.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                HashMap<String, String> param = new HashMap<>();
                param.put("email", email);
                param.put("psw", password);

                return param;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getmInstance(Login.this).addToRequestQueue(request);

    }

}
