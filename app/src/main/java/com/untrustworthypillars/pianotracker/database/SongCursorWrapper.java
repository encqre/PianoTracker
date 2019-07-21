package com.untrustworthypillars.pianotracker.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.untrustworthypillars.pianotracker.Song;
import com.untrustworthypillars.pianotracker.database.DbSchema.SongTable;

import java.util.Date;
import java.util.UUID;

public class SongCursorWrapper extends CursorWrapper {
    public SongCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Song getSong() {
        String songId = getString(getColumnIndex(SongTable.Cols.SONGID));
        int orderId = getInt(getColumnIndex(SongTable.Cols.ORDERID));
        String title = getString(getColumnIndex(SongTable.Cols.TITLE));
        String videoId = getString(getColumnIndex(SongTable.Cols.VIDEOID));
        long lastPlayed = getLong(getColumnIndex(SongTable.Cols.LASTPLAYED));
        int score = getInt(getColumnIndex(SongTable.Cols.SCORE));
        int difficulty = getInt(getColumnIndex(SongTable.Cols.DIFFICULTY));
        long secondsPlayed = getLong(getColumnIndex(SongTable.Cols.SECONDSPLAYED));
        int countPlayed = getInt(getColumnIndex(SongTable.Cols.COUNTPLAYED));
        int state = getInt(getColumnIndex(SongTable.Cols.STATE));

        Song song = new Song(UUID.fromString(songId));
        song.setOrderId(orderId);
        song.setTitle(title);
        song.setVideoId(videoId);
        song.setLastPlayed(new Date(lastPlayed));
        song.setScore(score);
        song.setDifficulty(difficulty);
        song.setSecondsPlayed(secondsPlayed);
        song.setCountPlayed(countPlayed);
        song.setState(state);

        return song;
    }
}
