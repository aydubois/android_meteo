package com.ayponyo.android.meteo;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.ayponyo.android.meteo.databinding.ActivityScrollingBinding;

public class FavoriteActivity extends AppCompatActivity {

    private ActivityScrollingBinding binding;
    private TextView mTextViewMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityScrollingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getString(R.string.favorites).toUpperCase());

        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        Bundle extras = getIntent().getExtras();
        String textMessage = extras.getString(Util.key_message);

        mTextViewMessage = findViewById(R.id.text_view_message);
        mTextViewMessage.setText("Message : "+textMessage);
    }
}