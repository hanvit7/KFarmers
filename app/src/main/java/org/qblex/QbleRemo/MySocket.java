package org.qblex.QbleRemo;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;


public class MySocket extends Thread {
    private static final String TAG = MySocket.class.getSimpleName();
    private Socket mSocket;

    private BufferedReader buffRecv;
    private BufferedWriter buffSend;

    private String mAddr;
    private int mPort;
    private boolean mConnected = false;
    private Handler mHandler = null;

    public boolean runtFlag = true;
    private int attemptConnect = 3;

    static class MessageTypeClass {
        public static final int SOCK_CONNECTED = 1;
        public static final int SOCK_DATA = 2;
        public static final int SOCK_DISCONNECTED = 3;
    }

    public enum MessageType {SOCKET_CONNECTED, SOCKET_DATA, SOCKET_DISCONNECTED, SOCKET_FAIL}

    public MySocket(String addr, int port, Handler handler) {
        mAddr = addr;
        mPort = port;
        mHandler = handler;
    }

    private void makeMessage(MessageType messageType, Object obj) {
        Message msg = Message.obtain();
        msg.what = messageType.ordinal();
        msg.obj = obj;
        mHandler.sendMessage(msg);
    }

    private boolean connect(String addr, int port) {
        try {
            InetSocketAddress socketAddress = new InetSocketAddress(InetAddress.getByName(addr), port);
            mSocket = new Socket();
             mSocket.connect(socketAddress, port);
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean isRuntFlag() {
        return runtFlag;
    }

    public void setRuntFlag(boolean runtFlag) {
        this.runtFlag = runtFlag;
    }

    public void setAttemptConnect(int num) {
        attemptConnect = num;
    }

    public int getAttemptConnect() {
        return attemptConnect;
    }

    @Override
    public void run() {
        int connectTry = 0;
        while (!connect(mAddr, mPort)) {
            if (connectTry < attemptConnect)
                makeMessage(MessageType.SOCKET_FAIL, String.valueOf(++connectTry));
            Log.d(TAG, "connectTry : " + connectTry);
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            while (!runtFlag) return;
        }
//        Log.d(TAG, "connect(mAddr, mPort)");
        if (mSocket == null) return;
//        Log.d(TAG, "mSocket != null");

        try {
            buffRecv = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            buffSend = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mConnected = true;
        makeMessage(MessageType.SOCKET_CONNECTED, null);
//        Log.d(TAG, "Connected");

        String aLine;
        while (!Thread.interrupted()) {
            try {
                aLine = buffRecv.readLine();
                if (aLine != null) makeMessage(MessageType.SOCKET_DATA, aLine);
                else break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        Log.d(TAG, "interrupted out");
        makeMessage(MessageType.SOCKET_DISCONNECTED, null);

        socketClose();
    }

    synchronized public boolean isConnected() {
        return mConnected;
    }

    public void sendString(String str) {
        PrintWriter out = new PrintWriter(buffSend, true);
        out.println(str);
        Log.d(TAG, "send : " + str);
    }

    public void socketClose() {
        try {
            buffRecv.close();
            buffSend.close();
            mSocket = null;
            Log.d(TAG, "Soket Close");
        } catch (IOException e) {
            e.printStackTrace();
        }
        mConnected = false;
    }
}

