package com.example.bannerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.bannerview.widget.BannerView;

public class MainActivity extends AppCompatActivity {
    BannerView bannerView;

    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View v11 = LayoutInflater.from(MainActivity.this).inflate(R.layout.item1, null);
                ((TextView)v11.findViewById(R.id.item_txt)).setText(String.valueOf(++count));
                bannerView.addBannerView(v11);
            }
        });

        findViewById(R.id.remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bannerView.removeBannerView();
            }
        });
        bannerView = (BannerView) findViewById(R.id.bannerView);
    }
}
