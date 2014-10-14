package com.woodywoodpecker.startmotion;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteDatabaseContentProvider {
	private SQLiteManager mManager;
	private Context context;
	private SQLiteDatabase mDb;

	public SQLiteDatabaseContentProvider(Context context) {
		this.context = context;
		this.mManager = new SQLiteManager(context);
		mDb = mManager.getReadableDatabase();
	}

	public boolean loginUser(String username, String password) {
		Cursor userCursor = null;

		if (username != null && password != null) {
			String sql = "SELECT * FROM Users WHERE Username='" + username
					+ "' " + "AND Password ='" + password + "';";

			userCursor = mDb.rawQuery(sql, null);
			boolean isLoggedIn = userCursor.moveToFirst();

			if (isLoggedIn) {
				// my user exist in the database record
				userCursor.close();

				return true;
			}
		}

		if (userCursor != null) {
			userCursor.close();
		}

		return false;
	}

	public boolean registerUser(String username, String password, String email) {
		if (username != null && password != null && email != null) {
			String sql = "INSERT INTO Users(Username, Password, Email) VALUES('"
					+ username + "','" + password + "','" + email + "');";
			mDb.execSQL(sql);

			return true;
		}

		return false;
	}

	public void updateUserData(String username, String password) {
		String sql = "UPDATE Users SET Password='" + password + "' "
				+ "WHERE Username='" + username + "';";
		mDb.execSQL(sql);
	}
}