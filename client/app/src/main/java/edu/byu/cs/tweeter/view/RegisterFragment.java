package edu.byu.cs.tweeter.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.presenter.RegisterPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.RegisterTask;
import edu.byu.cs.tweeter.view.main.MainActivity;

public class RegisterFragment extends Fragment implements RegisterPresenter.View, RegisterTask.Observer {

    private static final String LOG_TAG = "RegisterFragment";

    private Toast registerToast;

    private RegisterPresenter presenter;

    private RegisterRequest registerRequest;

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        presenter = new RegisterPresenter(this);

        View view = inflater.inflate(R.layout.fragment_register, container, false);

        registerRequest = new RegisterRequest(null, null, null, null, null);

        EditText firstNameField = (EditText) view.findViewById(R.id.firstNameField);
        firstNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                registerRequest.setFirstName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // do nothing
            }
        });

        EditText lastNameField = (EditText) view.findViewById(R.id.lastNameField);
        lastNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                registerRequest.setLastName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // do nothing
            }
        });

        EditText userNameField = (EditText) view.findViewById(R.id.userNameField);
        userNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                registerRequest.setUsername(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // do nothing
            }
        });

        EditText passwordField = (EditText) view.findViewById(R.id.passwordField);
        passwordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                registerRequest.setPassword(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // do nothing
            }
        });

        TextView takePictureTextView = view.findViewById(R.id.takeProfilePicture);
        SpannableString spannableString = new SpannableString(takePictureTextView.getText());

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Toast.makeText(getContext(), "Clicked Take Profile Picture", Toast.LENGTH_LONG).show();
            }
        };
        spannableString.setSpan(clickableSpan, 0,takePictureTextView.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        takePictureTextView.setText(spannableString);
        takePictureTextView.setMovementMethod(LinkMovementMethod.getInstance());

        Button registerButton = view.findViewById(R.id.RegisterButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerToast = Toast.makeText(getContext(), "Logging In", Toast.LENGTH_LONG);
                registerToast.show();

                // It doesn't matter what values we put here. We will be logged in with a hard-coded dummy user.
                RegisterTask registerTask = new RegisterTask(presenter, RegisterFragment.this);
                registerTask.execute(registerRequest);
            }
        });

        return view;
    }

    @Override
    public void registerSuccessful(RegisterResponse registerResponse) {
        Intent intent = new Intent(getContext(), MainActivity.class);

        intent.putExtra(MainActivity.CURRENT_USER_KEY, registerResponse.getUser());
        intent.putExtra(MainActivity.AUTH_TOKEN_KEY, registerResponse.getAuthToken());

        registerToast.cancel();
        startActivity(intent);
    }

    @Override
    public void registerUnsuccessful(RegisterResponse registerResponse) {
        Toast.makeText(getContext(), "Failed to register. " + registerResponse.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleException(Exception exception) {
        Log.e(LOG_TAG, exception.getMessage(), exception);
        Toast.makeText(getContext(), "Failed to login because of exception: " + exception.getMessage(), Toast.LENGTH_LONG).show();
    }
}
