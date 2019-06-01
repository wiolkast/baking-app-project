package com.example.baking_app_project.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable {
    private final String name;
    private final List<Ingredient> ingredients;
    private final List<Step> steps;

    public Recipe(String name, List<Ingredient> ingredients, List<Step> steps){
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    private Recipe(Parcel in) {
        name = in.readString();
        ingredients = new ArrayList<>();
        in.readList(ingredients, Ingredient.class.getClassLoader());
        steps = new ArrayList<>();
        in.readList(steps, Step.class.getClassLoader());
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public String getName(){
        return name;
    }

    public List<Ingredient> getIngredients(){
        return ingredients;
    }

    public List<Step> getSteps(){
        return steps;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeList(ingredients);
        dest.writeList(steps);
    }
}
