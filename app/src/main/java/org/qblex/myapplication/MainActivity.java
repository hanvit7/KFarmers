package org.qblex.myapplication;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class MainActivity extends AppCompatActivity
        implements
        NavigationView.OnNavigationItemSelectedListener,
        FragSetting.SettingListner,
        FragButton1.ButtonSelectedListener1,
        FragButton8.ButtonSelectedListener8,
        FragButton9.ButtonSelectedListener9 {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int REQ_CODE = 1001;

    public enum State {init, run, stop}

    MySocket socket = null;
    Handler mHandler;

    AliveThread aliveThread;

    ProgressDialog failProgressDialog;
    //    AlertDialog.Builder builder;
    AlertDialog failDialog;

    private ProgressDialog mProgressDialog;

    ProgressDialog ProgressDialog;

    ImageSwitcher imageSwitcher;
    UserSet userSet;

    //    State state = State.stop;
    boolean isLoge = false;
    String tempstr = null;
    int[] imageArray = {R.drawable.ic_cloud_off_white_48dp,
            R.drawable.ic_cloud_queue_white_48dp
    };

//    int[] imageNum = {R.drawable.ic_filter_none_white_48dp,
//            R.drawable.ic_filter_1_white_48dp,
//            R.drawable.ic_filter_2_white_48dp,
//            R.drawable.ic_filter_3_white_48dp,
//            R.drawable.ic_filter_4_white_48dp,
//            R.drawable.ic_filter_5_white_48dp,
//            R.drawable.ic_filter_6_white_48dp,
//            R.drawable.ic_filter_7_white_48dp,
//            R.drawable.ic_filter_8_white_48dp,
//            R.drawable.ic_filter_9_white_48dp,
//            R.drawable.ic_filter_9_plus_white_48dp
//    };


    int aliveCnt;

    FragSetting fragSetting;
    FragButton1 fragButton1;
    FragButton8 fragButton8;
    FragButton9 fragButton9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        if (mBackThread == null) mBackThread = new BackgroundThread();

        Intent intent = new Intent(this, LogoActivity.class);
        startActivityForResult(intent, REQ_CODE);

        if (userSet == null) {
            userSet = new UserSet(getApplicationContext());
        }

        setFragment();

        imageSwitcherInit();
        imageSwitcher.setImageResource(imageArray[0]);
        imageSwitcher.invalidate();
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        if (socket != null) {
//
//        }
//        Log.d(TAG, "isLoge : " + isLoge);
//    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (isLoge) {
//        imageSwitcher2.setImageResource(imageNum[0]);
//        imageSwitcher2.invalidate();
//        failDialogInit();
            massageHanlder();
            if (socket == null) socketStart();
            else {
                if (!socket.isConnected()) {
                    Log.d(TAG, "onRestart : socket.isConnected()");
                    socketStart();
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (socket != null) {
            if (socket.isConnected()) {
                Log.d(TAG, "onStop : socket.isConnected()");
                socket.sendString("#disconnect");
//                state = State.stop;
                aliveThread.setRuntFlag(false);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
//            if(data.getBooleanExtra("logo",false)) {
            isLoge = data.getBooleanExtra("logo", false);
//                Log.d(TAG, "ActivityResut:" + data.getStringExtra("logo"));
//            }
//            userSet = new UserSet(getApplicationContext());
//            userSet = (UserSet) data.getSerializableExtra("userset");
//            Log.d(TAG, "getFilmNum:" + userSet.getFilmNum());
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void socketStart() {
        socket = new MySocket(userSet.getWifiIp(), 80, mHandler);
        socket.setRuntFlag(true);
        socket.start();
    }

    private void commandHandle(String cmd) {
        int buttonNum;
        Log.d(TAG, "recv : " + cmd);
        if (cmd.contains("=")) {
            String[] split = cmd.split("=");
            if (split[0].contains("alive")) {//cmd alive
                if (split[1].equals("?")) {
//                    if (state != State.stop) {
                    socket.sendString("#alive" + ++aliveCnt);
                    aliveThread.alive = true;
//                    } else {
//                        Log.d(TAG, "retry disconnect : " + state);
//                        socket.sendString("#disconnect");
//                    }
                }
            } else if (split[0].contains("pin")) {//cmd pin
                if (getFragmentManager().findFragmentByTag("Setting") != null) {
//                    Log.d(TAG, "recv : " + cmd);
                    fragSetting.addTextToTextView(cmd);
                }

                String[] value = split[1].split(",");
                buttonNum = Integer.parseInt(value[0]);
                pinFlag[buttonNum] = Integer.parseInt(value[1]);
                onUpdataButton(buttonNum);
//            } else if (split[0].contains("num")) {//cmd num
//                Log.d(TAG, "cmd, num : " + cmd);
//                imageSwitcher2.setImageResource(imageNum[Integer.parseInt(split[1])]);
//                imageSwitcher2.invalidate();
            } else if (split[0].contains("wflist")) {//cmd wifi
                if (getFragmentManager().findFragmentByTag("Setting") != null) {
//                    Log.d(TAG, "recv : " + cmd);
                    fragSetting.addTextToTextView(cmd);
                }

                String[] value = split[1].split(",");

                if (Integer.parseInt(value[0]) == 0) {
                    fragSetting.wifiArrayList.clear();
                    tempstr = value[1];
                    fragSetting.wifiArrayList.add(0, "Select Wi-Fi to connect");
                    fragSetting.spinnerWifi.setAdapter(fragSetting.WifiAdapter);
                } else {
                    fragSetting.wifiArrayList.add(Integer.parseInt(value[0]), value[1]);
                }
            } else if (split[0].contains("wfssid")) {//cmd wifi
                if (getFragmentManager().findFragmentByTag("Setting") != null) {
//                    Log.d(TAG, "msg : " + cmd);
                    fragSetting.addTextToTextView(cmd);
                    fragSetting.wifissid2.setText(split[1]);
                    if (!userSet.isKeepTempWifi()) {
                        userSet.clearTempWifi();
                    }
                }
            } else if (split[0].contains("wfpsk")) {//cmd wifi
                if (getFragmentManager().findFragmentByTag("Setting") != null) {
//                    Log.d(TAG, "msg : " + cmd);
//                    fragSetting.addTextToTextView(cmd);
//                    fragSetting.wifiIp2.setText(split[1]);
                    if (!userSet.isKeepTempWifi()) {
                        userSet.clearTempWifi();
                    }
                }
            } else if (split[0].contains("wfip")) {//cmd wifi
                if (getFragmentManager().findFragmentByTag("Setting") != null) {
                    fragSetting.addTextToTextView(cmd);
                    fragSetting.wifiIp2.setText(split[1]);
                    if (userSet.isKeepTempWifi()) {
                        userSet.setWifiInfo(split[1]);
                        userSet.setWifiSel(userSet.getWifiSel() + 1);
                        fragSetting.refreshListView();
                        Log.d(TAG, "setWifiInfo");
                    } else {
                        userSet.clearTempWifi();
                    }
                }
            } else if (split[0].contains("wfstatus")) {//cmd wifi
                if (getFragmentManager().findFragmentByTag("Setting") != null) {
//                    Log.d(TAG, "msg : " + cmd);
                    fragSetting.addTextToTextView(cmd);

                    switch (Integer.parseInt(split[1])) {
                        case 0:
                            fragSetting.wifiStatus2.setText("Wi-Fi Idle Status");
                            break;
                        case 1:
                            fragSetting.wifiStatus2.setText("Wi-Fi SSID Available");
                            break;
                        case 2:
                            fragSetting.wifiStatus2.setText("Wi-Fi Scan Completed");
                            break;
                        case 3:
                            fragSetting.wifiStatus2.setText("Wi-Fi Connected");
                            break;
                        case 4:
                            fragSetting.wifiStatus2.setText("Wi-Fi Connect Fail");
                            break;
                        case 5:
                            fragSetting.wifiStatus2.setText("Wi-Fi Connect Lost");
                            break;
                        case 6:
                            fragSetting.wifiStatus2.setText("Wi-Fi Disconnected");
                            break;
                    }
                }
            } else {// cmd
                if (getFragmentManager().findFragmentByTag("Setting") != null) {
//                    Log.d(TAG, "msg : " + cmd);
                    fragSetting.addTextToTextView(cmd);
                }
            }
        } else {
            if (getFragmentManager().findFragmentByTag("Setting") != null) {
//                Log.d(TAG, "msg : " + cmd);
                fragSetting.addTextToTextView(cmd);
            }

            if (cmd.equals("initend")) {//cmd init
//                Log.d(TAG, "cmd, init finish: " + cmd);
//                state = State.run;
            } else if (cmd.equals("scanend")) {//cmd init
//                fragSetting.spinnerWifi.setSelection(0);
                fragSetting.spinnerWifi.setPrompt("Select Wi-Fi to connect.");
                fragSetting.wifiArrayList.remove(0);
                fragSetting.wifiArrayList.add(0, tempstr);
//                fragSetting.spinnerWifi.setAdapter(fragSetting.WifiAdapter);
            } else {// cmd
                Log.d(TAG, "cmd, else : " + cmd);
            }
        }
    }

    @Override
    public void settingSelected(FragSetting.SetParam index, String string, int value) {
        Log.d(TAG, "settingSelected : " + index + "," + string + "," + value);
        switch (index) {
            case FilmDisconnect:
                if (socket.isConnected()) socket.sendString("#disconnect");
                break;
            case SetInit:
                fragSetting.initTextView(userSet.getWifiIp());
//                if (socket.isConnected()) socket.sendString("#wfinfo");
                break;
            case FilmConnect:
//                if (string != null) userSet.setWifiIp(string);
                if (!socket.isConnected()) socketStart();
                else Toast.makeText(this, "Wifi is already connected.", Toast.LENGTH_SHORT).show();
                break;
            case SendMsg:
                if (socket.isConnected()) socket.sendString("#" + string);
                else Toast.makeText(this, "Wifi is disconnect.", Toast.LENGTH_SHORT).show();
                break;
            case SetDefault:
                userSet.setDefault();
                fragSetting.initTextView(userSet.getWifiIp());
                if (socket.isConnected()) socket.sendString("#wfinfo");
                break;
            case FilmNumSet:
                userSet.setFilmNum(value);
                break;
            case WifiScan:
                if (socket.isConnected()) {
                    fragSetting.wifiArrayList.remove(0);
                    fragSetting.wifiArrayList.add(0, "Scanning...");
                    fragSetting.spinnerWifi.setAdapter(fragSetting.WifiAdapter);
                    socket.sendString("#wfscan");
                } else Toast.makeText(this, "Wifi is disconnect.", Toast.LENGTH_SHORT).show();
                break;
            case WifiConnect:
                if (socket.isConnected()) {
                    socket.sendString("#wfconn=" + value + "," + string);
                    if (mProgressDialog == null)
                        mProgressDialog = ProgressDialog.show(this, "Film connecting to " + fragSetting.wifiArrayList.get(value),
                                "According to the wireless environment, it may take more than one minute.");
                    MyWifi myWifi = new MyWifi(this, new MyWifi.ProgressListner() {
                        @Override
                        public void progressDialog(MyWifi.WifiState wifiState, final String string) {
                            if (wifiState == MyWifi.WifiState.WAIT_CONNECT_FILM & string != null) {
                                if (!socket.isConnected()) socketStart();
                            } else if (wifiState == MyWifi.WifiState.CONNECT_SUCCESS) {
                                mProgressDialog.dismiss();
                            } else {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (string != null) {
                                            mProgressDialog.setMessage(string);
                                        }
                                    }
                                });
                            }
                        }
                    });
                    String[] wifiInfo = userSet.getWifiInfo();
                    myWifi.setWifiInfo(wifiInfo[0], wifiInfo[1]);
                    myWifi.setRunning(true, 20, 500);
                    myWifi.start();

                } else Toast.makeText(this, "Wifi is disconnect.", Toast.LENGTH_SHORT).show();

                break;
            default:
                break;
        }
    }

    Handler handler = new Handler();

    public void setFragment() {
        switch (userSet.getFilmNum()) {
            case 0:
//                switchFragment(FragSetting.newInstance(1), "Setting");
                fragSetting = FragSetting.newInstance(0);
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame, fragSetting, "Setting")
//                            .addToBackStack(null)
                        .commit();
                break;
            case 1:
                fragButton1 = new FragButton1();
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame, fragButton1)
//                            .addToBackStack(null)
                        .commit();
                break;
            case 8:
                fragButton8 = new FragButton8();
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame, fragButton8)
//                            .addToBackStack(null)
                        .commit();
                break;
            case 9:
                fragButton9 = new FragButton9();
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame, fragButton9)
//                            .addToBackStack(null)
                        .commit();
                break;
            default:
                fragButton1 = new FragButton1();
                getFragmentManager().beginTransaction().add(R.id.frame, fragButton1).commit();
                break;
        }
        Log.d(TAG, "filmNum" + userSet.getFilmNum());
