package com.android.muilat.journally;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class SignInActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null){
            startActivity(SignInActivity.createIntent(this));

            finish();
            return;
        }

        signInUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            handleSignInResponse(resultCode, data);
            return;
        }
    }

    @MainThread
    private void handleSignInResponse(int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);
        Toast toast;
        // Successfully signed in
        if (resultCode == ResultCodes.OK) {
//            startActivity(MainActivity.createIntent(this, response));
            startActivity(MainActivity.createIntent(this));
            finish();
            return;
        } else {
            // Sign in failed
            if (response == null) {
                // User pressed back button
                toast = Toast.makeText(this, "Sign in was cancelled!", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                toast = Toast.makeText(this, "You have no internet connection", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                toast = Toast.makeText(this, "Unknown Error!", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
        }
        toast = Toast.makeText(this, "Unknown Error!", Toast.LENGTH_LONG);
        toast.show();
    }

    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, SignInActivity.class);
        return in;
    }

    public void signInUI(){
        List<AuthUI.IdpConfig> selectedProviders = new ArrayList<>();
        selectedProviders.add(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build());
        selectedProviders.add(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setIsSmartLockEnabled(true)
                        .setProviders(selectedProviders)
                        .build(),
                RC_SIGN_IN);
    }

    public void onSignInButtonClick(View view){
        signInUI();
    }


}
