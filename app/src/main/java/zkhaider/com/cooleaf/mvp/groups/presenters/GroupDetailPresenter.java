package zkhaider.com.cooleaf.mvp.groups.presenters;

import zkhaider.com.cooleaf.mvp.groups.interactors.GroupInteractor;

/**
 * Created by ZkHaider on 8/23/15.
 */
public class GroupDetailPresenter {

    private GroupInteractor mGroupInteractor;

    public GroupDetailPresenter(GroupInteractor groupInteractor) {
        this.mGroupInteractor = groupInteractor;
    }



}
