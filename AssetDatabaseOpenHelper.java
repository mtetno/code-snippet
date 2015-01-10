package com.epblogs.easypadhai.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AssetDatabaseOpenHelper extends SQLiteOpenHelper {

    // DATABASE
    private static SQLiteDatabase mSqliteDb;
    private static AssetDatabaseOpenHelper mInstance;
    public static final int DATABASE_VERSION = 1;
    private static final String DB_PATH_SUFFIX = "/databases/";
    private static final String DATABASE_NAME = "mtetno.db";
    private static Context mContext;

    public AssetDatabaseOpenHelper(Context context, CursorFactory factory,
            int version) {
        super(context, DATABASE_NAME, factory, version);
        mContext = context;
    }

    public void initialise() {
        if (mInstance == null) {
            if (!checkDatabase()) {
                copyDataBase();
            }
            mInstance = new AssetDatabaseOpenHelper(mContext, null,
                    DATABASE_VERSION);
            mSqliteDb = mInstance.getWritableDatabase();
        }
    }

    public AssetDatabaseOpenHelper getInstance() {
        return mInstance;
    }

    public SQLiteDatabase getDatabase() {
        return mSqliteDb;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private static void copyDataBase() {

        try {
            // Open your local db as the input stream
            InputStream myInput = mContext.getAssets().open(DATABASE_NAME);

            // Path to the just created empty db
            String outFileName = getDatabasePath();

            // if the path doesn't exist first, create it
            File f = new File(mContext.getApplicationInfo().dataDir
                    + DB_PATH_SUFFIX);
            if (!f.exists())
                f.mkdir();

            // Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);

            // transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            // Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean checkDatabase() {
        SQLiteDatabase checkDB = null;

        try {
            try {
                String myPath = getDatabasePath();
                checkDB = SQLiteDatabase.openDatabase(myPath, null,
                        SQLiteDatabase.OPEN_READONLY);
                checkDB.close();
            } catch (Exception e) {
            }
        } catch (Throwable ex) {
        }
        return checkDB != null ? true : false;
    }

    private static String getDatabasePath() {
        return mContext.getApplicationInfo().dataDir + DB_PATH_SUFFIX
                + DATABASE_NAME;
    }

}