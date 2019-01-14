package com.example.vaish.globalwarming;

/**
 * Created by Vaish on 23-09-2016.
 */
public class GlobalWarming {
    private String murl;
    private String mtitle;

    public GlobalWarming(String title,String url){
        mtitle=title;
        murl=url;
    }

    public String getMurl(){
        return murl;
    }

    public String getMtitle(){
        return mtitle;
    }
}
