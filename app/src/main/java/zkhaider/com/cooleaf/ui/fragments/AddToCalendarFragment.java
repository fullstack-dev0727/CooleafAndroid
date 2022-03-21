package zkhaider.com.cooleaf.ui.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.List;
import java.util.Map;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.cooleafapi.entities.Event;
import zkhaider.com.cooleaf.utils.CalendarClient;

/**
 * Created by kcoleman on 2/12/15.
 */
public class AddToCalendarFragment extends Fragment {
    private Context mContext;
    private List<Event> mEvents;

    public void setArguments(Context context, List<Event> events)
    {
        mContext = context;
        mEvents = events;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_to_calendar, null);

        final ImageButton button = (ImageButton) root.findViewById(R.id.add_to_calendar);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final CalendarClient client = new CalendarClient(mContext);
                AlertDialog.Builder adb = new AlertDialog.Builder(mContext);

                final Map<String,Long> calendars = client.getCalendars();
                final CharSequence[] calendarKeys = calendars.keySet().toArray(new CharSequence[calendars.keySet().size()]);

                adb.setSingleChoiceItems(calendarKeys, -1, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface d, int position) {
                        long calendarId = calendars.get(calendarKeys[position]);
                        for(Event event : mEvents)
                            client.saveToCalendar(event, calendarId);
                        d.dismiss();
                    }

                });
                adb.setNegativeButton("Cancel", null);
                adb.setTitle(mContext.getString(R.string.which_calendar));
                adb.show();
            }
        });

        return root;
    }

}
