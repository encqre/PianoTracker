package com.untrustworthypillars.pianotracker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.untrustworthypillars.pianotracker.formatting.DateFormatting;
import java.util.List;

public class SongListFragment extends Fragment {

    private RecyclerView mSongRecyclerView;
    private SongAdapter mAdapter;
    private Callbacks mCallbacks;

    // Required interface for hosting activities
    public interface Callbacks {
        void onSongSelected(Song song);
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
    }

    private class SongHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Song mSong;
        private TextView mTitle;
        private TextView mInfo;
        private TextView mScore;
        private ProgressBar mProgressBar;
        private View mDifficultyIndicator;

        public SongHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_song, parent, false));
            itemView.setOnClickListener(this);

            mTitle = (TextView) itemView.findViewById(R.id.song_title);
            mInfo = (TextView) itemView.findViewById(R.id.song_info);
            mScore = (TextView) itemView.findViewById(R.id.song_progress_score);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.song_progress);
            mDifficultyIndicator = (View) itemView.findViewById(R.id.song_difficulty_indicator);
        }

        public void bind(Song song) {
            mSong = song;
            mTitle.setText(mSong.getTitle());
            mScore.setText(String.valueOf(mSong.getScore()));
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mScore.getLayoutParams(); // will change left margin of score text depending on digits
            if (mSong.getScore() == 100) {
                params.leftMargin = 22;
                mScore.setLayoutParams(params);
            } else if (mSong.getScore() < 10) {
                params.leftMargin = 56;
                mScore.setLayoutParams(params);
            }
            mInfo.setText("Last played: " + DateFormatting.dateToMonthNameAndDay(mSong.getLastPlayed()));
            mProgressBar.setProgress(mSong.getScore());
            int progressColor = getResources().getColor(R.color.state_not_learned, getContext().getTheme());
            switch (mSong.getState()){
                case 0:
                    progressColor = getResources().getColor(R.color.state_not_learned, getContext().getTheme());
                    break;
                case 1:
                    progressColor = getResources().getColor(R.color.state_learning, getContext().getTheme());
                    break;
                case 2:
                    progressColor = getResources().getColor(R.color.state_to_relearn, getContext().getTheme());
                    break;
                case 3:
                    progressColor = getResources().getColor(R.color.state_learned, getContext().getTheme());
                    break;
            }
            mProgressBar.getProgressDrawable().setColorFilter(progressColor, PorterDuff.Mode.SRC_IN);

            switch (mSong.getDifficulty()) {
                case 0:
                    mDifficultyIndicator.setBackgroundColor(getResources().getColor(R.color.difficulty_very_easy, getContext().getTheme()));
                    break;
                case 1:
                    mDifficultyIndicator.setBackgroundColor(getResources().getColor(R.color.difficulty_easy, getContext().getTheme()));
                    break;
                case 2:
                    mDifficultyIndicator.setBackgroundColor(getResources().getColor(R.color.difficulty_medium, getContext().getTheme()));
                    break;
                case 3:
                    mDifficultyIndicator.setBackgroundColor(getResources().getColor(R.color.difficulty_hard, getContext().getTheme()));
                    break;
                case 4:
                    mDifficultyIndicator.setBackgroundColor(getResources().getColor(R.color.difficulty_very_hard, getContext().getTheme()));
                    break;
            }
        }

        @Override
        public void onClick(View view) {
            mCallbacks.onSongSelected(mSong);
        }

    }

    private class SongAdapter extends RecyclerView.Adapter<SongHolder> {

        private List<Song> mSongs;


        public SongAdapter(List<Song> songs) {
            mSongs = songs;
        }


        @Override
        public SongHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new SongHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(SongHolder holder, int position) {
            Song song = mSongs.get(position);
            holder.bind(song);

        }

        @Override
        public int getItemCount() {
            return mSongs.size();
        }

        public void setSongs(List<Song> songs) {
            mSongs = songs;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_list, container, false);

        mSongRecyclerView = (RecyclerView) view.findViewById(R.id.song_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mSongRecyclerView.setLayoutManager(layoutManager);

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public void updateUI() {
        SongManager manager = SongManager.get(getActivity());

//        List<Song> songs = new ArrayList<>();
//        Song song1 = new Song();
//        Song song2 = new Song();
//        song1.setTitle("One song");
//        song1.setState(0);
//        song1.setCountPlayed(5);
//        song1.setSecondsPlayed(4802);
//        song1.setDifficulty(0);
//        song1.setScore(98);
//        song1.setLastPlayed(new Date());
//        song1.setVideoId("xorgzasdas");
//        song1.setOrderId(0);
//        songs.add(song1);
//        song2.setTitle("good two songs");
//        song2.setState(2);
//        song2.setCountPlayed(76);
//        song2.setSecondsPlayed(99765);
//        song2.setDifficulty(2);
//        song2.setScore(50);
//        song2.setLastPlayed(new Date());
//        song2.setVideoId("gjhjsdas");
//        song2.setOrderId(1);
//        songs.add(song2);
//
//        SongManager.get(getActivity()).addSong(song1);
//        SongManager.get(getActivity()).addSong(song2);

        List<Song> songs = manager.getSongs();

        if (mAdapter == null) {
            mAdapter = new SongAdapter(songs);
            mSongRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setSongs(songs);
            mAdapter.notifyDataSetChanged();
        }
    }
}
