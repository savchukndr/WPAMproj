package com.working.savch.was.pin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.working.savch.was.MainActivity;
import com.working.savch.was.R;
import com.working.savch.was.SplashActivity;
import com.working.savch.was.session.Session;

/**
 * Created by savch on 30.05.2017.
 * All rights are reserved.
 * If you will have any cuastion, please
 * contact via email (savchukndr@gmail.com)
 */

public class PinCodeActivityRes extends AppCompatActivity {
    private String firstInputPassword;
    private Session session;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_res);
        this.setFinishOnTouchOutside(false);
        session = new Session(this);

        final EditText edtPasscode111 = (EditText) findViewById(R.id.pin_111);
        final EditText edtPasscode222 = (EditText) findViewById(R.id.pin_222);
        final EditText edtPasscode333 = (EditText) findViewById(R.id.pin_333);
        final EditText edtPasscode444 = (EditText) findViewById(R.id.pin_444);

        final String mainResultPin = session.getPin();

        Button _buttonNext = (Button) findViewById(R.id.btn_next_pin);
        Button _buttonCancel = (Button) findViewById(R.id.btn_cancel_pin);

        _buttonNext.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(final View view) {
                       firstInputPassword = edtPasscode111.getText().toString() +
                               edtPasscode222.getText().toString() +
                               edtPasscode333.getText().toString() +
                               edtPasscode444.getText().toString();
                       if (firstInputPassword.equals(mainResultPin)){
                           session.setLoggedin(true);
                           Intent intentMain = new Intent(PinCodeActivityRes.this, MainActivity.class);
                           startActivity(intentMain);
                           finish();
                           SplashActivity.spl.finish();
                       }else{
                           //Toast.makeText(PinCodeActivityRes.this, "Pin is wrong", Toast.LENGTH_SHORT).show();
                           Toast toast = Toast.makeText(getApplicationContext(),
                                   R.string.password_wrong, Toast.LENGTH_SHORT);
                           toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                           toast.show();
                           //moveTaskToBack(true);
                           edtPasscode111.setText("");
                           edtPasscode222.setText("");
                           edtPasscode333.setText("");
                           edtPasscode444.setText("");
                       }
                   }
               }
        );

        _buttonCancel.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(final View view) {
                     moveTaskToBack(true);

                 }
             }
        );

        final StringBuilder sb = new StringBuilder();
        edtPasscode111.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(sb.length() == 0 & edtPasscode111.length() == 1)
                {
                    sb.append(s);
                    edtPasscode111.clearFocus();
                    edtPasscode222.requestFocus();
                    edtPasscode222.setCursorVisible(true);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                if(sb.length()==1)
                {
                    sb.deleteCharAt(0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(sb.length()==0)
                {
                    edtPasscode111.requestFocus();
                }
            }
        });
        edtPasscode222.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(sb.length() == 0 & edtPasscode222.length() == 1)
                {
                    sb.append(s);
                    edtPasscode222.clearFocus();
                    edtPasscode333.requestFocus();
                    edtPasscode333.setCursorVisible(true);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                if(sb.length()==1)
                {
                    sb.deleteCharAt(0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(sb.length()==0)
                {
                    edtPasscode222.requestFocus();
                }
            }
        });
        edtPasscode333.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(sb.length() == 0 & edtPasscode222.length() == 1)
                {
                    sb.append(s);
                    edtPasscode333.clearFocus();
                    edtPasscode444.requestFocus();
                    edtPasscode444.setCursorVisible(true);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                if(sb.length()==1)
                {
                    sb.deleteCharAt(0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(sb.length()==0)
                {
                    edtPasscode333.requestFocus();
                }
            }
        });
        edtPasscode222.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    edtPasscode111.requestFocus();
                    edtPasscode111.setSelection(edtPasscode111.getText().length());
                }
                return false;
            }
        });
        edtPasscode333.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    edtPasscode222.requestFocus();
                    edtPasscode222.setSelection(edtPasscode222.getText().length());
                }
                return false;
            }
        });
        edtPasscode444.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    edtPasscode333.requestFocus();
                    edtPasscode333.setSelection(edtPasscode333.getText().length());
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
