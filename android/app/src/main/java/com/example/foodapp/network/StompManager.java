package com.example.foodapp.network;

import static com.example.foodapp.consts.Constants.SOCKET_URL;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.foodapp.dto.response.NotificationResponse;
import com.example.foodapp.listeners.NotificationListener;
import com.example.foodapp.listeners.StompMessageListener;
import com.google.gson.Gson;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;


public class StompManager {
    private static StompManager instance;
    private final StompClient stompClient;

    private StompManager() {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, SOCKET_URL);
    }

    public static synchronized StompManager getInstance() {
        if (instance == null) {
            instance = new StompManager();
        }
        return instance;
    }

    @SuppressLint("CheckResult")
    public void connect() {
        stompClient.connect();
        stompClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {
                case OPENED:
                    Log.d("STOMP", "Connected!");
                    break;
                case ERROR:
                    Log.e("STOMP", "Error", lifecycleEvent.getException());
                    break;
                case CLOSED:
                    Log.d("STOMP", "Disconnected!");
                    break;
            }
        });
    }

    @SuppressLint("CheckResult")
    public void sendMessage(String destination, String messageJson) {
        stompClient.send(destination, messageJson)
                .subscribe(() -> Log.d("STOMP", "Message sent to " + destination),
                        throwable -> Log.e("STOMP", "Error sending message", throwable));
    }

    public Disposable subscribeTo(String destination, StompMessageListener listener) {
        return stompClient.topic(destination)
                .subscribe(stompMessage -> {
                    listener.onMessage(stompMessage.getPayload());
                }, throwable -> {
                    Log.e("STOMP", "Error in subscription to " + destination, throwable);
                });
    }

    @SuppressLint("CheckResult")
    public Disposable subscribeToNotifications(String userId, NotificationListener listener) {
        String topic = "/topic/notifications/" + userId;

        return stompClient.topic(topic)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stompMsg -> {
                            try {
                                NotificationResponse noti = new Gson()
                                        .fromJson(stompMsg.getPayload(), NotificationResponse.class);

                                if (listener != null) {
                                    listener.onNotificationReceived(noti);
                                }

                            } catch (Exception e) {
                                Log.e("STOMP_NOTIFICATION", "Parse error: " + e.getMessage());
                            }
                        }, thr ->
                                Log.e("STOMP_NOTIFICATION", "Subscribe error", thr)
                );
    }
}
