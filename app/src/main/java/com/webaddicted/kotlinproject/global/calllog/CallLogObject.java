package com.webaddicted.kotlinproject.global.calllog;

/**
 * Created by Deepak Sharma(webaddicted) on 05/04/2020
 */
public interface CallLogObject {

    String getNumber();

    void setNumber(String number);

    int getType();

    void setType(int type);

    long getDate();

    void setDate(long date);

    int getDuration();

    void setDuration(int duration);

    String getCoolDuration();

    String getContactName();

}
