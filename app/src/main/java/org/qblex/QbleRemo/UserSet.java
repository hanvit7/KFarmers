package org.qblex.QbleRemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by VIT_HOME on 2016-05-23.
 */
public class UserSet {
    private static final String TAG = UserSet.class.getSimpleName();
    private SharedPreferences setting;
    private SharedPreferences.Editor editor;

    public UserSet(Context context) {
        setting = PreferenceManager.getDefaultSharedPreferences(context);
        editor = setting.edit();
        if (setting.getInt("WifiSel", -1) == -1) setDefault();
    }

    public void setDefault() {
        editor.clear();
        setAutoConnect(false);
        setFilmNum(0);
        setWifiSel(0);
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add(0, "QbleSmartFilm" + "," + "Qwer1234" + "," + "192.168.11.11");
        arrayList.add(1, "QbleSmartFilm" + "," + "Qwer1234" + "," + "192.168.22.22");
        arrayList.add(2, "QbleSmartFilm" + "," + "Qwer1234" + "," + "192.168.33.33");
        setWifiList("wifi", arrayList);
    }

    public void keepTempWifi(String tempId, String tempPw) {
        setUserData("tempId", tempId);
        setUserData("tempPw", tempPw);
    }

    public void clearTempWifi() {
        setUserData("tempId", (String) null);
        setUserData("tempPw", (String) null);
    }

    public boolean isKeepTempWifi() {
        String tempId = setting.getString("tempId", null);
        Log.d(TAG, "tempId_ : " + tempId);
        if (tempId != null) return true;
        else return false;
    }

    public void setWifiInfo(String ip) {
        Log.d(TAG, "setWifiInfo:");
        ArrayList arrayList = getWifiList();
        int size = arrayList.size();
        Log.d(TAG, "size:" + size);
        String tempId = setting.getString("tempId", null);
        tempId = tempId.substring(0, tempId.indexOf(" (-"));
        Log.d(TAG, "tempId:" + tempId);
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).toString().contains(tempId)) {
                arrayList.remove(i);
                Log.d(TAG, "arrayList.remove(" + i + "):");
            }
        }
        arrayList.add(0, tempId + "," + setting.getString("tempPw", null) + "," + ip);

        for (int i = 0; i < arrayList.size(); i++) {
            Log.d(TAG, "set arrayList(" + i + "):" + arrayList.get(i));
        }
        setWifiList("wifi", arrayList);

        clearTempWifi();
    }

    public void setWifiList(String key, ArrayList<String> arrayList) {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < arrayList.size(); i++) {
            jsonArray.put(arrayList.get(i));
        }

        if (!arrayList.isEmpty()) {
            editor.putString(key, jsonArray.toString());
            Log.d(TAG, "key, val:" + key + ", " + jsonArray.toString());
        } else {
            editor.putString(key, null);
        }
        editor.commit();
    }

    public ArrayList<String> getWifiList() {
        String jsonKey = setting.getString("wifi", null);
        ArrayList<String> arrayList = new ArrayList<String>();

        if (jsonKey != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonKey);
                for (int i = 0; i < jsonArray.length(); i++) {
                    String string = jsonArray.optString(i);
                    arrayList.add(string);
//                    Log.d(TAG, "jsonArray(" + i + "):" + jsonArray.optString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
//            Assert.assertNotNull(jsonKey);
        }
        return arrayList;
    }

    public String[] getWifiInfo() {
        ArrayList arrayList = getWifiList();
        String[] wifiInfo = arrayList.get(getWifiSel()).toString().split(",");
        Log.d(TAG, "tempWifi : " + wifiInfo[0]);//AP_NAME
        Log.d(TAG, "tempWifi : " + wifiInfo[1]);//AP_PASSWORD
        Log.d(TAG, "tempWifi : " + wifiInfo[2]);//AP_IP
        return wifiInfo;
    }

    public String getWifiInfo(int sel) {
        ArrayList arrayList = getWifiList();
        String[] wifiInfo = arrayList.get(getWifiSel()).toString().split(",");
        return wifiInfo[sel];
    }

    public ArrayList<String> getWifiNameList() {
        ArrayList<String> arrayList = getWifiList();
        for (int i = 0; i < arrayList.size(); i++) {
            String[] wifiInfo = arrayList.get(i).toString().split(",");
            arrayList.set(i, wifiInfo[0] + " (" + wifiInfo[2] + ")");
            Log.d(TAG, "get arrayList(" + i + "):" + arrayList.get(i));
        }
        return arrayList;
    }

    public void setWifiSel(int value) {
        setUserData("WifiSel", value);
    }

    public int getWifiSel() {
        return setting.getInt("WifiSel", -1);
    }

//    public void setWifiIp(String userIp) {
//        setUserData("userip", userIp);
//    }

    public String getWifiIp() {
        return getWifiInfo(2);
    }

    public void setFilmNum(int value) {
        setUserData("filmNum", value);
    }

    public int getFilmNum() {
        return setting.getInt("filmNum", -1);
    }

    public boolean getAutoConnect() {
        return setting.getBoolean("autoConnect", false);
    }

    public void setAutoConnect(boolean value) {
        setUserData("autoConnect", value);
    }

    private void setUserData(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
//        editor.commit();
    }

    private void setUserData(String key, String value) {
//        editor = setting.edit();
        editor.putString(key, value);
        editor.apply();
//        editor.commit();
    }

    private void setUserData(String key, Boolean value) {
//        editor = setting.edit();
        editor.putBoolean(key, value);
        editor.apply();
//        editor.commit();
    }

}
