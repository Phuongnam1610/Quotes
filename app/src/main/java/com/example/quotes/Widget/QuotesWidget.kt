package com.example.quotes.Widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.quotes.R
import com.example.quotes.repository.PostRepository
import com.example.quotes.utils.Utils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class QuoteWidget : AppWidgetProvider() {
    private val jobs = mutableListOf<Job>()

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        if (intent?.action == AppWidgetManager.ACTION_APPWIDGET_UPDATE) {
            val appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
            if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                val appWidgetManager = AppWidgetManager.getInstance(context)
                context?.let { updateAppWidget(it, appWidgetManager, appWidgetId) }
            }
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val views = RemoteViews(context.packageName, R.layout.activity_widget)

//            jobs.forEach { it.cancel() }
//            jobs.clear()

            val job = GlobalScope.launch {
                val listPost = PostRepository().getAllPosts()
                val p = listPost.random()

                Utils.createBitmap(context, p) { scaledImage ->
                    views.setImageViewBitmap(R.id.widget_image, scaledImage)
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
            }
//            jobs.add(job)

            // Thiết lập click listener cho nút Increment
            val intent = Intent(context, QuoteWidget::class.java)
            intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                appWidgetId,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            views.setOnClickPendingIntent(R.id.widget_skip, pendingIntent)
        }

}