package zkhaider.com.cooleaf.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit.mime.TypedFile;
import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.cooleafapi.entities.Edit;
import zkhaider.com.cooleaf.cooleafapi.entities.FilePreview;
import zkhaider.com.cooleaf.cooleafapi.entities.Interest;
import zkhaider.com.cooleaf.cooleafapi.entities.ParentTag;
import zkhaider.com.cooleaf.cooleafapi.entities.Tag;
import zkhaider.com.cooleaf.cooleafapi.entities.User;
import zkhaider.com.cooleaf.mvp.users.events.LoadEditEvent;
import zkhaider.com.cooleaf.ui.events.LoadParentTagEvent;
import zkhaider.com.cooleaf.mvp.users.events.LoadedMeEvent;
import zkhaider.com.cooleaf.mvp.filepreviews.events.LoadUploadFileEvent;
import zkhaider.com.cooleaf.mvp.filepreviews.events.LoadedUploadFileEvent;
import zkhaider.com.cooleaf.ui.fragments.AddStructuresFragment;
import zkhaider.com.cooleaf.ui.helpers.RotatePictureHelper;
import zkhaider.com.cooleaf.utils.UriHelper;
import zkhaider.com.cooleaf.ui.roundedpicture.CircleTransform;
import zkhaider.com.cooleaf.utils.PixelToDP;
import zkhaider.com.cooleaf.ui.helpers.StructureUIHelper;

/**
 * @author ZkHaider
 *         Created by Haider on 12/24/2014.
 */
public class EditProfileActivity extends FailureActivity {

    public static final String TAG = EditProfileActivity.class.getSimpleName();

    private ImageButton profileUpload;
    private MaterialEditText mUsernameEditText;
    private MaterialEditText mPasswordEditText;
    private LinearLayout mLinearLayout;
    private LinearLayout touchInterceptor;
    private List<ParentTag> parentTags = new ArrayList<>();
    private List<List<Tag>> tagLists = new ArrayList<>();
    private FrameLayout[] frameLayouts;
    private AddStructuresFragment[] structureFragments;
    private LinearLayout[] linearLayouts;
    private int[] frameLayoutIds;
    private int[] relativeLayoutIds;
    private ProgressDialog mProgressDialog;

    /*
    Tracking
     */
    private static final int TAKE_PHOTO_REQUEST = 1;
    private static final int GET_FROM_GALLERY = 2;

    private Edit mEdit = new Edit();
    private User mUser;

