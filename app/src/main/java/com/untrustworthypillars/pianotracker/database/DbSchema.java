package com.untrustworthypillars.pianotracker.database;

public class DbSchema {
    public static final class SongTable {
        public static final String NAME = "songs";

        public static final class Cols {
            public static final String SONGID = "songid";
            public static final String ORDERID = "orderid";
            public static final String TITLE = "title";
            public static final String VIDEOID = "videoid";
            public static final String LASTPLAYED = "lastplayed";
            public static final String SCORE = "score";
            public static final String DIFFICULTY = "difficulty";
            public static final String SECONDSPLAYED = "secondsplayed";
            public static final String COUNTPLAYED = "countplayed";
            public static final String STATE = "state";
        }
    }

}
