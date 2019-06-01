package com.example.baking_app_project;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.baking_app_project.model.Recipe;

public class IngredientsStepsListActivity extends AppCompatActivity implements IngredientsStepsListFragment.OnStepClickListener {
    private boolean isTablet;
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_steps_list);

        Intent intent = getIntent();
        recipe = intent.getParcelableExtra("RecipeDetails");
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(recipe.getName());
        isTablet = getResources().getBoolean(R.bool.isTablet);
        if (isTablet) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            if (savedInstanceState == null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                StepDetailFragment stepDetailFragment = new StepDetailFragment();
                stepDetailFragment.updateData(recipe.getSteps().get(0).getVideoUrl(),
                        recipe.getSteps().get(0).getThumbnailUrl(),
                        recipe.getSteps().get(0).getDescription());
                fragmentManager.beginTransaction()
                        .add(R.id.step_detail_fragment_container, stepDetailFragment)
                        .commit();
            }
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    public void onStepClick(int stepId) {
        if (isTablet) {
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.updateData(recipe.getSteps().get(stepId).getVideoUrl(),
                    recipe.getSteps().get(stepId).getThumbnailUrl(),
                    recipe.getSteps().get(stepId).getDescription());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_detail_fragment_container, stepDetailFragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putExtra("RecipeDetails", recipe);
            intent.putExtra("StepId", stepId);
            startActivity(intent);
        }
    }
}
