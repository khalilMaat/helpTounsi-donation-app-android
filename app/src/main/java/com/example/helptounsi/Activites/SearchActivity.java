package com.example.helptounsi.Activites;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.helptounsi.R;
import com.example.helptounsi.Utils.Endpoints;
import com.example.helptounsi.Utils.VolleySingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity {

    Button recherche_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final EditText et_city;

        et_city = findViewById(R.id.et_city);
        recherche_btn = findViewById(R.id.recherche_btn);

        recherche_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = et_city.getText().toString();
                if(isValid(city)){
                    get_search_results(city);
                }
            }
        });

    }


    private void get_search_results(final String city) {
        StringRequest stringRequest = new StringRequest(
                Method.POST, Endpoints.search_donors, new Listener<String>() {
            @Override
            public void onResponse(String response) {
                //json response
                Intent intent = new Intent(SearchActivity.this, SearchResults.class);
                intent.putExtra("city", city);
                intent.putExtra("json", response);
                startActivity(intent);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchActivity.this, "Something went wrong:(", Toast.LENGTH_SHORT).show();
                Log.d("VOLLEY", Objects.requireNonNull(error.getMessage()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("city", city);

                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    private boolean isValid(String city){

        if(city.isEmpty()){
            showMsg("Enter city");
            return false;
        }
        return true;
    }


    private void showMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}