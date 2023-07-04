package com.maxus376.kostromafastfood;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewAdapterCart extends RecyclerView.Adapter<RecyclerViewAdapterCart.ViewHolder> {
    ArrayList<ListData> list;

    public RecyclerViewAdapterCart(ArrayList<ListData> list, Context context) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_cart, viewGroup, false);
        v.getBackground().setAlpha(0);
        ViewHolder holder = new ViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder cartViewHolder, int i) {
        Picasso.get().setLoggingEnabled(true);
        Picasso.get().load(list.get(i).getImg()).placeholder(R.drawable.logo).error(R.drawable.avatar).resize(106,106).into(cartViewHolder.image);
        cartViewHolder.name.setText(list.get(i).getName());
        cartViewHolder.price.setText(list.get(i).getPrice() + " руб.");
        cartViewHolder.count.setText(list.get(i).getNum());
        cartViewHolder.countNum = Integer.parseInt(list.get(i).getNum());
        cartViewHolder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartViewHolder.countNum++;
                cartViewHolder.count.setText(String.valueOf(cartViewHolder.countNum));
            }
        });
        cartViewHolder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartViewHolder.countNum--;
                cartViewHolder.count.setText(String.valueOf(cartViewHolder.countNum));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView name, price, count, plus, minus;
        public Integer countNum = 1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.textViewTitle);
            price = itemView.findViewById(R.id.textViewPrice);
            count = itemView.findViewById(R.id.count);
            plus = itemView.findViewById(R.id.plus);
            minus = itemView.findViewById(R.id.minus);
        }
    }
}