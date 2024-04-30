package com.example.helptounsi.Activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.helptounsi.R;
import com.example.helptounsi.Utils.Endpoints;
import com.example.helptounsi.Utils.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    //inputs
    private EditText nameT,cityT,mobileT,passwordT,passwordCT;
    private Button submit_button_R,sign_up_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //call inputs by id
        nameT = findViewById(R.id.name);
        cityT = findViewById(R.id.city);
        mobileT = findViewById(R.id.phone);
        passwordT = findViewById(R.id.password);
        passwordCT = findViewById(R.id.confirm_password);
        submit_button_R = findViewById(R.id.submit_button_R);
        sign_up_text = findViewById(R.id.sign_up_text);


        //Handler action when click to button
        submit_button_R.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String variable to contain inputs layout
                String name,city,mobile,password,confirm_pass;

                //Convert To String
                name = nameT.getText().toString();
                city = cityT.getText().toString();
                mobile = mobileT.getText().toString();
                password = passwordT.getText().toString();
                confirm_pass = passwordCT.getText().toString();

                //Constraint to each layout
                if(validateInput(name,city,mobile,password,confirm_pass)){
                    Register(name, city, mobile, password, confirm_pass);
                }
                //Show Message
                //Message(name+"\n"+city+"\n"+mobile+"\n"+password+"\n"+confirm_pass);
            }
        });

        sign_up_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                RegisterActivity.this.finish();
            }
        });

    }

    private void Register(String name, String city,String mobile, String password, String confrim_password){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.Register_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        if(ServerResponse.equals("Success")){
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                                    .putString("city",city).apply();
                            //if the response is good show a msg and open new Activity
                            Toast.makeText(RegisterActivity.this, ServerResponse, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            RegisterActivity.this.finish();
                        }else{
                            Toast.makeText(RegisterActivity.this, ServerResponse, Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "ERROR thabet fi Internet (WIFI)", Toast.LENGTH_SHORT)
                                .show();
                        Log.d("VOLLEY", volleyError.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("city", city);
                params.put("number", mobile);
                params.put("password", password);
                params.put("confirm_password", confrim_password);
                return params;
            }

        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);


    }

    private boolean validateInput(String name, String city, String mobile, String password, String confirm_pass){
        if(name.isEmpty()){
            Message("7ot esmk yehdik");
            return false;
        }else if(city.isEmpty()){
            Message("7ot blask");
            return false;
        }else if(mobile.isEmpty()){
            Message("7ot ra9mek");
            return false;
        }else if(mobile.length() != 8){
            Message("7ot ra9mek bel s7i7 yy rajel");
            return false;
        }else if(password.isEmpty()){
            Message("tansa el password zeda !!!");
            return false;
        }else if(password.length() < 8) {
            Message("Password mte3k ma3jebnich!!!");
            return false;
        }else if(!password.equals(confirm_pass)){
            Message("Ti 3awed el password");
            return false;
        }
        return true;
    }

    private void Message(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}