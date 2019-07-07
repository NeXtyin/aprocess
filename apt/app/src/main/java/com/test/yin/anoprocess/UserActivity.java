package com.test.yin.anoprocess;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.test.yin.annotations.Builder;
import com.test.yin.annotations.Optional;
import com.test.yin.annotations.Required;

/**
 * @author leilongling@kungeek.com
 * @version 3.2
 * date 2019/6/13.
 */
@Builder
public class UserActivity extends AppCompatActivity {

    @Required
    String name;

    @Required
    int age;

    @Optional
    String title;

    @Optional
    String company;

    @Optional
    int three;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        TextView tvUser = findViewById(R.id.tv_user);
        tvUser.setText(
                "name = " + name + "\n"
                + "age = " + age + "\n"
                + "title = " + title + "\n"
                + "conpany = " + company + "\n"
                + "three = " + three
        );

    }
}
