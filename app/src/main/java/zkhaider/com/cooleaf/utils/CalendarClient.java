package zkhaider.com.cooleaf.utils;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.widget.Toast;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.cooleafapi.entities.Event;

public class CalendarClient {

    private Context mContext;

    public CalendarClient(Context context) {
        mContext = context;
    }

    public void saveToCalendar(Event event, long calendarId) {
        ContentValues values = getValues(event, calendarId);

        ContentResolver contentResolver = mContext.getContentResolver();
        Uri uri = contentResolver.insert(Events.CONTENT_URI, values);
        Toast.makeText(mContext, mContext.getString(R.string.added_to_calendar), Toast.LENGTH_SHORT).show();
        Long.valueOf(uri.getLastPathSegment());
    }

    private ContentValues getValues(Event event, long calendarId)
    {
        ContentValues values = new ContentValues();
        /* Under the hood stuff */
        values.put(CalendarContract.Events.EVENT_TIMEZONE, event.getTimeZone().getMomentName());
        values.put(Events.CALENDAR_ID, calendarId);
		/* Displayed event content (except for date handled with the cal variable) */
        values.put(Events.TITLE, event.getName());

        values.put(Events.DESCRIPTION, event.getDescription());
        String address = event.getAddress() != null ? event.getAddress().displayAddress() : "";
        values.put(Events.EVENT_LOCATION, address);
        values.put(Events.DTSTART, event.getStartDate().getTime());
        long endTime = event.getEndDate() != null ? event.getEndDate().getTime() : event.getStartDate().getTime() + 3600; // 1 hour
        values.put(Events.DTEND, endTime);
        return values;
    }

    public Map<String, Long> getCalendars() {
        Map<String, Long> calendars = new HashMap<>();
        String[] projection = new String[] { Calendars._ID, Calendars.NAME,
                Calendars.ACCOUNT_NAME, Calendars.ACCOUNT_TYPE };
        Cursor calCursor = mContext.getContentResolver().query(Calendars.CONTENT_URI,
                projection, Calendars.VISIBLE + " = 1", null,
                Calendars._ID + " ASC");
        if (calCursor.moveToFirst()) {
            do {
                long id = calCursor.getLong(0);
                String displayName = calCursor.getString(1);
                calendars.put(displayName, id);
            } while (calCursor.moveToNext());
        }
        return calendars;
    }


}