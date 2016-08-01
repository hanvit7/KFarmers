package org.qblex.QbleRemo;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by VIT_HOME on 2016-05-02.
 */
public class FragSetting extends Fragment implements Button.OnClickListener {
    private static final String TAG = FragSetting.class.getSimpleName();
    public static final String ARGS_NTH = "args-nth";

    public enum SetParam {
        SetInit, FilmConnect, FilmDisconnect, WifiConnect,
        WifiScan, FilmNumSet, SendMsg, SetDefault
    }

    EditText editFilmAddr;
    Button btnConnect;
    Button btnDisconnect;
    Button btnWifiScan;
    Button btnWifiConnect;
    EditText editSendMsg;
    Button btnSendMsg;
    ScrollView scrollView;
    TextView ViewRecv;
    TextView wifiStatus2;
    TextView wifiIp2;
    TextView wifissid2;
    SettingListner settingListner;
    Spinner spinnerFilmCnt;
    Spinner spinnerWifi;
    EditText editWifiPassword;

    ArrayList<String> filmArrayList = null;
    ArrayAdapter<String> filmAdapter = null;
    String[] filmArrayVal = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

    ArrayList<String> wifiArrayList = null;
    ArrayAdapter<String> WifiAdapter = null;
    String[] wifiArrayVal = {""};

    Button btnDefault;
    int wifiNum;
    UserSet userSet;

    Switch autoConnectSwitch;
    ListView listView;
    WifiApAdapter wifiApAdapter;

    public static FragSetting newInstance(int start) {
        FragSetting fragment = new FragSetting();

        Bundle args = new Bundle();
        if (start != 0) args.putInt(ARGS_NTH, start);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_setting, container, false);

        editFilmAddr = (EditText) root.findViewById(R.id.editFilmAddr);
        btnConnect = (Button) root.findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(this);
        btnDisconnect = (Button) root.findViewById(R.id.btnDisconnect);
        btnDisconnect.setOnClickListener(this);

        btnWifiScan = (Button) root.findViewById(R.id.btnWifiScan);
        btnWifiScan.setOnClickListener(this);
        editWifiPassword = (EditText) root.findViewById(R.id.editWifipw);
        btnWifiConnect = (Button) root.findViewById(R.id.btnWifiConnect);
        btnWifiConnect.setOnClickListener(this);

        autoConnectSwitch = (Switch) root.findViewById(R.id.autoConnectSwitch);
        autoConnectSwitch.setOnClickListener(this);
//        editSendMsg = (EditText) root.findViewById(R.id.editSendMsg);
//        editSendMsg.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if(event==MotionEvent.)
//                return false;
//            }
//        });
//        btnSendMsg = (Button) root.findViewById(R.id.btnSendMsg);
//        btnSendMsg.setOnClickListener(this);

        scrollView = (ScrollView) root.findViewById(R.id.scrollView);
        ViewRecv = (TextView) root.findViewById(R.id.ViewRecv);
        wifiStatus2 = (TextView) root.findViewById(R.id.wifistatus2);
        wifissid2 = (TextView) root.findViewById(R.id.wifissid2);
        wifiIp2 = (TextView) root.findViewById(R.id.wifiip2);

