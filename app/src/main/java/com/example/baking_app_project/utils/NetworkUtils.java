package com.example.baking_app_project.utils;

import android.text.TextUtils;
import android.util.Log;

import com.example.baking_app_project.model.Ingredient;
import com.example.baking_app_project.model.Recipe;
import com.example.baking_app_project.model.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class NetworkUtils {
    private final static String LOG_TAG = NetworkUtils.class.getSimpleName();
    private final static String JSON_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private final static String JSON_RECIPE_NAME = "name";
    private final static String JSON_INGREDIENTS = "ingredients";
    private final static String JSON_QUANTITY = "quantity";
    private final static String JSON_MEASURE = "measure";
    private final static String JSON_INGREDIENT_NAME = "ingredient";
    private final static String JSON_STEPS = "steps";
    private final static String JSON_SHORT_DESCRIPTION = "shortDescription";
    private final static String JSON_DESCRIPTION = "description";
    private final static String JSON_VIDEO_URL = "videoURL";
    private final static String JSON_THUMBNAIL_URL = "thumbnailURL";

    private static URL createUrl() {
        URL url = null;
        try {
            url = new URL(JSON_URL);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL", e);
        }
        return url;
    }

    private static String getResponseFromHttpsUrl() throws IOException {
        String jsonResponse = "";
        URL url = createUrl();
        if (url == null) {
            return jsonResponse;
        }
        HttpsURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving JSON results. ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Recipe> extractRecipesFromJson(String json) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        List<Recipe> recipes = new ArrayList<>();
        try {
            JSONArray recipesArray = new JSONArray(json);
            for (int i = 0; i < recipesArray.length(); i++) {
                JSONObject currentRecipe = recipesArray.getJSONObject(i);
                String name = currentRecipe.getString(JSON_RECIPE_NAME);

                List<Ingredient> ingredients = new ArrayList<>();
                JSONArray ingredientsArray = currentRecipe.getJSONArray(JSON_INGREDIENTS);
                for (int j = 0; j < ingredientsArray.length(); j++) {
                    JSONObject currentIngredient = ingredientsArray.getJSONObject(j);
                    double quantity = currentIngredient.getDouble(JSON_QUANTITY);
                    String measure = currentIngredient.getString(JSON_MEASURE);
                    String ingredientName = currentIngredient.getString(JSON_INGREDIENT_NAME);
                    Ingredient ingredient = new Ingredient(quantity, measure, ingredientName);
                    ingredients.add(ingredient);
                }

                List<Step> steps = new ArrayList<>();
                JSONArray stepsArray = currentRecipe.getJSONArray(JSON_STEPS);
                for (int j = 0; j < stepsArray.length(); j++) {
                    JSONObject currentStep = stepsArray.getJSONObject(j);
                    String shortDescription = currentStep.getString(JSON_SHORT_DESCRIPTION);
                    String description = currentStep.getString(JSON_DESCRIPTION);
                    String videoUrl = currentStep.getString(JSON_VIDEO_URL);
                    String thumbnailUrl = currentStep.getString(JSON_THUMBNAIL_URL);
                    Step step = new Step(shortDescription, description, videoUrl, thumbnailUrl);
                    steps.add(step);
                }

                Recipe recipe = new Recipe(name, ingredients, steps);
                recipes.add(recipe);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing recipes JSON results. ", e);
        }
        return recipes;
    }

    public static List<Recipe> getRecipes() {
        List<Recipe> recipes = null;
        try {
            recipes = extractRecipesFromJson(getResponseFromHttpsUrl());
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving JSON. ", e);
        }
        return recipes;
    }
}
