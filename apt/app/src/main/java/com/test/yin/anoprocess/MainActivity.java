package com.test.yin.anoprocess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.goToUserActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserActivityBuilder.start(MainActivity.this, 22, "Main from", "网易", 666, "mainTitle");
            }
        });

        findViewById(R.id.goToRepositoryActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RepositoryActivityBuilder.start(MainActivity.this);
            }
        });
    }
}
