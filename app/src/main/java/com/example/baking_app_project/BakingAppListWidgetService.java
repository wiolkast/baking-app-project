package com.example.baking_app_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.baking_app_project.model.Ingredient;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BakingAppListWidgetService extends RemoteViewsService{

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewFactory(this.getApplicationContext());
    }

    private class ListRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {
        private final Context context;
        private List<Ingredient> ingredients;

        private ListRemoteViewFactory(Context applicationContext){
            this.context = applicationContext;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            ingredients = getIngredientList();
        }

        private List<Ingredient> getIngredientList(){
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String ingredientsString = preferences.getString("ingredientsString", "");
            if(ingredientsString == null || ingredientsString.isEmpty()) {
                return null;
            }
            List<String> strings = new ArrayList<>(Arrays.asList(TextUtils.split(ingredientsString, ",_,")));
            List<Ingredient> ingredients = new ArrayList<>();
            Gson gson = new Gson();
            for(String string : strings){
                Ingredient ingredient = gson.fromJson(string, Ingredient.class);
                ingredients.add(ingredient);
            }
            return ingredients;
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (ingredients != null) {
                return ingredients.size();
            }else {
                return 0;
            }
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if(ingredients == null || ingredients.size() == 0) return null;

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);
            views.setTextViewText(R.id.ingredient_name, ingredients.get(position).getIngredient());
            views.setTextViewText(R.id.ingredient_measure, ingredients.get(position).getMeasure());
            views.setTextViewText(R.id.ingredient_quantity, String.valueOf(ingredients.get(position).getQuantity()));

            Intent fillInIntent = new Intent();
            views.setOnClickFillInIntent(R.id.container, fillInIntent);

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
