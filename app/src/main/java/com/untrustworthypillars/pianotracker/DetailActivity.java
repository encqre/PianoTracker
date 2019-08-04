package com.untrustworthypillars.pianotracker;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;
import java.util.UUID;

public class DetailActivity extends AppCompatActivity implements SongDetailFragment.Callbacks {

    private static final String EXTRA_SONG_ID = "pianotracker.song_id";
    private static final String EXTRA_FILTER_STATE_ID = "pianotracker.filter_state_id";
    private static final String EXTRA_FILTER_DIFFICULTY_ID = "pianotracker.filter_difficulty_id";
    private static final String EXTRA_SORT_ID = "pianotracker.sort_id";

    private ViewPager mViewPager;
    private List<Song> mSongs;
    private Toolbar mToolbar;

    public static Intent newIntent(Context packageContext, UUID songId, Integer stateFilter, Integer difficultyFilter, Integer sort) {
        Intent intent = new Intent(packageContext, DetailActivity.class);
        intent.putExtra(EXTRA_SONG_ID, songId);
        intent.putExtra(EXTRA_FILTER_STATE_ID, stateFilter);
        intent.putExtra(EXTRA_FILTER_DIFFICULTY_ID, difficultyFilter);
        intent.putExtra(EXTRA_SORT_ID, sort);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        mViewPager = (ViewPager) findViewById(R.id.song_view_pager);

        UUID songId = (UUID) getIntent().getSerializableExtra(EXTRA_SONG_ID);

        Integer stateFilter = (Integer) getIntent().getSerializableExtra(EXTRA_FILTER_STATE_ID);
        Integer difficultyFilter = (Integer) getIntent().getSerializableExtra(EXTRA_FILTER_DIFFICULTY_ID);
        Integer sort = (Integer) getIntent().getSerializableExtra(EXTRA_SORT_ID);

        setSongList(stateFilter, difficultyFilter, sort);

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {

            @Override
            public Fragment getItem(int position) {
                Song song = mSongs.get(position);
                return SongDetailFragment.newInstance(song.getSongId());
            }

            @Override
            public int getCount() {
                return mSongs.size();
            }
        });

        for (int i = 0; i < mSongs.size(); i++) {
            if (mSongs.get(i).getSongId().equals(songId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    @Override
    public void onSongUpdated(Song song) {

    }

    public void setSongList(Integer stateFilter, Integer difficultyFilter, Integer sort) {
        if (stateFilter != null || difficultyFilter != null) {
            mSongs = SongManager.get(this).getSongsFiltered(stateFilter, difficultyFilter);
        } else {
            mSongs = SongManager.get(this).getSongs();
        }

        if (sort != null) {
            if (sort == R.id.list_toolbar_sort_score) {
                mSongs = Song.sortByScore(mSongs);
            } else if (sort == R.id.list_toolbar_sort_totaltime) {
                mSongs = Song.sortByTotalTime(mSongs);
            } else if (sort == R.id.list_toolbar_sort_totalcount) {
                mSongs = Song.sortByTotalCount(mSongs);
            } else {
                mSongs = Song.sortByLastPlayed(mSongs);
            }
        } else {
            mSongs = Song.sortByOrderId(mSongs);
        }

    }
}
