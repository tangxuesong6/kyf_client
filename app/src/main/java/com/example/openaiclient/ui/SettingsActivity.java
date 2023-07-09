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
import com.example.openaiclient.listener.DbDeleteListener;
import com.example.openaiclient.sql.DbManager;
import com.example.openaiclient.util.ConfigSharedPreferences;

public class SettingsActivity extends AppCompatActivity {
    private EditText serverUrl;
    private EditText serverApiKey;
    private EditText localApiKey;

    private EditText maxTokens;
    private EditText contentLength;

    private TextView deleteAllMsg;
    private TextView save;
    private MyProgressDialog myProgressDialog;
    private RadioGroup radioGroup;
    private RadioButton radioServer;
    private RadioButton radioLocal;
    private int currentMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        translucentUI();
        setContentView(R.layout.activity_settings);
        initView();
        initListener();

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
        deleteAllMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myProgressDialog.show();
                DbManager.getInstance().deleteAll(new DbDeleteListener() {
                    @Override
                    public void onSuccess() {
                        myProgressDialog.dismiss();
                    }
                });
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serverUrlStr = serverUrl.getText().toString();
                String serverApiKeyStr = serverApiKey.getText().toString();
                String localApiKeyStr = localApiKey.getText().toString();
                String maxTokensStr = maxTokens.getText().toString();
                String contentLengthStr = contentLength.getText().toString();

                if (currentMode == ConfigParams.SERVER_MODE) {
                    if (serverUrlStr.isEmpty()) {
                        Toast.makeText(SettingsActivity.this, getResources().getText(R.string.err_server_url), Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else if (currentMode == ConfigParams.LOCAL_MODE) {
                    if (localApiKeyStr.isEmpty()) {
                        Toast.makeText(SettingsActivity.this, getResources().getText(R.string.err_local_api_key), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if (!serverUrlStr.isEmpty()) {
                    ConfigSharedPreferences.getInstance().saveServerUrl(SettingsActivity.this.getApplicationContext(), serverUrlStr);
                }
                if (!serverApiKeyStr.isEmpty()) {
                    ConfigSharedPreferences.getInstance().saveServerApiKey(SettingsActivity.this.getApplicationContext(), serverApiKeyStr);
                }
                if (!localApiKeyStr.isEmpty()) {
                    ConfigSharedPreferences.getInstance().saveLocalApiKey(SettingsActivity.this.getApplicationContext(), localApiKeyStr);
                    ConfigSharedPreferences.getInstance().saveCurrentMode(SettingsActivity.this.getApplicationContext(), ConfigParams.LOCAL_MODE);
                } else {
                    ConfigSharedPreferences.getInstance().saveCurrentMode(SettingsActivity.this.getApplicationContext(), ConfigParams.SERVER_MODE);
                }
                if (!maxTokensStr.isEmpty()) {
                    ConfigSharedPreferences.getInstance().saveMaxTokens(SettingsActivity.this.getApplicationContext(), Integer.parseInt(maxTokensStr));
                }
                if (!contentLengthStr.isEmpty()) {
                    ConfigSharedPreferences.getInstance().saveContentLength(SettingsActivity.this.getApplicationContext(), Integer.parseInt(contentLengthStr));
                }

                ConfigSharedPreferences.getInstance().saveCurrentMode(SettingsActivity.this.getApplicationContext(), currentMode);

                Intent intent = new Intent(SettingsActivity.this, ChatActivity.class);
                startActivity(intent);
                SettingsActivity.this.finish();
            }

        });
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

    private void initView() {
        serverUrl = findViewById(R.id.edt_server_url);
        serverApiKey = findViewById(R.id.edt_server_api_key);
        localApiKey = findViewById(R.id.edt_local_api_key);

        maxTokens = findViewById(R.id.edt_max_allow_tokens);
        contentLength = findViewById(R.id.edt_max_content_length);

        deleteAllMsg = findViewById(R.id.tv_delete);
        save = findViewById(R.id.tv_save);

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


        maxTokens.setText(ConfigSharedPreferences.getInstance().getMaxTokens(this.getApplicationContext()) + "");

        contentLength.setText(ConfigSharedPreferences.getInstance().getContentLength(this.getApplicationContext()) + "");
        myProgressDialog = new MyProgressDialog(this, R.style.dialogTransparent, R.layout.lottie_dialog);
        myProgressDialog.setCancelable(false);

        currentMode = ConfigSharedPreferences.getInstance().getCurrentMode(this.getApplicationContext());
        if (currentMode == ConfigParams.SERVER_MODE) {
            radioServer.setChecked(true);
        } else if (currentMode == ConfigParams.LOCAL_MODE) {
            radioLocal.setChecked(true);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SettingsActivity.this, ChatActivity.class);
        startActivity(intent);
        SettingsActivity.this.finish();
    }

    @Override
    protected void onDestroy() {
        if (myProgressDialog != null) {
            if (myProgressDialog.isShowing()) {
                myProgressDialog.dismiss();
            }
            myProgressDialog.removeAnim();
        }
        super.onDestroy();
    }
}