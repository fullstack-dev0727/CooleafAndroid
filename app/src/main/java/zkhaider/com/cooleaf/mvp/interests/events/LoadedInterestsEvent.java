package zkhaider.com.cooleaf.mvp.interests.events;

import java.util.List;

import zkhaider.com.cooleaf.cooleafapi.entities.Interest;

/**
 * Created by Haider on 3/1/2015.
 */
public class LoadedInterestsEvent {

    private List<Interest> mInterests;

    public LoadedInterestsEvent(List<Interest> interests) {
        this.mInterests = interests;
    }

    public List<Interest> getInterests() {
        return mInterests;
    }
}
