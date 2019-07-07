package com.test.yin.anoprocess;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.test.yin.annotations.Builder;

/**
 * @author leilongling@kungeek.com
 * @version 3.2
 * date 2019/6/12.
 */
@Builder
public class RepositoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository);
    }
}
