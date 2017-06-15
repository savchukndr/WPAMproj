package com.working.savch.was;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.api.BooleanResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.working.savch.was.base.MySQLAdapter;
import com.working.savch.was.history.TransactionActivity;
import com.working.savch.was.login.LoginActivity;
import com.working.savch.was.pin.PinCodeActivity;
import com.working.savch.was.session.Session;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    static final String STATE_NAME = "masterName";
    Context context;
    MySQLAdapter dbHelper;
    TextView textViewInfo;
    MenuItem itemC;
    MenuItem itemCP;
    private String mCurrentName;
    private String mCurrentEmail;
    private boolean inputMoneyFlag = true;
    private GoogleApiClient mGoogleApiClient;
    private Session session;
    private Bitmap bitmap;
    private int userId;
    private int categoryChoose;
    private String userInputValue;
    private String isCheckedPin = "true";

    private static int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0) return 1;
        else return k;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putString(STATE_NAME, mCurrentName);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);

        session = new Session(this);
        if (session.loggedin()) {
            mCurrentEmail = session.getEmail();
            mCurrentName = session.getName();
        }


        AppRater.app_launched(this);
        MobileAds.initialize(this, "ca-app-pub-7423558564398166~3561711933");
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        /*.addTestDevice("619AF985B301CF86444F0AE42D8EC98F")*/
        adView.loadAd(adRequest);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Bundle extras = getIntent().getExtras();
        //mCurrentEmail = extras.getString("userEmail");

        setActivityBackgroundColor(getResources().getColor(R.color.jumbo));

        dbHelper = new MySQLAdapter(this);

        textViewInfo = (TextView) findViewById(R.id.amount_view);
        double controlSum = 0.0d;
        textViewInfo.setText(String.format("%.2f", controlSum));

        dbHelper.openToWrite();
        Cursor cursor = dbHelper.querySum();
        while (cursor.moveToNext()) {
            String tmp = String.valueOf(cursor.getDouble(cursor.getColumnIndex("totalSum")));
            if (tmp.equals("0.0")) {
                textViewInfo.setText("0.00");
            } else {
                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                symbols.setGroupingSeparator(' ');
                DecimalFormat format = new DecimalFormat("#,###.00", symbols);
                textViewInfo.setTextSize(50);
                String zeroEdit = String.valueOf(delSpacesInString(String.format("%14s", format.format(cursor.getDouble(cursor.getColumnIndex("totalSum"))))));
                if(zeroEdit.substring(0,1).equals(",")){
                    zeroEdit = "0" + zeroEdit;
                    textViewInfo.setText(zeroEdit);
                }else {
                    textViewInfo.setText(String.valueOf(delSpacesInString(String.format("%14s", format.format(cursor.getDouble(cursor.getColumnIndex("totalSum")))))));
                }
                if (delSpacesInString(String.format("%14s", format.format(cursor.getDouble(cursor.getColumnIndex("totalSum"))))).length() >= 12) {
                    textViewInfo.setTextSize(30);
                }
            }
        }

        Cursor cursorUserId = dbHelper.queueUserId(mCurrentEmail);
        while (cursorUserId.moveToNext()) {
            userId = cursorUserId.getInt(cursorUserId.getColumnIndex("id_user"));
        }

        //MainActivity buttons
        Button _incomeButton = (Button) findViewById(R.id.btn_income);
        Button _spendButton = (Button) findViewById(R.id.btn_spend);
        Button _historyButton = (Button) findViewById(R.id.btn_history);

        //income button
        _incomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textViewInfo = (TextView) findViewById(R.id.amount_view);
                EditText addEditText = (EditText) findViewById(R.id.addEditText);
                String tmpAdd = addEditText.getText().toString();
                if (tmpAdd.length() >= 4 && tmpAdd.contains(".") && tmpAdd.substring(0,4).equals("0.00") ){
                    tmpAdd = "0";
                }
                strCheck(tmpAdd);
                if (inputMoneyFlag) {
                    double amount;
                    amount = Double.parseDouble(tmpAdd);

                    dbHelper.openToWrite();
                    dbHelper.insertTransactionTable(amount, "aboutTODO", userId, 6);
                    Snackbar.make(view, getString(R.string.main_record_add),
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Cursor cursor = dbHelper.querySum();
                    while (cursor.moveToNext()) {
                        String tmp = String.valueOf(cursor.getDouble(cursor.getColumnIndex("totalSum")));
                        if (tmp.equals("0.0")) {
                            textViewInfo.setText("0,00");
                        } else {
                            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                            symbols.setGroupingSeparator(' ');
                            DecimalFormat format = new DecimalFormat("#,###.00", symbols);
                            textViewInfo.setTextSize(50);
                            String zeroEdit = String.valueOf(delSpacesInString(String.format("%14s", format.format(cursor.getDouble(cursor.getColumnIndex("totalSum"))))));
                            if(zeroEdit.substring(0,1).equals(",")){
                                zeroEdit = "0" + zeroEdit;
                                textViewInfo.setText(zeroEdit);
                            }else {
                                textViewInfo.setText(String.valueOf(delSpacesInString(String.format("%14s", format.format(cursor.getDouble(cursor.getColumnIndex("totalSum")))))));
                            }

                            if (delSpacesInString(String.format("%14s", format.format(cursor.getDouble(cursor.getColumnIndex("totalSum"))))).length() >= 12) {
                                textViewInfo.setTextSize(30);
                            }
                        }
                    }
                    addEditText.setText("");
                } else {
                    Snackbar.make(view, getString(R.string.main_one_field),
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    addEditText.setText("");
                }
            }
        });

        //spend button
        _spendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final TextView textViewInfo = (TextView) findViewById(R.id.amount_view);
                final EditText addEditText = (EditText) findViewById(R.id.addEditText);
                String tmpAdd = addEditText.getText().toString();


                if (tmpAdd.length() >= 4 && tmpAdd.contains(".") && tmpAdd.substring(0,4).equals("0.00")){
                    tmpAdd = "0";
                }
                strCheck(tmpAdd);
                if (inputMoneyFlag) {
                    final double amount;
                    amount = Double.parseDouble(tmpAdd) * (-1);

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle(R.string.chooseCategoryAlert)
                            .setItems(R.array.category_array, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // The 'which' argument contains the index position
                                    // of the selected item
                                    categoryChoose = which;


                                    final AlertDialog.Builder inputAlert = new AlertDialog.Builder(context);
                                    inputAlert.setTitle(R.string.about_trans_alert_title);
                                    inputAlert.setMessage(R.string.about_trans_alert_message);
                                    final EditText userInput = new EditText(context);
                                    inputAlert.setView(userInput);
                                    inputAlert.setPositiveButton(R.string.submit_button, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            userInputValue = userInput.getText().toString();

                                            dbHelper.openToWrite();
                                            long rowID = dbHelper.insertTransactionTable(amount, userInputValue, userId, categoryChoose);
                                            Snackbar.make(view, getString(R.string.main_record_add),
                                                    Snackbar.LENGTH_LONG)
                                                    .setAction("Action", null).show();
                                            Cursor cursor = dbHelper.querySum();
                                            while (cursor.moveToNext()) {
                                                String tmp = String.valueOf(cursor.getDouble(cursor.getColumnIndex("totalSum")));
                                                if (tmp.equals("0.0")) {
                                                    textViewInfo.setText("0.00");
                                                } else {
                                                    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                                                    symbols.setGroupingSeparator(' ');
                                                    DecimalFormat format = new DecimalFormat("#,###.00", symbols);
                                                    textViewInfo.setTextSize(50);
                                                    String zeroEdit = String.valueOf(delSpacesInString(String.format("%14s", format.format(cursor.getDouble(cursor.getColumnIndex("totalSum"))))));
                                                    if(zeroEdit.substring(0,1).equals(",")){
                                                        zeroEdit = "0" + zeroEdit;
                                                        textViewInfo.setText(zeroEdit);
                                                    }else {
                                                        textViewInfo.setText(String.valueOf(delSpacesInString(String.format("%14s", format.format(cursor.getDouble(cursor.getColumnIndex("totalSum")))))));
                                                    }
                                                    if (delSpacesInString(String.format("%14s", format.format(cursor.getDouble(cursor.getColumnIndex("totalSum"))))).length() >= 12) {
                                                        textViewInfo.setTextSize(30);
                                                    }
                                                }
                                            }
                                            addEditText.setText("");
                                        }
                                    });
                                    AlertDialog alertDialog = inputAlert.create();
                                    alertDialog.show();
                                }
                            });
                    AlertDialog alrt = builder.create();
                    alrt.show();


                } else {
                    Snackbar.make(view, getString(R.string.main_one_field),
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    addEditText.setText("");
                }
            }
        });

        //Histry onClick button
        _historyButton.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(final View view) {
                      Intent intentTrans = new Intent(getApplicationContext(), TransactionActivity.class);
                      intentTrans.putExtra("userEmail", mCurrentEmail);
                      startActivity(intentTrans);
                      overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                      finish();
                  }
              }
            );


        /*//Add transaction record to db
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textViewInfo = (TextView) findViewById(R.id.amount_view);
                EditText addEditText = (EditText) findViewById(R.id.addEditText);
                EditText delEditText = (EditText) findViewById(R.id.delEditText);
                String tmpAdd = addEditText.getText().toString();
                String tmpDel = delEditText.getText().toString();
                //check if not entered both field in one time
                strCheck(tmpAdd, tmpDel);
                if(inputMoneyFlag) {
                    double amount;
                    if(plusMinusChoise){
                        amount = Double.parseDouble(tmpAdd);
                    }else{
                        amount = Double.parseDouble(tmpDel) * (-1);
                    }
                    dbHelper.openToWrite();
                    long rowID = dbHelper.insertTransactionTable(amount, "aboutTODO", 1, 3);
                    //Log.d(LOG_TAG, "row inserted, ID = " + rowID);
                    Snackbar.make(view, getString(R.string.main_record_add),
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Cursor cursor = dbHelper.querySum();
                    while(cursor.moveToNext())
                    {
                        textViewInfo.setText(String.valueOf(String.format("%.2f",cursor.getDouble(cursor.getColumnIndex("totalSum")))));
                    }
                    addEditText.setText("");
                    delEditText.setText("");
                }else{
                    Snackbar.make(view, getString(R.string.main_one_field),
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    addEditText.setText("");
                    delEditText.setText("");
                }
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        /*if (savedInstanceState != null) {
            mCurrentName = savedInstanceState.getString(STATE_NAME);
            //Log.d("STATE", "------ not null --------");
        } else {
            //Log.d("STATE", "------ null --------");
            mCurrentName = extras.getString("userName");
        }*/
        // Probably initialize members with default values for a new instance

        //if (extras != null) {

        String firstLetter = imageSelecter(mCurrentName);
        View hView = navigationView.getHeaderView(0);
        String helloNameUser = getString(R.string.nav_hello) + " " + firstWord(mCurrentName) + "!";

        TextView txtHelloName = (TextView) hView.findViewById(R.id.userNameHelloTextView);
        txtHelloName.setText(helloNameUser);

        TextView txtHelloEmail = (TextView) hView.findViewById(R.id.userEmailHelloTextView);
        txtHelloEmail.setText(mCurrentEmail);

        ImageView imgName = (ImageView) hView.findViewById(R.id.imageAcc);
            /*if(session.hasPhoto()){
                Uri imageUri = Uri.parse(session.getPhoto());
                try {
                    bitmap = getThumbnail(imageUri);
                    imgName.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {*/
        switch (firstLetter) {
            case "А":
            case "A":
                imgName.setImageResource(R.drawable.a);
                break;
            case "Б":
            case "B":
                imgName.setImageResource(R.drawable.b);
                break;
            case "Ц":
            case "C":
                imgName.setImageResource(R.drawable.c);
                break;
            case "Д":
            case "D":
                imgName.setImageResource(R.drawable.d);
                break;
            case "Е":
            case "Э":
            case "E":
                imgName.setImageResource(R.drawable.e);
                break;
            case "Ф":
            case "F":
                imgName.setImageResource(R.drawable.f);
                break;
            case "Г":
            case "G":
                imgName.setImageResource(R.drawable.g);
                break;
            case "Х":
            case "H":
                imgName.setImageResource(R.drawable.h);
                break;
            case "И":
            case "I":
                imgName.setImageResource(R.drawable.i);
                break;
            case "Ю":
            case "J":
                imgName.setImageResource(R.drawable.j);
                break;
            case "К":
            case "K":
                imgName.setImageResource(R.drawable.k);
                break;
            case "Л":
            case "L":
                imgName.setImageResource(R.drawable.l);
                break;
            case "М":
            case "M":
                imgName.setImageResource(R.drawable.m);
                break;
            case "Н":
            case "N":
                imgName.setImageResource(R.drawable.n);
                break;
            case "О":
            case "O":
                imgName.setImageResource(R.drawable.o);
                break;
            case "П":
            case "P":
                imgName.setImageResource(R.drawable.p);
                break;
            case "Q":
                imgName.setImageResource(R.drawable.q);
                break;
            case "Р":
            case "R":
                imgName.setImageResource(R.drawable.r);
                break;
            case "С":
            case "S":
                imgName.setImageResource(R.drawable.s);
                break;
            case "Т":
            case "T":
                imgName.setImageResource(R.drawable.t);
                break;
            case "У":
            case "U":
                imgName.setImageResource(R.drawable.u);
                break;
            case "В":
            case "V":
                imgName.setImageResource(R.drawable.v);
                break;
            case "W":
                imgName.setImageResource(R.drawable.w);
                break;
            case "X":
                imgName.setImageResource(R.drawable.x);
                break;
            case "Y":
                imgName.setImageResource(R.drawable.y);
                break;
            case "З":
            case "Z":
                imgName.setImageResource(R.drawable.z);
                break;
            default:
                imgName.setImageResource(R.drawable.failed_print);
                break;
        }
        // }
    }

    @Override
    public void onBackPressed() {
        //moveTaskToBack(true);
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
        finish();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        try {
            FingerprintManagerCompat fingerprintManagerCompat = FingerprintManagerCompat.from(context);
            if (!fingerprintManagerCompat.isHardwareDetected()) {
                // Device doesn't support fingerprint authentication
                menu.findItem(R.id.action_check_pin).setVisible(!session.isPinCheck());
            } else if (!fingerprintManagerCompat.hasEnrolledFingerprints()) {
                // User hasn't enrolled any fingerprints to authenticate with
                menu.findItem(R.id.action_check_pin).setVisible(!session.isPinCheck());
            } else {
                // Everything is ready for fingerprint authentication
                menu.findItem(R.id.action_check_pin).setVisible(false);

            }

        }
        catch(Exception e) {
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        itemC = menu.findItem(R.id.action_check);
        itemCP = menu.findItem(R.id.action_check_pin);
        FingerprintManagerCompat fingerprintManagerCompat = FingerprintManagerCompat.from(context);


        if (!fingerprintManagerCompat.isHardwareDetected()) {
            // Device doesn't support fingerprint authentication
            itemC.setVisible(false);
            itemCP.setVisible(true);
            if (session.isPin()) {
                itemCP.setChecked(true);
            }
        } else if (!fingerprintManagerCompat.hasEnrolledFingerprints()) {
            // User hasn't enrolled any fingerprints to authenticate with
            itemC.setVisible(false);
            itemCP.setVisible(true);
            if (session.isPin()) {
                itemCP.setChecked(true);
            }
        } else {
            // Everything is ready for fingerprint authentication
            itemCP.setVisible(false);
            if (session.fingerPrint()) {
                itemC.setChecked(true);
            }

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_about:
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                return true;
            case R.id.action_logout:
                final AlertDialog.Builder exitAlert = new AlertDialog.Builder(context);
                exitAlert.setTitle(R.string.exit_alert_title);
                exitAlert.setMessage(R.string.exit_alert_messege);
                //inputAlert.setView(userInput);
                exitAlert.setPositiveButton(R.string.yes_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        session.setFingerPrint(false);
                        logOut();
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }
                });
                exitAlert.setNegativeButton(R.string.no_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = exitAlert.create();
                alertDialog.show();
                return true;
            case R.id.action_check:

                if (item.isChecked()) {
                    item.setChecked(false);
                    session.setFingerPrint(false);
                } else {
                    final AlertDialog.Builder fingerAlert = new AlertDialog.Builder(context);
                    fingerAlert.setTitle(R.string.attention_alert);
                    fingerAlert.setMessage(R.string.setFinger_alert_text);
                    //inputAlert.setView(userInput);
                    fingerAlert.setNeutralButton(R.string.gotIt_btn, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            item.setChecked(true);
                            session.setFingerPrint(true);
                        }
                    });
                    AlertDialog alertDialogFinger = fingerAlert.create();
                    alertDialogFinger.show();
                }
                return true;
            case R.id.action_check_pin:
                if (item.isChecked()) {
                    item.setChecked(false);
                    session.setPinBool(false);
                } else {
                    item.setChecked(true);
                    session.setPinCheck(true);
                    Intent intentPin = new Intent(MainActivity.this, PinCodeActivity.class);
                    startActivity(intentPin);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;

        if (id == R.id.nav_main) {
            //intent = new Intent(getApplicationContext(),MainActivity.class);

        } else if (id == R.id.nav_transaction) {
            intent = new Intent(getApplicationContext(), TransactionActivity.class);
            intent.putExtra("userEmail", mCurrentEmail);
            startActivity(intent);
            finish();
        }/* else if (id == R.id.nav_settings) {
        }*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*public void strCheck(String x, String y) {
        //inputMoneyFlag = !x.isEmpty() && y.isEmpty() || !(!x.isEmpty() && !y.isEmpty());
        if (!x.isEmpty() && y.isEmpty()){
            inputMoneyFlag = true;
            plusMinusChoise = true;
        }else if (x.isEmpty() && !y.isEmpty()){
            inputMoneyFlag = true;
            plusMinusChoise = false;
        }else if(x.isEmpty() && y.isEmpty()){
            inputMoneyFlag = false;
        }else if(!x.isEmpty() && !y.isEmpty()){
            inputMoneyFlag = false;
        }
    }*/

    public void setActivityBackgroundColor(int color) {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(color);
    }

    public void strCheck(String x) {
        if (!x.isEmpty()) {
            inputMoneyFlag = true;
        } else if (x.isEmpty()) {
            inputMoneyFlag = false;
        }
    }

    private String imageSelecter(String name) {
        return name.substring(0, 1).toUpperCase();
    }

    private String firstWord(String sentense) {
        Pattern p = Pattern.compile("^([\\w\\-]+)");
        Matcher m = p.matcher(sentense);
        if (m.find()) {
            return m.group(1);
        } else {
            return "NO";
        }
    }

    private void logOut() {
        session.setLoggedin(false);
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    private String delSpacesInString(String inputStr) {
        return inputStr.replaceAll("^\\s+", "");
    }

}