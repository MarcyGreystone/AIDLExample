package com.marcy.userserver;

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
import android.widget.Button;

import com.marcy.aidlexample.Elf;

public class ServerActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private ElfManagerService.ElfManagerBinder mElfBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        initService();
        Button mButton = findViewById(R.id.bt_add);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mElfBinder.addElf(new Elf("Estinien" , 32 , "dell"));
            }
        });
    }

    /**
     * 初始化服务
     */
    private void initService(){
        ServiceConnection mServiceConnection = new ServerServiceConnection();
        bindService(new Intent(this , ElfManagerService.class) , mServiceConnection,  Context.BIND_AUTO_CREATE);
    }

    class ServerServiceConnection implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, TAG + "-->onServiceConnected: ");
            mElfBinder = (ElfManagerService.ElfManagerBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, TAG + "-->onServiceDisconnected: ");
        }
    }
}
