package com.woodywoodpecker.startmotion;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class SQLiteManager extends SQLiteAssetHelper {

	private static final String DATABASE_NAME = "startMotionDb.sqlite3";
	private static final int DATABASE_VERSION = 1;

	public SQLiteManager(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
}