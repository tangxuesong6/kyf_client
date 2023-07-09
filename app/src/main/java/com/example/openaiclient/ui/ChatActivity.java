package com.example.openaiclient.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.openaiclient.ConfigParams;
import com.example.openaiclient.LocalModeOpenAi;
import com.example.openaiclient.MsgAdapter;
import com.example.openaiclient.OpenAiImpl;
import com.example.openaiclient.R;
import com.example.openaiclient.ServerModeOpenAi;
import com.example.openaiclient.bean.MsgBean;
import com.example.openaiclient.bean.RequestBean;
import com.example.openaiclient.bean.RespBean;
import com.example.openaiclient.listener.DbInitListener;
import com.example.openaiclient.listener.DbInsertListener;
import com.example.openaiclient.listener.DbMsgListListener;
import com.example.openaiclient.listener.DbMsgListener;
import com.example.openaiclient.listener.OpenAiListener;
import com.example.openaiclient.sql.DbManager;
import com.example.openaiclient.sql.MsgDatabase;
import com.example.openaiclient.sql.MsgDb;
import com.example.openaiclient.util.ConfigSharedPreferences;
import com.example.openaiclient.util.LogUtil;
import com.example.openaiclient.util.MainThread;
import com.example.openaiclient.util.SoftInputUtils;
import com.example.openaiclient.util.ThreadPoolManager;
import com.example.openaiclient.util.TokenizerUtil;
import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.EncodingRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView msgRecyclerView;
    private EditText inputText;
    private TextView send;

    private TextView prompts;
    private LinearLayout chatBottom;

    private ImageView settings;
    private LinearLayoutManager layoutManager;
    private MsgAdapter adapter;
    private List<MsgBean> msgBeanList = new ArrayList<>();

    private int contentSize = 4;
    private int maxTokens = 60;

    private OpenAiImpl openAi;


    private EncodingRegistry encodingRegistry;
    private MyProgressDialog myProgressDialog;

    private ActivityResultLauncher intentLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        translucentUI();

        setContentView(R.layout.activity_chat);

        intentLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    String content = data.getStringExtra("content");
                    inputText.setText(content);
                }
            }
        });
        initView();

        if (ConfigSharedPreferences.getInstance().getCurrentMode(this.getApplicationContext()) == ConfigParams.SERVER_MODE) {
            openAi = new ServerModeOpenAi();
            openAi.init(ConfigSharedPreferences.getInstance().getServerApiKey(this.getApplicationContext()));
        } else {
            openAi = new LocalModeOpenAi();
            openAi.init(ConfigSharedPreferences.getInstance().getLocalApiKey(this.getApplicationContext()));
        }

        initDbAndEncodings(new InitListener() {
            @Override
            public void ok() {
                myProgressDialog.dismiss();
                Toast.makeText(ChatActivity.this, getResources().getText(R.string.init_success), Toast.LENGTH_SHORT).show();
                inputText.setHint(getResources().getText(R.string.enter_content));
                DbManager.getInstance().getLastMsg(new DbMsgListener() {
                    @Override
                    public void onGedMsg(MsgDb msg) {
                        if (msg != null) {
                            int lastId = msg.id;

                            DbManager.getInstance().getLimitMsg(lastId, 100, new DbMsgListListener() {

                                @Override
                                public void onGetMsgList(List<MsgDb> msgDbList) {
                                    for (int i = 0; i < msgDbList.size(); i++) {
                                        MsgDb msgDb = msgDbList.get(i);

                                        msgBeanList.add(new MsgBean(msgDb.getContent(), msgDb.getType()));
                                        if (msgBeanList.size() > 0) {
                                            adapter.notifyDataSetChanged();
//                                            adapter.notifyItemInserted(msgBeanList.size() - 1);
                                            msgRecyclerView.scrollToPosition(msgBeanList.size() - 1);
                                        }
                                    }
                                }
                            });
                        }
                    }
                });

                initListener();
            }
        });


        SoftInputUtils softInputUtil = new SoftInputUtils();
        softInputUtil.attachSoftInput(chatBottom, new SoftInputUtils.ISoftInputChangedListener() {
            @Override
            public void onChanged(boolean isSoftInputShow, int softInputHeight, int viewOffset) {
                if (isSoftInputShow) {
                    if (adapter != null) {
                        chatBottom.setTranslationY(chatBottom.getTranslationY() - viewOffset);
                        msgRecyclerView.scrollToPosition(adapter.getItemCount() - 1);
                    }
                } else {
                    chatBottom.setTranslationY(0);
                }
            }
        });


        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatActivity.this, SettingsActivity.class));
                ChatActivity.this.finish();
            }
        });


    }

    private void initListener() {
        prompts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentLauncher.launch(new Intent(ChatActivity.this, PromptsActivity.class));
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if (!content.equals("")) {
                    myProgressDialog.show();


                    RequestBean requestBean = new RequestBean();
                    requestBean.maxTokens = 1024;
                    List<RequestBean.ContentsDTO> list = new ArrayList<>();


                    if (msgBeanList.size() <= contentSize) {
                        for (int i = 0; i < msgBeanList.size(); i++) {
                            RequestBean.ContentsDTO contentsDTO = new RequestBean.ContentsDTO();
                            if (msgBeanList.get(i).getType() == MsgBean.TYPE_SEND) {
                                contentsDTO.role = "user";
                            } else {
                                contentsDTO.role = "assistant";
                            }
                            contentsDTO.content = msgBeanList.get(i).getContent();
                            list.add(contentsDTO);
                        }
                    } else {
                        int msgSize = msgBeanList.size();
                        for (int i = msgSize - contentSize + 1; i < msgSize; i++) {
                            RequestBean.ContentsDTO contentsDTO = new RequestBean.ContentsDTO();
                            if (msgBeanList.get(i).getType() == MsgBean.TYPE_SEND) {
                                contentsDTO.role = "user";
                            } else if (msgBeanList.get(i).getType() == MsgBean.TYPE_RECEIVED) {
                                contentsDTO.role = "assistant";
                            } else {
                                contentsDTO.role = "system ";
                            }
                            contentsDTO.content = msgBeanList.get(i).getContent();
                            list.add(contentsDTO);
                        }

                    }
                    RequestBean.ContentsDTO contentsDTO = new RequestBean.ContentsDTO();
                    contentsDTO.role = "user";
                    contentsDTO.content = content;
                    list.add(contentsDTO);

                    int surplusTokens = maxTokens - TokenizerUtil.countMessageTokens(encodingRegistry, "gpt-3.5-turbo", list);
                    if (surplusTokens <= 0) {
                        myProgressDialog.dismiss();
                        Toast.makeText(ChatActivity.this, "token error: " + getResources().getText(R.string.err_token), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    requestBean.contents = list;
                    requestBean.maxTokens = surplusTokens;

                    openAi.request(requestBean, new OpenAiListener() {
                        @Override
                        public void onSuccess(RespBean respBean) {
                            DbManager.getInstance().insert(new DbInsertListener() {
                                @Override
                                public void onSuccess() {
                                    msgBeanList.add(new MsgBean(content, MsgBean.TYPE_SEND));
                                    msgBeanList.add(new MsgBean(respBean.message, MsgBean.TYPE_RECEIVED));
                                    myProgressDialog.dismiss();

                                    adapter.notifyItemInserted(msgBeanList.size() - 1);
                                    msgRecyclerView.scrollToPosition(msgBeanList.size() - 1);
                                    inputText.setText("");//清空输入框中的内容
                                }
                            }, new MsgDb(content, MsgBean.TYPE_SEND), new MsgDb(respBean.message, MsgBean.TYPE_RECEIVED));
                        }

                        @Override
                        public void onFail(String msg) {
                            MainThread.run(new Runnable() {
                                @Override
                                public void run() {
                                    myProgressDialog.dismiss();
                                    Toast.makeText(ChatActivity.this, "net error: " + msg, Toast.LENGTH_SHORT).show();
                                }
                            });
                            LogUtil.d(msg);
                        }
                    });

                }
            }
        });
    }

    private void initView() {
        msgRecyclerView = findViewById(R.id.rv_chat);
        inputText = findViewById(R.id.input_text);
        send = findViewById(R.id.send);
        settings = findViewById(R.id.img_settings);
        prompts = findViewById(R.id.tv_prompts);
        chatBottom = findViewById(R.id.ll_chat_bottom);

        contentSize = ConfigSharedPreferences.getInstance().getContentLength(this.getApplicationContext());
        maxTokens = ConfigSharedPreferences.getInstance().getMaxTokens(this.getApplicationContext());
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);

        adapter = new MsgAdapter(msgBeanList);
        msgRecyclerView.setLayoutManager(layoutManager);
        msgRecyclerView.setAdapter(adapter);

        myProgressDialog = new MyProgressDialog(this, R.style.dialogTransparent, R.layout.lottie_dialog);
        myProgressDialog.setCancelable(false);
        myProgressDialog.show();
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

    private void initDbAndEncodings(InitListener listener) {
        inputText.setHint(getResources().getText(R.string.initializing));
        AtomicInteger autoInteger = new AtomicInteger(0);
        ThreadPoolManager.executeTask(new Runnable() {
            @Override
            public void run() {
                encodingRegistry = Encodings.newLazyEncodingRegistry();
                List<RequestBean.ContentsDTO> list = new ArrayList<>();
                RequestBean.ContentsDTO contentsDTO = new RequestBean.ContentsDTO();
                list.add(contentsDTO);
                //初始化一下，防止第一次请求卡顿
                int tokens = TokenizerUtil.countMessageTokens(encodingRegistry, "gpt-3.5-turbo", list);
                autoInteger.incrementAndGet();
                LogUtil.d("Tokenization model loaded: " + autoInteger.get());

                if (autoInteger.get() == 2) {
                    MainThread.run(new Runnable() {
                        @Override
                        public void run() {
                            listener.ok();
                        }
                    });
                }
            }
        });

        DbManager.getInstance().init(new DbInitListener() {
            @Override
            public void onSuccess(MsgDatabase msgDatabase) {
                autoInteger.incrementAndGet();
                LogUtil.d("DB init success: " + autoInteger.get());
                if (autoInteger.get() == 2) {
                    listener.ok();
                }
            }
        });

    }

    interface InitListener {
        void ok();
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