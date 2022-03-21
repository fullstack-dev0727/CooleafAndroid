package zkhaider.com.cooleaf.mvp.authentication.interactors;

import zkhaider.com.cooleaf.cooleafapi.entities.User;

/**
 * Created by ZkHaider on 8/23/15.
 */
public interface IAuthenticationInteractor {

    void authenticated(User user);
    void deauthenticated();

}
