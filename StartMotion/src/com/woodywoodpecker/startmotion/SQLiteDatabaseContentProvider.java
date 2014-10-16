package com.woodywoodpecker.startmotion;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteDatabaseContentProvider {
	private SQLiteManager mManager;
	private SQLiteDatabase mDb;

	public SQLiteDatabaseContentProvider(Context context) {
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

	public boolean existUsername(String username) {
		Cursor userCursor = null;

		if (username != null) {
			String sql = "SELECT * FROM Users WHERE Username='" + username
					+ "';";

			userCursor = mDb.rawQuery(sql, null);
			boolean existUser = userCursor.moveToFirst();

			if (existUser) {
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

	public boolean existEmail(String email) {
		Cursor userCursor = null;

		if (email != null) {
			String sql = "SELECT * FROM Users WHERE Username='" + email + "';";

			userCursor = mDb.rawQuery(sql, null);
			boolean existUser = userCursor.moveToFirst();

			if (existUser) {
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
}