    private Uri imageUri;
    private String mCurrentPhotoPath;
    private File imageFile;
    private TypedFile photo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_information);

        initUserBundle();
        initActionBar();
        initViews();
        initData();
        initViewSizes();
        initCreateViews();
        initFragments();
        initRelativeLayouts();
        initListeners();

    }

    private void initUserBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String userJSON = bundle.getString("user");
            Gson gson = new Gson();
            mUser = gson.fromJson(userJSON, User.class);

            mEdit.setProfile(mUser.getProfile());
            mEdit.setRole(mUser.getRole());

        }
    }

    private void initActionBar() {
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);
        actionBar.setTitle(R.string.edit_profile);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.show();

        final View activityRootView = findViewById(R.id.rootViewBasicInfo);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                // rect will be populated with coordinates of your view area that is visible
                activityRootView.getWindowVisibleDisplayFrame(rect);
                int heightDiff = activityRootView.getRootView().getHeight() - (rect.bottom - rect.top);
                if (heightDiff > 250) {
                    // If more than 100 pixels it is probably a keyboard therefore hide actionbar
                    actionBar.hide();
                } else {
                    actionBar.show();
                }
            }
        });
    }

    private void initViews() {
        profileUpload = (ImageButton) findViewById(R.id.profileUpload);
        mUsernameEditText = (MaterialEditText) findViewById(R.id.usernameEditText);
        mUsernameEditText.setText(mUser.getName());
        mPasswordEditText = (MaterialEditText) findViewById(R.id.basicInfoPasswordEdit);
        mPasswordEditText.setVisibility(View.GONE);
        mLinearLayout = (LinearLayout) findViewById(R.id.basicInfoDynamicLinearLayout);
        touchInterceptor = (LinearLayout) findViewById(R.id.rootViewBasicInfo);
    }


    private void initData() {
        if (mUser != null) {
            parentTags = mUser.getRole().getOrganization().getParentTags();
        }
    }

    private void initViewSizes() {
        frameLayouts = new FrameLayout[parentTags.size()];
        frameLayoutIds = new int[frameLayouts.length];
        linearLayouts = new LinearLayout[parentTags.size()];
        relativeLayoutIds = new int[linearLayouts.length];
    }

    private void initCreateViews() {

        for (int i = 0; i < frameLayouts.length; i++) {
            // Create FrameLayouts and add parameters
            frameLayouts[i] = new FrameLayout(this);
            FrameLayout.LayoutParams frameLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            frameLayouts[i].setLayoutParams(frameLayoutParams);

            // Set id tags
            frameLayouts[i].setId(i + 1);
            frameLayoutIds[i] = i;

            // Create RelativeLayouts and add parameters
            linearLayouts[i] = new LinearLayout(this);
            linearLayouts[i].setOrientation(LinearLayout.VERTICAL);
            RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            relativeLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

            // Convert 16 pixels to DP
            PixelToDP pixelToDP = new PixelToDP(this, 16);
            linearLayouts[i].setPadding(pixelToDP.getDp(), 0, pixelToDP.getDp(), 0);
            linearLayouts[i].setLayoutParams(relativeLayoutParams);

            // Set id tags
            linearLayouts[i].setId(i + 1);
            relativeLayoutIds[i] = i;

            // Add views
            mLinearLayout.addView(frameLayouts[i]);
            mLinearLayout.addView(linearLayouts[i]);
        }

    }

    private void initFragments() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        HashMap<Integer, List<Tag>> myHashMap = mUser.getRole().createMyTags();

        structureFragments = new AddStructuresFragment[parentTags.size()];

        for (int i = 0; i < structureFragments.length; i++) {
            structureFragments[i] = new AddStructuresFragment();
            structureFragments[i].setArguments(this,
                    parentTags.get(i),
                    parentTags.get(i).getRequired(),
                    myHashMap.get(parentTags.get(i).getId()));
            ft.add(frameLayouts[i].getId(), structureFragments[i]);
        }

        ft.commit();
    }

    private void initRelativeLayouts() {

        HashMap<Integer, List<Tag>> tagHash = mUser.getRole().createMyTags();
        for (int i = 0; i < parentTags.size(); i++) {
            int parentId = parentTags.get(i).getId();
            List<Tag> tagList = tagHash.get(parentId);
            StructureUIHelper structureUIHelper = new StructureUIHelper(this, tagList,
                    linearLayouts[i]);
            structureUIHelper.initStructures();
        }
    }

    @Subscribe
    public void onLoadTagIdEvent(LoadParentTagEvent loadParentTagEvent) {
        // Get the parentTag from the event
        ParentTag parentTag = loadParentTagEvent.getParentId();

        // Get the id in string format
        String stringId = Integer.toString(parentTag.getId());

        // Get our current structures that haven't changed yet
        HashMap<String, List<Integer>> hashMap = mUser.getRole().getStructureMap();

        // If the hashmap does not contain this key initialize a new arraylist and store that
        if (!hashMap.containsKey(stringId)) {
            List<Integer> newList = new ArrayList<>();
            hashMap.put(stringId, newList);
        }

        // Get the integerTags from the hashmap
        List<Integer> integerTags = hashMap.get(stringId);

        // Get all sub tags from parentTag
        List<Tag> tags = parentTag.getAllTags();
        for (int i = 0; i < tags.size(); i++) {
            /*
             Check if you are currently active for the tags from AddStructuresFragment
             and adjust accordingly
              */
            int id = tags.get(i).getId();
            boolean active = tags.get(i).getActive();

            /*
             If our current integer tag id list did not contain this tag's id and this
             tag is active now, add it to the integerTag list
              */
            if (!integerTags.contains(id) && active) {
                integerTags.add(id);
            }
            /*
            Else if the integerTags list does contain the id and it now not active, remove
            it
             */
            else if (integerTags.contains(id) && !active) {
                int index = integerTags.indexOf(id);
                integerTags.remove(index);
            }
        }

        // Set the Structures UI
        StructureUIHelper structureUIHelper;
        int id = parentTag.getId();
        int counter = 0;
        for (ParentTag tempParentTag : parentTags) {
            int tempId = tempParentTag.getId();
            if (id == tempId) {
                structureUIHelper = new StructureUIHelper(this, parentTag.getAllTags(), linearLayouts[counter]);
                structureUIHelper.initStructures();
            }
            counter++;
        }

        if (!hashMap.containsKey(stringId)) {
            hashMap.put(Integer.toString(id), integerTags);
        } else if (hashMap.containsKey(Integer.toString(id))) {
            hashMap.remove(Integer.toString(id));
            hashMap.put(Integer.toString(id), integerTags);
        }

        mEdit.getRole().setStructureMap(hashMap);
    }

    private void initListeners() {
        profileUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListNoTitle();
            }
        });

        touchInterceptor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (mUsernameEditText.isFocused()) {
                        Rect outRect = new Rect();
                        mPasswordEditText.getGlobalVisibleRect(outRect);
                        if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                            mPasswordEditText.clearFocus();
                            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    }
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (mPasswordEditText.isFocused()) {
                        Rect outRect = new Rect();
                        mPasswordEditText.getGlobalVisibleRect(outRect);
                        if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                            mPasswordEditText.clearFocus();
                            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    }
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.doneEdit:

                String username = mUsernameEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();

                username.trim();
                password.trim();

                mEdit.setName(username);

                setCategoryIds();

                mBus.post(new LoadEditEvent(mEdit));
                startProgressDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

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
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        imageUri = Uri.fromFile(image);

        return image;
    }

    /*
    Take picture from your camera
     */
    private void dispatchTakePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Make sure that there is a camera activity to handle the intent
        if (intent.resolveActivity(getPackageManager()) != null) {

            // Create the File where the mTypedFile would go
            File picFile = null;

            try {
                picFile = createImageFile();
                imageFile = picFile;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_REQUEST && resultCode == RESULT_OK) {
            // Go ahead and post the event onto the Server
            photo = new TypedFile("image/*", imageFile);
            RotatePictureHelper.rotatePicture(imageFile, EditProfileActivity.this, profileUpload);
            mBus.post(new LoadUploadFileEvent(photo));
            startProgressDialog();
        } else if (requestCode == GET_FROM_GALLERY && resultCode == RESULT_OK) {
            imageUri = data.getData();
            photo = UriHelper.handleUri(imageUri, this);
            imageFile  = photo.file();
            mBus.post(new LoadUploadFileEvent(photo));
            startProgressDialog();
        } else if (resultCode != Activity.RESULT_CANCELED) {
            Toast.makeText(this, R.string.generalError, Toast.LENGTH_LONG).show();
        }
    }

    /*
    Set Category Ids to mEdit
     */
    private void setCategoryIds() {
        List<Integer> categoryIds = new ArrayList<>();
        List<Interest> interests = mUser.getCategories();
        for (int i = 0; i < interests.size(); i++) {
            categoryIds.add(interests.get(i).getId());
        }

        Collections.sort(categoryIds);

        mEdit.setCategoryIds(categoryIds);
    }

    /*
    Material Dialog list with no title
     */
    private void showListNoTitle() {
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
                .show();
    }

    private void aspectBitmap() {

        int viewWidth = profileUpload.getMeasuredWidth();
        int viewHeight = profileUpload.getMeasuredHeight();

        if (imageUri != null) {
            Picasso.with(EditProfileActivity.this)
                    .load(imageUri)
                    .transform(new CircleTransform())
                    .resize(viewWidth, viewHeight)
                    .centerCrop()
                    .into(profileUpload);
        } else {
            Toast.makeText(this, "Image Uri is null", Toast.LENGTH_LONG).show();
        }
    }

    /*
    Take a mTypedFile from your gallery
     */
    private void dispatchUploadFromGallery() {
        // Launch gallery intent
        startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore
                .Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
    }

    @Override
    public void onResume() {
        super.onResume();

        int measuredHeight = 0;
        int measuredWidth = 0;

        if (profileUpload.getDrawable() != null) {
            measuredHeight = profileUpload.getDrawable().getIntrinsicHeight();
            measuredWidth = profileUpload.getDrawable().getIntrinsicWidth();

            if (mUser != null) {
                String imgUrl = mUser.getProfile().getPicture().getVersions().getLargeURL();

                Picasso.with(EditProfileActivity.this)
                        .load(imgUrl)
                        .transform(new CircleTransform())
                        .resize(measuredWidth, measuredHeight)
                        .centerCrop()
                        .into(profileUpload);
            }
        }

    }

    @Subscribe
    public void onLoadedUploadProfilePictureEvent(LoadedUploadFileEvent loadedUploadFileEvent) {
        aspectBitmap();
        stopProgressDialog();
        FilePreview filePreview = loadedUploadFileEvent.getFilePreview();
        mEdit.setFileCache(filePreview.getFileCache());
    }

    @Subscribe
    public void onLoadedMeEvent(LoadedMeEvent loadedMeEvent) {
        stopProgressDialog();
        User user = loadedMeEvent.getUser();

        Intent i = new Intent(EditProfileActivity.this, MyProfileActivity.class);

        Bundle bundle = new Bundle();
        Gson gson = new Gson();
        bundle.putString("user", gson.toJson(user));
        i.putExtras(bundle);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    private void startProgressDialog() {
        mProgressDialog = new ProgressDialog(EditProfileActivity.this);
        mProgressDialog.setMessage("Updating...");
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();
    }

    private void stopProgressDialog() {
        mProgressDialog.dismiss();
    }

}