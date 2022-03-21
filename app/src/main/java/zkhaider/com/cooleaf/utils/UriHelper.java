package zkhaider.com.cooleaf.utils;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import retrofit.mime.TypedFile;

/**
 * Created by Valerie on 6/24/2015.
 */
public class UriHelper {

    public static final String TAG = UriHelper.class.getSimpleName();
    private static final int END_OF_DATA = -1;

    public static TypedFile handleUri(Uri imageUri, Activity activity) {
        TypedFile photo = null;

        // Go ahead and post the event onto the server
        String selectedImagePath = null;
        boolean isCloudPhoto = false;
        // If User has Google Photos configure Uri
        if (imageUri.toString().startsWith("content://com.google.android.apps.photos.content")) {
            String unusablePath = imageUri.getPath();

            if (unusablePath.contains("external/")) {
                int startIndex = unusablePath.indexOf("external/");
                int endIndex = unusablePath.indexOf("/ACTUAL");
                String embeddedPath = unusablePath.substring(startIndex, endIndex);

                Uri.Builder builder = imageUri.buildUpon()
                                        .path(embeddedPath)
                                        .authority("media");
                imageUri = builder.build();
                photo = new TypedFile("image/jpg", new File(embeddedPath));
            } else {
                isCloudPhoto = true;
                InputStream is = null;
                FileOutputStream fout = null;

                try {
                    is = activity.getContentResolver().openInputStream(imageUri);

                    File tempFile = File.createTempFile("tempFile", ".jpg");
                    tempFile.deleteOnExit();

                    fout = new FileOutputStream(tempFile);

                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = is.read(buf)) != END_OF_DATA) {
                        fout.write(buf, 0, len);
                    }
                    photo = new TypedFile("image/jpg", tempFile);
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fout != null) {
                            fout.close();
                        }
                    } catch (Exception e) {
                        Log.d(TAG, e.getMessage());
                    }
                }
            }
        }
        Uri selectedImageUri = imageUri;
        Cursor cursor = activity.getContentResolver().query(selectedImageUri, null, null, null, null);
        if (cursor == null) {
            selectedImagePath = selectedImageUri.getPath();
            photo = new TypedFile("image/jpg", new File(selectedImagePath));
        } else if (!isCloudPhoto){
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            selectedImagePath = cursor.getString(idx);
            photo = new TypedFile("image/jpg", new File(selectedImagePath));
        }
        return photo;
    }


}

