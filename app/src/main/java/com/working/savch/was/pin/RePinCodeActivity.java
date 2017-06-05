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

import com.working.savch.was.R;
import com.working.savch.was.session.Session;

/**
 * Created by savch on 05.06.2017.
 * All rights are reserved.
 * If you will have any cuastion, please
 * contact via email (savchukndr@gmail.com)
 */

public class RePinCodeActivity extends AppCompatActivity {
    private String secondInputPassword;
    private String firstInputPassword;
    private Session session;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_re);
        session = new Session(this);
        this.setFinishOnTouchOutside(false);

        final EditText edtPasscode11 = (EditText) findViewById(R.id.pin_11);
        final EditText edtPasscode22 = (EditText) findViewById(R.id.pin_22);
        final EditText edtPasscode33 = (EditText) findViewById(R.id.pin_33);
        final EditText edtPasscode44 = (EditText) findViewById(R.id.pin_44);

        Button _buttonNext = (Button) findViewById(R.id.btn_next_pin);
        Button _buttonCancel = (Button) findViewById(R.id.btn_cancel_pin);

        _buttonNext.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(final View view) {
                   secondInputPassword = edtPasscode11.getText().toString() +
                           edtPasscode22.getText().toString() +
                           edtPasscode33.getText().toString() +
                           edtPasscode44.getText().toString();
                   Bundle extras = getIntent().getExtras();
                   firstInputPassword = extras.getString("userPin");
                   if(secondInputPassword.equals(firstInputPassword)){
                       session.setPin(secondInputPassword);
                       session.setPinBool(true);
                       //Toast.makeText(getApplicationContext(),"Password seted",Toast.LENGTH_SHORT).show();

                       Toast toast = Toast.makeText(getApplicationContext(),
                               R.string.password_ok, Toast.LENGTH_SHORT);
                       toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                       toast.show();
                       finish();
                   }else{
                       //Toast.makeText(RePinCodeActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                       Toast toast = Toast.makeText(getApplicationContext(),
                               R.string.password_not_match, Toast.LENGTH_SHORT);
                       toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                       toast.show();
                       Intent intentRe = new Intent(getApplicationContext(), PinCodeActivity.class);
                       startActivity(intentRe);
                       finish();
                   }

               }
           }
        );

        _buttonCancel.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(final View view) {
                     session.setPinCheck(false);
                     finish();
                 }
             }
        );

        final StringBuilder sb=new StringBuilder();

        edtPasscode11.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(sb.length() == 0 & edtPasscode11.length() == 1)
                {
                    sb.append(s);
                    edtPasscode11.clearFocus();
                    edtPasscode22.requestFocus();
                    edtPasscode22.setCursorVisible(true);
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
                    edtPasscode11.requestFocus();
                }
            }
        });
        edtPasscode22.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(sb.length() == 0 & edtPasscode22.length() == 1)
                {
                    sb.append(s);
                    edtPasscode22.clearFocus();
                    edtPasscode33.requestFocus();
                    edtPasscode33.setCursorVisible(true);
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
                    edtPasscode22.requestFocus();
                }
            }
        });
        edtPasscode33.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(sb.length() == 0 & edtPasscode22.length() == 1)
                {
                    sb.append(s);
                    edtPasscode33.clearFocus();
                    edtPasscode44.requestFocus();
                    edtPasscode44.setCursorVisible(true);
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
                    edtPasscode33.requestFocus();
                }
            }
        });
        edtPasscode22.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    edtPasscode11.requestFocus();
                    edtPasscode11.setSelection(edtPasscode11.getText().length());
                }
                return false;
            }
        });
        edtPasscode33.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    edtPasscode22.requestFocus();
                    edtPasscode22.setSelection(edtPasscode22.getText().length());
                }
                return false;
            }
        });
        edtPasscode44.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    edtPasscode33.requestFocus();
                    edtPasscode33.setSelection(edtPasscode33.getText().length());
                }
                return false;
            }
        });
    }
}
