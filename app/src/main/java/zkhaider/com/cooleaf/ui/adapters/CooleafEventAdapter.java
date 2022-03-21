package zkhaider.com.cooleaf.ui.adapters;

import android.animation.Animator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.cooleafapi.entities.Event;
import zkhaider.com.cooleaf.cooleafapi.entities.User;
import zkhaider.com.cooleaf.ui.helpers.EventHelper;
import zkhaider.com.cooleaf.ui.listeners.OnItemTouchListener;
import zkhaider.com.cooleaf.ui.listeners.OnRevealListener;
import zkhaider.com.cooleaf.ui.revealcolor.RevealColorView;
import zkhaider.com.cooleaf.ui.roundedpicture.CircleTransform;

/**
 * Created by Haider on 2/4/2015.
 */
public class CooleafEventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = CooleafEventAdapter.class.getSimpleName();

    private final static int REVEAL_DURATION = 400;

    private static final int TYPE_PROFILE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private OnItemTouchListener mOnItemTouchListener;
    private boolean mShowHeader = false;
    private Context mContext;
    private User mUser;
    private List<Event> mEvents = new ArrayList<>();

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public void setEvents(List<Event> events) {
        mEvents = events;
    }

    public void addEvents(List<Event> events) {
        mEvents.addAll(events);
    }

    public Event getEvent(int position) {
        return mEvents.get(position);
    }

    public void setShowHeader(boolean showHeader) {
        mShowHeader = showHeader;
    }

    public void setOnItemTouchListener(OnItemTouchListener onItemTouchListener) {
        this.mOnItemTouchListener = onItemTouchListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        mContext = viewGroup.getContext();

        switch (viewType) {
            case TYPE_PROFILE_HEADER:
                View headerView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.header_profile,
                        viewGroup, false);
                return new ActivityViewHolder(headerView, viewType);

            case TYPE_ITEM:
                View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_item,
                        viewGroup, false);
                return new ActivityViewHolder(itemView, viewType);

            default:
                View defaultView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_item,
                        viewGroup, false);
                return new ActivityViewHolder(defaultView, viewType);
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {

        final ActivityViewHolder activityViewHolder = (ActivityViewHolder) viewHolder;
        int offset = mShowHeader ? 1 : 0;
        if (activityViewHolder.mHolderId == TYPE_ITEM) {

            // Initialize Row Item Stuff
            final Event event = mEvents.get(position - offset);

            int participantsCount = event.getParticipantsCount();
            String participantsString = participantsCount == 1 ? " Participant" : " Participants";
            String dateText = EventHelper.dateRangeToString(event);
            String joinButtonText = (event.getAttending()) ? "Leave" : "Join";
            boolean attending = event.getAttending();

            activityViewHolder.mActivityTitle.setText(event.getName());
            activityViewHolder.mActivityDescription.setText(event.getDescription());
            activityViewHolder.mNumOfParticipants.setText(participantsCount + participantsString);
            activityViewHolder.mActivityHashTag.setText("");
            activityViewHolder.mActivityRewardPoints.setText(String.valueOf(event.getRewardPoints()) + " Reward Points");
            activityViewHolder.mActivityRewardPoints.setVisibility(View.INVISIBLE);
            activityViewHolder.mActivityTime.setText(dateText);
            activityViewHolder.mJoinEvent.setText(joinButtonText);
            //activityViewHolder.mJoinEvent.setEnabled(!event.isPast());
            adjustColor(activityViewHolder, attending);

            String activityImageUrl;
            if (event.getImage() != null) {
                activityImageUrl = event.getImage().getWideUrl();


                Picasso.with(viewHolder.itemView.getContext())
                        .load(activityImageUrl)
                        .into(activityViewHolder.mActivityImage);
            }


        } else if (mUser != null && activityViewHolder.mProfilePicture != null && mShowHeader) {
            String imageUrl = mUser.getProfile().getPicture().getVersions().getLargeURL();
            String companyLogoUrl = mUser.getRole().getOrganization().getPicture().getVersions().getMainURL();

            Picasso.with(viewHolder.itemView.getContext())
                    .load(imageUrl)
                    .transform(new CircleTransform())
                    .fit()
                    .centerCrop()
                    .into(activityViewHolder.mProfilePicture);

            Picasso.with(viewHolder.itemView.getContext())
                    .load(companyLogoUrl)
                    .into(activityViewHolder.mCompanyLogo);

            String personName = mUser.getName();
            if (mUser.getName().contains(" "))
                personName = mUser.getName().split(" ")[0];

            String personTitles = mUser.getRole().getOrganization().getName();
            int personRewards = mUser.getRewardPoints();
            activityViewHolder.mPersonName.setText("Hi, " + personName);
            activityViewHolder.mPersonTitles.setText(personTitles);

            int showRewards = (personRewards != 0) ? View.VISIBLE : View.GONE;
            activityViewHolder.mPersonReward.setText(String.valueOf(personRewards) + " Reward Points");
            activityViewHolder.mPersonReward.setVisibility(showRewards);
        }
    }

    private void adjustColor(ActivityViewHolder activityViewHolder, boolean attending) {
        if (attending) {
            activityViewHolder.mRevealColorView.setBackgroundColor(activityViewHolder.mEventColor);
            activityViewHolder.mJoinEvent.setTextColor(activityViewHolder.mWhite);
            activityViewHolder.mActivityDescription.setTextColor(activityViewHolder.mWhite);
            activityViewHolder.mAddComment.setTextColor(activityViewHolder.mWhite);
        } else {
            activityViewHolder.mRevealColorView.setBackgroundColor(activityViewHolder.mWhite);
            activityViewHolder.mJoinEvent.setTextColor(activityViewHolder.mEventColor);
            activityViewHolder.mActivityDescription.setTextColor(activityViewHolder.mPrimaryMaterialTextLight);
            activityViewHolder.mAddComment.setTextColor(activityViewHolder.mPrimaryMaterialDark);
        }
    }

    @Override
    public int getItemCount() {
        if (mEvents != null && mShowHeader) {
            return mEvents.size() + 1;
        }
        if (mEvents != null) {
            return mEvents.size();
        }
        if (mUser != null) {
            return 1;
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position) && mShowHeader) {
            return TYPE_PROFILE_HEADER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public Event removeItem(int position) {
        final Event event = mEvents.remove(position);
        notifyItemRemoved(position);
        return event;
    }

    public void addItem(int position, Event event) {
        mEvents.add(position, event);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Event event = mEvents.remove(fromPosition);
        mEvents.add(toPosition, event);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void replaceItem(Event event) {
        for (int i = 0, size = mEvents.size(); i < size; i++) {
            int id = mEvents.get(i).getId();
            if (id == event.getId()) {
                mEvents.remove(i);
                mEvents.add(i, event);
                notifyItemChanged(i);
            }
        }
    }

    public class ActivityViewHolder extends RecyclerView.ViewHolder implements OnRevealListener {

        int mHolderId;

        // For header
        protected ImageView mCompanyLogo;
        protected ImageButton mProfilePicture;
        protected TextView mPersonName;
        protected TextView mPersonTitles;
        protected TextView mPersonReward;

        // For row item
        protected ImageButton mActivityImage;
        protected TextView mNumOfParticipants;
        protected TextView mActivityHashTag;
        protected TextView mActivityTitle;
        protected TextView mActivityDescription;
        protected TextView mActivityTime;
        protected TextView mActivityRewardPoints;
        protected ImageButton mActivityButton;
        protected RelativeLayout mRelativeLayout;
        protected View mActivityEventMask;
        protected TextView mAddComment;
        protected TextView mJoinEvent;
        protected RevealColorView mRevealColorView;

        // Color state list
        protected Resources mResources;
        protected ColorStateList colorViewStateList;
        protected ColorStateList colorBackgroundStateList;

        private int mPrimaryMaterialDark;
        private int mPrimaryMaterialTextLight;
        private int mWhite;
        private int mEventColor;

        public ActivityViewHolder(View v, int viewType) {
            super(v);

            mResources = v.getContext().getResources();
            colorViewStateList = mResources.getColorStateList(R.color.ripple_drawable_text);
            colorBackgroundStateList = mResources.getColorStateList(R.color.ripple_drawable_view);

            mPrimaryMaterialDark = mResources.getColor(R.color.primary_material_dark);
            mPrimaryMaterialTextLight = mResources.getColor(R.color.primary_text_default_material_light);
            mWhite = mResources.getColor(android.R.color.white);
            mEventColor = mResources.getColor(R.color.eventCard);

            switch (viewType) {
                case TYPE_PROFILE_HEADER:
                    initHeaderViews(v);
                    initListeners();
                    break;
                case TYPE_ITEM:
                    initActivityViews(v);
                    initListeners();
                    break;
                default:
                    initActivityViews(v);
                    initListeners();
            }
        }

        private void initHeaderViews(View v) {
            mCompanyLogo = (ImageView) v.findViewById(R.id.companyLogo);
            mProfilePicture = (ImageButton) v.findViewById(R.id.profilePictureDashboard);
            mPersonName = (TextView) v.findViewById(R.id.personNameDashBoard);
            mPersonTitles = (TextView) v.findViewById(R.id.personTitlesDashboard);
            mPersonReward = (TextView) v.findViewById(R.id.personRewardsDashboard);
            mHolderId = 0;
        }

        private void initActivityViews(View v) {
            mActivityImage = (ImageButton) v.findViewById(R.id.eventImage);
            mNumOfParticipants = (TextView) v.findViewById(R.id.eventParticipants);
            mActivityHashTag = (TextView) v.findViewById(R.id.eventHashtag);
            mActivityTitle = (TextView) v.findViewById(R.id.eventTitle);
            mActivityTime = (TextView) v.findViewById(R.id.eventTime);
            mActivityRewardPoints = (TextView) v.findViewById(R.id.eventRewards);
            mActivityButton = (ImageButton) v.findViewById(R.id.eventCalenderButton);
            mRelativeLayout = (RelativeLayout) v.findViewById(R.id.eventInfo);
            mActivityEventMask = v.findViewById(R.id.eventMask);
            mActivityDescription = (TextView) v.findViewById(R.id.eventDescription);
            mAddComment = (TextView) v.findViewById(R.id.addCommentButton);
            mJoinEvent = (TextView) v.findViewById(R.id.joinButton);
            mRevealColorView = (RevealColorView) v.findViewById(R.id.reveal);

            mActivityRewardPoints.setVisibility(View.INVISIBLE);

            mHolderId = 1;
        }

        private void initListeners() {
            switch (mHolderId) {
                case TYPE_PROFILE_HEADER:
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnItemTouchListener.onCardViewTap(v, getAdapterPosition());
                        }
                    });
                    mProfilePicture.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnItemTouchListener.onCardViewTap(v, getAdapterPosition());
                        }
                    });
                    break;
                case TYPE_ITEM:
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnItemTouchListener.onCardViewTap(v, getAdapterPosition());
                        }
                    });
                    mActivityImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnItemTouchListener.onCardViewTap(v, getAdapterPosition());
                        }
                    });
                    mJoinEvent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int offset = mShowHeader ? 1 : 0;
                            Event event = mEvents.get(getAdapterPosition() - offset);
                            if (event.isPast()) {
                                Toast.makeText(mContext, String.format("Can not %s past event", event.getAttending() ? "leave" : "join"), Toast.LENGTH_LONG).show();
                            } else {
                                mOnItemTouchListener.onJoin(((TextView) v.findViewById(R.id.joinButton)),
                                        getAdapterPosition());
                                onReveal();
                            }

                        }
                    });
                    mAddComment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnItemTouchListener.onAddComment(v, getAdapterPosition());
                        }
                    });
                    break;
            }

        }

        @Override
        public void onReveal() {
            int offset = mShowHeader ? 1 : 0;
            Event event = mEvents.get(getAdapterPosition() - offset);
            revealJoin(event.getAttending());
        }

        public void revealJoin(boolean attending) {
            Point lastTouched = new Point();
            lastTouched.x = (int) mJoinEvent.getX();
            lastTouched.y = (int) mJoinEvent.getY();
            if (!attending) {
                revealAfterJoin(lastTouched);
            } else {
                revealAfterLeave(lastTouched);
            }
        }

        private void revealAfterJoin(Point lastTouched) {
            mRevealColorView.reveal(
                    lastTouched.x,
                    lastTouched.y,
                    mEventColor,
                    0,
                    REVEAL_DURATION,
                    new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            mJoinEvent.setTextColor(mWhite);
                            mAddComment.setTextColor(mWhite);
                            mActivityDescription.setTextColor(mWhite);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {

                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
        }

        private void revealAfterLeave(Point lastTouched) {
            mRevealColorView.reveal(
                    lastTouched.x,
                    lastTouched.y,
                    mWhite,
                    0,
                    REVEAL_DURATION,
                        new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                mJoinEvent.setTextColor(mEventColor);
                                mAddComment.setTextColor(mPrimaryMaterialDark);
                                mActivityDescription.setTextColor(mPrimaryMaterialTextLight);
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {

                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                    });
        }

    }

}