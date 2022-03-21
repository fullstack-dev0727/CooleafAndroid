package zkhaider.com.cooleaf.mvp.interests.controllers;

import java.util.List;

import retrofit.Callback;
import zkhaider.com.cooleaf.cooleafapi.CooleafAPIClient;
import zkhaider.com.cooleaf.cooleafapi.interfaces.IInterest;
import zkhaider.com.cooleaf.cooleafapi.entities.Event;
import zkhaider.com.cooleaf.cooleafapi.entities.Interest;
import zkhaider.com.cooleaf.cooleafapi.entities.User;

/**
 * Created by ZkHaider on 8/22/15.
 */
public class InterestController {

    public void getInterests(Callback<List<Interest>> callback) {
        IInterest interests = CooleafAPIClient.getAsynsRestAdapter().create(IInterest.class);
        interests.getInterests(callback);
    }

    public void getInterest(int interestId, Callback<Interest> callback) {
        IInterest interests = CooleafAPIClient.getAsynsRestAdapter().create(IInterest.class);
        interests.getInterest(interestId, callback);
    }

    public void getInterestEvents(int interestId, int page, int perPage, Callback<List<Event>> callback) {
        IInterest interests = CooleafAPIClient.getAsynsRestAdapter().create(IInterest.class);
        interests.getInterestEvents(interestId, page, perPage, callback);
    }


    public void getInterestsUsers(int interestId, int page, int perPage, Callback<List<User>> callback) {
        IInterest interests = CooleafAPIClient.getAsynsRestAdapter().create(IInterest.class);
        interests.getInterestUsers(interestId, page, perPage, callback);
    }

    public void searchInterests(String query, Callback<List<Interest>> callback) {
        IInterest interests = CooleafAPIClient.getAsynsRestAdapter().create(IInterest.class);
        interests.searchInterests(query, callback);
    }

}
