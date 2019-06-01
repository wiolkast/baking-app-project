package com.example.baking_app_project;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

public class BakingAppWidgetProvider extends AppWidgetProvider {

    public static void updateBakingAppWidget(Context context, AppWidgetManager appWidgetManager,
                                             int[] appWidgetIds){
        for(int appWidgetId : appWidgetIds){
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_view);

        Intent intent = new Intent(context, BakingAppListWidgetService.class);
        views.setRemoteAdapter(R.id.widget_list_view, intent);
        views.setEmptyView(R.id.widget_list_view, R.id.empty_view);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String recipeName = preferences.getString("recipeName", "");
        views.setTextViewText(R.id.recipe_name, recipeName);

        Intent appIntent = new Intent(context, MainActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.container, appPendingIntent);
        views.setPendingIntentTemplate(R.id.widget_list_view, appPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        BakingAppWidgetService.startActionUpdateWidget(context);
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}

