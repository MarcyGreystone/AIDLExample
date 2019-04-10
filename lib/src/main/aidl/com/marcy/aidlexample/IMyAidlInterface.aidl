// IMyAidlInterface.aidl
package com.marcy.aidlexample;
import com.marcy.aidlexample.Elf;
import com.marcy.aidlexample.IOnElfItemChangeListener;

// Declare any non-default types here with import statements

interface IMyAidlInterface {
    //获取单个elf信息
    Elf getElf(int i);

    //获取elflist
    List<Elf> getElfList();

    //增加elf信息
    void addElf(in Elf elf);

    //注册监听
    void registerListener(IOnElfItemChangeListener listener);
    //移除监听
    void unregisterListener(IOnElfItemChangeListener listener);
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
}
