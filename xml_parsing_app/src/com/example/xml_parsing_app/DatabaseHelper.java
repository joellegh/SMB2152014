package com.example.xml_parsing_app;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static String DB_PATH = "/data/data/com.example.xml_parsing_app/databases/";
	private static String DB_NAME = "PBdatabase.db";
	private SQLiteDatabase myDataBase;
	private final Context myContext;

	public static final String KEY_ID = "_id";
	public static final String KEY_UNAME = "uname";
	public static final String KEY_PASS = "pass";
	private static final String USERS_TABLE = "users";

	private static final int DATABASE_VERSION = 1;

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
		this.myContext = context;
	}

	/*
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 */
	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();
		if (dbExist) {
			// do nothing - database already exist.
		} else {
			/*
			 * By calling this method and empty database will be created into
			 * the default system path of your application so we will be able to
			 * overwrite that database with our database.
			 */
			this.getReadableDatabase();
			copyDataBase();
		}
	}

	/*
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transferring bytestream.
	 * 
	 * @throws IOException
	 */
	private void copyDataBase() throws IOException {
		// TODO Auto-generated method stub
		// Open your local db as the input stream.
		InputStream myInput;
		myInput = myContext.getAssets().open(DB_NAME);
		// Path to the just created empty db.
		String outFileName = DB_PATH + DB_NAME;
		// Open the empty db as the output stream.
		OutputStream myOutput;
		myOutput = new FileOutputStream(outFileName);
		// transfer bytes from the inputfile to the outputfile.
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		// Close the streams.
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	/*
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {
		// TODO Auto-generated method stub
		SQLiteDatabase checkDB = null;
		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {
			// database does't exist yet.
		}
		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	public void openDataBase() throws SQLException {
		// Open the database.
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READONLY);
	}

	@Override
	public synchronized void close() {
		if (myDataBase != null) {
			myDataBase.close();
		}
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	// Method to return the user id according to the username and password.
	public int user_id(String uname, String password) {
		int user_id = 0;
		String selectQuery = null;
		selectQuery = "SELECT " + KEY_ID + " FROM " + USERS_TABLE + " WHERE "
				+ KEY_UNAME + " LIKE '" + uname + "' AND " + KEY_PASS
				+ " LIKE '" + password + "'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				user_id = cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return user_id;
	}

	public void insertValues(String uname, String pass) {
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "INSERT or replace INTO users (uname, pass) VALUES('"
				+ uname + "','" + pass + "')";
		db.execSQL(sql);
		db.close();
	}
}