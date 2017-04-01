package com.example.savch.wpamproj;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import butterknife.ButterKnife;
import butterknife.BindView;

public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener{
    private static final String TAG = "LoginActivity";
    final String LOG_TAG = "myLogs";
    private static final int REQUEST_SIGNUP = 0;
    MySQLAdapter dbHelper;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG1 = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private TextView mStatusTextView;
    private ProgressDialog mConnectionProgressDialog;

    private String personName;
    private String personEmail;

    private boolean gogSignIn = false;


    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.link_signup) TextView _signupLink;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //Butterknife error !!!
        SignInButton _signupGoogButton = (SignInButton) findViewById(R.id.btn_login_gog);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        //Google button listener
        _signupGoogButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mConnectionProgressDialog.show();
                signIn();
            }
        });


        //Login button listener
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });


        //Sign up link to SignupActivity listener
        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        dbHelper = new MySQLAdapter(this);
        mConnectionProgressDialog = new ProgressDialog(this);
        mConnectionProgressDialog.setMessage("Signing in...");
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    //login with Login Button
    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        // TODO: Check from SQLlite
                        if (userAutentification(email, password)){
                            onLoginSuccess();
                        }else{
                            onLoginFailed();
                            // onLoginFailed();
                        }
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            mConnectionProgressDialog.dismiss();

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

        final String email;
        final String password;
        final String name;

        mConnectionProgressDialog.show();
        name = personName;
        email = personEmail;
        password = "gogadmin";
        if (!ifUserExsist(email)) {
            dbHelper.openToWrite();
            long rowID = dbHelper.insert("name", "email", "password", name, email, password);
            Log.d(LOG_TAG, "row inserted, ID = " + rowID);
        }

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        // TODO: Check from SQLlite
                        if (userAutentification(email, password)){
                            onLoginSuccess();
                        }else{
                            onLoginFailed();
                            // onLoginFailed();
                        }
                        mConnectionProgressDialog.dismiss();
                    }
                }, 3000);

        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }

        }

    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    public Boolean userAutentification(String email, String password)
    {
        dbHelper.openToWrite();

        // Setting up the cursor which points to the desired table
        Cursor cursor = dbHelper.queueAll();

        Boolean records_Exist = false;

        // Checking if the table has values other than the header using the cursor
        if(cursor != null && cursor.getCount() > 0)
        {
            // Moving the cursor to the first row in the table
            cursor.moveToFirst();

            do
            {
                // Checking if the user name provided by the user exists in the database
                if(cursor.getString(cursor.getColumnIndex("email")).equals(email))
                {
                    if(cursor.getString(cursor.getColumnIndex("password")).equals(password))
                    {
                        records_Exist = true;
                        break;
                    }
                }

            }while(cursor.moveToNext()); // Moves to the next row
        }

        // Closing the cursor
        assert cursor != null;
        cursor.close();
        //dbHelper.deleteAll();
        // Closing the database
        dbHelper.close();

        return records_Exist;
    }

    // SignIn google button method
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG1, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            assert acct != null;
            personName = acct.getDisplayName();
            personEmail = acct.getEmail();
            gogSignIn = true;
            //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.btn_login_gog).setVisibility(View.GONE);
        } /*else {

            findViewById(R.id.btn_login_gog).setVisibility(View.VISIBLE);
        }*/
    }

    public boolean ifUserExsist(String email){
        dbHelper.openToWrite();

        Cursor cursor = dbHelper.queueAll();
        boolean ifExsist = false;
        if(cursor != null && cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            do
            {
                if(cursor.getString(cursor.getColumnIndex("email")).equals(email))
                {
                    ifExsist = true;
                    break;
                }
            }while(cursor.moveToNext());
        }
        assert cursor != null;
        cursor.close();
        dbHelper.close();

        return ifExsist;
    }
}