package com.example.todolist.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.todolist.MainActivity;
import com.example.todolist.R;
import com.example.todolist.auth.SessionManager;
import com.example.todolist.db.DB;
import com.example.todolist.model.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TodoWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int widgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, widgetId);
        }
    }

    public static void updateWidget(Context context, AppWidgetManager appWidgetManager, int widgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_todo);

        // Open app on click
        Intent mainIntent = new Intent(context, MainActivity.class);
        PendingIntent mainPending = PendingIntent.getActivity(context, 0, mainIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.widget_root, mainPending);

        // Add task intent
        Intent addIntent = new Intent(context, MainActivity.class);
        addIntent.putExtra("openAddTask", true);
        PendingIntent addPending = PendingIntent.getActivity(context, 1, addIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.widget_btn_add, addPending);

        // Today's date header
        String today = new SimpleDateFormat("EEE, dd MMM", Locale.getDefault())
                .format(Calendar.getInstance().getTime());
        views.setTextViewText(R.id.widget_date, today);

        // Load today's tasks
        SessionManager session = new SessionManager(context);
        int userId = session.getCurrentUserId();
        StringBuilder taskText = new StringBuilder();

        if (userId != -1) {
            DB db = new DB(context);
            List<Task> tasks = db.getTasksSortedFiltered(userId, DB.FILTER_INCOMPLETE,
                    DB.SORT_BY_DEADLINE, null);

            // Show only tasks with today's deadline or no deadline (up to 5)
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            long startOfDay = cal.getTimeInMillis();
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            long endOfDay = cal.getTimeInMillis();

            int count = 0;
            for (Task task : tasks) {
                if (count >= 5) break;
                long dl = task.getDeadline() != null ? task.getDeadline() : 0;
                if (dl == 0 || (dl >= startOfDay && dl <= endOfDay)) {
                    String check = task.isCompleted() ? "☑ " : "☐ ";
                    taskText.append(check).append(task.getTitle()).append("\n");
                    count++;
                }
            }
            if (count == 0) taskText.append("Không có task hôm nay 🎉");
        } else {
            taskText.append("Vui lòng đăng nhập để xem tasks");
        }

        views.setTextViewText(R.id.widget_task_list, taskText.toString().trim());
        appWidgetManager.updateAppWidget(widgetId, views);
    }
}
