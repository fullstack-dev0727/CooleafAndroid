package zkhaider.com.cooleaf.ui.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import zkhaider.com.cooleaf.R;
import zkhaider.com.cooleaf.mvp.registrations.presenters.RegisterPresenter;

/**
 * Created by Haider on 12/23/2014.
 */
public class SignUpFragment extends Fragment {

    public static final String TAG = SignUpFragment.class.getSimpleName();

    public static SignUpFragment mSignUpFragment;

    @InjectView(R.id.signUpLayout)
    LinearLayout mTouchInterceptor;

    @InjectView(R.id.signUpButton)
    Button signUpButton;

    @InjectView(R.id.termsAndAgreement)
    TextView mTermsTextView;

    private MaterialEditText emailEditText;

    private RegisterPresenter mRegisterPresenter;

    public static SignUpFragment newInstance() {
        if (mSignUpFragment == null)
            mSignUpFragment = new SignUpFragment();
        return mSignUpFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_signup, null);
        ButterKnife.inject(this, root);

        initPresenter();
        emailEditText = (MaterialEditText) root.findViewById(R.id.emailEditText);
        initTerms();
        initTouchIntercetor();

        return root;
    }

    private void initPresenter() {
        mRegisterPresenter = new RegisterPresenter();
    }

    private void initTerms() {

        SpannableString ss = new SpannableString(getResources().getString(R.string.termsAndAgreement));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                new AlertDialog.Builder(getActivity())
                        .setView(LayoutInflater.from(getActivity()).inflate(R.layout.dialog_terms_of_service, null))
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Simple ok
                            }
                        })
                        .show();
            }
        };
        ss.setSpan(clickableSpan, 29, 45, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTermsTextView.setText(ss);
        mTermsTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void initTouchIntercetor() {
        mTouchInterceptor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (emailEditText.isFocused()) {
                        Rect outRect = new Rect();
                        emailEditText.getGlobalVisibleRect(outRect);
                        if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                            emailEditText.clearFocus();
                            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    }
                }
                return false;
            }
        });
    }

    @OnClick(R.id.signUpButton)
    public void performRegistrationCheck() {
        String email = emailEditText.getText().toString().trim();
        if (email.isEmpty())
            showAlert();
        else
            mRegisterPresenter.performRegisterationCheck(email);
    }

    private void showAlert() {
        // Create dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.login_error)
                .setTitle(R.string.login_error_title)
                .setPositiveButton(android.R.string.ok, null);
        //Create Alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
