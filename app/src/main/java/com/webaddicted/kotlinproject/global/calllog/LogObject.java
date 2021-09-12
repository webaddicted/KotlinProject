package com.webaddicted.kotlinproject.global.calllog;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.text.DecimalFormat;
/**
 * Created by Deepak Sharma(webaddicted) on 05/04/2020
 */
public class LogObject implements CallLogObject {
    private String number;
    private long date;
    private int duration, type;
    private final Context context;

    LogObject(Context context) {
        this.context = context;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getCoolDuration() {
        return getCoolDuration(getDuration());
    }

    public String getContactName(){
        if(getNumber() != null){
            return findNameByNumber(getNumber());
        } else {
            return null;
        }
    }

    private String findNameByNumber(final String phoneNumber) {
        ContentResolver cr = context.getContentResolver();

        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));

        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME},null, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null);
        if (cursor == null) {
            return null;
        }

        String contactName = null;

//        if(cursor.moveToFirst()) {
//            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
//        }

        if(!cursor.isClosed()) {
            cursor.close();
        }

        return (contactName == null) ? phoneNumber : contactName;
    }

    private String getCoolDuration(float sum) {

        String duration = "";
        String result;

        if (sum >= 0 && sum < 3600) {

            result = String.valueOf(sum / 60);
            String decimal = result.substring(0, result.lastIndexOf("."));
            String point = "0" + result.substring(result.lastIndexOf("."));

            int minutes = Integer.parseInt(decimal);
            float seconds = Float.parseFloat(point) * 60;

            DecimalFormat formatter = new DecimalFormat("#");
            duration = minutes + " min " + formatter.format(seconds) + " secs";

        } else if (sum >= 3600) {

            result = String.valueOf(sum / 3600);
            String decimal = result.substring(0, result.lastIndexOf("."));
            String point = "0" + result.substring(result.lastIndexOf("."));

            int hours = Integer.parseInt(decimal);
            float minutes = Float.parseFloat(point) * 60;

            DecimalFormat formatter = new DecimalFormat("#");
            duration = hours + " hrs " + formatter.format(minutes) + " min";

        }

        return duration;
    }
}
