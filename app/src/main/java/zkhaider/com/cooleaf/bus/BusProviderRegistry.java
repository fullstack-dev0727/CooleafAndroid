package zkhaider.com.cooleaf.bus;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import zkhaider.com.cooleaf.base.BaseBusProviderRegistry;
import zkhaider.com.cooleaf.mvp.authentication.subscribers.AuthenticationSubscriber;
import zkhaider.com.cooleaf.mvp.comments.subscribers.CommentSubscriber;
import zkhaider.com.cooleaf.mvp.events.subscribers.EventDetailSubscriber;
import zkhaider.com.cooleaf.mvp.events.subscribers.EventSubscriber;
import zkhaider.com.cooleaf.mvp.feeds.subscribers.FeedSubscriber;
import zkhaider.com.cooleaf.mvp.filepreviews.subscribers.FilePreviewSubscriber;
import zkhaider.com.cooleaf.mvp.groups.subscribers.GroupSubscriber;
import zkhaider.com.cooleaf.mvp.interests.subscribers.InterestSubscriber;
import zkhaider.com.cooleaf.mvp.participants.subscribers.ParticipantsSubscriber;
import zkhaider.com.cooleaf.mvp.registrations.subscribers.RegisterSubscriber;
import zkhaider.com.cooleaf.mvp.search.subscribers.SearchSubscriber;
import zkhaider.com.cooleaf.mvp.users.subscribers.UserSubscriber;

/**
 * Created by ZkHaider on 8/21/15.
 */
public class BusProviderRegistry extends BaseBusProviderRegistry {

    public BusProviderRegistry(Context applicationContext) {
        super(applicationContext);
    }

    // TODO -- This could potentially be done better and less tightly coupled
    @Override
    protected List<EventBusSubscriber> createDefaultSubscribers() {
        List<EventBusSubscriber> subscribers = new ArrayList<>();
        subscribers.addAll(Arrays.asList(
                new RegisterSubscriber(),
                new AuthenticationSubscriber(),
                new InterestSubscriber(),
                new EventSubscriber(),
                new EventDetailSubscriber(),
                new UserSubscriber(),
                new ParticipantsSubscriber(),
                new FilePreviewSubscriber(),
                new FeedSubscriber(),
                new SearchSubscriber(),
                new CommentSubscriber(),
                new GroupSubscriber()
        ));
        return subscribers;
    }

}
