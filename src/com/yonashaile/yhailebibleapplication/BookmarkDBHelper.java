package com.yonashaile.yhailebibleapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * @author yhaile
 * 
 */
public class BookmarkDBHelper extends SQLiteOpenHelper{

	
	 
	// Create a helper object to create, open, and/or manage a database.
	/**
	 * @param context
	 */
	public BookmarkDBHelper(Context context) {
		super (context, "VerseBookmark.db", null, 1);  // devices require extension of ".db"
	}

	
	// Called when the database is created for the first time
	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db)	{
		//Build SQL string to create a database
		String sSQL = "Create Table Bookmarks (BookmarkId integer" +
		" primary key autoincrement, Verse text);";
		
		//Execute a single SQL statement that is NOT a SELECT or any other SQL statement that returns data
		db.execSQL(sSQL);
	}

	// Called when the database needs to be upgraded
	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase argO, int argl, int arg2) {}
}