package com.example.baking_app_project;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;

public class BakingAppWidgetService extends JobIntentService {

    private static final String ACTION_UPDATE_WIDGET = "com.example.baking_app_project.action.update_widget";
    private static final int SERVICE_JOB_ID = 11;

    private static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, BakingAppWidgetService.class, SERVICE_JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        final String action = intent.getAction();
        if (ACTION_UPDATE_WIDGET.equals(action)) {
            handleActionUpdateWidget();
        }
    }

    public static void startActionUpdateWidget(Context context) {
        Intent intent = new Intent(context, BakingAppWidgetService.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        enqueueWork(context, intent);
    }

    private void handleActionUpdateWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingAppWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
        BakingAppWidgetProvider.updateBakingAppWidget(this, appWidgetManager, appWidgetIds);
    }
}
