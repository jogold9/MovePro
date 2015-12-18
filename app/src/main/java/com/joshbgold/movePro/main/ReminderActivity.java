package com.joshbgold.movePro.main;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.joshbgold.movePro.R;
import com.joshbgold.movePro.backend.AlarmReceiver;
import com.joshbgold.movePro.content.Moves;

import java.text.DateFormat;
import java.util.Date;


public class ReminderActivity extends Activity {

    AlarmManager alarmManager;
    private TextView movesAndQuotesTextView;
    private StringBuilder movesString = new StringBuilder();
    private static PendingIntent pendingIntent;
    private float volume = (float) 0.5;
    String name = "Josh";
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        mContext = getApplicationContext();
        movesAndQuotesTextView = (TextView) findViewById(R.id.doThisThing);
        final Button cancelButton = (Button) findViewById(R.id.cancelAllButton);
        final Button exitButton = (Button) findViewById(R.id.exitButton);
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.drawable.om_mani_short); //used to play mp3 audio file
        final CheckBox checkbox = (CheckBox) findViewById(R.id.completedCheckbox);

        //vibrate the device for 1/2 second if the device is capable
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(500);
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mediaplayer) {
                mediaplayer.stop();
                mediaplayer.release();
            }
        });

        volume = loadPrefs("volumeKey", volume); //gets the current volume

        mediaPlayer.setVolume(volume, volume); //sets right speaker volume and left speaker volume for mediaPlayer
        mediaPlayer.start();

        //Puts random move instruction into text view (i.e. breathe, stretch, go outside, etc).
        Moves moveObject = new Moves();
        movesString = moveObject.getMoves();
        movesAndQuotesTextView.setText(movesString);

        //Sent email with time stamp if user marks rehab as completed
        View.OnClickListener completedCheckbox = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        };

        //cancel all alarms
        View.OnClickListener cancelAll = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlarmActivity.getAppContext();
                Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, intent, 0);
                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);

                if (mediaPlayer != null) try {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                    }
                } catch (Exception e) {
                    Log.d("Alarm Activity", e.toString());
                }

                Toast.makeText(ReminderActivity.this, "Alarms Canceled", Toast.LENGTH_LONG).show();
            }
        };

        View.OnClickListener quitApp = new View.OnClickListener() {  //this block stops music when exiting
            @Override
            public void onClick(View view) {

                if (mediaPlayer != null) try {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                    }
                } catch (Exception e) {
                    Log.d("Alarm Activity", e.toString());
                }

                finish();
            }
        };

        checkbox.setOnClickListener(completedCheckbox);
        cancelButton.setOnClickListener(cancelAll);
        exitButton.setOnClickListener(quitApp);
    }

    private void sendEmail() {
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        //String emailDestinations[] = { "info@bridgetownpt.com" };
        String emailDestinations[] = { "mhatfield@bridgetownpt.com, gheadley@bridgetownpt.com" };
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, emailDestinations);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Rehab completed");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "At " + currentDateTimeString + " I did these things: " + movesString);
        startActivity(Intent.createChooser(emailIntent, "Send your email in:"));
    }

    //get prefs
    private float loadPrefs(String key, float value) {
        SharedPreferences sharedPreferences = getSharedPreferences("MoveAppPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getFloat(key, value);
    }
}
