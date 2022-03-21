package zkhaider.com.cooleaf.cooleafapi.interfaces;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.GET;
import zkhaider.com.cooleaf.cooleafapi.entities.LocationTag;

/**
 * Created by ZkHaider on 5/24/15.
 */
public interface ILocationTags {
    @GET("/v2/users/location_tags.json")
    void getLocationTags(Callback<ArrayList<LocationTag>> callback);
}
