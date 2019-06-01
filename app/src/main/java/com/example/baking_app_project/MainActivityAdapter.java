package com.example.baking_app_project;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.baking_app_project.model.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.ViewHolder> {
    private final List<Recipe> recipes;
    private final RecipeClickListener recipeClickListener;

    public MainActivityAdapter(@NonNull List<Recipe> recipes, RecipeClickListener recipeClickListener) {
        this.recipes = recipes;
        this.recipeClickListener = recipeClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.recipe_list_item) TextView recipeListItem;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recipeClickListener.onRecipeClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Recipe currentRecipe = recipes.get(i);
        viewHolder.recipeListItem.setText(currentRecipe.getName());
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public interface RecipeClickListener{
        void onRecipeClick(int itemId);
    }

    public void updateData(List<Recipe> recipes){
        this.recipes.clear();
        if(recipes != null){
            this.recipes.addAll(recipes);
        }
        notifyDataSetChanged();
    }
}
