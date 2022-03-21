package zkhaider.com.cooleaf.mvp.participants.controllers;

import java.util.List;

import retrofit.Callback;
import zkhaider.com.cooleaf.cooleafapi.CooleafAPIClient;
import zkhaider.com.cooleaf.cooleafapi.interfaces.IParticipant;
import zkhaider.com.cooleaf.cooleafapi.entities.EventsToJoinRequest;
import zkhaider.com.cooleaf.cooleafapi.entities.User;

/**
 * Created by ZkHaider on 8/22/15.
 */
public class ParticipantsController {

    public void getParticipants(int eventId, int page, int perPage, Callback<List<User>> callback) {
        IParticipant participants = CooleafAPIClient.getAsynsRestAdapter().create(IParticipant.class);
        participants.getParticipants(eventId, page, perPage, callback);
    }

    public void joinEvents(int eventId, EventsToJoinRequest eventsToJoinRequest, Callback<Void> callback) {
        IParticipant participant = CooleafAPIClient.getAsynsRestAdapter().create(IParticipant.class);
        participant.join(eventId, eventsToJoinRequest, callback);
    }

}
