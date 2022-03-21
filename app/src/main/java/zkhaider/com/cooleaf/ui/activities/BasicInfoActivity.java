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
import android.widget.Toast;

import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit.mime.TypedFile;
import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.cooleafapi.entities.Edit;
import zkhaider.com.cooleaf.cooleafapi.entities.FilePreview;
import zkhaider.com.cooleaf.cooleafapi.entities.ParentTag;
import zkhaider.com.cooleaf.cooleafapi.entities.Registration;
import zkhaider.com.cooleaf.cooleafapi.entities.RegistrationRequest;
import zkhaider.com.cooleaf.cooleafapi.entities.Tag;
import zkhaider.com.cooleaf.cooleafapi.entities.User;
import zkhaider.com.cooleaf.mvp.authentication.events.LoadAuthenticateEvent;
import zkhaider.com.cooleaf.ui.events.LoadParentTagEvent;
import zkhaider.com.cooleaf.mvp.registrations.events.LoadRegisterEvent;
import zkhaider.com.cooleaf.mvp.users.events.LoadedMeEvent;
import zkhaider.com.cooleaf.mvp.registrations.events.LoadedRegisterEvent;
import zkhaider.com.cooleaf.mvp.filepreviews.events.LoadUploadFileEvent;
import zkhaider.com.cooleaf.mvp.filepreviews.events.LoadedUploadFileEvent;
import zkhaider.com.cooleaf.ui.fragments.AddStructuresFragment;
import zkhaider.com.cooleaf.ui.helpers.RotatePictureHelper;
import zkhaider.com.cooleaf.ui.roundedpicture.CircleTransform;
import zkhaider.com.cooleaf.utils.PixelToDP;
import zkhaider.com.cooleaf.ui.helpers.StructureUIHelper;
import zkhaider.com.cooleaf.utils.UriHelper;

/**
 * @author ZkHaider
 *         Created by Haider on 12/24/2014.
 */

// TODO - Refactor this with CameraActivity
public class BasicInfoActivity extends FailureActivity {

    public static final String TAG = BasicInfoActivity.class.getSimpleName();

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

    private Registration registration;
    private RegistrationRequest registrationRequest;
    private Edit edit;

    private Uri imageUri;
    private String mCurrentPhotoPath;
    private File imageFile;
    private TypedFile photo;

