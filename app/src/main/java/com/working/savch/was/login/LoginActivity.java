package com.working.savch.was.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.working.savch.was.IntroActivity;
import com.working.savch.was.session.Session;
import com.working.savch.was.fingerPrint.FingerprintActivity;
import com.working.savch.was.MainActivity;
import com.working.savch.was.base.MySQLAdapter;
import com.working.savch.was.R;
import com.working.savch.was.signup.SignupActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import com.facebook.FacebookSdk;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.BindView;

public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener{
    private static final String TAG = "LoginActivity";
    //final String LOG_TAG = "myLogs";
    private static final int REQUEST_SIGNUP = 0;
    MySQLAdapter dbHelper;


    private GoogleApiClient mGoogleApiClient;
    private static final String TAG1 = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private static final int FB_SIGN_IN = 64206;
    private TextView mStatusTextView;
    private ProgressDialog mConnectionProgressDialog;

    private String personName;
    private String personEmail;
    private String personNameSimple;

    private boolean gogSignIn = false;

    private CallbackManager callbackManager;
    private Session session;
    private Uri personPhoto;
    private boolean hasPhoto = false;
    private LoginButton _signupFaceButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        session = new Session(this);

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
        if(session.loggedin()){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        //*****************************************************************************
        /*//Login button listener
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
        });*/
        //*****************************************************************************
        /*
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;


        if (currentapiVersion >= 23) {
            _fingerButton.setVisibility(View.VISIBLE);
        }else{
            _fingerButton.setVisibility(View.GONE);
        }
        //Finger button sighnIn
        _fingerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                alertDialog.setTitle(R.string.attention_alert);
                alertDialog.setMessage(getString(R.string.attention_alert_text));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.attention_alert_button),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(getApplicationContext(), FingerprintActivity.class);
                                startActivity(intent);
                            }
                        });
                alertDialog.show();
            }
        });*/

        //FAcebook initialization
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        _signupFaceButton = (LoginButton)findViewById(R.id.login_button);
        _signupFaceButton.setReadPermissions("email");
        _signupFaceButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                System.out.println("onSuccess");
                mConnectionProgressDialog.show();
                String accessToken = loginResult.getAccessToken().getToken();
                //Log.i("accessToken", accessToken);

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                       // Log. i("LoginActivity", response.toString());
                        // Get facebook data from login
                        Bundle bFacebookData = getFacebookData(object);

                        final String email;
                        final String password;
                        final String name;

                        name = bFacebookData.getString("first_name");
                        email = bFacebookData.getString("email");
                        password = "adminacc";
                        if (!ifUserExsist(email)) {
                            dbHelper.openToWrite();
                            long rowID = dbHelper.insert(name, email, password);
                            //Log.d(LOG_TAG, "row inserted, ID = " + rowID);
                        }

                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        // On complete call either onLoginSuccess or onLoginFailed
                                        if (userAutentification(email, password)) {
                                            onLoginSuccess(name, email);
                                        } else {
                                            onLoginFailed();
                                            // onLoginFailed();
                                        }
                                        mConnectionProgressDialog.dismiss();
                                    }
                                }, 3000);
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),
                        "Facebook authentication canceled.",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(getApplicationContext(),
                        "Facebook authentication error:\n" + e,
                        Toast.LENGTH_LONG).show();
            }
        });

        dbHelper = new MySQLAdapter(this);
        mConnectionProgressDialog = new ProgressDialog(this);
        mConnectionProgressDialog.setMessage("Signing in...");
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        //Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    //********************************************************************************
    //login with Login Button
    /*public void login() {
       // Log.d(TAG, "Login");

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

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed

                        if (userAutentification(email, password)){
                            onLoginSuccess(personNameSimple, email);
                        }else{
                            onLoginFailed();
                            // onLoginFailed();
                        }
                        progressDialog.dismiss();
                    }
                }, 3000);
    }*/
    //******************************************************************************


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            mConnectionProgressDialog.dismiss();

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            /*GoogleSignInAccount acct = result.getSignInAccount();
            assert acct != null;
            personPhoto = acct.getPhotoUrl();
            hasPhoto = true;*/

            final String email;
            final String password;
            final String name;

            mConnectionProgressDialog.show();
            name = personName;
            email = personEmail;
            password = "adminacc";
            if (!ifUserExsist(email)) {
                dbHelper.openToWrite();
                long rowID = dbHelper.insert(name, email, password);
                //Log.d(LOG_TAG, "row inserted, ID = " + rowID);
            }
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onLoginSuccess or onLoginFailed
                            if (userAutentification(email, password)) {
                                onLoginSuccess(name, email);
                            } else {
                                onLoginFailed();
                                // onLoginFailed();
                            }
                            mConnectionProgressDialog.dismiss();
                        }
                    }, 3000);

        }

        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }

        }

        if (requestCode == FB_SIGN_IN) {

            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess(String name, String email) {
        session.setLoggedin(true);
        session.setEmail(email);
        session.setName(name);
        /*if(hasPhoto){
            session.setPhoto(personPhoto.toString());
            session.setHasPhoto(hasPhoto);
        }*/
        //_loginButton.setEnabled(true);

        /*Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("userName", name);
        intent.putExtra("userEmail", email);*/
        Intent intentIntro = new Intent(getApplicationContext(), IntroActivity.class);
        startActivity(intentIntro);
        //finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        //_loginButton.setEnabled(true);
    }

    //*************************************************************************************
    /*public boolean validate() {
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
    }*/
    //*************************************************************************************

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
                //if(cursor.getString(cursor.getColumnIndex("email")) == null) {
                  //  records_Exist = false;
                //}else{
                    if (cursor.getString(cursor.getColumnIndex("email")).equals(email)) {
                        if (cursor.getString(cursor.getColumnIndex("password")).equals(password)) {
                            personNameSimple = cursor.getString(cursor.getColumnIndex("name"));
                            records_Exist = true;
                            break;
                        }
                    }
                //}

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
       // Log.d(TAG1, "handleSignInResult:" + result.isSuccess());
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
        dbHelper.openToRead();

        Cursor cursor = dbHelper.queueAll();
        if(cursor == null){
            String dd = "nolik";
        }else{
            String dd = "nie nolik";
        }
        boolean ifExsist = false;
        if(cursor != null && cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            do
            {
                if (cursor.getString(cursor.getColumnIndex("email")) == null) {
                    ifExsist = false;
                }else{
                    if (cursor.getString(cursor.getColumnIndex("email")).equals(email)) {

                        ifExsist = true;
                        break;
                    }
                }
            }while(cursor.moveToNext());
        }
        assert cursor != null;
        cursor.close();
        dbHelper.close();

        return ifExsist;
    }

    private Bundle getFacebookData(JSONObject object) {

        try {
            Bundle bundle = new Bundle();
            /*
            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }*/

            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));

            return bundle;
        }
        catch(JSONException e) {
           // Log.d(TAG,"Error parsing JSON");
            return new Bundle();
        }
    }

    public static boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }
}