package com.ddtpt.android.yffa;

import android.content.Context;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by e228596 on 10/15/2014.
 */
public class Stats {
    private int mId;
    Double mMod;
    private String mName;
    private String mShortName;
    private boolean mIsScored;

    public Stats(int id, double mod, String name, String shortname, boolean isscored) {
        mId = id;
        mMod = mod;
        mName = name;
        mShortName = shortname;
        mIsScored = isscored;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public double getMod() {
        return mMod;
    }

    public void setMod(double mod) {
        mMod = mod;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getShortName() {
        return mShortName;
    }

    public void setShortName(String shortName) {
        mShortName = shortName;
    }

    public boolean getIsScored() {
        return mIsScored;
    }

    public void setIsScored(boolean isScored) {
        mIsScored = isScored;
    }

}
