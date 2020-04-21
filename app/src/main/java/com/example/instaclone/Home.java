package com.example.instaclone;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.parse.ParseUser;

public class Home extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewpager;
    private TabAdapter tabadapter;
    private TabLayout tablayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setTitle("Welcome "+ParseUser.getCurrentUser().getUsername());


        toolbar=findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        viewpager=findViewById(R.id.myviewpager);
        tabadapter=new TabAdapter(getSupportFragmentManager());
        viewpager.setAdapter(tabadapter);

        tablayout=findViewById(R.id.myTabLayout);
        tablayout.setupWithViewPager(viewpager,false);

    }
}
