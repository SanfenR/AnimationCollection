package com.mz.sanfen.startbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toast toast = Toast.makeText(MainActivity.this,  "", Toast.LENGTH_SHORT);
        ((RatingBar)findViewById(R.id.starBar)).setOnStarChangeListener(new RatingBar.OnStarChangeListener() {
            @Override
            public void onStarChange(float mark) {
                toast.setText(mark + "");
                toast.show();
            }
        });
    }


}
