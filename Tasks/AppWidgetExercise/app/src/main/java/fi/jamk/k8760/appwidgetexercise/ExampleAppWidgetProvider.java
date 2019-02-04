package fi.jamk.k8760.appwidgetexercise;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class ExampleAppWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
            //current date
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            String formattedDate = df.format(c);
            //number of week
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(c);
            int curWeek = calendar.get(Calendar.WEEK_OF_YEAR);
            int curDay = calendar.get(Calendar.DAY_OF_YEAR);
            //Christmas
            int year = Calendar.getInstance().get(Calendar.YEAR);
            String dtChristmas = "25.12." + String.valueOf(year);
            String dtLast = "31.12." + String.valueOf(year);
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            int weekTillCristmas;
            int daysTillChristmas;
            try {
                Date dateCh = format.parse(dtChristmas);
                Date dateLast = format.parse(dtLast);
                calendar.setTime(dateCh);
                int cristmasWeek = calendar.get(Calendar.WEEK_OF_YEAR);
                int cristmasDay = calendar.get(Calendar.DAY_OF_YEAR);
                calendar.setTime(dateLast);
                int lastWeek = calendar.get(Calendar.WEEK_OF_YEAR);
                int lastDay = calendar.get(Calendar.DAY_OF_YEAR);
                if (cristmasWeek >= curWeek) {
                    weekTillCristmas = cristmasWeek - curWeek;
                    views.setTextViewText(R.id.week, "Weeks: " + String.valueOf(weekTillCristmas));
                }
                else views.setTextViewText(R.id.week, "Chistmas is gone already!");
                if (cristmasDay >= curDay) {
                    daysTillChristmas = cristmasDay - curDay;
                    views.setTextViewText(R.id.day, "Days: " + String.valueOf(daysTillChristmas));
                }
                else views.setTextViewText(R.id.day, "Chistmas is gone already!");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
