package com.maxus376.kostromafastfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListOrders extends AppCompatActivity {

    TextView textViewOrders;
    SharedPreferences sharedPreferences;
    ArrayList<ListDataOrder> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_orders);
        //textViewOrders = findViewById(R.id.textOrders);
        sharedPreferences = getSharedPreferences("KostromaFastFood", MODE_PRIVATE);
        //textViewOrders.setMovementMethod(new ScrollingMovementMethod());
        fetchData("http://172.20.10.7/kostroma-fastfood/kostromafastfood/public/api/users/orders/list?email="
                +sharedPreferences.getString("email", "")); //iPhone Wi-Fi
        /*fetchData("http://192.168.0.105/kostroma-fastfood/kostromafastfood/public/api/users/orders/list?email="
                +sharedPreferences.getString("email", ""));*/ //TP-Link Wi-Fi

        arrayList = new ArrayList<>();
    }

    public void parseJSON(String response){
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject stu = jsonArray.getJSONObject(i);
                String id = stu.getString("id");
                String destination_address = stu.getString("destination_address");
                String time = "23";
                String sum_price = stu.getString("sum_price");
                arrayList.add(new ListDataOrder(id, destination_address, sum_price, time));
                //textViewCartData.append("Item: " + name + "\nPrice: " + price + "$\nNumber of Item: " + numItem + "\n\n");
                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                RecyclerViewAdapterOrder adapter = new RecyclerViewAdapterOrder(arrayList, this);
                //recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(adapter);
                /*textViewOrders.append("Order placed on: " + created_at + "\nStatus: " + status + "\n");
                for (int j = 0; j < jsonArrayItem.length(); j++) {
                    JSONObject jsonObjectItem = jsonArrayItem.getJSONObject(j);
                    textViewOrders.append("Items: \n" + jsonObjectItem.getString("name") + "\nPrice: "
                            + jsonObjectItem.getString("price") + "\n");
                }
                textViewOrders.append("\n\n");*/
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void fetchData(String apiUrl) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseJSON(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(stringRequest);
    }
}