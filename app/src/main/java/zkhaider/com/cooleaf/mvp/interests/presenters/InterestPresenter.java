package zkhaider.com.cooleaf.mvp.interests.presenters;

import com.squareup.otto.Subscribe;

import java.util.List;

import zkhaider.com.cooleaf.bus.BusProvider;
import zkhaider.com.cooleaf.mvp.interests.events.LoadInterestEvent;
import zkhaider.com.cooleaf.mvp.interests.events.LoadedInterestEvent;
import zkhaider.com.cooleaf.mvp.interests.interactors.IInterestDetailInteractor;
import zkhaider.com.cooleaf.mvp.interests.interactors.IInterestSearchInteractor;
import zkhaider.com.cooleaf.mvp.users.events.LoadEditEvent;
import zkhaider.com.cooleaf.mvp.interests.events.LoadInterestsEvent;
import zkhaider.com.cooleaf.mvp.interests.events.LoadedInterestsEvent;
import zkhaider.com.cooleaf.mvp.users.events.LoadedMeEvent;
import zkhaider.com.cooleaf.cooleafapi.entities.Edit;
import zkhaider.com.cooleaf.ui.fragments.InterestsFragment;

/**
 * Created by ZkHaider on 8/21/15.
 */
public class InterestPresenter {

    private IInterestSearchInteractor mInterestSearchInteractor;
    private IInterestDetailInteractor mInterestDetailInteractor;

    private InterestsFragment mTarget;
    private Edit mEdit;

    /*********************************************************************************************
     * Initialization
     *********************************************************************************************/

    public InterestPresenter(InterestsFragment target) {
        this.mTarget = target;
    }

    public InterestPresenter(IInterestSearchInteractor interestSearchInteractor) {
        this.mInterestSearchInteractor = interestSearchInteractor;
    }

    public InterestPresenter(IInterestDetailInteractor interestDetailInteractor) {
        this.mInterestDetailInteractor = interestDetailInteractor;
    }

    /*********************************************************************************************
     * Load Interests
     *********************************************************************************************/

    public void loadInterests() {
        BusProvider.getInstance().post(new LoadInterestsEvent());
    }

    /*********************************************************************************************
     * Load Interest
     *********************************************************************************************/

    public void loadInterest(int interestId) {
        BusProvider.postEvent(new LoadInterestEvent(interestId));
    }

    /*********************************************************************************************
     * BusProvider Methods
     *********************************************************************************************/

    public void register() {
        BusProvider.getInstance().register(this);
    }

    public void unregister() {
        BusProvider.getInstance().unregister(this);
    }

    public void registerOnBus() {
        BusProvider.getInstance().register(this);
    }

    public void unregisterOnBus() {
        BusProvider.getInstance().unregister(this);
    }

    /*********************************************************************************************
     * Accessory Methods
     *********************************************************************************************/

    public void setEdit(Edit edit) {
        this.mEdit = edit;
    }

    public void saveCategoryIdsToProfile(List<Integer> categoryIds) {
        if (categoryIds.isEmpty())
            mTarget.displayError();
        else {
            mEdit.setCategoryIds(categoryIds);
            BusProvider.postEvent(new LoadEditEvent(mEdit));
        }
    }

    /*********************************************************************************************
     * Subscription Events
     *********************************************************************************************/

    @Subscribe
    public void onLoadedInterestsEvent(LoadedInterestsEvent loadedInterestsEvent) {
        mTarget.onInterestsLoaded(loadedInterestsEvent.getInterests());
    }

    @Subscribe
    public void onLoadedInterestEvent(LoadedInterestEvent loadedInterestEvent) {
        if (mInterestSearchInteractor != null)
            mInterestSearchInteractor.gotInterestFromSearch(loadedInterestEvent.getInterest());
    }

    @Subscribe
    public void onLoadedMeEvent(LoadedMeEvent loadedMeEvent) {
        mTarget.goToDashboard();
    }

}
