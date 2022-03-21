package zkhaider.com.cooleaf.ui.activities;

import android.content.res.Resources;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.Menu;

import com.google.gson.Gson;

import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.cooleafapi.entities.Edit;
import zkhaider.com.cooleaf.ui.fragments.InterestsFragment;

/**
 * Created by Haider on 2/1/2015.
 */
public class InterestsActivity extends FailureActivity {

    private Edit mEdit = new Edit();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);

        initBundle();
        initActionBar();
        initInterestsFragement();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.interests_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(InterestsActivity.this, DashboardActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    private void initBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String editJSON = bundle.getString("edit");
            Gson gson = new Gson();
            mEdit = gson.fromJson(editJSON, Edit.class);
        }

    }

    private void initActionBar() {
        Resources resources = getResources();

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
            actionBar.setTitle(resources.getString(R.string.interestsTitle));
        }
    }

    private void initInterestsFragement() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        InterestsFragment fragment = new InterestsFragment();
        fragment.setEdit(mEdit);

        ft.replace(R.id.container, fragment);
        ft.commit();
    }

}
