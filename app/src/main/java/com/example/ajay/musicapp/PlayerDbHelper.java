package com.example.ajay.musicapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import static android.os.Build.ID;

public class PlayerDbHelper extends SQLiteOpenHelper {

    public static final String dbName="player_db";
    public static final String TABLE="Playlist";

    public static final String PATH_COLUMN="path";


    public static final int version=1;

    public PlayerDbHelper(Context context) {
        super(context,dbName,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NAME_TABLE = "CREATE TABLE " + TABLE + "("+ ID + " INTEGER PRIMARY KEY," + PATH_COLUMN + " TEXT)";
        db.execSQL(CREATE_NAME_TABLE);
        Log.i("TAG","table created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      //  db.execSQL("drop table if exists Playlist");

    }
    public long putInDb (String path)
    {
        ContentValues cv=new ContentValues();
        cv.put(PATH_COLUMN,path);


        SQLiteDatabase db = getWritableDatabase();
        long i=getWritableDatabase().insert(TABLE,null,cv);
        Log.i("TAG","songs added to dataBase");
        db.close();
        return i;
    }
    public ArrayList<String>  getPlaylist()
    {
        SQLiteDatabase k=getReadableDatabase();

        Cursor g=k.rawQuery("SELECT  * FROM " + TABLE,null);
        ArrayList<String>  playlistSong=new ArrayList<>();

        while(g.moveToNext())
        {
            String y=g.getString(1);

              playlistSong.add(y);
        }

        k.close();
        return playlistSong;
    }
    public int removeSong(String path)
    {
        SQLiteDatabase k=getReadableDatabase();
      int i=  k.delete(TABLE,PATH_COLUMN + "=" + path, null);
        k.close();
        return i;

    }
    public int removeAllSongs()
    {
        SQLiteDatabase k=getReadableDatabase();
        int i=  k.delete(TABLE,null, null);
        k.close();
        return i;

    }

}
