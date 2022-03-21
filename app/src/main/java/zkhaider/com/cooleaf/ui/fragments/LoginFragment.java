package zkhaider.com.cooleaf.ui.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.cooleafapi.utils.CooleafSessionManager;
import zkhaider.com.cooleaf.mvp.authentication.presenters.AuthenticationPresenter;
import zkhaider.com.cooleaf.utils.NetworkHelper;

/**
 * Created by Haider on 12/23/2014.
 */
public class LoginFragment extends Fragment {

    private static String TAG = LoginFragment.class.getSimpleName();

    public static LoginFragment fragment;

    @InjectView(R.id.loginButton)
    Button loginButton;

    @InjectView(R.id.loginLayout)
    LinearLayout mTouchInterceptor;

    private MaterialEditText[] editTexts;

    private AuthenticationPresenter mAuthenticationPresenter;

    public static Fragment newInstance() {
        if (fragment == null)
            fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        CooleafSessionManager.clearSession(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, null);
        ButterKnife.inject(this, root);
        initPresenter();
        initTouchInterceptor();
        initEditTexts(root);
        return root;
    }

    private void initPresenter() {
        mAuthenticationPresenter = new AuthenticationPresenter();
    }

    private void initTouchInterceptor() {
        mTouchInterceptor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (editTexts[0].isFocused()) {
                        Rect outRect = new Rect();
                        editTexts[0].getGlobalVisibleRect(outRect);
                        if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                            editTexts[0].clearFocus();
                            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    } else if (editTexts[1].isFocused()) {
                        Rect outRect = new Rect();
                        editTexts[1].getGlobalVisibleRect(outRect);
                        if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                            editTexts[1].clearFocus();
                            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    }
                }
                return false;
            }
        });

    }

    private void initEditTexts(View root) {
        editTexts = new MaterialEditText[2];
        editTexts[0] = (MaterialEditText) root.findViewById(R.id.loginUsernameEditText);
        editTexts[1] = (MaterialEditText) root.findViewById(R.id.loginPassworkEditText);
    }

    @OnClick(R.id.loginButton)
    public void performLogin() {
        boolean isConnected = NetworkHelper.isInternetAvailable(getActivity());
        if (isConnected) {
            String email = editTexts[0].getText().toString().trim();
            String password = editTexts[1].getText().toString().trim();
            if (email.isEmpty() || password.isEmpty())
                showEmptyError();
            else
                mAuthenticationPresenter.performAuthentication(email, password);
        } else {
            showNetworkError();
        }
    }

    private void showEmptyError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.login_error)
                .setTitle(R.string.login_error_title)
                .setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showNetworkError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.network_error)
                .setTitle(R.string.network_error_title)
                .setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
