package com.marcy.aidlexample;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Marcy on 2019/4/9
 */
public class Elf implements Parcelable {
    private String name;
    private int age;
    private String family;

    public Elf(String name, int age, String family) {
        this.name = name;
        this.age = age;
        this.family = family;
    }

    protected Elf(Parcel in) {
        name = in.readString();
        age = in.readInt();
        family = in.readString();
    }

    public static final Creator<Elf> CREATOR = new Creator<Elf>() {
        @Override
        public Elf createFromParcel(Parcel in) {
            return new Elf(in);
        }

        @Override
        public Elf[] newArray(int size) {
            return new Elf[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //不写入parcel会导致数据无法正常传递
        dest.writeString(name);
        dest.writeInt(age);
        dest.writeString(family);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    @Override
    public String toString() {
        return "Elf{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", family='" + family + '\'' +
                '}';
    }
}
