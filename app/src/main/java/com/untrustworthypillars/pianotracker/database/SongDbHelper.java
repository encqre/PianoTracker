package com.untrustworthypillars.pianotracker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.untrustworthypillars.pianotracker.database.DbSchema.SongTable;

public class SongDbHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "songDB.db";

    public SongDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + SongTable.NAME + "(" + "_id integer primary key autoincrement, " +
                SongTable.Cols.SONGID + ", " +
                SongTable.Cols.ORDERID + ", " +
                SongTable.Cols.TITLE + ", " +
                SongTable.Cols.VIDEOID + ", " +
                SongTable.Cols.LASTPLAYED + ", " +
                SongTable.Cols.SCORE + ", " +
                SongTable.Cols.DIFFICULTY + ", " +
                SongTable.Cols.SECONDSPLAYED + ", " +
                SongTable.Cols.COUNTPLAYED + ", " +
                SongTable.Cols.STATE + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
