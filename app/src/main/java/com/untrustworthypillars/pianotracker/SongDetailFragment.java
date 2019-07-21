package com.untrustworthypillars.pianotracker;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.UUID;

public class SongDetailFragment extends Fragment {

    private static final String ARG_SONG_ID = "song_id";

    private Song mSong;
    private Callbacks mCallbacks;
    private TextView mTitle;
    private Button mVideo;

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
        mVideo = (Button) v.findViewById(R.id.detail_video_button);
        mVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                watchYoutubeVideo(getContext(), mSong.getVideoId());
            }
        });



        return v;
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
