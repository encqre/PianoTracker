package com.untrustworthypillars.pianotracker;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Song {

    private UUID mSongId;
    private int mOrderId;
    private String mTitle;
    private String mVideoId;
    private Date mLastPlayed;
    private int mScore; //0-100 - combined rating of how much % of piece i know and how well i can play it(how many mistakes on average)
    private int mDifficulty; // 0(bright green)-very ez, 1(darker green)-ez, 2(yellow)-medium, 3(orange)-hard, 4(red)-very hard
    private long mSecondsPlayed;
    private int mCountPlayed;
    private int mState; //0(black/none)-Not Learned, 1(yellow)-Learning, 2(cyan)-To re-learn, 3(green)-Learned


    public Song() {
        this(UUID.randomUUID());
    }

    public Song(UUID id) {
        mSongId = id;
    }

    public UUID getSongId() {
        return mSongId;
    }

    public int getOrderId() {
        return mOrderId;
    }

    public void setOrderId(int orderId) {
        mOrderId = orderId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getVideoId() {
        return mVideoId;
    }

    public void setVideoId(String videoId) {
        mVideoId = videoId;
    }

    public Date getLastPlayed() {
        return mLastPlayed;
    }

    public void setLastPlayed(Date lastPlayed) {
        mLastPlayed = lastPlayed;
    }

    public int getScore() {
        return mScore;
    }

    public void setScore(int score) {
        mScore = score;
    }

    public int getDifficulty() {
        return mDifficulty;
    }

    public void setDifficulty(int difficulty) {
        mDifficulty = difficulty;
    }

    public long getSecondsPlayed() {
        return mSecondsPlayed;
    }

    public void setSecondsPlayed(long secondsPlayed) {
        mSecondsPlayed = secondsPlayed;
    }

    public int getCountPlayed() {
        return mCountPlayed;
    }

    public void setCountPlayed(int countPlayed) {
        mCountPlayed = countPlayed;
    }

    public int getState() {
        return mState;
    }

    public void setState(int state) {
        mState = state;
    }

    public static List<Song> sortByOrderId(List<Song> songs) {
        Collections.sort(songs, new Comparator<Song>() {
            @Override
            public int compare(Song o1, Song o2) {
                if (o1.getOrderId() > o2.getOrderId()) {
                    return 1;
                } else if (o1.getOrderId() < o2.getOrderId()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        return songs;
    }

    public static List<Song> sortByScore(List<Song> songs) {
        Collections.sort(songs, new Comparator<Song>() {
            @Override
            public int compare(Song o1, Song o2) {
                if (o1.getScore() > o2.getScore()) {
                    return -1;
                } else if (o1.getScore() < o2.getScore()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        return songs;
    }

    public static List<Song> sortByLastPlayed(List<Song> songs) {
        Collections.sort(songs, new Comparator<Song>() {
            @Override
            public int compare(Song o1, Song o2) {
                if (o1.getLastPlayed().getTime() > o2.getLastPlayed().getTime()) {
                    return 1;
                } else if (o1.getLastPlayed().getTime() < o2.getLastPlayed().getTime()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        return songs;
    }

    public static List<Song> sortByTotalTime(List<Song> songs) {
        Collections.sort(songs, new Comparator<Song>() {
            @Override
            public int compare(Song o1, Song o2) {
                if (o1.getSecondsPlayed() > o2.getSecondsPlayed()) {
                    return -1;
                } else if (o1.getSecondsPlayed() < o2.getSecondsPlayed()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        return songs;
    }

    public static List<Song> sortByTotalCount(List<Song> songs) {
        Collections.sort(songs, new Comparator<Song>() {
            @Override
            public int compare(Song o1, Song o2) {
                if (o1.getCountPlayed() > o2.getCountPlayed()) {
                    return -1;
                } else if (o1.getCountPlayed() < o2.getCountPlayed()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        return songs;
    }

}
