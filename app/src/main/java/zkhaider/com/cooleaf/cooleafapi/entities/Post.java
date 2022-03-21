package zkhaider.com.cooleaf.cooleafapi.entities;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import zkhaider.com.cooleaf.utils.DateUtils;
import zkhaider.com.cooleaf.utils.TimeUtils;

/**
 * Created by ZkHaider on 6/8/15.
 */
public class Post {

    @SerializedName("id")
    private int mId;
    public int getId() {
        return mId;
    }

    @SerializedName("user_id")
    private int mUserId;
    public int getUserId() {
        return mUserId;
    }

    @SerializedName("feed_id")
    private int mFeedId;
    public int getFeedId() {
        return mFeedId;
    }

    @SerializedName("user_name")
    private String mUserName;
    public String getUserName() {
        return mUserName;
    }

    @SerializedName("content")
    private String mContent;
    public String getContent() {
        return mContent;
    }

    @SerializedName("updated_at")
    private String mUpdatedAt;
    public String getUpdatedAt() {
        return mUpdatedAt;
    }
    public Date getUpdatedAtDate() {
        return DateUtils.parseDate(getUpdatedAt());
    }
    public String getTimeAgo() {
        return TimeUtils.timeAgo(getUpdatedAtDate());
    }

    @SerializedName("user")
    private Participant mUser;
    public Participant getUser() {
        return mUser;
    }

    @SerializedName("user_picture")
    private Picture mUserPicture;
    public Picture getUserPicture() {
        return mUserPicture;
    }

    @SerializedName("feedable_id")
    private int mFeedableId;
    public int getFeedableId() {
        return mFeedableId;
    }

    @SerializedName("feedable_type")
    private String mFeedableType;
    public String getFeedableType() {
        return mFeedableType;
    }

    @SerializedName("feedable_picture_url")
    private String mImageUrl;
    public String getImageUrl() {
        return mImageUrl;
    }
    public String getLargePath() {
        return mImageUrl.replace("{{SIZE}}", "500x200");
    }
    public String getSmallPath() {
        return mImageUrl.replace("{{SIZE}}", "164x164");
    }

    @SerializedName("feedable_path")
    private String mFeedablePath;
    public String getFeedablePath() {
        return mFeedablePath;
    }

    @SerializedName("comment_count")
    private int mCommentCount;
    public int getCommentCount() {
        return mCommentCount;
    }

    @SerializedName("comments")
    private List<Post> mComments;
    public List<Post> getComments() {
        return mComments;
    }

    @SerializedName("attachment")
    private Attachment mAttachment;
    public Attachment getAttachment() {
        return mAttachment;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public void setFeedId(int feedId) {
        mFeedId = feedId;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public void setUpdatedAt(String updatedAt) {
        mUpdatedAt = updatedAt;
    }

    public void setUser(Participant user) {
        mUser = user;
    }

    public void setUserPicture(Picture userPicture) {
        mUserPicture = userPicture;
    }

    public void setFeedableId(int feedableId) {
        mFeedableId = feedableId;
    }

    public void setFeedableType(String feedableType) {
        mFeedableType = feedableType;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public void setFeedablePath(String feedablePath) {
        mFeedablePath = feedablePath;
    }

    public void setCommentCount(int commentCount) {
        mCommentCount = commentCount;
    }

    public void setComments(List<Post> comments) {
        mComments = comments;
    }

    public void setAttachment(Attachment attachment) {
        mAttachment = attachment;
    }
}