//        } else {
//            Toast.makeText(this, "이미 추가되어 있습니다.", Toast.LENGTH_SHORT).show();
//        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_connect) {
            if (!socket.isConnected()) socketStart();
        } else if (id == R.id.nav_manage) {
            if (getFragmentManager().findFragmentByTag("Setting") == null) {
                fragSetting = FragSetting.newInstance(userSet.getFilmNum());
                switchFragment(fragSetting, "Setting");
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void switchFragment(Fragment fragment, String name) {
        getFragmentManager().beginTransaction()
//                .setCustomAnimations(R.anim.enter_from_bottom, R.anim.alpha_hide,
//                        R.anim.alpha_show, R.anim.leave_to_bottom)
                .replace(R.id.frame, fragment, name)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
            Log.d(TAG, "getFragmentManager().getBackStackEntryCount() : " + getFragmentManager().getBackStackEntryCount());
            setFragment();
            Log.d(TAG, "getFragmentManager().getBackStackEntryCount() : " + getFragmentManager().getBackStackEntryCount());
            if (socket != null) {
                if (socket.isConnected()) socket.sendString("#init");
            }
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onButtonSelected1(int index) {
        if (socket != null)//??
            if (socket.isConnected()) {
                if (pinFlag[index] == 1) socket.sendString("#pin=" + index + ",0");
                else socket.sendString("#pin=" + index + ",1");
            } else {
                Toast.makeText(this, "Wifi is disconnect.", Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    public void onButtonSelected8(int index) {
        if (socket.isConnected()) {
            if (pinFlag[index] == 1) socket.sendString("#pin=" + index + ",0");
            else socket.sendString("#pin=" + index + ",1");
        } else {
            Toast.makeText(this, "Wifi is disconnect.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onButtonSelected9(int index) {
        if (socket.isConnected()) {
            if (pinFlag[index] == 1) socket.sendString("#pin=" + index + ",0");
            else socket.sendString("#pin=" + index + ",1");
        } else {
            Toast.makeText(this, "Wifi is disconnect.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onUpdataButton(int index) {
        switch (userSet.getFilmNum()) {
            case 0:
                break;
            case 1:
                fragButton1.updataButton(index, pinFlag[index]);
                break;
            case 8:
                fragButton8.updataButton(index, pinFlag[index]);
                break;
            case 9:
                fragButton9.updataButton(index, pinFlag[index]);
                break;
            default:
                break;
        }
    }

    private void massageHanlder() {
        mHandler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                String msg = (String) inputMessage.obj;
                switch (inputMessage.what) {
                    case 0://Connected
                        if (failProgressDialog != null) failProgressDialog.dismiss();
                        imageSwitcher.setImageResource(imageArray[1]);
                        imageSwitcher.invalidate();
                        aliveCnt = 0;
//                        try {
                        if (getFragmentManager().findFragmentByTag("Setting") != null) {
                            socket.sendString("#wfinfo");
                        } else {
                            socket.sendString("#init");
                        }

                        if (aliveThread == null) {
                            aliveThread = new AliveThread();
                            aliveThread.start();
                        }
                        aliveThread.setRuntFlag(true);
                        aliveThread.alive = true;
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Log.d(TAG, "Exception 1");
//                        }
                        break;

                    case 1://data
                        if (msg.startsWith("#")) commandHandle(msg.substring(1));
                        break;

                    case 2://Disonnected
                        imageSwitcher.setImageResource(imageArray[0]);
                        imageSwitcher.invalidate();
                        aliveThread.setRuntFlag(false);
//                        imageSwitcher2.setImageResource(imageNum[0]);
//                        imageSwitcher2.invalidate();
                        if (getFragmentManager().findFragmentByTag("Setting") != null)
                            fragSetting.initTextView(null);
//                        state = State.stop;
                        break;

                    case 3://Connect Fail
                        if (Integer.parseInt(msg) == socket.getAttemptConnect()) {//접속 시도 횟수
//                            failProgressDialog.setMessage("Try to Connect : " + msg + "sec");
//                            failProgressDialog.show();
//                        } else {
                            socket.setRuntFlag(false);
                            if (aliveThread != null) aliveThread.setRuntFlag(false);
//                            if (failProgressDialog != null) failProgressDialog.dismiss();
//                            failDialogStart();
//                            failDialogInit();
//                            failDialog.show();
                        }
                        break;
                }
            }
        };
    }

    int[] pinFlag = new int[11];

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();

//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }


    private void imageSwitcherInit() {
        imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());
                return imageView;
            }
        });

//        imageSwitcher2 = (ImageSwitcher) findViewById(R.id.imageSwitcher2);
//        imageSwitcher2.setFactory(new ViewSwitcher.ViewFactory() {
//            @Override
//            public View makeView() {
//                ImageView imageView = new ImageView(getApplicationContext());
//                return imageView;
//            }
//        });

        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);

        imageSwitcher.setInAnimation(in);
        imageSwitcher.setOutAnimation(out);
//        imageSwitcher2.setInAnimation(in);
//        imageSwitcher2.setOutAnimation(out);
    }

    private void failDialogInit() {
        failProgressDialog = new ProgressDialog(this);
        failProgressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
        failProgressDialog.setTitle("Connect to Qble Smart Film");

//        AlertDialog.Builder builder;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Connect Fail");
        builder.setMessage("Would you like to check the settings or WiFi?");
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "setPositiveButton");
                //세팅화면열기
            }
        });
        builder.setNegativeButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "setNegativeButton");
                if (getFragmentManager().findFragmentByTag("Setting") == null) {
                    fragSetting = FragSetting.newInstance(userSet.getFilmNum());
                    switchFragment(fragSetting, "Setting");
                }
            }
        });
        builder.setNeutralButton("WiFi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "setNeutralButton");
            }
        });

        failDialog = builder.create();
    }

    class AliveThread extends Thread {
        public boolean alive = false;
        public boolean runtFlag = true;

        public void run() {
            while (runtFlag) {
                if (socket.isConnected()) {
                    if (alive) alive = false;
                    else socket.sendString("#disconnect");
//                    Log.d(TAG, "aliveThread.alive : " + aliveThread.alive);
                    try {
                        Thread.sleep(10000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public boolean isRuntFlag() {
            return runtFlag;
        }

        public void setRuntFlag(boolean runtFlag) {
            this.runtFlag = runtFlag;
        }
    }
}
