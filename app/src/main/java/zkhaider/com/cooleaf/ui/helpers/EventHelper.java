package zkhaider.com.cooleaf.ui.helpers;

import java.text.SimpleDateFormat;
import java.util.Date;

import zkhaider.com.cooleaf.cooleafapi.entities.Event;

/**
 * Created by kcoleman on 2/18/15.
 */
public class EventHelper {

    public static String dateRangeToString(Event event)
    {
        if (event.getEndDate() != null) {
            return formatDate(event.getStartDate())
                   + " - " +
                   formatDate(event.getEndDate());
        } else {
            return formatDate(event.getStartDate());
        }
    }

    private static String formatDate(Date date)
    {
        SimpleDateFormat displayDateFormat = new SimpleDateFormat("MMM d, h:mm a");

        String dateString = displayDateFormat.format(date);
        return dateString;
    }
}
