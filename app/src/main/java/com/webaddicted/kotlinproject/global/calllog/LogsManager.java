package com.webaddicted.kotlinproject.global.calllog;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class LogsManager {

    public static final int INCOMING = CallLog.Calls.INCOMING_TYPE;
    public static final int OUTGOING = CallLog.Calls.OUTGOING_TYPE;
    public static final int MISSED = CallLog.Calls.MISSED_TYPE;
    public static final int TOTAL = 579;

    public static final int INCOMING_CALLS = 672;
    public static final int OUTGOING_CALLS = 609;
    public static final int MISSED_CALLS = 874;
    public static final int ALL_CALLS = 814;
    private static final int READ_CALL_LOG = 47;
    private final Context context;


    public LogsManager(Context context) {
        this.context = context;
    }

    public int getOutgoingDuration() {
        int sum = 0;

        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
                CallLog.Calls.TYPE + " = " + CallLog.Calls.OUTGOING_TYPE, null, null);

        int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);

        while (cursor.moveToNext()) {
            String callDuration = cursor.getString(duration);
            sum += Integer.parseInt(callDuration);
        }

        cursor.close();

        return sum;
    }

    public int getIncomingDuration() {
        int sum = 0;

        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
                CallLog.Calls.TYPE + " = " + CallLog.Calls.INCOMING_TYPE, null, null);

        int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);

        while (cursor.moveToNext()) {
            String callDuration = cursor.getString(duration);
            sum += Integer.parseInt(callDuration);
        }

        cursor.close();

        return sum;
    }

    public int getTotalDuration() {
        int sum = 0;

        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);

        int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);

        while (cursor.moveToNext()) {
            String callDuration = cursor.getString(duration);
            sum += Integer.parseInt(callDuration);
        }

        cursor.close();

        return sum;
    }

    public String getCoolDuration(int type) {
        float sum;

        switch (type) {
            case INCOMING:
                sum = getIncomingDuration();
                break;
            case OUTGOING:
                sum = getOutgoingDuration();
                break;
            case TOTAL:
                sum = getTotalDuration();
                break;
            default:
                sum = 0;
        }

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

    public List<LogObject> getLogs(int callType) {
        List<LogObject> logs = new ArrayList<>();

        String selection;

        switch (callType) {
            case INCOMING_CALLS:
                selection = CallLog.Calls.TYPE + " = " + CallLog.Calls.INCOMING_TYPE;
                break;
            case OUTGOING_CALLS:
                selection = CallLog.Calls.TYPE + " = " + CallLog.Calls.OUTGOING_TYPE;
                break;
            case MISSED_CALLS:
                selection = CallLog.Calls.TYPE + " = " + CallLog.Calls.MISSED_TYPE;
                break;
            case ALL_CALLS:
                selection = null;
            default:
                selection = null;
        }

        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, selection, null, null);
        int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = cursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);

        while (cursor.moveToNext()) {
            LogObject log = new LogObject(context);

            log.setNumber(cursor.getString(number));
            log.setType(cursor.getInt(type));
            log.setDuration(cursor.getInt(duration));
            log.setDate(cursor.getLong(date));

            logs.add(log);
        }

        cursor.close();


        return logs;
    }

}
