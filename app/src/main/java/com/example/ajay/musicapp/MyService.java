package com.example.ajay.musicapp;

import android.app.Service;



import android.content.Intent;

import android.os.Binder;
import android.os.IBinder;




public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        return new MyBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    class MyBinder extends Binder{
        public MyService getServices()
        {
            return MyService.this;
        }
    }

}
