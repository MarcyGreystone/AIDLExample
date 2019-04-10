// IOnElfItemChangeListener.aidl
package com.marcy.aidlexample;
import com.marcy.aidlexample.Elf;

// Declare any non-default types here with import statements
//监听器

interface IOnElfItemChangeListener {
    //监听elf单人信息添加
    void onElfItemAdd(in Elf newElf);

    void onElfItemAddGetList(in List<Elf> newElfList);
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
}
