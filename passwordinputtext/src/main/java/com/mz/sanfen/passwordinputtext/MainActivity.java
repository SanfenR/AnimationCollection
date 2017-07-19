package com.mz.sanfen.passwordinputtext;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mz.sanfen.passwordinputtext.inputtext.PasswordInputView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final PasswordInputView passwordInputView = (PasswordInputView) findViewById(R.id.passwordInputView);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, passwordInputView.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
