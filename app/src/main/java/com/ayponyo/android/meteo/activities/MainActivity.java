package com.ayponyo.android.meteo;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView mTextViewCityName;
    private TextView mTextViewErrorNetwork;
    private Button mButtonFavorites;
    private LinearLayout mLinearLayoutMain;
    private TextInputEditText mTextInputMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextViewCityName = (TextView) findViewById(R.id.text_view_city_name);
        mTextViewErrorNetwork = (TextView) findViewById(R.id.text_view_error_network);
        mButtonFavorites = (Button) findViewById(R.id.button_favorites);
        mLinearLayoutMain = (LinearLayout) findViewById(R.id.linear_layout_main);
        mTextInputMessage = (TextInputEditText) findViewById(R.id.text_input_message);


        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d("CONNECT_TEST", "Oui je suis connecté");
            Toast.makeText(this,mTextViewCityName.getText(), Toast.LENGTH_SHORT).show();
            mTextViewCityName.setText(R.string.city_name);
            Toast.makeText(this,mTextViewCityName.getText(), Toast.LENGTH_SHORT).show();

            mButtonFavorites.setOnClickListener(mFavoriteListener);
        } else {
            Log.d("CONNECT_TEST", "Non j’ai rien du tout");
            mTextViewErrorNetwork.setVisibility(View.VISIBLE);
            mButtonFavorites.setVisibility(View.GONE);
            mLinearLayoutMain.setVisibility(View.GONE);
        }

    }
    private final View.OnClickListener mFavoriteListener =
            v -> {
                String textMessage = mTextInputMessage.getText().toString();

                Intent intent = new Intent(this, FavoriteActivity.class);
                intent.putExtra(Util.key_message, textMessage);
                startActivity(intent);
            };
}