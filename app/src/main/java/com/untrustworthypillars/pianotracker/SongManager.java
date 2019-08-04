package com.untrustworthypillars.pianotracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.untrustworthypillars.pianotracker.database.DbSchema.SongTable;
import com.untrustworthypillars.pianotracker.database.SongCursorWrapper;
import com.untrustworthypillars.pianotracker.database.SongDbHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SongManager {
    private static SongManager sSongManager;

    private Context mContext;
    private SQLiteDatabase mSongDatabase;

    public static SongManager get(Context context) {
        if (sSongManager == null) {
            sSongManager = new SongManager(context);
        }
        return sSongManager;
    }

    private SongManager(Context context) {
        mContext = context.getApplicationContext();
        mSongDatabase = new SongDbHelper(mContext).getWritableDatabase();
    }

    public List<Song> getSongs() {
        List<Song> songs = new ArrayList<>();

        SongCursorWrapper cursor = querySongs(null, null);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                songs.add(cursor.getSong());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return songs;
    }

    public Song getSong(UUID songId) {
        SongCursorWrapper cursor = querySongs(SongTable.Cols.SONGID + " = ?", new String[] {songId.toString()});

        try {
            if(cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getSong();
        } finally {
            cursor.close();
        }
    }

    public List<Song> getSongsFiltered(Integer state, Integer difficulty) {
        List<Song> songs = new ArrayList<>();
        SongCursorWrapper cursor;

        //For some reason whereArgs don't really work with integer columns, so doing queries bit different
        if (state != null && difficulty != null) {
            cursor = querySongs(SongTable.Cols.STATE + " = " + state.toString() + " AND "
                    + SongTable.Cols.DIFFICULTY + " = " + difficulty.toString(), null);
        } else if (state != null) {
            cursor = querySongs(SongTable.Cols.STATE + " = " + state.toString(), null);
        } else if (difficulty != null) {
            cursor = querySongs(SongTable.Cols.DIFFICULTY + " = " + difficulty.toString(), null);
        } else {
            cursor = querySongs(SongTable.Cols.STATE + " = ?", new String[]{"99999"}); //maybe fix to do something prettier
        }

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                songs.add(cursor.getSong());
                cursor.moveToNext();
            }
        } finally {
                cursor.close();
        }


        return songs;
    }

    public void addSong(Song s) {
        ContentValues values = getContentValues(s);
        mSongDatabase.insert(SongTable.NAME, null, values);
    }

    public void updateSong(Song song) {
        String songId = song.getSongId().toString();
        ContentValues values = getContentValues(song);
        mSongDatabase.update(SongTable.NAME, values, SongTable.Cols.SONGID + " = ?", new String[] {songId});
    }

    public void deleteSong(Song song) {
        String songId = song.getSongId().toString();

        mSongDatabase.delete(SongTable.NAME, SongTable.Cols.SONGID + " = ?", new String[] {songId});
    }

    private SongCursorWrapper querySongs(String whereClause, String[] whereArgs) {
        Cursor cursor = mSongDatabase.query(
                SongTable.NAME,
                null, //columns - null selects all columns
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new SongCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Song song) {
        ContentValues values = new ContentValues();
        values.put(SongTable.Cols.SONGID, song.getSongId().toString());
        values.put(SongTable.Cols.ORDERID, song.getOrderId());
        values.put(SongTable.Cols.TITLE, song.getTitle());
        values.put(SongTable.Cols.VIDEOID, song.getVideoId());
        values.put(SongTable.Cols.LASTPLAYED, song.getLastPlayed().getTime());
        values.put(SongTable.Cols.SCORE, song.getScore());
        values.put(SongTable.Cols.DIFFICULTY, song.getDifficulty());
        values.put(SongTable.Cols.SECONDSPLAYED, song.getSecondsPlayed());
        values.put(SongTable.Cols.COUNTPLAYED, song.getCountPlayed());
        values.put(SongTable.Cols.STATE, song.getState());

        return values;
    }

}
