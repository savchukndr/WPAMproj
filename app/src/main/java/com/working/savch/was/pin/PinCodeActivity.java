package com.working.savch.was.pin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.working.savch.was.MainActivity;
import com.working.savch.was.R;
import com.working.savch.was.history.TransactionActivity;
import com.working.savch.was.session.Session;

/**
 * Created by savch on 30.05.2017.
 * All rights are reserved.
 * If you will have any cuastion, please
 * contact via email (savchukndr@gmail.com)
 */

public class PinCodeActivity extends AppCompatActivity {
    private String firstInputPassword;
    private Session session;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        this.setFinishOnTouchOutside(false);
        session = new Session(this);

        final EditText edtPasscode1 = (EditText) findViewById(R.id.pin_1);
        final EditText edtPasscode2 = (EditText) findViewById(R.id.pin_2);
        final EditText edtPasscode3 = (EditText) findViewById(R.id.pin_3);
        final EditText edtPasscode4 = (EditText) findViewById(R.id.pin_4);
        final StringBuilder sb = new StringBuilder();

        Button _buttonNext = (Button) findViewById(R.id.btn_next_pin);
        Button _buttonCancel = (Button) findViewById(R.id.btn_cancel_pin);

        _buttonNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    firstInputPassword = edtPasscode1.getText().toString() +
                            edtPasscode2.getText().toString() +
                            edtPasscode3.getText().toString() +
                            edtPasscode4.getText().toString();
                    Intent intentRe = new Intent(getApplicationContext(), RePinCodeActivity.class);
                    intentRe.putExtra("userPin", firstInputPassword);
                    startActivity(intentRe);
                    finish();
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

        edtPasscode1.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(sb.length() == 0 & edtPasscode1.length() == 1)
                {
                    sb.append(s);
                    edtPasscode1.clearFocus();
                    edtPasscode2.requestFocus();
                    edtPasscode2.setCursorVisible(true);
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
                    edtPasscode1.requestFocus();
                }
            }
        });
        edtPasscode2.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(sb.length() == 0 & edtPasscode2.length() == 1)
                {
                    sb.append(s);
                    edtPasscode2.clearFocus();
                    edtPasscode3.requestFocus();
                    edtPasscode3.setCursorVisible(true);
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
                    edtPasscode2.requestFocus();
                }
            }
        });
        edtPasscode3.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(sb.length() == 0 & edtPasscode2.length() == 1)
                {
                    sb.append(s);
                    edtPasscode3.clearFocus();
                    edtPasscode4.requestFocus();
                    edtPasscode4.setCursorVisible(true);
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
                    edtPasscode3.requestFocus();
                }
            }
        });
        edtPasscode2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    edtPasscode1.requestFocus();
                    edtPasscode1.setSelection(edtPasscode1.getText().length());
                }
                return false;
            }
        });
        edtPasscode3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    edtPasscode2.requestFocus();
                    edtPasscode2.setSelection(edtPasscode2.getText().length());
                }
                return false;
            }
        });
        edtPasscode4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    edtPasscode3.requestFocus();
                    edtPasscode3.setSelection(edtPasscode3.getText().length());
                }
                return false;
            }
        });
    }
}
