package com.example.baking_app_project;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.baking_app_project.model.Ingredient;
import com.example.baking_app_project.model.Recipe;
import com.example.baking_app_project.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsStepsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_INGREDIENT_SECTION_LABEL = 0;
    private final int VIEW_TYPE_INGREDIENT = 1;
    private final int VIEW_TYPE_STEP_SECTION_LABEL = 2;
    private final int VIEW_TYPE_STEP = 3;
    private final List<Ingredient> ingredients;
    private final List<Step> steps;
    private final StepClickListener stepClickListener;
    private final AddIngredientsListener addIngredientsListener;
    private final Boolean isTablet;
    private static int position = -1;

    public IngredientsStepsListAdapter(@NonNull Recipe recipe, StepClickListener stepClickListener, AddIngredientsListener addIngredientsListener, Context context) {
        this.ingredients = recipe.getIngredients();
        this.steps = recipe.getSteps();
        this.stepClickListener = stepClickListener;
        this.addIngredientsListener = addIngredientsListener;
        this.isTablet = context.getResources().getBoolean(R.bool.isTablet);;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == VIEW_TYPE_INGREDIENT_SECTION_LABEL) {
            Context context = viewGroup.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.ingredients_label_item, viewGroup, false);
            return new IngredientsLabelViewHolder(view);
        }
        if (viewType == VIEW_TYPE_INGREDIENT) {
            Context context = viewGroup.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.ingredient_list_item, viewGroup, false);
            return new IngredientViewHolder(view);
        }
        if (viewType == VIEW_TYPE_STEP_SECTION_LABEL) {
            Context context = viewGroup.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.steps_label_item, viewGroup, false);
            return new StepsLabelViewHolder(view);
        }
        else {
            Context context = viewGroup.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.step_list_item, viewGroup, false);
            return new StepViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return VIEW_TYPE_INGREDIENT_SECTION_LABEL;
        }
        if (position - 1 < ingredients.size()) {
            return VIEW_TYPE_INGREDIENT;
        }
        if (position == ingredients.size() +1) {
            return VIEW_TYPE_STEP_SECTION_LABEL;
        }
        if (position - ingredients.size() -2 < steps.size()) {
            return VIEW_TYPE_STEP;
        }
        return -1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof IngredientViewHolder) {
            ((IngredientViewHolder) viewHolder).populate(ingredients.get(i - 1));
        }
        if (viewHolder instanceof StepViewHolder) {
            ((StepViewHolder) viewHolder).populate(steps.get(i - 2 - ingredients.size()));
        }
    }

    @Override
    public int getItemCount() {
        return ingredients.size() + steps.size() + 2;
    }

    public class IngredientsLabelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.button_ingredients) Button ingredientsButton;

        private IngredientsLabelViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ingredientsButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            addIngredientsListener.onButtonClick();
        }
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ingredient_description) TextView ingredientDescription;

        private IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void populate(Ingredient ingredient) {
            String ingredientString = ingredient.getQuantity() + " " + ingredient.getMeasure()
                    + "  " + ingredient.getIngredient();
            ingredientDescription.setText(ingredientString);
        }
    }

    private class StepsLabelViewHolder extends RecyclerView.ViewHolder {
        private StepsLabelViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.step_list_item) TextView stepListItem;

        private StepViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            position = ingredients.size() + 2;
        }

        private void populate(Step step) {
            stepListItem.setText(step.getShortDescription());
            if(isTablet && getAdapterPosition() == position){
                stepListItem.setTypeface(null, Typeface.BOLD);
            } else if(isTablet) {
                stepListItem.setTypeface(null, Typeface.NORMAL);
            }
        }

        @Override
        public void onClick(View v) {
            stepClickListener.onStepClick(getAdapterPosition() - ingredients.size() - 2);
            position = getAdapterPosition();
            notifyDataSetChanged();
        }
    }

    public interface StepClickListener{
        void onStepClick(int itemId);
    }

    public interface AddIngredientsListener{
        void onButtonClick();
    }
}
