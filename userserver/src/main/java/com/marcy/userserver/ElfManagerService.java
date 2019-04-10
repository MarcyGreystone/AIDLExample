package com.marcy.userserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.marcy.aidlexample.Elf;
import com.marcy.aidlexample.IMyAidlInterface;
import com.marcy.aidlexample.IOnElfItemChangeListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Marcy on 2019/4/9
 */
public class ElfManagerService extends Service {

    private final String TAG = this.getClass().getSimpleName();

    private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);

    //CopyOnWriteArrayList 支持并发读写。进行自动线程同步
    private CopyOnWriteArrayList<Elf> mElfList;

    private CopyOnWriteArrayList<IOnElfItemChangeListener> mListenerList = new CopyOnWriteArrayList<>();
    private ElfManagerBinder mBinder = new ElfManagerBinder();

    public ElfManagerService() {
        mElfList = new CopyOnWriteArrayList<>();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, TAG + "-->onCreate: ");
        mElfList.add(new Elf("Haurchefant" , 28 , "Fortemps"));
        mElfList.add(new Elf("Aymeric" , 32 , "Borel"));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, TAG + "-->onBind: ");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, TAG + "-->onUnbind: ");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        mIsServiceDestoryed.set(true);
        super.onDestroy();
    }


    public class ElfManagerBinder extends IMyAidlInterface.Stub{

        @Override
        public Elf getElf(int i) {
            return mElfList.get(i);
        }

        @Override
        public List<Elf> getElfList() {
            return mElfList;
        }

        @Override
        public void addElf(Elf elf) {
            mElfList.add(elf);
            Log.d(TAG, TAG + "-->Server ElfManagerBinder onElfItemAdd: " + elf.toString());
            Log.d(TAG, TAG + "-->Server ElfManagerBinder addElf: " + mElfList.toString());
            for(int i = 0 ; i < mListenerList.size() ; i++){
                IOnElfItemChangeListener listener = mListenerList.get(i);
                Log.d(TAG, TAG + "-->onCreate: notify listener " + listener);
                try {
                    listener.onElfItemAdd(elf);
                    listener.onElfItemAddGetList(mElfList);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void registerListener(com.marcy.aidlexample.IOnElfItemChangeListener listener) {
            if(!mListenerList.contains(listener)){
                mListenerList.add(listener);
            }else{
                Log.d(TAG, TAG + "-->registerListener: already exists.");
            }
            Log.d(TAG, TAG + "-->registerListener: size = " + mListenerList.size());
        }

        @Override
        public void unregisterListener(com.marcy.aidlexample.IOnElfItemChangeListener listener) {
            if(mListenerList.contains(listener)){
                mListenerList.remove(listener);
                Log.d(TAG, TAG + "-->unregisterListener: unregister listener success");
            }else{
                Log.d(TAG, TAG + "-->unregisterListener: not found can not unregister");
            }
            Log.d(TAG, TAG + "-->unregisterListener: size = " + mListenerList.size());
        }

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) {

        }

    }

}
