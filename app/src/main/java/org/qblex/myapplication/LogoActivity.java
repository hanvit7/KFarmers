package org.qblex.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public class LogoActivity extends Activity {
    private static final String TAG = LogoActivity.class.getSimpleName();

    ProgressBar progressBar;

    int timeCnt;
    UserSet userSet;

    TimeThread timeThread;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        setContentView(R.layout.activity_logo);

        userSet = new UserSet(getApplicationContext());
//        Log.d(TAG, "getFilmNum:" + userSet.getFilmNum());
//        userSet.setFilmNum(1);
//        Log.d(TAG, "getFilmNum:" + userSet.getFilmNum());
//        userSet.getWifiInfo();

        Intent intent = getIntent();
        if (intent != null) {
//            userSet = (UserSet) intent.getSerializableExtra("userset");
            String string = intent.getStringExtra("set");
            Log.d(TAG, "userset" + string);
        }

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary),
                android.graphics.PorterDuff.Mode.SRC_IN);

        timeCnt = 0;
        timeThread = new TimeThread();
        timeThread.setRunning(true);
        timeThread.start();

        if (userSet.getAutoConnect()) {
            MyWifi myWifi = new MyWifi(this, new MyWifi.ProgressListner() {
                @Override
                public void progressDialog(MyWifi.WifiState wifiState, final String string) {
                    switch (wifiState) {
                        case WAIT:
                            timeCnt++;
                            break;
                        case CHECK_WIFI_ENABLE:
                            if (timeCnt < 20) timeCnt++;
                            break;
                        case CHECK_WIFI_CONNECTION:
                            if (timeCnt < 30) timeCnt++;
                            break;
                        case CHECK_FILM_INFO:
                            if (timeCnt < 40) timeCnt++;
                            break;
                        case SEARCH_WIFI:
                            if (timeCnt < 50) timeCnt++;
                            break;
                        case WAIT_SEARCH_WIFI:
                            if (timeCnt < 70) timeCnt++;
                            break;
                        case CONNECT_FILM:
                            if (timeCnt < 80) timeCnt++;
                            break;
                        case WAIT_CONNECT_FILM:
                            if (timeCnt < 90) timeCnt++;
                            break;
                        case CONNECT_SUCCESS:
                            timeThread.setCounting(true);
                            break;
                        case CONNECT_FAIL:
                            failMessage();
                            timeThread.setCounting(true);
                            break;
                    }
                    progressing(timeCnt);
                }
            });

            String[] wifiInfo = userSet.getWifiInfo();
            myWifi.setWifiInfo(wifiInfo[0], wifiInfo[1]);
            Log.d(TAG, "setWifiInfo : " + wifiInfo[0] + ", " + wifiInfo[1]);
            myWifi.setRunning(true, 0, 40);
            myWifi.start();
        } else {
            timeThread.setCounting(true);
        }
    }

    class TimeThread extends Thread {
        boolean running = false;
        boolean counting = false;

        public void setCounting(boolean counting) {
            this.counting = counting;
        }

        public void setRunning(boolean running) {
            this.running = running;
        }

        @Override
        public void run() {
            while (running) {
                try {
                    if (counting) {
                        if (timeCnt < 100) {
                            progressing(timeCnt++);
                        } else {
                            progressing(100);
                            counting = false;
                        }
                    } else {
                        if (timeCnt == 100) finishLogo();
                    }
                    Thread.sleep(15);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void progressing(final int timeCnt) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                progressBar.setProgress(timeCnt);
            }
        });
    }

    public void failMessage() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Can't Find QbleSmartFilm.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void finishLogo() {
        Log.d(TAG, "finishLogo");
        timeThread.running = false;
        Intent intent = new Intent();
        intent.putExtra("logo", true);
        setResult(RESULT_OK, intent);
        finish();
    }

}
