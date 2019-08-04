package com.untrustworthypillars.pianotracker;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
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
    private SongManager mSongManager;
    private List<Song> mSongs;
    private boolean mFilterStateIsActive = false;
    private int mFilterStateToolbarId = 0;
    private Integer mFilterState = null;
    private boolean mFilterDifficultyIsActive = false;
    private int mFilterDifficultyToolbarId = 0;
    private Integer mFilterDifficulty = null;
    private boolean mSortIsActive = false;
    private Integer mSort = null;


    // Required interface for hosting activities
    public interface Callbacks {
        void onSongSelected(Song song, Integer stateFilter, Integer difficultyFilter, Integer sort);
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
            } else {
                params.leftMargin = 39;
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
            mCallbacks.onSongSelected(mSong, mFilterState, mFilterDifficulty, mSort);
        } //TODO once filtering is done, need to pass the whole list or maybe parameters to query that specific list

    }

    private class SongAdapter extends RecyclerView.Adapter<SongHolder> {

        private List<Song> mAdapterSongs;


        public SongAdapter(List<Song> songs) {
            mAdapterSongs = songs;
        }


        @Override
        public SongHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new SongHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(SongHolder holder, int position) {
            Song song = mAdapterSongs.get(position);
            holder.bind(song);

        }

        @Override
        public int getItemCount() {
            return mAdapterSongs.size();
        }

        public void setSongs(List<Song> songs) {
            mAdapterSongs = songs;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_list, container, false);

        mSongRecyclerView = (RecyclerView) view.findViewById(R.id.song_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mSongRecyclerView.setLayoutManager(layoutManager);

        mSongManager = SongManager.get(getActivity());
        updateUI();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.list_toolbar, menu);
        MenuItem filterItem = menu.findItem(R.id.list_toolbar_filter);
        MenuItem filterStateItem = menu.findItem(R.id.list_toolbar_filter_state);
        MenuItem sortItem = menu.findItem(R.id.list_toolbar_sort);
        MenuItem addItem = menu.findItem(R.id.list_toolbar_add);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.list_toolbar_filter_state_0:
                mFilterStateIsActive = true;
                mFilterStateToolbarId = R.id.list_toolbar_filter_state_0;
                mFilterState = 0;
                item.setChecked(true);
                getActivity().invalidateOptionsMenu();
                updateUI();
                return true;
            case R.id.list_toolbar_filter_state_1:
                mFilterStateIsActive = true;
                mFilterStateToolbarId = R.id.list_toolbar_filter_state_1;
                mFilterState = 1;
                item.setChecked(true);
                getActivity().invalidateOptionsMenu();
                updateUI();
                return true;
            case R.id.list_toolbar_filter_state_2:
                mFilterStateIsActive = true;
                mFilterStateToolbarId = R.id.list_toolbar_filter_state_2;
                mFilterState = 2;
                item.setChecked(true);
                getActivity().invalidateOptionsMenu();
                updateUI();
                return true;
            case R.id.list_toolbar_filter_state_3:
                mFilterStateIsActive = true;
                mFilterStateToolbarId = R.id.list_toolbar_filter_state_3;
                mFilterState = 3;
                item.setChecked(true);
                getActivity().invalidateOptionsMenu();
                updateUI();
                return true;
            case R.id.list_toolbar_filter_difficulty_0:
                mFilterDifficultyIsActive = true;
                mFilterDifficultyToolbarId = R.id.list_toolbar_filter_difficulty_0;
                mFilterDifficulty = 0;
                item.setChecked(true);
                getActivity().invalidateOptionsMenu();
                updateUI();
                return true;
            case R.id.list_toolbar_filter_difficulty_1:
                mFilterDifficultyIsActive = true;
                mFilterDifficultyToolbarId = R.id.list_toolbar_filter_difficulty_1;
                mFilterDifficulty = 1;
                item.setChecked(true);
                getActivity().invalidateOptionsMenu();
                updateUI();
                return true;
            case R.id.list_toolbar_filter_difficulty_2:
                mFilterDifficultyIsActive = true;
                mFilterDifficultyToolbarId = R.id.list_toolbar_filter_difficulty_2;
                mFilterDifficulty = 2;
                item.setChecked(true);
                getActivity().invalidateOptionsMenu();
                updateUI();
                return true;
            case R.id.list_toolbar_filter_difficulty_3:
                mFilterDifficultyIsActive = true;
                mFilterDifficultyToolbarId = R.id.list_toolbar_filter_difficulty_3;
                mFilterDifficulty = 3;
                item.setChecked(true);
                getActivity().invalidateOptionsMenu();
                updateUI();
                return true;
            case R.id.list_toolbar_filter_difficulty_4:
                mFilterDifficultyIsActive = true;
                mFilterDifficultyToolbarId = R.id.list_toolbar_filter_difficulty_4;
                mFilterDifficulty = 4;
                item.setChecked(true);
                getActivity().invalidateOptionsMenu();
                updateUI();
                return true;
            case R.id.list_toolbar_filter_default:
                mFilterStateIsActive = false;
                mFilterDifficultyIsActive = false;
                getActivity().invalidateOptionsMenu();
                mFilterState = null;
                mFilterDifficulty = null;
                updateUI();
                return true;
            case R.id.list_toolbar_sort_lastplayed:
                mSortIsActive = true;
                mSort = R.id.list_toolbar_sort_lastplayed;
                item.setChecked(true);
                getActivity().invalidateOptionsMenu();
                updateUI();
                return true;
            case R.id.list_toolbar_sort_score:
                mSortIsActive = true;
                mSort = R.id.list_toolbar_sort_score;
                item.setChecked(true);
                getActivity().invalidateOptionsMenu();
                updateUI();
                return true;
            case R.id.list_toolbar_sort_totaltime:
                mSortIsActive = true;
                mSort = R.id.list_toolbar_sort_totaltime;
                item.setChecked(true);
                getActivity().invalidateOptionsMenu();
                updateUI();
                return true;
            case R.id.list_toolbar_sort_totalcount:
                mSortIsActive = true;
                mSort = R.id.list_toolbar_sort_totalcount;
                item.setChecked(true);
                getActivity().invalidateOptionsMenu();
                updateUI();
                return true;
            case R.id.list_toolbar_sort_default:
                mSortIsActive = false;
                getActivity().invalidateOptionsMenu();
                mSort = null;
                updateUI();
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
    public void onPrepareOptionsMenu(Menu menu) {
        //Deal with changes related to filter options

        MenuItem filterItem = menu.findItem(R.id.list_toolbar_filter);
        Drawable filterIcon = filterItem.getIcon();
        MenuItem filterStateItem = menu.findItem(R.id.list_toolbar_filter_state);
        MenuItem filterDifficultyItem = menu.findItem(R.id.list_toolbar_filter_difficulty);

        filterStateItem.setChecked(mFilterStateIsActive);
        filterDifficultyItem.setChecked(mFilterDifficultyIsActive);
        if (mFilterStateIsActive || mFilterDifficultyIsActive){
            filterIcon.setTint(getResources().getColor(R.color.difficulty_very_hard, getContext().getTheme()));
            if (mFilterStateIsActive){
                MenuItem activeStateFilterItem = menu.findItem(mFilterStateToolbarId);
                activeStateFilterItem.setChecked(true);
            }
            if (mFilterDifficultyIsActive) {
                MenuItem activeDifficultyFilterItem = menu.findItem(mFilterDifficultyToolbarId);
                activeDifficultyFilterItem.setChecked(true);
            }
        } else {
            filterIcon.setTint(getResources().getColor(R.color.white, getContext().getTheme()));
        }

        //Deal with changes related to sort options

        MenuItem sortItem = menu.findItem(R.id.list_toolbar_sort);
        Drawable sortIcon = sortItem.getIcon();

        if (mSortIsActive) {
            sortIcon.setTint(getResources().getColor(R.color.difficulty_very_hard, getContext().getTheme()));
            MenuItem activeSortItem = menu.findItem(mSort);
            activeSortItem.setChecked(true);
        } else {
            sortIcon.setTint(getResources().getColor(R.color.white, getContext().getTheme()));
        }

        super.onPrepareOptionsMenu(menu);
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

    /**Function will get a new song list with all currently applied filters/sorters*/
    public void setSongList() {
        if (mFilterDifficultyIsActive && mFilterStateIsActive) {
            mSongs = mSongManager.getSongsFiltered(mFilterState, mFilterDifficulty);
        } else if (mFilterStateIsActive) {
            mSongs = mSongManager.getSongsFiltered(mFilterState, null);
        } else if (mFilterDifficultyIsActive) {
            mSongs = mSongManager.getSongsFiltered(null, mFilterDifficulty);
        } else {
            mSongs = mSongManager.getSongs();
        }

        if (mSortIsActive) {
            if (mSort == R.id.list_toolbar_sort_score) {
                mSongs = Song.sortByScore(mSongs);
            } else if (mSort == R.id.list_toolbar_sort_totaltime) {
                mSongs = Song.sortByTotalTime(mSongs);
            } else if (mSort == R.id.list_toolbar_sort_totalcount) {
                mSongs = Song.sortByTotalCount(mSongs);
            } else {
                mSongs = Song.sortByLastPlayed(mSongs);
            }
        } else {
            mSongs = Song.sortByOrderId(mSongs);
        }
    }

    public void updateUI() {
        setSongList();

        if (mAdapter == null) {
            mAdapter = new SongAdapter(mSongs);
            mSongRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setSongs(mSongs);
            mAdapter.notifyDataSetChanged();
        }
    }
}
