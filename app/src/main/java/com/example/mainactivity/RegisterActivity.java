package com.example.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

public class

RegisterActivity extends AppCompatActivity {

    MaterialEditText userName, email, password;

    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String txtUserName = userName.getText().toString();
                String txtEmail = email.getText().toString();
                String txtPassword = password.getText().toString();


                if (TextUtils.isEmpty(txtUserName) || TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword))
                       {
                    Toast.makeText(RegisterActivity.this, "빈칸을 전부 채워주세요", Toast.LENGTH_SHORT).show();
                } else {


                        registerNewAccount(txtUserName, txtEmail, txtPassword );

                }
            }
        });
    }

    private void registerNewAccount(final String username, final String email, final String password
                                    ) {

        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("회원가입 중");
        progressDialog.show();

        String uRl = "http://ahtj1234.dothome.co.kr/Register.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                if (response.equals("회원가입을 완료했습니다.")) {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, Login.class));
                    finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                HashMap<String, String> param = new HashMap<>();
                param.put("username", username);
                param.put("email", email);
                param.put("psw", password);

                return param;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getmInstance(RegisterActivity.this).addToRequestQueue(request);

    }

}
