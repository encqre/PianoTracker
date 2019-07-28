package com.untrustworthypillars.pianotracker;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Date;

public class AddSongDialog extends DialogFragment {

    private EditText mOrderId;
    private EditText mTitle;
    private EditText mVideoId;
    private Spinner mDifficultySpinner;
    private Spinner mStateSpinner;
    private EditText mScore;
    private EditText mSecondsPlayed;
    private EditText mCountPlayed;

    public static AddSongDialog newInstance () {
        Bundle args = new Bundle();
//        args.putSerializable(ARG_CATEGORY, category);

        AddSongDialog fragment = new AddSongDialog();
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

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
        mDifficultySpinner.setSelection(0);
        mStateSpinner.setAdapter(stateAdapter);
        mStateSpinner.setSelection(0);

        mOrderId = (EditText) v.findViewById(R.id.dialog_orderid);
        mOrderId.setInputType(InputType.TYPE_CLASS_NUMBER);

        mTitle = (EditText) v.findViewById(R.id.dialog_title);
        mVideoId = (EditText) v.findViewById(R.id.dialog_videoid);
        mScore = (EditText) v.findViewById(R.id.dialog_score);
        mScore.setInputType(InputType.TYPE_CLASS_NUMBER);
        mSecondsPlayed = (EditText) v.findViewById(R.id.dialog_seconds);
        mSecondsPlayed.setInputType(InputType.TYPE_CLASS_NUMBER);
        mCountPlayed = (EditText) v.findViewById(R.id.dialog_count);
        mCountPlayed.setInputType(InputType.TYPE_CLASS_NUMBER);

        AlertDialog dialog = new AlertDialog.Builder(getActivity()).setView(v).setTitle("Add a new song")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Song song = new Song();
                        song.setLastPlayed(new Date());
                        song.setOrderId(Integer.valueOf(mOrderId.getText().toString()));
                        song.setTitle(mTitle.getText().toString());
                        song.setVideoId(mVideoId.getText().toString());
                        song.setScore(Integer.valueOf(mScore.getText().toString()));
                        song.setDifficulty(mDifficultySpinner.getSelectedItemPosition());
                        song.setState(mStateSpinner.getSelectedItemPosition());
                        song.setSecondsPlayed(Long.valueOf(mSecondsPlayed.getText().toString()));
                        song.setCountPlayed(Integer.valueOf(mCountPlayed.getText().toString()));
                        SongManager.get(getActivity()).addSong(song);
                        Toast.makeText(getActivity(), "New song added!", Toast.LENGTH_SHORT).show();
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
        dialog.setTitle(Html.fromHtml("<font color='#FFFFFF'>Add a New Song</font>"));
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
