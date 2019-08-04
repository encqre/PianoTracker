package com.untrustworthypillars.pianotracker;

import android.content.Intent;
import androidx.fragment.app.Fragment;

public class ListActivity extends SingleFragmentActivity implements SongListFragment.Callbacks, SongDetailFragment.Callbacks {

    @Override
    protected Fragment createFragment() {
        return new SongListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_list;
    }

    @Override
    public void onSongSelected(Song song, Integer stateFilter, Integer difficultyFilter, Integer sort) {
        Intent intent = DetailActivity.newIntent(this, song.getSongId(), stateFilter, difficultyFilter, sort);
        startActivity(intent);
    }

    @Override
    public void onSongUpdated(Song song) {
        SongListFragment listFragment = (SongListFragment) getSupportFragmentManager().
                findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }
}
