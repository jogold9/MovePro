package com.joshbgold.movePro.backend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.util.Calendar;

public class AlarmReceiver extends WakefulBroadcastReceiver {

 /*  sharedPreferences not pulling the same default preferences yet, I think the context has something to do with it. May need to learn how to
    using a specific named sharedPreferences file instead of using the default sharedPreferences file
    http://stackoverflow.com/questions/14658469/android-intent-context-confusing

    "BroadcastReceivers do not inherit context either. In fact, they do not contain context at all, but simply receive the current context when an
    event is received (Such as onReceive(Context context, Intent intent))" */

    Context myContext;
    Context onReceiveContext;

    public AlarmReceiver(Context context){
        myContext = context;
    }

    public AlarmReceiver() {

    }

    private boolean workHoursOnly = false;
    private boolean noWeekends = false;

    @Override
    public void onReceive(final Context context, Intent intent) {
        onReceiveContext = context;

        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int today = calendar.get(Calendar.DAY_OF_WEEK);
        boolean isWeekend = (today == Calendar.SUNDAY) || (today == Calendar.SATURDAY);
        boolean isOutsideWorkHours = (currentHour < 9) || (currentHour > 16);

        workHoursOnly = loadPrefs("workHoursOnlyKey", workHoursOnly);

        noWeekends = loadPrefs("noWeekendsKey", noWeekends);

        if(isWeekend && noWeekends) {
            try {
                Thread.sleep(1);  //waits for millisecond
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        else if (isOutsideWorkHours  && workHoursOnly){
            //Alarm not wanted outside of work hours
            try {
                Thread.sleep(1);  //waits for millisecond
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        else {
            //Alarm is wanted, and should go off
            Intent myIntent = new Intent();
            myIntent.setClassName("com.joshbgold.movePro", "com.joshbgold.movePro.main.ReminderActivity");
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(myIntent);
        }

    }

    //get prefs
    private boolean loadPrefs(String key,boolean value) {
        SharedPreferences sharedPreferences = onReceiveContext.getSharedPreferences("MoveAppPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, value);
    }
}