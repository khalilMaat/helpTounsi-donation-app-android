package com.example.helptounsi.Activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    //inputs
    private EditText user_name, password;
    private TextView signUpText;
    private Button submit_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //call inputs by id
        user_name = findViewById(R.id.user_name);
        password = findViewById(R.id.password);
        signUpText = findViewById(R.id.sign_up_text);
        submit_button = findViewById(R.id.submit_button);

        //open the register page
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        //handler button action Login
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String var to contient inputs
                String userN,pass;
                user_name.setError(null);
                password.setError(null);

                //Convert input to string
                userN = user_name.getText().toString();
                pass = password.getText().toString();

                if(Validation(userN,pass)){
                    Login(userN,pass);
                }


                //Message(userN+"\n"+pass);

            }
        });

    }

    private void Login(final String userN, final String pass){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.Login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        if(!ServerResponse.equals("Credential Error")){
                            //if the response is good show a msg and open new Activity
                            Toast.makeText(LoginActivity.this, ServerResponse, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                                    .putString("number",userN).apply();
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                                    .putString("city",ServerResponse).apply();
                            LoginActivity.this.finish();
                        }else{
                            Toast.makeText(LoginActivity.this, ServerResponse, Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "ERROR Verifier la connexion WIFI", Toast.LENGTH_SHORT)
                                .show();
                        Log.d("VOLLEY", Objects.requireNonNull(volleyError.getMessage()));
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("number", userN);
                params.put("password", pass);

                return params;
            }

        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
    private void Message(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
    private boolean Validation(String user_name, String password){
        if(user_name.isEmpty()){
            Message("Entrer votre numero !!");
            this.user_name.setError("Entrer votre numero!!");
            return false;
        }else if(password.isEmpty()){
            Message("Entrer votre mot de passe!!");
            this.password.setError("Entrer votre mot de passe !!");
            return false;
        }
        return true;
    }
}