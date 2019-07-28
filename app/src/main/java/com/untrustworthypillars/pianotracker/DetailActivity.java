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

    private ViewPager mViewPager;
    private List<Song> mSongs;
    private Toolbar mToolbar;

    public static Intent newIntent(Context packageContext, UUID songId) {
        Intent intent = new Intent(packageContext, DetailActivity.class);
        intent.putExtra(EXTRA_SONG_ID, songId);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        mViewPager = (ViewPager) findViewById(R.id.song_view_pager);

        UUID songId = (UUID) getIntent().getSerializableExtra(EXTRA_SONG_ID);

        mSongs = SongManager.get(this).getSongs();
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
}
