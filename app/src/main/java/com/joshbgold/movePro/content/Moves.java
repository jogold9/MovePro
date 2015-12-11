package com.joshbgold.movePro.content;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.joshbgold.movePro.main.AlarmActivity;

import java.util.ArrayList;

public class Moves extends Activity {

    private static Context mAppContext = AlarmActivity.getAppContext();

    public ArrayList<String> movementsArrayList = new ArrayList<>();

    public boolean adductor = false;
    public boolean backbends = false;
    public boolean ceilingReach = false;
    public boolean chestStretch = false;
    public boolean figure4 = false;
    public boolean hamstrings = false;
    public boolean hipFlexor = false;
    public boolean neckExtension = false;
    public boolean neckRetraction = false;
    public boolean quadStretch = false;
    public boolean scapRetraction = false;
    public boolean squats = false;
    public boolean walk = false;
    public String customReminder ="";

    public Moves() {

        customReminder = loadPrefs("customReminder", customReminder);
        adductor = loadPrefs("adductor", adductor);
        backbends = loadPrefs("backbends", backbends);
        chestStretch = loadPrefs("chestStretch", chestStretch);
        ceilingReach = loadPrefs("ceilingReach", ceilingReach);
        figure4 = loadPrefs("figure4", figure4);
        hamstrings = loadPrefs("hamstrings", hamstrings);
        hipFlexor = loadPrefs("hipFlexor", hipFlexor);
        neckExtension = loadPrefs("neckExtension", neckExtension);
        neckRetraction = loadPrefs("neckRetraction", neckRetraction);
        quadStretch = loadPrefs("quadStretch", quadStretch);
        scapRetraction = loadPrefs("scapRetraction", scapRetraction);
        squats = loadPrefs("squats", squats);
        walk = loadPrefs("walk", walk);
    }

    public StringBuilder getMoves(){
        StringBuilder movements = new StringBuilder();

        if(customReminder != null && !customReminder.isEmpty()){
            movementsArrayList.add(customReminder);
        }
        if(adductor){
            movementsArrayList.add("adductor");
        }
        if(backbends){
            movementsArrayList.add("back bends");
        }
        if (ceilingReach){
            movementsArrayList.add("ceiling reach");
        }
        if (chestStretch){
            movementsArrayList.add("chest stretch");
        }
        if(figure4){
            movementsArrayList.add("figure 4");
        }
        if(hamstrings){
            movementsArrayList.add("hamstrings");
        }
        if(hipFlexor){
            movementsArrayList.add("hip flexor stretch");
        }
        if (neckExtension){
            movementsArrayList.add("neck extension");
        }
        if (neckRetraction){
            movementsArrayList.add("neck retraction");
        }
        if(quadStretch){
            movementsArrayList.add("quadriceps stretch");
        }
        if (scapRetraction){
            movementsArrayList.add("scapular retraction");
        }
        if(squats){
            movementsArrayList.add("squats");
        }
        if (walk){
            movementsArrayList.add("walk");
        }

        //here we get the ArrayList content that the user specified in the checkboxes
        if (movementsArrayList.isEmpty()){
            movements.append("Walk now! It's critical for your brain health and physical health.");
        }
        else if (movementsArrayList.size() == 1){
            movements.append(movementsArrayList.get(0));
        }

        //adds the 1st alement of the arrayList to the string. Then for the rest, it adds commas space and the string
        else {
            movements.append(movementsArrayList.get(0).toString());
            for (int i = 1; i < movementsArrayList.size(); i++){
                movements.append(", ").append(movementsArrayList.get(i).toString());
            }
        }

        return movements;
    }

    //get prefs
    private boolean loadPrefs(String key,boolean value) {
        SharedPreferences sharedPreferences = mAppContext.getSharedPreferences("MoveAppPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, value);
    }
    private String loadPrefs(String key,String value) {
        SharedPreferences sharedPreferences = mAppContext.getSharedPreferences("MoveAppPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, value);
    }
}
