package zkhaider.com.cooleaf.cooleafapi.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import zkhaider.com.cooleaf.utils.DateUtils;
import zkhaider.com.cooleaf.utils.TimeUtils;

/**
 * Created by Haider on 2/4/2015.
 */
public class Event {

    public static final String TAG = Event.class.getSimpleName();

    @SerializedName("id")
    private int mId;
    public int getId() {
        return mId;
    }

    @SerializedName("address")
    private Address mAddress;
    public Address getAddress() {
        return mAddress;
    }

    @SerializedName("description")
    private String mDescription;
    public String getDescription() {
        return mDescription;
    }

    @SerializedName("name")
    private String mName;
    public String getName() {
        return mName;
    }

    @SerializedName("image_url")
    private String mImageUrl;
    public String getSmallPath() {
        return mImageUrl.replace("{{SIZE}}", "164x164");
    }
    public String getLargePath() {
        return mImageUrl.replace("{{SIZE}}", "500x200");
    }
    public String getWideUrl() {
        return mImageUrl.replace("{{SIZE}}", "1600x400");
    }

    @SerializedName("image")
    private Image mImage;
    public Image getImage() {
        return mImage;
    }

    @SerializedName("participants")
    private List<Participant> mParticipants = new ArrayList<>();
    public List<Participant> getParticipants() {
        return mParticipants;
    }

    @SerializedName("participants_count")
    private int mParticipantsCount;
    public int getParticipantsCount() {
        return mParticipantsCount;
    }

    @SerializedName("timezone")
    private TimeZone mTimeZone;
    public TimeZone getTimeZone() {
        return mTimeZone;
    }

    @SerializedName("start_time")
    private String mStartTime;
    public String getStartTime() {
        return mStartTime;
    }
    public Date getStartDate() {
        return DateUtils.parseDate(getStartTime());
    }

    @SerializedName("last_start_time")
    private String mLastStartTime;
    public String getLastStartTime() {
        return mLastStartTime;
    }
    public Date getLastStartDate() {
        return DateUtils.parseDate(getLastStartTime());
    }
    public String getTimeAgo() {
        return TimeUtils.timeAgo(getLastStartDate());
    }

    @SerializedName("end_time")
    private String mEndTime;
    public String getEndTime() {
        return mEndTime;
    }
    public Date getEndDate() {
        return DateUtils.parseDate(getEndTime());
    }

    @SerializedName("reward_points")
    private int mRewardPoints;
    public int getRewardPoints() {
        return mRewardPoints;
    }

    @SerializedName("past")
    private boolean mPast;
    public boolean isPast() {
        return mPast;
    }

    @SerializedName("joinable")
    private boolean mJoinable;
    public boolean getJoinable(){ return mJoinable; }

    @SerializedName("attending")
    private boolean mAttending;
    public boolean getAttending(){ return mAttending; }

    @SerializedName("paid")
    private boolean mPaid;
    public boolean getPaid(){return mPaid;}

    @SerializedName("series")
    private Series mSeries;
    public Series getSeries() {
        return mSeries;
    }

    @SerializedName("coordinator")
    private Coordinator mCoordinator;
    public Coordinator getCoordinator() {
        return mCoordinator;
    }

    @SerializedName("coordinator_name")
    private String mCoordinatorName;
    public String getCoordinatorName() {
        return mCoordinatorName;
    }


}
