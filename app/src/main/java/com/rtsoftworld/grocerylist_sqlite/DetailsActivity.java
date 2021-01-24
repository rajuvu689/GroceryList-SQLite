package com.rtsoftworld.grocerylist_sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {
    private TextView itemNameDetail;
    private TextView quantityDetail;
    private TextView dateDetail;
    private int groceryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        itemNameDetail = findViewById(R.id.itemNameDetail);
        quantityDetail = findViewById(R.id.quantityDetail);
        dateDetail = findViewById(R.id.dateDetail);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            itemNameDetail.setText(bundle.getString("name"));
            quantityDetail.setText(bundle.getString("quantity"));
            dateDetail.setText(bundle.getString("date"));
            groceryId = bundle.getInt("id");
        }

    }
}