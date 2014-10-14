package com.woodywoodpecker.startmotion;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UserDataPreference {
	public final static String classTag = "USER_INFO";
	private SharedPreferences mPrefs;
	private Editor mEditor;

	public UserDataPreference(Context context) {
		this.mPrefs = context.getSharedPreferences(classTag,
				Context.MODE_PRIVATE);
		mEditor = mPrefs.edit();
	}

	public boolean rememeber(boolean state) {
		if (state) {
			mEditor.putBoolean("remember", true);
			
			return mEditor.commit();
		} else {
			mEditor.putBoolean("remember", false);
			
			return mEditor.commit();
		}
	}

	public boolean isLogged() {
		return mPrefs.getBoolean("remember", false);
	}
}