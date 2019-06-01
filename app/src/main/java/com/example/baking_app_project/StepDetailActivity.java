package com.example.baking_app_project;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.baking_app_project.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailActivity extends AppCompatActivity {
    @BindView(R.id.button_previous) Button buttonPrevious;
    @BindView(R.id.button_next) Button buttonNext;
    private static final String STEP_ID = "step_id";
    private Recipe recipe;
    private int stepId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        recipe = intent.getParcelableExtra("RecipeDetails");
        stepId = intent.getIntExtra("StepId", 0);

        if (getSupportActionBar() != null) getSupportActionBar().setTitle(recipe.getName());

        if (savedInstanceState != null) {
            stepId = savedInstanceState.getInt(STEP_ID);
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.updateData(recipe.getSteps().get(stepId).getVideoUrl(),
                    recipe.getSteps().get(stepId).getThumbnailUrl(),
                    recipe.getSteps().get(stepId).getDescription());
            fragmentManager.beginTransaction()
                    .add(R.id.step_detail_fragment_container, stepDetailFragment)
                    .commit();
        }

        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        int orientation = getResources().getConfiguration().orientation;
        if(!isTablet && orientation == Configuration.ORIENTATION_LANDSCAPE) {
            buttonPrevious.setVisibility(View.GONE);
            buttonNext.setVisibility(View.GONE);
        }

        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stepId > 0) {
                    stepId = stepId - 1;
                    fragmentUpdateData();
                }
            }
        });
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stepId < recipe.getSteps().size() - 1) {
                    stepId = stepId + 1;
                    fragmentUpdateData();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STEP_ID, stepId);
        super.onSaveInstanceState(outState);
    }

    private void fragmentUpdateData() {
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.updateData(recipe.getSteps().get(stepId).getVideoUrl(),
                recipe.getSteps().get(stepId).getThumbnailUrl(),
                recipe.getSteps().get(stepId).getDescription());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_detail_fragment_container, stepDetailFragment)
                .commit();
    }
}
