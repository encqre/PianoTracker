package com.untrustworthypillars.pianotracker;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Date;
import java.util.UUID;

public class EditSongDialog extends DialogFragment {

    private static final String ARG_SONG = "song";

    private UUID mSongId;
    private Song song;
    private EditText mOrderId;
    private EditText mTitle;
    private EditText mVideoId;
    private Spinner mDifficultySpinner;
    private Spinner mStateSpinner;
    private EditText mScore;
    private EditText mSecondsPlayed;
    private EditText mCountPlayed;

    public static EditSongDialog newInstance (UUID songid) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_SONG, songid);

        EditSongDialog fragment = new EditSongDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mSongId = (UUID) getArguments().getSerializable(ARG_SONG);
        song = SongManager.get(getActivity()).getSong(mSongId);
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_song, null);

        mDifficultySpinner = (Spinner) v.findViewById(R.id.dialog_difficulty_spinner);
        mStateSpinner = (Spinner) v.findViewById(R.id.dialog_state_spinner);
        //Creating adapters for spinners, using resources array as list of items and default layout for single spinner item

        ArrayAdapter<CharSequence> difficultyAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.difficulty_array, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> stateAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.state_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDifficultySpinner.setAdapter(difficultyAdapter);
        mDifficultySpinner.setSelection(song.getDifficulty());
        mStateSpinner.setAdapter(stateAdapter);
        mStateSpinner.setSelection(song.getState());

        mOrderId = (EditText) v.findViewById(R.id.dialog_orderid);
        mOrderId.setInputType(InputType.TYPE_CLASS_NUMBER);
        mOrderId.setText(String.valueOf(song.getOrderId()));

        mTitle = (EditText) v.findViewById(R.id.dialog_title);
        mTitle.setText(song.getTitle());

        mVideoId = (EditText) v.findViewById(R.id.dialog_videoid);
        mVideoId.setText(song.getVideoId());

        mScore = (EditText) v.findViewById(R.id.dialog_score);
        mScore.setInputType(InputType.TYPE_CLASS_NUMBER);
        mScore.setText(String.valueOf(song.getScore()));

        mSecondsPlayed = (EditText) v.findViewById(R.id.dialog_seconds);
        mSecondsPlayed.setInputType(InputType.TYPE_CLASS_NUMBER);
        mSecondsPlayed.setText(String.valueOf(song.getSecondsPlayed()));

        mCountPlayed = (EditText) v.findViewById(R.id.dialog_count);
        mCountPlayed.setInputType(InputType.TYPE_CLASS_NUMBER);
        mCountPlayed.setText(String.valueOf(song.getCountPlayed()));

        AlertDialog dialog = new AlertDialog.Builder(getActivity()).setView(v).setTitle("Edit this song")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Song song = SongManager.get(getActivity()).getSong(mSongId);
                        song.setOrderId(Integer.valueOf(mOrderId.getText().toString()));
                        song.setTitle(mTitle.getText().toString());
                        song.setVideoId(mVideoId.getText().toString());
                        song.setScore(Integer.valueOf(mScore.getText().toString()));
                        song.setDifficulty(mDifficultySpinner.getSelectedItemPosition());
                        song.setState(mStateSpinner.getSelectedItemPosition());
                        song.setSecondsPlayed(Long.valueOf(mSecondsPlayed.getText().toString()));
                        song.setCountPlayed(Integer.valueOf(mCountPlayed.getText().toString()));
                        Log.d("why dont", song.getSongId().toString());
                        Log.d("ey bitch", song.getTitle());
                        SongManager.get(getActivity()).updateSong(song);
                        Toast.makeText(getActivity(), "Song updated!", Toast.LENGTH_SHORT).show();
                        sendResult(Activity.RESULT_OK);

                    }

                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //
                    }
                })
                .create();

        dialog.getWindow().setBackgroundDrawableResource(R.color.dark_gray);
        dialog.setTitle(Html.fromHtml("<font color='#FFFFFF'>Edit this song</font>"));
        return dialog;

    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
