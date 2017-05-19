package com.working.savch.was.fingerPrint;

/**
 * Created by savch on 02.04.2017.
 */

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.working.savch.was.MainActivity;
import com.working.savch.was.R;
import com.working.savch.was.session.Session;

public class FingerprintHandler extends
        FingerprintManager.AuthenticationCallback {

    private CancellationSignal cancellationSignal;
    private Context appContext;
    public boolean ifEntered;
    private ImageView imge;
    private TextView txte;
    private Session sessione;


    public FingerprintHandler(Context context, ImageView img, TextView txt, Session session) {
        appContext = context;
        imge = img;
        txte = txt;
        sessione = session;
    }

    public void startAuth(FingerprintManager manager,
                          FingerprintManager.CryptoObject cryptoObject) {

        cancellationSignal = new CancellationSignal();

        if (ActivityCompat.checkSelfPermission(appContext,
                Manifest.permission.USE_FINGERPRINT) !=
                PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errMsgId,
                                      CharSequence errString) {
        txte.setText("Authentication error\n" + errString);
        /*Toast.makeText(appContext,
                "Authentication error\n" + errString,
                Toast.LENGTH_LONG).show();*/
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId,
                                     CharSequence helpString) {
        txte.setText("Authentication help\n" + helpString);
        /*Toast.makeText(appContext,
                "Authentication help\n" + helpString,
                Toast.LENGTH_LONG).show();*/
    }

    @Override
    public void onAuthenticationFailed() {
        imge.setImageResource(R.drawable.failed_print);
        txte.setText(R.string.fingerprint_failed);
        /*Toast.makeText(appContext,
                "Authentication failed.",
                Toast.LENGTH_LONG).show();*/
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        imge.setImageResource(R.drawable.done_print);
        txte.setText(R.string.fingerprint_sacc);
        /*Toast.makeText(appContext,
                "Authentication succeeded.",
                Toast.LENGTH_LONG).show();*/
        sessione.setLoggedin(true);
        sessione.setName("Master");
        sessione.setEmail("");
        Intent intent = new Intent(appContext,MainActivity.class);

        intent.putExtra("userName", "Master");
        intent.putExtra("userEmail", "");
        appContext.startActivity(intent);
    }
}