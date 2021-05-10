package com.example.assignment3mcc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {
    EditText editTextLoginEmail;
    EditText editTextLoginPassword;
    String email ;
    String password;
    String token;
    RequestQueue requestQueue;
    String loginEmail ;
    String loginPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        editTextLoginEmail = findViewById(R.id.loginEmail);
        editTextLoginPassword = findViewById(R.id.loginPassword);


        Intent intent =getIntent();
        email=intent.getStringExtra("email");
        password=intent.getStringExtra("password");
    }

    public void login(View v) {
        loginEmail =editTextLoginEmail.getText().toString();
        loginPassword =editTextLoginPassword.getText().toString();
        if(email.equals(loginEmail) && password.equals(loginPassword)) {
            CheckLogin();
            addToken();
        }else{
            Toast.makeText(getApplicationContext(),"Not Match",Toast.LENGTH_SHORT).show();
        }
    }

    private void CheckLogin() {
        String URL="https://mcc-users-api.herokuapp.com/login";
        requestQueue= Volley.newRequestQueue(getApplicationContext());
        Log.d("TAG", "requestQueue: "+requestQueue);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objres = new JSONObject(response);
                    Log.d("TAG", "onResponse: " + objres.toString());
                } catch (JSONException e) {
                    Log.d("TAG", "Server Error ");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "onErrorResponse: "+error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap data = new HashMap();
                data.put("email",loginEmail);
                data.put("password",loginPassword);
                return data;
            }

        };
        requestQueue.add(stringRequest);
    }


    private void addToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(!task.isSuccessful()){
                    Log.e("sara","Failed to get token"+task.getException());
                    return;
                }
                token =task.getResult();
                Log.e("sara","Token: "+token);
                String URL="https://mcc-users-api.herokuapp.com/add_reg_token";
                requestQueue= Volley.newRequestQueue(getApplicationContext());
                Log.d("TAG", "requestQueue: "+requestQueue);
                StringRequest stringRequest=new StringRequest(Request.Method.PUT, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject objres = new JSONObject(response);
                            Log.d("TAG", "onResponse: "+objres.toString());
                        } catch (JSONException e) {
                            Log.d("TAG", "Server Error ");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TAG", "onErrorResponse: "+error);
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap data = new HashMap();
                            data.put("email",loginEmail);
                            data.put("password",loginPassword);
                            data.put("reg_token",token);
                        return data;
                    }
                };
                requestQueue.add(stringRequest);


            }
        });
    }

}