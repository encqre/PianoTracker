package com.untrustworthypillars.pianotracker;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.SystemClock;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.UUID;

import com.untrustworthypillars.pianotracker.formatting.DateFormatting;
import com.untrustworthypillars.pianotracker.formatting.ColorFormatting;

public class SongDetailFragment extends Fragment {

    //TODO on page swap need to stop timer and save the new seconds value. Probably need to get to activity's onPageChange method

    private static final String ARG_SONG_ID = "song_id";

    private static final int REQUEST_ADD_SONG = 0;
    private static final int REQUEST_EDIT_SONG = 1;

    public static final String[] SONG_DIFFICULTIES = {"Very Easy", "Easy", "Medium", "Hard", "Very Hard"};
    public static final String[] SONG_STATES = {"Not Learned", "Learning", "Need To Re-learn", "Learned"};

    private Song mSong;
    private UUID mSongId;
    private Callbacks mCallbacks;
    private TextView mTitle;
    private Button mVideoLink;
    private TextView mDifficulty;
    private TextView mState;
    private TextView mLastPlayed;
    private Button mDecreaseScore;
    private Button mIncreaseScore;
    private EditText mScore;
    private Chronometer mChronometer;
    private TextView mCountPlayed;
    private FloatingActionButton mAddCountFAB;
    private FloatingActionButton mRecordFAB;

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
        mSongId = (UUID) getArguments().getSerializable(ARG_SONG_ID);
        mSong = SongManager.get(getActivity()).getSong(mSongId);
    }

    @Override
    public void onPause() {
        super.onPause();
//        Log.d("hey dude", "onPause happened for " + mSong.getTitle());
    }


    @Override
    public void onDetach() {
        super.onDetach();
//        Log.d("hey dude", "onDetach happened for " + mSong.getTitle());
        mCallbacks = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_song_detail, container, false);



        mTitle = (TextView) v.findViewById(R.id.detail_title);

        mVideoLink = (Button) v.findViewById(R.id.detail_video_button);
        mVideoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                watchYoutubeVideo(getContext(), mSong.getVideoId());
            }
        });

        mDifficulty = (TextView) v.findViewById(R.id.detail_difficulty);
        mState = (TextView) v.findViewById(R.id.detail_state);
        mLastPlayed = (TextView) v.findViewById(R.id.detail_last_played);

        mDecreaseScore = (Button) v.findViewById(R.id.detail_score_button_sub);
        mDecreaseScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newValue = Integer.valueOf(mScore.getText().toString()) - 1;
                mScore.setText(String.valueOf(newValue));
                mSong.setScore(newValue);
                SongManager.get(getActivity()).updateSong(mSong);
            }
        });

        mIncreaseScore = (Button) v.findViewById(R.id.detail_score_button_add);
        mIncreaseScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newValue = Integer.valueOf(mScore.getText().toString()) + 1;
                mScore.setText(String.valueOf(newValue));
                mSong.setScore(newValue);
                SongManager.get(getActivity()).updateSong(mSong);
            }
        });

        mScore = (EditText) v.findViewById(R.id.detail_score);
        mChronometer = (Chronometer) v.findViewById(R.id.detail_time);
        mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            public void onChronometerTick(Chronometer cArg) {
                long totalSeconds = (SystemClock.elapsedRealtime() - cArg.getBase())/1000;
                int h = (int)(totalSeconds /3600);
                int m = (int)(totalSeconds - h*3600)/60;
                int s = (int)(totalSeconds - h*3600- m*60);
                if (totalSeconds <= 59) {
                    cArg.setText(s + " s");
                } else if (totalSeconds <= 3599) {
                    cArg.setText(m + " min " + s + " s");
                } else {
                    cArg.setText(h + " h " + m + " min " + s + " s");
                }
            }
        });

        mCountPlayed = (TextView) v.findViewById(R.id.detail_count);

        mAddCountFAB = (FloatingActionButton) v.findViewById(R.id.detail_fab_add_count);
        mAddCountFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newCount = Integer.valueOf(mCountPlayed.getText().toString()) + 1;
                mCountPlayed.setText(String.valueOf(newCount));
                mLastPlayed.setText("Last played: " + DateFormatting.dateDeltaToNow(new Date()));
                mSong.setCountPlayed(newCount);
                mSong.setLastPlayed(new Date());
                SongManager.get(getActivity()).updateSong(mSong);
            }
        });

        mRecordFAB = (FloatingActionButton) v.findViewById(R.id.detail_fab_record);
        mRecordFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecordFAB.getCompatElevation() == 18f) {
                    //set the Chronometer to start at current played time and start it
                    long milisPlayed = mSong.getSecondsPlayed() * 1000;
                    mChronometer.setBase(SystemClock.elapsedRealtime() - milisPlayed);
                    mChronometer.start();
                    mRecordFAB.setImageDrawable(getResources().getDrawable(R.drawable.stop_red_square, getContext().getTheme()));
                    mRecordFAB.setCompatElevation(19f);
                } else if (mRecordFAB.getCompatElevation() == 19f) {
                    //stop the chronometer, save the new total seconds played value
                    mChronometer.stop();
                    long newSeconds = (SystemClock.elapsedRealtime() - mChronometer.getBase()) / 1000;
                    mSong.setSecondsPlayed(newSeconds);
                    SongManager.get(getActivity()).updateSong(mSong);
                    mRecordFAB.setImageDrawable(getResources().getDrawable(R.drawable.play_sign, getContext().getTheme()));
                    mRecordFAB.setCompatElevation(18f);
                }
            }
        });

        updateUI();

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail_toolbar, menu);
        MenuItem editItem = menu.findItem(R.id.pager_toolbar_edit);
        MenuItem addItem = menu.findItem(R.id.pager_toolbar_add);
        MenuItem deleteItem = menu.findItem(R.id.pager_toolbar_delete);

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
                FragmentManager fmg = getActivity().getSupportFragmentManager();
                EditSongDialog editDialog = EditSongDialog.newInstance(this.mSong.getSongId());
                editDialog.setTargetFragment(this, REQUEST_EDIT_SONG);
                editDialog.show(fmg, "EditSongDialog");
                return true;
            case R.id.pager_toolbar_delete:
                AlertDialog deleteDialog = new AlertDialog.Builder(getActivity()).setTitle("Delete this?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                  SongManager.get(getActivity()).deleteSong(mSong);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //
                            }
                        })
                        .create();
                deleteDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ADD_SONG) {
            //nothing probably
        } else if (requestCode == REQUEST_EDIT_SONG) {
            updateUI();
        }
    }


    private void updateUI() {
        UUID id = mSong.getSongId();
        mSong = SongManager.get(getActivity()).getSong(id);
        mTitle.setText(mSong.getTitle());


        //Spannable allows to color only certain part of Text/Textview

        String difText1 = "Difficulty: ";
        String difText2 = SONG_DIFFICULTIES[mSong.getDifficulty()];
        Spannable difSpannable = new SpannableString(difText1 + difText2);
        difSpannable.setSpan(new ForegroundColorSpan(ColorFormatting.getDifficultyColor(mSong.getDifficulty(), getContext())), difText1.length(), (difText1 + difText2).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mDifficulty.setText(difSpannable, TextView.BufferType.SPANNABLE);

        String stateText1 = "Learning state: ";
        String stateText2 = SONG_STATES[mSong.getState()];
        Spannable stateSpannable = new SpannableString(stateText1 + stateText2);
        stateSpannable.setSpan(new ForegroundColorSpan(ColorFormatting.getStateColor(mSong.getState(), getContext())), stateText1.length(), (stateText1 + stateText2).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mState.setText(stateSpannable, TextView.BufferType.SPANNABLE);

        String lpText1 = "Last played: ";
        String lpText2 = DateFormatting.dateDeltaToNow(mSong.getLastPlayed());
        Spannable lpSpannable = new SpannableString(lpText1 + lpText2);
        int lpColor = getResources().getColor(R.color.white, getContext().getTheme());
        lpSpannable.setSpan(new ForegroundColorSpan(lpColor), lpText1.length(), (lpText1 + lpText2).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mLastPlayed.setText(lpSpannable, TextView.BufferType.SPANNABLE);

        mScore.setText(String.valueOf(mSong.getScore()));
        if (mSong.getSecondsPlayed() <= 59) {
            mChronometer.setText(String.valueOf(mSong.getSecondsPlayed()) + " s");
        } else if (mSong.getSecondsPlayed() <= 3599) {
            long minutes = (mSong.getSecondsPlayed() / 60);
            long seconds = mSong.getSecondsPlayed() % 60;
            mChronometer.setText(String.valueOf(minutes) + " min " + String.valueOf(seconds) + " s");
        } else {
            long hours = (mSong.getSecondsPlayed()/3600);
            long minutes = (mSong.getSecondsPlayed() % 3600) / 60;
            long seconds = (mSong.getSecondsPlayed() % 3600) % 60;
            mChronometer.setText(String.valueOf(hours) + " h " + String.valueOf(minutes) + " min " + String.valueOf(seconds) + " s");
        }
        mCountPlayed.setText(String.valueOf(mSong.getCountPlayed()));

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
