package com.hamzah.hijriclock; 

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import android.os.Build;
import android.widget.TextView;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
 
public class Main implements IXposedHookLoadPackage{
	
	XSharedPreferences pref;
	
	@Override
	public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
		
		XC_MethodHook hook = new XC_MethodHook(){
			@Override
			protected void afterHookedMethod(MethodHookParam param)
					throws Throwable {
				pref = new XSharedPreferences(this.getClass().getPackage().getName(), Keys.PREF);
				
				TextView t = (TextView) param.thisObject;
				
				String date = HijriCalendar.getDate(null, pref.getBoolean(Keys.SHOW_DATE, true), pref.getBoolean(Keys.SHOW_MONTH, true), pref.getBoolean(Keys.SHOW_YEAR, true), pref.getBoolean(Keys.SHOW_MONTH_AS_NUMBER, false), pref.getBoolean(Keys.SHOW_SLASH, false));
				
				if(pref.getBoolean(Keys.SHOW_BEFORE_CLOCK, false)){
					String orig = (String) t.getText();
					t.setText(date + " " + orig);
				}
				else
					t.append(date);
			}
		};
		
		if(Build.VERSION.SDK_INT>=14)
			findAndHookMethod("com.android.systemui.statusbar.policy.Clock", lpparam.classLoader, "updateClock", hook);
	       
	    else if(Build.VERSION.SDK_INT<=13)
	    	findAndHookMethod("com.android.systemui.statusbar.Clock", lpparam.classLoader, "updateClock", hook);
	}
 
}