        spinnerFilmCnt = (Spinner) root.findViewById(R.id.spinnerFilmCnt);
        filmArrayList = new ArrayList<String>(Arrays.asList(filmArrayVal));
        filmAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_style, filmArrayList);
        filmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilmCnt.setAdapter(filmAdapter);
        spinnerFilmCnt.setSelection(getArguments().getInt(ARGS_NTH, 1) - 1);
        spinnerFilmCnt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView) parent.getChildAt(0)).setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                settingListner.settingSelected(SetParam.FilmNumSet, null, position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnDefault = (Button) root.findViewById(R.id.btnDefault);
        btnDefault.setOnClickListener(this);

        spinnerWifi = (Spinner) root.findViewById(R.id.spinnerWifi);
        wifiArrayList = new ArrayList<String>(Arrays.asList(wifiArrayVal));
//        WifiAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, wifiArrayList);
        WifiAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_style, wifiArrayList);
        WifiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWifi.setAdapter(WifiAdapter);
        spinnerWifi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView) parent.getChildAt(0)).setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                wifiNum = position;
                Log.d(TAG, "clicked");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if (userSet == null) userSet = new UserSet(getActivity());
        userSet.clearTempWifi();
        listView = (ListView) root.findViewById(R.id.listView);
        wifiApAdapter = new WifiApAdapter(userSet.getWifiNameList());
        listView.setAdapter(wifiApAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                userSet.setWifiSel(position);
                listView.setAdapter(wifiApAdapter);
                return true;
            }
        });

        settingListner.settingSelected(SetParam.SetInit, null, 0);
        Log.d(TAG, "settingListner SetInit");
        return root;
    }

    class WifiApAdapter extends BaseAdapter {
        ArrayList<String> arrayList_;
        int wifiSel;

        WifiApAdapter(ArrayList arrayList) {
            arrayList_ = new ArrayList<String>(arrayList);
        }

        @Override
        public int getCount() {
            return arrayList_.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList_.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(getActivity());
            textView.setText(arrayList_.get(position));
            textView.setTextSize(16.0f);
            wifiSel = userSet.getWifiSel();
            if (position == wifiSel) textView.setTextColor(Color.RED);
            else textView.setTextColor(Color.BLACK);

            return textView;
        }
    }

    public void initTextView(String FilmAddr) {
        if (FilmAddr != null)
            editFilmAddr.setText(FilmAddr);
        wifiArrayList.clear();
        wifiArrayList.add(0, "Requires Wi-Fi Scan");
        spinnerWifi.setAdapter(WifiAdapter);
        spinnerWifi.setSelection(0);
//        spinnerWifi.setTextAlignment();
//        ViewRecv.clearComposingText();
//        ViewRecv.append("Receive message");

        wifiStatus2.setText("");
        wifissid2.setText("");
        wifiIp2.setText("");

        autoConnectSwitch.setChecked(userSet.getAutoConnect());
        refreshListView();
    }

    public void refreshListView(){
        wifiApAdapter.arrayList_ = userSet.getWifiNameList();
        listView.setAdapter(wifiApAdapter);
    }
    public void addTextToTextView(String text) {
        ViewRecv.append("\n" + text);
        scrollView.post(new Runnable() {
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (settingListner != null)
            Log.d(TAG, "settingSelected");
        switch (v.getId()) {
            case R.id.btnConnect:
                settingListner.settingSelected(SetParam.FilmConnect, editFilmAddr.getText().toString(), 0);
                break;
            case R.id.btnDisconnect:
                settingListner.settingSelected(SetParam.FilmDisconnect, null, 0);
                break;
//            case R.id.btnSendMsg:
//                settingListner.settingSelected(SetParam.SendMsg, editSendMsg.getText().toString(), 0);
//                break;
            case R.id.btnDefault:
                settingListner.settingSelected(SetParam.SetDefault, null, 0);
                break;
            case R.id.btnWifiScan:
                settingListner.settingSelected(SetParam.WifiScan, null, 0);
                if (userSet.isKeepTempWifi()) userSet.clearTempWifi();
                break;
            case R.id.btnWifiConnect:
                if (wifiArrayList.get(0).contains("Scan")) {
                    Toast.makeText(getActivity(), "Requires Wi-Fi Scan.", Toast.LENGTH_SHORT).show();
                } else {
                    userSet.keepTempWifi(wifiArrayList.get(wifiNum), editWifiPassword.getText().toString());
                    settingListner.settingSelected(SetParam.WifiConnect, editWifiPassword.getText().toString(), wifiNum);
                }
                break;
            case R.id.autoConnectSwitch:
                userSet.setAutoConnect(autoConnectSwitch.isChecked());
                Log.d(TAG, "R.id.autoConnectSwitch" + autoConnectSwitch.isChecked());
                break;
            default:
                break;
        }
    }


    public interface SettingListner {
        void settingSelected(SetParam index, String string, int value);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof SettingListner) settingListner = (SettingListner) activity;
//        if (getArguments().getInt(ARGS_NTH) == 0) return; // init
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        final int nth = getArguments().getInt(ARGS_NTH);
//        ((MainActivity) getActivity()).switchFragment(FragSetting.newInstance(nth + 1));
    }

}


