package com.maxus376.kostromafastfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class Cart extends AppCompatActivity {

    TextView textViewCartData, textViewDisDur, textViewPrice, nameUser, phone;
    SharedPreferences sharedPreferences;
    Button buttonConfirm, buttonRemove;
    ArrayList<ListData> arrayList = new ArrayList<ListData>();

    int pricePerKM = 5;
    String urlConfirm = "http://172.20.10.7/kostroma-fastfood/kostromafastfood/public/api/users/cart/confirm?user_email=";

    String urlRemove = "http://172.20.10.7/kostroma-fastfood/kostromafastfood/public/api/users/cart/clear?user_email="; //iPhone Wi-Fi

    /*String urlConfirm = "http://192.168.0.105/kostroma-fastfood/kostromafastfood/public/api/users/cart/confirm?user_email=";

    String urlRemove = "http://192.168.0.105/kostroma-fastfood/kostromafastfood/public/api/users/cart/clear?user_email=";*/ //TP-Link Wi-Fi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        //textViewCartData = findViewById(R.id.textCartData);
        textViewPrice = findViewById(R.id.price);
        textViewDisDur = findViewById(R.id.textDisDur);
        sharedPreferences = getSharedPreferences("KostromaFastFood", MODE_PRIVATE);
        nameUser = findViewById(R.id.nameUser);
        nameUser.setText(sharedPreferences.getString("name", ""));
        phone = findViewById(R.id.phone);
        //phone.setText(sharedPreferences.getString("phone", ""));
        buttonConfirm = findViewById(R.id.btnConfirmOrder);
        buttonRemove = findViewById(R.id.btnClearCart);
        fetchData();
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest(view, urlConfirm + sharedPreferences.getString("email", ""));
            }
        });

        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest(view, urlRemove + sharedPreferences.getString("email", ""));
            }
        });
    }

    public void sendRequest(View v, String apiUrl) {
        Log.e("url", apiUrl);
        v.setEnabled(false);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                v.setEnabled(true);
                if (response.equals("success")) {
                    Toast.makeText(getApplicationContext(), "Успешно", Toast.LENGTH_SHORT).show();
                    finish();
                } else
                    Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                v.setEnabled(true);
                error.printStackTrace();
            }
        });
        queue.add(stringRequest);
    }

    public void parseJSON(String data) {
        try {
            JSONArray jsonArray = new JSONArray(data);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            JSONArray jsonArray1 = jsonObject.getJSONArray("cart");
            int totalPrice =0;
            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject stu = jsonArray1.getJSONObject(i);
                String id = stu.getString("id");
                String name = stu.getString("name");
                String image = stu.getString("image");
                String numItem = stu.getString("numItem");
                String price = stu.getString("price");
                arrayList.add(new ListData(id, name, image, price, numItem));
                //textViewCartData.append("Item: " + name + "\nPrice: " + price + "$\nNumber of Item: " + numItem + "\n\n");
                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                RecyclerViewAdapterCart adapter = new RecyclerViewAdapterCart(arrayList, this);
                //recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(adapter);
                //Integer sum_price = 0;
                //sum_price = sum_price+Integer.parseInt(price);
                totalPrice += Integer.parseInt(stu.getString("price"));
            }
            textViewPrice.setText(String.valueOf("Сумма: "+totalPrice) + " руб.");
            String distance = String.valueOf(jsonObject.getInt("distance") / 1000);
            String duration = String.valueOf((jsonObject.getInt("duration") / 60));
            //int total_price = Integer.sum(jsonObject.getInt("price"));

            /*textViewDisDur.setText("Distance: " + distance + " KM\nDelivery Fee: "
                    + total_price + " $" + "\nDuration: " + duration + "Minutes");*/
        } catch (JSONException e) {
            Log.e("error", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public void fetchData() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "http://172.20.10.7/kostroma-fastfood/kostromafastfood/public/api/users/cart/list?user_email="
                        + sharedPreferences.getString("email", ""), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSON(response);
            } //iPhone Wi-Fi
            /*StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    "http://192.168.0.105/kostroma-fastfood/kostromafastfood/public/api/users/cart/list?user_email="
                            + sharedPreferences.getString("email", ""), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    parseJSON(response);
                } *///TP-Link Wi-Fi
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(stringRequest);
    }
}