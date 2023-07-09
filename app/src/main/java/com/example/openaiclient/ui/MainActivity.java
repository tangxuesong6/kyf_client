package com.example.openaiclient.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.openaiclient.ConfigParams;
import com.example.openaiclient.R;
import com.example.openaiclient.util.ConfigSharedPreferences;

public class MainActivity extends AppCompatActivity {
    private EditText serverUrl;
    private EditText serverApiKey;
    private EditText localApiKey;
    private TextView logIn;
    private RadioGroup radioGroup;
    private RadioButton radioServer;
    private RadioButton radioLocal;
    private int currentMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        translucentUI();
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }

    private void translucentUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            View decorView = window.getDecorView();

            // Set the status bar to be transparent
            window.setStatusBarColor(Color.TRANSPARENT);

            // Set the navigation bar to be transparent
            window.setNavigationBarColor(Color.TRANSPARENT);

            // Set the system UI flags to enable full screen mode
            int flags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            decorView.setSystemUiVisibility(flags);

            // Add the system bar backgrounds
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();

            // Set the status bar to be translucent
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // Set the navigation bar to be translucent
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    private void initListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.radio_server) {
                    currentMode = ConfigParams.SERVER_MODE;
                } else if (checkedId == R.id.radio_local) {
                    currentMode = ConfigParams.LOCAL_MODE;
                }

            }
        });
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serverUrlStr = serverUrl.getText().toString();
                String serverApiKeyStr = serverApiKey.getText().toString();
                String localApiKeyStr = localApiKey.getText().toString();

                if (currentMode == ConfigParams.SERVER_MODE) {
                    if (serverUrlStr.isEmpty()) {
                        Toast.makeText(MainActivity.this, getResources().getText(R.string.err_server_url), Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else if (currentMode == ConfigParams.LOCAL_MODE) {
                    if (localApiKeyStr.isEmpty()) {
                        Toast.makeText(MainActivity.this, getResources().getText(R.string.err_local_api_key), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (!serverUrlStr.isEmpty()) {
                    ConfigSharedPreferences.getInstance().saveServerUrl(MainActivity.this.getApplicationContext(), serverUrlStr);
                }
                if (!serverApiKeyStr.isEmpty()) {
                    ConfigSharedPreferences.getInstance().saveServerApiKey(MainActivity.this.getApplicationContext(), serverApiKeyStr);
                }
                if (!localApiKeyStr.isEmpty()) {
                    ConfigSharedPreferences.getInstance().saveLocalApiKey(MainActivity.this.getApplicationContext(), localApiKeyStr);

                }
                ConfigSharedPreferences.getInstance().saveCurrentMode(MainActivity.this.getApplicationContext(), currentMode);
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(intent);
                MainActivity.this.finish();

            }
        });
    }

    private void initView() {
        serverUrl = findViewById(R.id.edt_server_url);
        serverApiKey = findViewById(R.id.edt_server_api_key);
        localApiKey = findViewById(R.id.edt_local_api_key);
        logIn = findViewById(R.id.tv_login);
        radioGroup = findViewById(R.id.radio_group);
        radioServer = findViewById(R.id.radio_server);
        radioLocal = findViewById(R.id.radio_local);
        if (ConfigSharedPreferences.getInstance().hasServerUrl(this.getApplicationContext())) {
            serverUrl.setText(ConfigSharedPreferences.getInstance().getServerUrl(this.getApplicationContext()));
        }
        if (ConfigSharedPreferences.getInstance().hasServerApiKey(this.getApplicationContext())) {
            serverApiKey.setText(ConfigSharedPreferences.getInstance().getServerApiKey(this.getApplicationContext()));
        }
        if (ConfigSharedPreferences.getInstance().hasLocalApiKey(this.getApplicationContext())) {
            localApiKey.setText(ConfigSharedPreferences.getInstance().getLocalApiKey(this.getApplicationContext()));
        }
        currentMode = ConfigSharedPreferences.getInstance().getCurrentMode(this.getApplicationContext());
        if (currentMode == ConfigParams.SERVER_MODE) {
            radioServer.setChecked(true);
        } else if (currentMode == ConfigParams.LOCAL_MODE) {
            radioLocal.setChecked(true);
        }

    }

}