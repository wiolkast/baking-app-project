package com.example.baking_app_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.baking_app_project.model.Ingredient;
import com.example.baking_app_project.model.Recipe;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsStepsListFragment extends Fragment implements IngredientsStepsListAdapter.StepClickListener, IngredientsStepsListAdapter.AddIngredientsListener {
    @BindView(R.id.rv_steps_list)
    RecyclerView recyclerView;
    private OnStepClickListener callback;
    private Recipe recipe;

    public IngredientsStepsListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ingredients_steps_list, container, false);

        ButterKnife.bind(this, rootView);

        if(getActivity()!= null) {
            Intent intent = getActivity().getIntent();
            recipe = intent.getParcelableExtra("RecipeDetails");
        }

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        IngredientsStepsListAdapter adapter = new IngredientsStepsListAdapter(recipe, this, this, getActivity());
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onStepClick(int itemId) {
        callback.onStepClick(itemId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnStepClickListener");
        }
    }

    @Override
    public void onButtonClick() {
        saveIngredients(recipe.getIngredients(), recipe.getName());
        BakingAppWidgetService.startActionUpdateWidget(getContext());
    }

    public interface OnStepClickListener {
        void onStepClick(int stepId);
    }

    private void saveIngredients(List<Ingredient> ingredients, String recipeName) {
        Gson gson = new Gson();
        List<String> strings = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            strings.add(gson.toJson(ingredient));
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        preferences.edit().putString("ingredientsString", TextUtils.join(",_,", strings)).apply();
        preferences.edit().putString("recipeName", recipeName).apply();
    }
}