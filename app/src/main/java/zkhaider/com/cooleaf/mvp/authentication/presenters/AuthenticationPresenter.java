package zkhaider.com.cooleaf.mvp.authentication.presenters;

import com.squareup.otto.Subscribe;

import zkhaider.com.cooleaf.bus.BusProvider;
import zkhaider.com.cooleaf.cooleafapi.CooleafAPIClient;
import zkhaider.com.cooleaf.mvp.authentication.events.DeauthorizeEvent;
import zkhaider.com.cooleaf.mvp.authentication.events.LoadAuthenticateEvent;
import zkhaider.com.cooleaf.mvp.authentication.interactors.IAuthenticationInteractor;

/**
 * Created by ZkHaider on 8/22/15.
 */
public class AuthenticationPresenter {


    /*********************************************************************************************
     * Initialization
     *********************************************************************************************/

    public AuthenticationPresenter() {

    }

    /*********************************************************************************************
     * BusProvider Methods
     *********************************************************************************************/

    public void registerOnBus() {
        BusProvider.getInstance().register(this);
    }

    public void unregisterOnBus() {
        BusProvider.getInstance().unregister(this);
    }

    /*********************************************************************************************
     * Authentication Methods
     *********************************************************************************************/

    public void performAuthentication(String email, String password) {
        BusProvider.getInstance().post(new LoadAuthenticateEvent(email, password));
    }

    /*********************************************************************************************
     * Deauthentication Methods
     *********************************************************************************************/


    /*********************************************************************************************
     * Subscription Methods
     *********************************************************************************************/


}
