package com.hamzah.hijriclock; 

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
 
public class Main implements IXposedHookLoadPackage{
	
	XSharedPreferences pref;
	private static String OldDate;
	
	
	@Override
	public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
		if(!lpparam.packageName.equals("com.android.systemui"))
			return;
		XC_MethodHook hook = new XC_MethodHook(){
			@Override
			protected void afterHookedMethod(MethodHookParam param)
					throws Throwable {
				pref = new XSharedPreferences(this.getClass().getPackage().getName(), Keys.PREF);
				
				TextView t = (TextView) param.thisObject;
				t.setSingleLine(false); //To prevent the SystemUI force stop
				String orig =t.getText().toString();//was causing ClassCastExeption
				String date = HijriCalendar.getDate(null, pref.getBoolean(Keys.SHOW_DATE, true),
						pref.getBoolean(Keys.SHOW_MONTH, true), pref.getBoolean(Keys.SHOW_YEAR, true),
						pref.getBoolean(Keys.SHOW_MONTH_AS_NUMBER, false), pref.getBoolean(Keys.SHOW_SLASH, false),
						pref.getBoolean(Keys.USE_ARABIC_TEXT, false), pref.getBoolean(Keys.USE_ARABIC_NUMBERS, false),
						pref.getInt(Keys.OFFSET_DAY, 0), pref.getInt(Keys.OFFSET_MONTH, 0));
				if(!date.equals(OldDate)){//Check if the date changed or not
				if(pref.getBoolean(Keys.SHOW_BEFORE_CLOCK, false)){
					t.setText(date+"\n"+orig);					
				}else{
					t.setText(orig+"\n"+date);}
				OldDate=date;}
			}
			};
		
		if(Build.VERSION.SDK_INT>=14)
			findAndHookMethod("com.android.systemui.statusbar.policy.DateView", lpparam.classLoader, "updateClock", hook);
	       
	    else if(Build.VERSION.SDK_INT<=13)
	    	findAndHookMethod("com.android.systemui.statusbar.DateView", lpparam.classLoader, "updateClock", hook);
	}
 
}
