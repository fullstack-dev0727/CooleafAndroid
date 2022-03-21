package zkhaider.com.cooleaf.mvp.users.events;

import java.util.ArrayList;

import zkhaider.com.cooleaf.cooleafapi.entities.LocationTag;

/**
 * Created by Haider on 2/18/2015.
 */
public class LoadedLocationTagEvent {

    private ArrayList<LocationTag> mLocationTags;

    public ArrayList<LocationTag> getLocationTags()
    {
        return this.mLocationTags;
    }

    public LoadedLocationTagEvent(ArrayList<LocationTag> locationTags) {
        this.mLocationTags = locationTags;
    }

}
