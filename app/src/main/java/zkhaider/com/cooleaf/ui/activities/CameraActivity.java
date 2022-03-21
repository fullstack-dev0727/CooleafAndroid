package zkhaider.com.cooleaf.ui.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit.mime.TypedFile;
import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.cooleafapi.entities.FilePreview;
import zkhaider.com.cooleaf.utils.UriHelper;

/**
 * Created by ZkHaider on 7/13/15.
 */
public abstract class CameraActivity extends FailureActivity {

    private static final String TAG = CameraActivity.class.getSimpleName();

    /*
    Tracking
     */
    protected static final int TAKE_PHOTO_REQUEST = 1;
    protected static final int GET_FROM_GALLERY = 2;

    protected RelativeLayout mImageContainer;
    protected ImageView mAttachment;
    protected ImageButton mRemoveImageButton;
    protected ProgressBar mProgressBar;
    protected Resources mResources;

    protected File mFile;
    protected Uri mUri;
    protected String mCurrentPath;
    protected TypedFile mTypedFile;
    protected FilePreview mFilePreview = new FilePreview();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mResources = getResources();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case TAKE_PHOTO_REQUEST:
                startProgress();
                if (resultCode == RESULT_OK) {
                    mTypedFile = new TypedFile("image/*", mFile);
                }
                break;
            case GET_FROM_GALLERY:
                startProgress();
                if (resultCode == RESULT_OK) {
                    mUri = data.getData();
                    mTypedFile = UriHelper.handleUri(mUri, this);
                }
                break;
            default:
                if (mProgressBar != null)
                    stopProgress();
                break;
        }
    }

    /********************************************************************************************
     *  Initialization Methods
     ********************************************************************************************/

    private void initRemoveImageListener() {
        mRemoveImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearImageView();
            }
        });
    }

    /********************************************************************************************
     *  Helper Methods
     ********************************************************************************************/

    /***
     *  Show Alert Dialog for selection of photos
     */
    protected void showListNoTitle() {
        new AlertDialog.Builder(this)
                .setItems(R.array.pictureOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                dispatchTakePicture();
                                break;
                            case 1:
                                dispatchUploadFromGallery();
                                break;
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

    /***
     *  Take picture from your camera
     */
    private void dispatchTakePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Make sure that there is a camera activity to handle the intent
        if (intent.resolveActivity(getPackageManager()) != null) {

            // Create the File where the mTypedFile would go
            File picFile = null;

            try {
                picFile = createImageFile();
                mFile = picFile;
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

            // Continue only if the file was successfully created
            if (picFile != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picFile));
                startActivityForResult(intent, TAKE_PHOTO_REQUEST);
            }
        }
    }

    /****
     *  Take a mTypedFile from your gallery
     */
    private void dispatchUploadFromGallery() {
        // Launch gallery intent
        startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore
                .Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
    }

    /***
     *  Create an image file
     */
    private File createImageFile() throws IOException {
        // Create the Image File name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName, // Prefix
                ".jpg", // Suffix
                storageDir // Directory
        );

        // Save the file, path for ACTION_VIEW intents
        mCurrentPath = "file:" + image.getAbsolutePath();
        mUri = Uri.fromFile(image);

        return image;
    }

    protected void aspectBitmap() {

        stopProgress();
        mAttachment.getLayoutParams().height = mResources.getDimensionPixelSize(R.dimen.inner_box_height);

        mAttachment.setVisibility(View.VISIBLE);
        mRemoveImageButton.setVisibility(View.VISIBLE);

        if (mUri != null) {
            Picasso.with(CameraActivity.this)
                    .load(mUri)
                    .fit()
                    .centerCrop()
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .into(mAttachment, new Callback() {
                        @Override
                        public void onSuccess() {
                            mAttachment.setVisibility(View.VISIBLE);
                            mRemoveImageButton.setVisibility(View.VISIBLE);
                            initRemoveImageListener();
                        }

                        @Override
                        public void onError() {

                        }
                    });

        } else {
            Toast.makeText(this, "Image Uri is null", Toast.LENGTH_LONG).show();
        }
    }

    protected void clearCache() {
        mFile = null;
        mUri = null;
        mCurrentPath = null;
        mTypedFile = null;
    }

    private void clearImageView() {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < 16) {
            mAttachment.setBackgroundDrawable(null);
            mFilePreview = new FilePreview();
            clearCache();
            hideContainer();
        } else {
            mAttachment.setBackground(null);
            mFilePreview = new FilePreview();
            clearCache();
            hideContainer();
        }
    }

    protected void showContainer() {
        mImageContainer.setVisibility(View.VISIBLE);
    }

    protected void hideContainer() {
        mAttachment.setVisibility(View.GONE);
        mRemoveImageButton.setVisibility(View.GONE);
        if (mImageContainer != null)
            mImageContainer.setVisibility(View.GONE);
    }

    protected void startProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    protected void stopProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

}
