package com.example.assignment3mcc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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

public class MainActivity extends AppCompatActivity {
    EditText editTextFirstName;
    EditText editTextSecondName;
    EditText editTextEmail;
    EditText editTextPassword;
    RequestQueue requestQueue;
    String firstName;
    String secondName;
    String email ;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextFirstName = findViewById(R.id.firstName);
        editTextSecondName = findViewById(R.id.secondName);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
    }
    public void signUp(View v){
            firstName =editTextFirstName.getText().toString();
            secondName =editTextSecondName.getText().toString();
            email =editTextEmail.getText().toString();
            password =editTextPassword.getText().toString();
            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            intent.putExtra("email",email);
            intent.putExtra("password",password);
            addUser();
            startActivity(intent);
    }
    private void addUser() {
        String URL="https://mcc-users-api.herokuapp.com/add_new_user";
        requestQueue= Volley.newRequestQueue(getApplicationContext());
        Log.d("TAG", "requestQueue: "+requestQueue);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
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
                data.put("firstName",firstName);
                data.put("secondName",secondName);
                data.put("email",email);
                data.put("password",password);
                return data;
            }

        };
        requestQueue.add(stringRequest);
    }







}