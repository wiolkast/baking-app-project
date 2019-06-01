package com.example.baking_app_project;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailFragment extends Fragment implements Player.EventListener {
    private final static String LOG_TAG = StepDetailFragment.class.getSimpleName();
    @BindView(R.id.screen_container) ConstraintLayout screenContainer;
    @BindView(R.id.media_container) ConstraintLayout mediaContainer;
    @BindView(R.id.video) PlayerView exoPlayerView;
    @BindView(R.id.image) ImageView imageView;
    @BindView(R.id.step_description) TextView stepDescriptionTv;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    private SimpleExoPlayer exoPlayer;
    private static final String VIDEO_URL = "video_url";
    private static final String IMAGE_URL = "image_url";
    private static final String STEP_DESCRIPTION = "step_description";
    private String videoUrl;
    private String imageUrl;
    private String stepDescription;

    public StepDetailFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);
        if (savedInstanceState != null) {
            videoUrl = savedInstanceState.getString(VIDEO_URL);
            imageUrl = savedInstanceState.getString(IMAGE_URL);
            stepDescription = savedInstanceState.getString(STEP_DESCRIPTION);
        }

        ButterKnife.bind(this, rootView);

        initializePlayer(Uri.parse(videoUrl));

        if (stepDescription != null) stepDescriptionTv.setText(stepDescription);

        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        int orientation = getResources().getConfiguration().orientation;
        if(!isTablet && orientation == Configuration.ORIENTATION_LANDSCAPE) setFullscreenMode();

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    private void setFullscreenMode(){
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
            getActivity().getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        }
        stepDescriptionTv.setVisibility(View.GONE);
        screenContainer.setPadding(0,0,0,0);
        mediaContainer.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mediaContainer.setPadding(0,0,0,0);
        exoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
    }

    private void initializePlayer(Uri mediaUri) {
        imageView.setVisibility(View.GONE);
        exoPlayerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        if (exoPlayer == null) {
            exoPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getContext()), new DefaultTrackSelector(), new DefaultLoadControl());
            exoPlayerView.setPlayer(exoPlayer);
            exoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            exoPlayer.addListener(this);
        }
        String userAgent = Util.getUserAgent(getContext(), getString(R.string.app_name));
        MediaSource mediaSource = new ExtractorMediaSource
                .Factory(new DefaultHttpDataSourceFactory(userAgent)).createMediaSource(mediaUri);
        exoPlayer.prepare(mediaSource, true, true);
        exoPlayer.setPlayWhenReady(true);
    }

    private void releasePlayer() {
        exoPlayer.stop();
        exoPlayer.release();
        exoPlayer = null;
    }

    public void updateData(String videoUrl, String imageUrl, String stepDescription) {
        this.videoUrl = videoUrl;
        this.imageUrl = imageUrl;
        this.stepDescription = stepDescription;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(VIDEO_URL, videoUrl);
        outState.putString(IMAGE_URL, imageUrl);
        outState.putString(STEP_DESCRIPTION, stepDescription);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if(playbackState == Player.STATE_BUFFERING){
            progressBar.setVisibility(View.VISIBLE);
            exoPlayerView.setVisibility(View.GONE);
        } else if (playbackState == Player.STATE_READY){
            progressBar.setVisibility(View.GONE);
            exoPlayerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {
    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        exoPlayerView.setVisibility(View.GONE);
        if (TextUtils.isEmpty(imageUrl)) imageUrl = null;
        Picasso.with(getContext()).load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(imageView);
        imageView.setVisibility(View.VISIBLE);
        switch (error.type) {
            case ExoPlaybackException.TYPE_SOURCE:
                Log.e(LOG_TAG, "TYPE_SOURCE: " + error.getSourceException().getMessage());
                break;

            case ExoPlaybackException.TYPE_RENDERER:
                Log.e(LOG_TAG, "TYPE_RENDERER: " + error.getRendererException().getMessage());
                break;

            case ExoPlaybackException.TYPE_UNEXPECTED:
                Log.e(LOG_TAG, "TYPE_UNEXPECTED: " + error.getUnexpectedException().getMessage());
                break;
        }
    }

    @Override
    public void onPositionDiscontinuity(int reason) {
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
    }

    @Override
    public void onSeekProcessed() {
    }
}
