package com.example.androidhandler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HandlerActivity extends AppCompatActivity {
    private static final int MESSAGE_WHAT_1 = 1;
    private static final int MESSAGE_WHAT_2 = 2;
    private static final String MESSAGE_INFO_1 = "message_info_1";
    private static final String MESSAGE_INFO_2 = "message_info_2";
    private Thread myThread;
    private static Handler myHandler;
    private Looper myLooper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);
        Button message_one_button = findViewById(R.id.message_one);
        Button message_two_button = findViewById(R.id.message_two);
        showMessage();
        sendMessage(message_one_button, MESSAGE_INFO_1, MESSAGE_WHAT_1);
        sendMessage(message_two_button, MESSAGE_INFO_2, MESSAGE_WHAT_2);
    }

    private void showMessage() {
        myThread = new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                myLooper = Looper.myLooper();
                if (myLooper != null) {
                    myHandler = new Handler(myLooper) {
                        @Override
                        public void handleMessage(@NonNull Message msg) {
                            String message = "";
                            switch (msg.what) {
                                case MESSAGE_WHAT_1:
                                case MESSAGE_WHAT_2:
                                    message = msg.obj.toString();
                                    break;
                            }
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    };
                }
                Looper.loop();
            }
        };
        myThread.start();
    }

    private void sendMessage(Button messageOneButton, final String messageInfo, final int messageWhat) {
        messageOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = Message.obtain();
                message.what = messageWhat;
                message.obj = messageInfo;
                myHandler.sendMessage(message);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myLooper.quit();
    }
}