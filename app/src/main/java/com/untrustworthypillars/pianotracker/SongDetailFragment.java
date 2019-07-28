package com.untrustworthypillars.pianotracker;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.UUID;

import com.untrustworthypillars.pianotracker.formatting.DateFormatting;

public class SongDetailFragment extends Fragment {

    private static final String ARG_SONG_ID = "song_id";

    private static final int REQUEST_ADD_SONG = 0;
    private static final int REQUEST_EDIT_SONG = 1;

    public static final String[] SONG_DIFFICULTIES = {"Very Easy", "Easy", "Medium", "Hard", "Very Hard"};
    public static final String[] SONG_STATES = {"Not Learned", "Learning", "Need To Re-learn", "Learned"};

    private Song mSong;
    private Callbacks mCallbacks;
    private TextView mTitle;
    private Button mVideoLink;
    private TextView mDifficulty;
    private TextView mState;
    private TextView mLastPlayed;
    private Button mDecreaseScore;
    private Button mIncreaseScore;
    private EditText mScore;
    private TextView mTimePlayed;
    private TextView mCountPlayed;
    private FloatingActionButton mAddCountFAB;
    private FloatingActionButton mRecordFAB;
    private Drawable test;

    //Required interface for hosting activities
    public interface Callbacks {
        void onSongUpdated(Song song);
    }

    public static SongDetailFragment newInstance(UUID songId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_SONG_ID, songId);

        SongDetailFragment fragment = new SongDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        UUID songId = (UUID) getArguments().getSerializable(ARG_SONG_ID);
        mSong = SongManager.get(getActivity()).getSong(songId);
    }

    @Override
    public void onPause() {
        super.onPause();
        SongManager.get(getActivity()).updateSong(mSong);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_song_detail, container, false);



        mTitle = (TextView) v.findViewById(R.id.detail_title);
        mTitle.setText(mSong.getTitle());

        mVideoLink = (Button) v.findViewById(R.id.detail_video_button);
        mVideoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                watchYoutubeVideo(getContext(), mSong.getVideoId());
            }
        });

        mDifficulty = (TextView) v.findViewById(R.id.detail_difficulty);
        mDifficulty.setText("Difficulty: " + SONG_DIFFICULTIES[mSong.getDifficulty()]);

        mState = (TextView) v.findViewById(R.id.detail_state);
        mState.setText("Learning state: " + SONG_STATES[mSong.getState()]);

        mLastPlayed = (TextView) v.findViewById(R.id.detail_last_played);
        mLastPlayed.setText("Last played: " + DateFormatting.dateDeltaToNow(mSong.getLastPlayed()));

        mDecreaseScore = (Button) v.findViewById(R.id.detail_score_button_sub);
        mDecreaseScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });

        mIncreaseScore = (Button) v.findViewById(R.id.detail_score_button_add);
        mIncreaseScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });

        mScore = (EditText) v.findViewById(R.id.detail_score);
        mScore.setText(String.valueOf(mSong.getScore()));

        mTimePlayed = (TextView) v.findViewById(R.id.detail_time);
        if (mSong.getSecondsPlayed() <= 59) {
            mTimePlayed.setText(String.valueOf(mSong.getSecondsPlayed()) + "s");
        } else if (mSong.getSecondsPlayed() <= 3599) {
            long minutes = (mSong.getSecondsPlayed() / 60);
            long seconds = mSong.getSecondsPlayed() % 60;
            mTimePlayed.setText(String.valueOf(minutes) + "min " + String.valueOf(seconds) + "s");
        } else {
            long hours = (mSong.getSecondsPlayed()/3600);
            long minutes = (mSong.getSecondsPlayed() % 3600) / 60;
            long seconds = (mSong.getSecondsPlayed() % 3600) % 60;
            mTimePlayed.setText(String.valueOf(hours) + "h " + String.valueOf(minutes) + "min " + String.valueOf(seconds) + "s");
        }

        mCountPlayed = (TextView) v.findViewById(R.id.detail_count);
        mCountPlayed.setText(String.valueOf(mSong.getCountPlayed()));

        mAddCountFAB = (FloatingActionButton) v.findViewById(R.id.detail_fab_add_count);
        mAddCountFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });

        mRecordFAB = (FloatingActionButton) v.findViewById(R.id.detail_fab_record);
        test = mRecordFAB.getDrawable();
        mRecordFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("GOOD SHIT", String.valueOf(mRecordFAB.getCompatElevation()));
                if (mRecordFAB.getCompatElevation() == 18f) {
                    mRecordFAB.setImageDrawable(getResources().getDrawable(R.drawable.stop_red_square, getContext().getTheme()));
                    mRecordFAB.setCompatElevation(19f);
                } else if (mRecordFAB.getCompatElevation() == 19f) {
                    mRecordFAB.setImageDrawable(getResources().getDrawable(R.drawable.play_sign, getContext().getTheme()));
                    mRecordFAB.setCompatElevation(18f);
                }

            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail_toolbar, menu);
        MenuItem editItem = menu.findItem(R.id.pager_toolbar_edit);
        MenuItem addItem = menu.findItem(R.id.pager_toolbar_add);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pager_toolbar_add:
                //Launching AddSongDialog
                FragmentManager fm = getActivity().getSupportFragmentManager();
                AddSongDialog dialog = AddSongDialog.newInstance();
                dialog.setTargetFragment(this, REQUEST_ADD_SONG);
                dialog.show(fm, "AddSongDialog");
                return true;
            case R.id.pager_toolbar_edit:
//                FragmentManager fm = getActivity().getSupportFragmentManager();
                //TODO
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ADD_SONG) {
            //nothing probably
        } else if (requestCode == REQUEST_EDIT_SONG) {
            //TODO - need to refresh the song data
        }
    }


    private void updateSong() {
        SongManager.get(getActivity()).updateSong(mSong);
        mCallbacks.onSongUpdated(mSong);
    }

    public static void watchYoutubeVideo(Context context, String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            //No youtube app?
        }
    }

}
