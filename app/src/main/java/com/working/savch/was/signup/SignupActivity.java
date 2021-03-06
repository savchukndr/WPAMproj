package com.working.savch.was.signup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.working.savch.was.IntroActivity;
import com.working.savch.was.R;
import com.working.savch.was.session.Session;
import com.working.savch.was.base.MySQLAdapter;
import com.working.savch.was.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    final String LOG_TAG = "myLogs";
    MySQLAdapter dbHelper;
    private Session session;

    @BindView(R.id.input_name) EditText _nameText;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @BindView(R.id.btn_signup) Button _signupButton;
    @BindView(R.id.link_login) TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        session = new Session(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        dbHelper = new MySQLAdapter(this);
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);



        final String name = _nameText.getText().toString();
        final String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        //String reEnterPassword = _reEnterPasswordText.getText().toString();
        if (ifUserExsist(email)){
            //check if user email are in user table
            onUserExist();
        }else {
            final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();

            Log.d(LOG_TAG, "--- Insert in mytable: ---");

            dbHelper.openToWrite();
            long rowID = dbHelper.insert(name, email, password);
            Log.d(LOG_TAG, "row inserted, ID = " + rowID);

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onSignupSuccess or onSignupFailed
                            // depending on success
                            onSignupSuccess(name, email);
                            // onSignupFailed();
                            progressDialog.dismiss();
                        }
                    }, 3000);
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityIfNeeded(intent, 0);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public void onSignupSuccess(String name, String email) {
        session.setLoggedin(true);
        session.setName(name);
        session.setEmail(email);
        _signupButton.setEnabled(true);
        /*setResult(RESULT_OK, null);
        finish();*/
        /*Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("userName", name);
        intent.putExtra("userEmail", email);
        startActivity(intent);*/
        /*Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("userName", name);
        intent.putExtra("userEmail", email);*/
        Intent intentIntro = new Intent(getApplicationContext(), IntroActivity.class);
        startActivity(intentIntro);
    }

    //If user exist call toast massage
    public void onUserExist(){
        Toast.makeText(getBaseContext(), "User with such email already axist", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }


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

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
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