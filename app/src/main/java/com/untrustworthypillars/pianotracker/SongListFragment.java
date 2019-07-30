package com.untrustworthypillars.pianotracker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.untrustworthypillars.pianotracker.formatting.DateFormatting;
import com.untrustworthypillars.pianotracker.formatting.ColorFormatting;
import java.util.List;

public class SongListFragment extends Fragment {

    private static final int REQUEST_ADD_SONG = 0;

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
            int progressColor = ColorFormatting.getStateColor(mSong.getState(), getContext());
            mProgressBar.getProgressDrawable().setColorFilter(progressColor, PorterDuff.Mode.SRC_IN);

            mDifficultyIndicator.setBackgroundColor(ColorFormatting.getDifficultyColor(mSong.getDifficulty(), getContext()));

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.list_toolbar, menu);
        MenuItem filterItem = menu.findItem(R.id.list_toolbar_filter);
        MenuItem sortItem = menu.findItem(R.id.list_toolbar_sort);
        MenuItem addItem = menu.findItem(R.id.list_toolbar_add);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.list_toolbar_filter:
                //TODO
                return true;
            case R.id.list_toolbar_sort:
                //TODO
                return true;
            case R.id.list_toolbar_add:
                FragmentManager fm = getActivity().getSupportFragmentManager();
                AddSongDialog dialog = AddSongDialog.newInstance();
                dialog.setTargetFragment(this, REQUEST_ADD_SONG);
                dialog.show(fm, "AddSongDialog");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ADD_SONG) {
            updateUI();
        }
    }

    public void updateUI() {
        SongManager manager = SongManager.get(getActivity());
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