    private List<Integer> integerTags = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_information);

        initRegistrationRequest();
        initBundleRegistration();
        initActionBar();
        initTagsOnRegstrationRequest();
        initViews();
        initData();
        initViewSizes();
        initCreateViews();
        initFragments();
        initLinearLayouts();
        initListeners();

    }

    private void initRegistrationRequest() {
        registrationRequest = new RegistrationRequest();
    }

    private void initBundleRegistration() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String registrationJSON = bundle.getString("registration");
            Gson gson = new Gson();
            registration = gson.fromJson(registrationJSON, Registration.class);
        }
    }

    private void initActionBar() {
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);
        actionBar.setTitle(R.string.sign_up);
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

    private void initTagsOnRegstrationRequest() {
        registrationRequest.setTagIds(registration.getCategoryIds());
    }

    private void initViews() {
        profileUpload = (ImageButton) findViewById(R.id.profileUpload);
        mUsernameEditText = (MaterialEditText) findViewById(R.id.usernameEditText);
        mUsernameEditText.setText(registration.getName());
        mPasswordEditText = (MaterialEditText) findViewById(R.id.basicInfoPasswordEdit);
        mLinearLayout = (LinearLayout) findViewById(R.id.basicInfoDynamicLinearLayout);
        touchInterceptor = (LinearLayout) findViewById(R.id.rootViewBasicInfo);
    }

    private void initData() {
        if (registration != null) {
            parentTags = registration.getParentTags();

            for (int i = 0; i < parentTags.size(); i++) {
                ParentTag parentTag = parentTags.get(i);
                List<Tag> tags = parentTag.getAllTags();
                tagLists.add(i, tags);
            }
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
            LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);

            // Convert 16 pixels to DP
            PixelToDP pixelToDP = new PixelToDP(this, 16);
            linearLayouts[i].setPadding(pixelToDP.getDp(), 0, pixelToDP.getDp(), 0);
            linearLayouts[i].setLayoutParams(linearLayoutParams);

            // Set id tags
            linearLayouts[i].setId(i);
            relativeLayoutIds[i] = i;

            // Add views
            mLinearLayout.addView(frameLayouts[i]);
            mLinearLayout.addView(linearLayouts[i]);
        }

    }

    private void initFragments() {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        HashMap<Integer, List<Tag>> myHashMap = registration.createMyTags();

        structureFragments = new AddStructuresFragment[parentTags.size()];

        for (int i = 0; i < structureFragments.length; i++) {
            structureFragments[i] = new AddStructuresFragment();
            structureFragments[i].setArguments(this, parentTags.get(i), parentTags.get(i).getRequired()
                    , myHashMap.get(parentTags.get(i).getId()));
            ft.add(frameLayouts[i].getId(), structureFragments[i]);
        }

        ft.commit();
    }

    private void initLinearLayouts() {

        HashMap<Integer, List<Tag>> tagHash = registration.createMyTags();
        for (int i = 0; i < parentTags.size(); i++) {
            int parentId = parentTags.get(i).getId();
            List<Tag> tagList = tagHash.get(parentId);
            StructureUIHelper structureUIHelper = new StructureUIHelper(this, tagList,
                    linearLayouts[i]);
            structureUIHelper.initStructures();
            linearLayouts[i].invalidate();
        }
    }

    @Subscribe
    public void onLoadTagIdEvent(LoadParentTagEvent loadTagIdEvent) {
        ParentTag parentTag = loadTagIdEvent.getParentId();
        List<Tag> tags = parentTag.getAllTags();
        for (int i = 0; i < tags.size(); i++) {

            int id = tags.get(i).getId();
            boolean active = tags.get(i).getActive();

            if (!integerTags.contains(id) && active) {
                integerTags.add(id);
            } else if (integerTags.contains(id) && !active) {
                int index = integerTags.indexOf(id);
                integerTags.remove(index);
            }
        }


        StructureUIHelper structureUIHelper;
        int id = parentTag.getId();
        int counter = 0;
        for (ParentTag tempParentTag : parentTags) {
            int tempId = tempParentTag.getId();
            if (id == tempId) {
                structureUIHelper = new StructureUIHelper(this, tags, linearLayouts[counter]);
                structureUIHelper.initStructures();
                linearLayouts[counter].invalidate();
            }
            counter++;
        }

        if (!integerTags.isEmpty()) {
            registrationRequest.setTagIds(integerTags);
        }
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
                Intent backIntent = new Intent(BasicInfoActivity.this, MainActivity.class);
                backIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(backIntent);
                this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                break;
            case R.id.next_action:

                String username = mUsernameEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();

                username.trim();
                password.trim();

                registrationRequest.setGender(registration.getGender());
                registrationRequest.setToken(registration.getToken());

                if (registrationRequest.getTags().isEmpty()) {
                    // Create dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(R.string.check_tags)
                            .setPositiveButton(android.R.string.ok, null);
                    //Create Alert dialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    break;
                }

                if (username.isEmpty() ||
                        password.isEmpty() ||
                        registrationRequest.getToken().isEmpty()
                        ) {
                    // Create dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(R.string.registration_fields_check)
                            .setPositiveButton(android.R.string.ok, null);
                    //Create Alert dialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    break;
                } else {
                    // Send the registration object
                    registrationRequest.setName(username);
                    registrationRequest.setPassword(password);
                    mBus.post(new LoadRegisterEvent(registrationRequest));
                    break;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.next_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /***
     *  TODO -- Move CreateImageFile and DispatchTakePicture to a helper class
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
            //TODO - Create any API Calls here

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
            aspectBitmap();
            // Go ahead and post the event onto the Server
            photo = new TypedFile("image/*", imageFile);
            RotatePictureHelper.rotatePicture(imageFile, BasicInfoActivity.this, profileUpload);

        } else if (requestCode == GET_FROM_GALLERY && resultCode == RESULT_OK) {
            imageUri = data.getData();
            aspectBitmap();
            photo = UriHelper.handleUri(imageUri, this);
            imageFile = photo.file();
        } else if (resultCode != Activity.RESULT_CANCELED) {
            Toast.makeText(this, R.string.generalError, Toast.LENGTH_LONG).show();
        }
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
            Picasso.with(BasicInfoActivity.this)
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
    }

    @Subscribe
    public void onLoadedRegisterEvent(LoadedRegisterEvent loadedRegisterEvent) {
        User user = loadedRegisterEvent.getUser();
        mBus.post(new LoadAuthenticateEvent(user.getEmail(), registrationRequest.getPassword()));
    }

    @Subscribe
    public void onLoadedMeEvent(LoadedMeEvent loadMeEvent) {

        User user = loadMeEvent.getUser();

        edit = new Edit();

        edit.setId(user.getId());
        edit.setName(user.getName());
        edit.setEmail(user.getEmail());
        edit.setRole(user.getRole());
        edit.setProfile(user.getProfile());

        if (photo != null) {
            mBus.post(new LoadUploadFileEvent(photo));
            startProgressDialog();
        } else if (photo == null) {
            Intent i = new Intent(BasicInfoActivity.this, InterestsActivity.class);
            Bundle bundle = new Bundle();
            Gson gson = new Gson();
            bundle.putString("edit", gson.toJson(edit));
            i.putExtras(bundle);

            startActivity(i);
            this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        }
    }

    @Subscribe
    public void onLoadedUploadProfilePictureEvent(LoadedUploadFileEvent loadedUploadFileEvent) {
        stopProgressDialog();
        FilePreview filePreview = loadedUploadFileEvent.getFilePreview();
        edit.setFileCache(filePreview.getFileCache());

        Intent i = new Intent(BasicInfoActivity.this, InterestsActivity.class);
        Bundle bundle = new Bundle();
        Gson gson = new Gson();
        bundle.putString("edit", gson.toJson(edit));
        i.putExtras(bundle);

        // Go ahead and Flag the activity and clear the top and flag a new task, to prevent
        // the user from going back from the interests back
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(i);
        this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    private void startProgressDialog() {
        mProgressDialog = new ProgressDialog(BasicInfoActivity.this);
        mProgressDialog.setMessage("Updating...");
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();
    }

    private void stopProgressDialog() {
        mProgressDialog.dismiss();
    }


}