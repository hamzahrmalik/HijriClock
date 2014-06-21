package com.hamzah.hijriclock;


import java.io.DataOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

public class Hijri_Clock extends Activity {
	
	CheckBox CB_show_date;
	CheckBox CB_show_month;
	CheckBox CB_show_year;
	CheckBox CB_show_month_as_number;
	CheckBox CB_show_slash;
	CheckBox CB_show_before_clock;
	
	SharedPreferences pref;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hijri__clock);
		
		pref = getSharedPreferences(Keys.PREF, Context.MODE_WORLD_READABLE);
		
		CB_show_date = (CheckBox) findViewById(R.id.show_date);
		CB_show_month = (CheckBox) findViewById(R.id.show_month);
		CB_show_year = (CheckBox) findViewById(R.id.show_year);
		CB_show_month_as_number = (CheckBox) findViewById(R.id.show_month_as_number);
		CB_show_slash = (CheckBox) findViewById(R.id.show_slash);
		CB_show_before_clock = (CheckBox) findViewById(R.id.show_before_clock);
		load();
	}
	
	public void apply(View v){
		Editor editor = pref.edit();
		
		editor.putBoolean(Keys.SHOW_DATE, CB_show_date.isChecked());
		editor.putBoolean(Keys.SHOW_MONTH, CB_show_month.isChecked());
		editor.putBoolean(Keys.SHOW_YEAR, CB_show_year.isChecked());
		editor.putBoolean(Keys.SHOW_MONTH_AS_NUMBER, CB_show_month_as_number.isChecked());
		editor.putBoolean(Keys.SHOW_SLASH, CB_show_slash.isChecked());
		editor.putBoolean(Keys.SHOW_BEFORE_CLOCK, CB_show_before_clock.isChecked());
		
		editor.apply();
		
		Toast.makeText(this, "Changes Applied. Restarting SystemUI...", Toast.LENGTH_LONG).show();
		
		killPackage("com.android.systemui");
	}
	
	public void load(){
		CB_show_date.setChecked(pref.getBoolean(Keys.SHOW_DATE, true));
		CB_show_month.setChecked(pref.getBoolean(Keys.SHOW_MONTH, true));
		CB_show_year.setChecked(pref.getBoolean(Keys.SHOW_YEAR, true));
		CB_show_month_as_number.setChecked(pref.getBoolean(Keys.SHOW_MONTH_AS_NUMBER, false));
		CB_show_slash.setChecked(pref.getBoolean(Keys.SHOW_SLASH, false));
		CB_show_before_clock.setChecked(pref.getBoolean(Keys.SHOW_BEFORE_CLOCK, false));
	}
	
	public void xda(View v){
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://forum.xda-developers.com/xposed/modules/mod-hijri-date-statusbar-t2790604/post53579328"));
		startActivity(browserIntent);
	}
	
	public void more(View v){
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://repo.xposed.info/users/hamzahrmalik"));
		startActivity(browserIntent);
	}
	
	public static void killPackage(String packageToKill) { 
        
        Process su = null; 
         
        // get superuser 
        try { 
             
            su = Runtime.getRuntime().exec("su"); 
             
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
        // kill given package 
        if (su != null ){ 
             
            try { 
                 
                DataOutputStream os = new DataOutputStream(su.getOutputStream());  
                os.writeBytes("pkill " + packageToKill + "\n"); 
                os.flush(); 
                os.writeBytes("exit\n"); 
                os.flush(); 
                su.waitFor(); 
                 
            } catch (IOException e) { 
                 
                e.printStackTrace(); 
                 
            } catch (InterruptedException e) { 
                 
                e.printStackTrace(); 
                 
            } 
        } 
    }  
}
