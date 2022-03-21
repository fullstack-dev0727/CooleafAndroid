package zkhaider.com.cooleaf.ui.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.squareup.picasso.Picasso;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.ui.TouchView.TouchImageView;

/**
 * Created by Petar Vasilev on 10/4/2015.
 */
public class ImagePreviewActivity extends Activity {

    public static final String TAG = ImagePreviewActivity.class.getSimpleName();
    private String mImageUrl;
    private CircularProgressView circularProgressView = null;
    private TouchImageView previewImageView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_image_preview);
        initView();
        initData();

    }
    public void initView() {
        circularProgressView = (CircularProgressView) findViewById(R.id.progress_view);
        previewImageView = (TouchImageView) findViewById(R.id.previewImage);
    }
    public void initData() {
        mImageUrl = getIntent().getStringExtra("imageurl");

        circularProgressView.setVisibility(View.VISIBLE);
        LoadImageTask loadImageTask = new LoadImageTask();
        loadImageTask.execute(mImageUrl);
    }

    private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {

            try {
                Bitmap bitmap = Picasso.with(ImagePreviewActivity.this).load(params[0]).get();
                return bitmap;
            } catch (Exception e){}
            return null;
        }
        @Override
        protected void onPreExecute() {

        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled())
                return;
            circularProgressView.setVisibility(View.GONE);
            previewImageView.setImageBitmap(bitmap);

        }
    }
}
