package zkhaider.com.cooleaf.ui.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by Valerie on 5/27/2015.
 */
public final class RotatePictureHelper {
    private static RotatePictureHelper instance;
    public static final String TAG = RotatePictureHelper.class.getSimpleName();

    private RotatePictureHelper() {

    }

    public static RotatePictureHelper getInstance() {
        if (instance == null) {
            instance = new RotatePictureHelper();
        }
        return instance;
    }

    public static void rotatePicture(File imageFile, Context context, ImageView imageView) {
        //finds orientation of mTypedFile
        int rotation = 0;
        try {
            ExifInterface exif = new ExifInterface(imageFile.getPath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch(orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90: rotation = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180: rotation = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270: rotation = 270;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Matrix matrix = new Matrix();
        matrix.postRotate(rotation);
        BitmapFactory.Options options = new BitmapFactory.Options();
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        //sets to just check the dimensions of the image before using memory with the image
        options.inJustDecodeBounds = true;
        try {
            Bitmap bm = BitmapFactory.decodeStream(new FileInputStream(imageFile), null, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //reduces memory cost of the image by scaling down the image at load time to close to the size of the ImageView
        options.inSampleSize = calculateInSampleSize(options, imageView.getMeasuredWidth(), imageView.getMeasuredHeight());
        //switches to actually decoding the picture instead of just getting the dimensions
        options.inJustDecodeBounds = false;
        options.inScaled = true;
        //sets the density of the image to the density of the device's screen so there is no unnecessary refinement
        options.inScreenDensity = metrics.densityDpi;
        options.inTargetDensity =  metrics.densityDpi;
        options.inDensity = DisplayMetrics.DENSITY_DEFAULT;
        try {
            Bitmap bm = BitmapFactory.decodeStream(new FileInputStream(imageFile), null, options);
            bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
            OutputStream out = new BufferedOutputStream(new FileOutputStream(imageFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            bm.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Calculates the best sample size to use to not get an OutOfMemory error.
     *
     * @param options Options object to get bitmap dimensions from
     * @param reqWidth Goal width for the bitmap
     * @param reqHeight Goal Height for the bitmap
     * @return int factor to use for the sample size
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

}
