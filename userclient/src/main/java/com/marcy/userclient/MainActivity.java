package com.marcy.userclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.marcy.aidlexample.Elf;
import com.marcy.aidlexample.IMyAidlInterface;
import com.marcy.aidlexample.IOnElfItemChangeListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private TextView mTvInform;
    private IMyAidlInterface mMyAidlInterface;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, TAG + "-->onServiceConnected: " + name);
            IMyAidlInterface aidlInterface = IMyAidlInterface.Stub.asInterface(service);
            mMyAidlInterface = aidlInterface;
            try {
                List<Elf> list = aidlInterface.getElfList();
                Log.d(TAG, "onServiceConnected: query elf list , list type:" + list.getClass().getCanonicalName());
                Log.d(TAG, "onServiceConnected: query book list:" + list.toString());
                //注册监听器
                aidlInterface.registerListener(mOnElfItemChangeListener);

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private IOnElfItemChangeListener mOnElfItemChangeListener = new IOnElfItemChangeListener.Stub() {
        @Override
        public void onElfItemAdd(Elf newElf) {
            Log.d(TAG, TAG + "-->onElfItemAdd: " + newElf.toString());
        }

        @Override
        public void onElfItemAddGetList(List<Elf> newElfList) {
            Log.d(TAG, TAG + "-->onElfItemAddGetList: " + newElfList.toString());
        }

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvInform = findViewById(R.id.tv_inform);

    }

    public void bindService(View view){
        Intent intent = new Intent();
        intent.setAction("com.marcy.userserver.elf");
//        intent.setPackage("com.marcy.userserver");
        intent.setComponent(new ComponentName("com.marcy.userserver" , "com.marcy.userserver.ElfManagerService"));
        bindService(intent , mConnection , Context.BIND_AUTO_CREATE);
    }

    public void addElf(View view){
        Elf elf = new Elf("Carvallain" , 34 , "Durendaire");
        try {
            mMyAidlInterface.addElf(elf);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setElfInform(View view){
        try {
            mTvInform.setText(mMyAidlInterface.getElfList().toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void unBindService(View view){
        if(mMyAidlInterface != null){
            unbindService(mConnection);
            mMyAidlInterface = null;
        }
    }

    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        Log.d(TAG, TAG + "-->bindService: ");
        return super.bindService(service, conn, flags);
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
        Log.d(TAG, TAG + "-->unbindService: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if(mMyAidlInterface == null)
                return;
            mTvInform.setText(mMyAidlInterface.getElfList().toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        if(mMyAidlInterface != null && mMyAidlInterface.asBinder().isBinderAlive()){
            try {
                mMyAidlInterface.unregisterListener(mOnElfItemChangeListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mConnection);
        super.onDestroy();
    }
}
