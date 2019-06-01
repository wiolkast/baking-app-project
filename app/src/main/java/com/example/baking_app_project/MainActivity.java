package com.example.baking_app_project;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.baking_app_project.model.Recipe;
import com.example.baking_app_project.utils.AppExecutors;
import com.example.baking_app_project.utils.NetworkUtils;
import com.example.baking_app_project.utils.SimpleIdlingResource;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainActivityAdapter.RecipeClickListener {
    @BindView(R.id.rv_recipes_list) RecyclerView recyclerView;
    @BindView(R.id.tv_error_message) TextView errorTextView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    private MainActivityAdapter adapter;
    private List<Recipe> recipes;
    @Nullable private SimpleIdlingResource idlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        int spanCount;
        if(isTablet){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            spanCount = 3;
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            spanCount = 1;
        }
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new MainActivityAdapter(new ArrayList<Recipe>(), this);
        recyclerView.setAdapter(adapter);

        getIdlingResource();
    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                if(idlingResource != null) idlingResource.setIdleState(false);
                recipes = NetworkUtils.getRecipes();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        if(recipes == null){
                            errorTextView.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                        } else {
                            errorTextView.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            adapter.updateData(recipes);
                        }
                        if(idlingResource != null) idlingResource.setIdleState(true);
                    }
                });
            }
        });
    }

    @Override
    public void onRecipeClick(int itemId) {
        Intent intent = new Intent(this, IngredientsStepsListActivity.class);
        intent.putExtra("RecipeDetails", recipes.get(itemId));
        startActivity(intent);
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource(){
        if(idlingResource == null){
            idlingResource = new SimpleIdlingResource();
        }
        return idlingResource;
    }
}