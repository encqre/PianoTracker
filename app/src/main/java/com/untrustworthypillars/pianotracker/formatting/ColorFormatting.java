package com.untrustworthypillars.pianotracker.formatting;

import android.content.Context;

import com.untrustworthypillars.pianotracker.R;

public class ColorFormatting {

    public static int getDifficultyColor(int difficulty, Context context) {
        int color = context.getResources().getColor(R.color.very_light_gray, context.getTheme());
        switch (difficulty) {
            case 0:
                color = context.getResources().getColor(R.color.difficulty_very_easy, context.getTheme());
                break;
            case 1:
                color = context.getResources().getColor(R.color.difficulty_easy, context.getTheme());
                break;
            case 2:
                color = context.getResources().getColor(R.color.difficulty_medium, context.getTheme());
                break;
            case 3:
                color = context.getResources().getColor(R.color.difficulty_hard, context.getTheme());
                break;
            case 4:
                color = context.getResources().getColor(R.color.difficulty_very_hard, context.getTheme());
                break;
        }
        return color;
    }

    public static int getStateColor(int state, Context context) {
        int color = context.getResources().getColor(R.color.very_light_gray, context.getTheme());
        switch (state) {
            case 0:
                color = context.getResources().getColor(R.color.state_not_learned, context.getTheme());
                break;
            case 1:
                color = context.getResources().getColor(R.color.state_learning, context.getTheme());
                break;
            case 2:
                color = context.getResources().getColor(R.color.state_to_relearn, context.getTheme());
                break;
            case 3:
                color = context.getResources().getColor(R.color.state_learned, context.getTheme());
                break;
        }
        return color;
    }
}
