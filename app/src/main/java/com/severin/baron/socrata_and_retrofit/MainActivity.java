package com.severin.baron.socrata_and_retrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.severin.baron.socrata_and_retrofit.Adapter.PermitAdapter;
import com.severin.baron.socrata_and_retrofit.Model.BuildingPermit;
import com.severin.baron.socrata_and_retrofit.REST.ApiClient;
import com.severin.baron.socrata_and_retrofit.REST.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    ApiInterface apiInterface;
    RecyclerView recyclerView;
    String apiToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Replace this with your api token
        apiToken = getResources().getString(R.string.api_key);

        // The Retrofit object is a singleton that contains our base URL, 
        // and can be used with different queries
        Retrofit client = ApiClient.getClient();
        // ApiInterface is automatically subclassed by Retrofit, and 
        // a client is built according to annotations within our interface
        apiInterface = client.create(ApiInterface.class);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_permits);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button constructionButton = (Button) findViewById(R.id.button_construction);
        Button valueButton = (Button) findViewById(R.id.button_value);
        final EditText valueEditText = (EditText) findViewById(R.id.editText_value);

        constructionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateWithConstructionPermits();
            }
        });

        valueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value;
                try {
                    value = Integer.parseInt(valueEditText.getText().toString());
                } catch (NumberFormatException e) {
                    value = 1;
                }
                updateWithValuePermits(value);
            }
        });
    }

    // Our queries are built with a combination of code from ApiInterface
    // and arguments provided here
    private void updateWithValuePermits(int value) {
        Call<List<BuildingPermit>> call = apiInterface.getPermitsByValue(apiToken,
                ApiInterface.WHERE_VALUE_IS_UNDER_X + value, 100);
        enqueueCall(call);
    }
    private void updateWithConstructionPermits() {
        Call<List<BuildingPermit>> call = apiInterface.getPermitsByCategory(apiToken, 
                ApiInterface.CATEGORY_COMMERCIAL, 100);
        enqueueCall(call);
    }
    
    
    
    
    private void enqueueCall(Call<List<BuildingPermit>> call) {
        
        Log.d("Request String: ", call.request().toString());

        call.enqueue(new Callback<List<BuildingPermit>>() {

            @Override
            public void onResponse(Call<List<BuildingPermit>> call, Response<List<BuildingPermit>> response) {

                // If response code is from 200
                if (response.code() > 199 && response.code() < 300) {

                    // This inflates the response body into objects according to
                    // annotations found in the BuildingPermit and BuildingLocation
                    // classes
                    List<BuildingPermit> permits = response.body();
                    PermitAdapter adapter = new PermitAdapter(getBaseContext(), permits);
                    recyclerView.swapAdapter(adapter, true);
                } else {
                    Log.d("Invalid response code: ", Integer.toString(response.code()));
                    Log.d("Response message: ", response.message());
                }

            }

            @Override
            public void onFailure(Call<List<BuildingPermit>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getBaseContext(), "No data received. Please check your" +
                        " connection and try again", Toast.LENGTH_LONG).show();
            }
        });
        
    }
    
    
}
