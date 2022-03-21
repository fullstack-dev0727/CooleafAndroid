package zkhaider.com.cooleaf.ui.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.mvp.feeds.events.CreateNewFeedEvent;
import zkhaider.com.cooleaf.mvp.filepreviews.events.LoadUploadFileEvent;
import zkhaider.com.cooleaf.cooleafapi.entities.FilePreview;
import zkhaider.com.cooleaf.cooleafapi.entities.Interest;
import zkhaider.com.cooleaf.ui.helpers.RotatePictureHelper;

/**
 * Created by ZkHaider on 8/11/15.
 */
public abstract class PostActivity extends CameraActivity {

    private static final String TAG = PostActivity.class.getSimpleName();

    private static String mFeedableType;
    private static String mCommentableType;
    public FloatingActionsMenu mFloatingActionsMenu;
    private Interest mInterest;
    private int mEventId;
    private boolean mIsEdit;

    /******************************************************************************************
     *  LifeCycle Methods
     ******************************************************************************************/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO_REQUEST:
                if (resultCode == RESULT_OK) {
                    RotatePictureHelper.rotatePicture(mFile, PostActivity.this, mAttachment);
                    mBus.post(new LoadUploadFileEvent(mTypedFile));
                }
                break;
            case GET_FROM_GALLERY:
                if (resultCode == RESULT_OK) {
                    mBus.post(new LoadUploadFileEvent(mTypedFile));
                }
                break;
            default:
                break;
        }
    }

    /******************************************************************************************
     *  Getter / Setter Methods
     ******************************************************************************************/

    public void setInterest(Interest interest) {
        mInterest = interest;
    }

    public static void setFeedableType(String mFeedableType) {
        PostActivity.mFeedableType = mFeedableType;
    }

    public static void setCommentableType(String mCommentableType) {
        PostActivity.mCommentableType = mCommentableType;
    }

    /******************************************************************************************
     *  Helper Methods
     ******************************************************************************************/

    public void showNewFeedDialog() {
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View v = layoutInflater.inflate(R.layout.feed_new_dialog, null);
        TextInputLayout textInputLayout =
                (TextInputLayout) v.findViewById(R.id.newFeedTextInputLayout);
        textInputLayout.setHint(mResources.getString(R.string.new_post));
        final EditText newFeedEditText = (EditText) v.findViewById(R.id.newFeedEditText);
        final ImageButton addFileButton = (ImageButton) v.findViewById((R.id.addFileButton));
        mAttachment = (ImageView) v.findViewById(R.id.photoAttachment);
        mRemoveImageButton = (ImageButton) v.findViewById(R.id.removeimage);
        mRemoveImageButton.setVisibility(View.GONE);
        mProgressBar = (ProgressBar) v.findViewById(R.id.fileProgress);

        addFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListNoTitle();
            }
        });

        newFeedDialog(v, newFeedEditText);
    }

    private void newFeedDialog(View v, final EditText newFeedEditText) {
        new AlertDialog.Builder(this)
                .setView(v)
                .setPositiveButton(mResources.getString(R.string.post), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String content = newFeedEditText.getText().toString().trim();
                        if (!content.isEmpty()) {
                            createNewFeed(content);
                        } else {
                            showErrorDialog();
                        }
                        mFloatingActionsMenu.collapse();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        mFloatingActionsMenu.collapse();
                    }
                })
                .show();
    }

    private void createNewFeed(String content) {
        String fileCache = (mFilePreview.getFileCache() != null) ? mFilePreview.getFileCache() : null;
        String original = (mFilePreview.getOriginal() != null) ? mFilePreview.getOriginal() : null;
        String thumb = (mFilePreview.getVersions() != null) ? mFilePreview.getVersions().getThumb() : null;
        mBus.post(new CreateNewFeedEvent(
                mFeedableType,
                mInterest.getId(),
                content,
                fileCache,
                original,
                thumb));
        mFilePreview = new FilePreview();
    }

    public void addNewCommentToEvent(int eventId) {

        mEventId = eventId;

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View v = layoutInflater.inflate(R.layout.feed_new_dialog, null);
        TextInputLayout textInputLayout =
                (TextInputLayout) v.findViewById(R.id.newFeedTextInputLayout);
        textInputLayout.setHint(mResources.getString(R.string.new_post));
        final EditText newFeedEditText = (EditText) v.findViewById(R.id.newFeedEditText);
        final ImageButton addFileButton = (ImageButton) v.findViewById((R.id.addFileButton));
        mAttachment = (ImageView) v.findViewById(R.id.photoAttachment);
        mRemoveImageButton = (ImageButton) v.findViewById(R.id.removeimage);
        mRemoveImageButton.setVisibility(View.GONE);
        mProgressBar = (ProgressBar) v.findViewById(R.id.fileProgress);

        addFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListNoTitle();
            }
        });

        newCommentDialog(v, newFeedEditText);
    }

        private void newCommentDialog(View view, final EditText editText) {
            new AlertDialog.Builder(this)
                    .setView(view)
                    .setPositiveButton(mResources.getString(R.string.post), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String content = editText.getText().toString().trim();
                            if (!content.isEmpty()) {
                                createNewComment(content);
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                .show();
    }

    private void createNewComment(String content) {
        String fileCache = (mFilePreview.getFileCache() != null) ? mFilePreview.getFileCache() : null;
        String original = (mFilePreview.getOriginal() != null) ? mFilePreview.getOriginal() : null;
        String thumb = (mFilePreview.getVersions() != null) ? mFilePreview.getVersions().getThumb() : null;
        mBus.post(new CreateNewFeedEvent(
                mCommentableType,
                mEventId,
                content,
                fileCache,
                original,
                thumb));
        mFilePreview = new FilePreview();
    }

    private void showErrorDialog() {
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.empty_post_error))
                .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

}
