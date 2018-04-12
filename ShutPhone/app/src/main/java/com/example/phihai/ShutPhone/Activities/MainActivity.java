package com.example.phihai.ShutPhone.Activities;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.coresdk.common.config.EstimoteSDK;
import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;
import com.example.phihai.ShutPhone.KioskMode.KioskService;
import com.example.phihai.ShutPhone.R;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.UUID;

import static com.example.phihai.ShutPhone.KioskMode.PrefUtils.setKioskModeActive;
import static com.example.phihai.ShutPhone.R.id;
import static com.example.phihai.ShutPhone.R.layout;
import static com.example.phihai.ShutPhone.R.string;
import static com.google.android.gms.games.Games.API;
import static com.google.android.gms.games.Games.Achievements;
import static com.google.android.gms.games.Games.SCOPE_GAMES;



public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity" ;
    private Button btnStart;
    private Button btnQuit;
    private TextView pointsView;
    TextView showpoints;
    public int points;
    private int time = 0;
    private Chronometer chronometer;
    private BeaconManager beaconManager;
   // private final List<Integer> blockedKeys = new ArrayList<>(Arrays.asList(KeyEvent.KEYCODE_VOLUME_DOWN, KeyEvent.KEYCODE_VOLUME_UP));
    private static final int PERMISSION_REQUEST_CODE = 101;
    CountDownTimer Timer;
    SharedPreferences sharedPreferences ;
    FirebaseAuth firebaseAuth;
    GoogleApiClient apiClient;
    FirebaseAnalytics mFirebaseAnalytics;
    protected customViewGroup blockingView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        setContentView(layout.activity_main);
        chronometer = (Chronometer) findViewById(id.chronometer);
        btnStart = (Button) findViewById(id.start_button);
        btnQuit = (Button) findViewById(id.quit_button);
        pointsView = (TextView)findViewById(id.points_view);
        showpoints = (TextView) findViewById(id.showpoints);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        apiClient = new GoogleApiClient.Builder(this)
                .addApi(API)
                .addScope(SCOPE_GAMES)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.e(TAG, "Could not connect to Play games services");
                        finish();
                    }
                }).build();
        if (AccessToken.getCurrentAccessToken() == null) {
            goLoginActivity();
        }
        beaconManager = new BeaconManager(getApplicationContext());
        beaconManager.setMonitoringListener(new BeaconManager.BeaconMonitoringListener() {
                                                @Override
                                                public void onEnteredRegion(BeaconRegion region, List<Beacon> beacons) {
                                                   btnStart.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            btnStart.setEnabled(false);
                                                            btnQuit.setEnabled(true);
                                                            if(btnStart.isClickable()) {
                                                                startKioskService();
                                                                startTime();
                                                                setKioskModeActive(true, getApplicationContext());
                                                                Toast.makeText(getApplicationContext(), "Time is running!", Toast.LENGTH_SHORT).show();
                                                            }

                                                            btnQuit.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    if(btnQuit.isEnabled()){
                                                                        stopTime();
                                                                        WindowManager manager = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
                                                                        manager.removeView(blockingView);
                                                                        setKioskModeActive(false, getApplicationContext());
                                                                        Toast.makeText(getApplicationContext(), "You can leave the app now!", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    btnStart.setEnabled(true);
                                                                    btnQuit.setEnabled(false);
                                                                }
                                                            });
                                                        }
                                                    });


                                                    showNotification(
                                                            "Welcome to ShutPhone!",
                                                            "You are currently in Beacon Range.");
                                                }
                                                @Override
                                                public void onExitedRegion(BeaconRegion beaconRegion) {
                                                    stopTime();
                                                    btnStart.setEnabled(false);
                                                    btnQuit.setEnabled(false);
                                                    setKioskModeActive(false, getApplicationContext());
                                                    showNotification(
                                                            "Scanning stopped!",
                                                            "You are out of range.");
                                                }
                                                });

        beaconManager.setForegroundScanPeriod(2000,2000);
        beaconManager.setBackgroundScanPeriod(2000,2000);


        // permission for location
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission for location denied");
            makeRequest();
        }

        EstimoteSDK.initialize(getApplicationContext(), "shutapps-2-0-k14", "d4071bd846c4f402db31e519358ba66d");
        EstimoteSDK.enableDebugLogging(true);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new BeaconRegion(
                        "monitored region",
                        UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), 38862, 26732));
                Log.d(TAG, "beacon connected!");
            }
        });
    }

    protected void onStart() {
        super.onStart();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
        apiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        apiClient.disconnect();
        Context context = this;
        sharedPreferences = context.getSharedPreferences(
                "ShutPhone", Context.MODE_PRIVATE);

        points = sharedPreferences.getInt("points", 0);
        pointsView.setText("" +points);
    }


    protected void onPause(){
        super.onPause();
        //interrupt the multitask button
        ((ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE)).moveTaskToFront(getTaskId(), 0);
    }
    protected void onResume(){
        super.onResume();
        Context context = this;
        sharedPreferences = context.getSharedPreferences("ShutPhone", Context.MODE_PRIVATE);
        points = sharedPreferences.getInt("points", 0);
        pointsView.setText("" +points);
    }

    protected void onDestroy() {
        super.onDestroy();
        beaconManager.disconnect();
        if(null != Timer){
            Timer.cancel();
            if(blockingView == null){
                WindowManager manager = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
                manager.removeView(blockingView);
            }
        }
       // beaconManager.unbind(this);
    }


    public void startTime(){
        Timer =  new CountDownTimer(7200000, 1000) {
            public void onTick(long millisUntilFinished) {
                time++;
                int seconds = time % 60;
                int minutes = time / 60;
                String stringTime = String.format("%02d:%02d", minutes, seconds);
                chronometer.setText(stringTime);
               switch (time) {
                   case 10: //1800
                      //   points += 10;
                       SharedPreferences.Editor editor = sharedPreferences.edit();
                       editor.putInt("points", points+=10);
                       editor.commit();
                         pointsView.setText(String.valueOf(points));
                         pointsView.setText("" +points);
                   // myRef.setValue("Points: " + points);
                   break;
                   //  }
                    case 20://3600
                        // if(time == 20){//3600
                      //  points += 10;
                        editor = sharedPreferences.edit();
                        editor.putInt("points", points+=10);
                        editor.commit();
                        pointsView.setText(String.valueOf(points));
                        pointsView.setText("" +points);
                        //myRef.setValue("Points: " + points);
                        break;
                        //  }
                    case 30:
                        // if(time == 30){//5400
                     //   points += 10;
                        editor = sharedPreferences.edit();
                        editor.putInt("points", points+=10);
                        editor.commit();
                        pointsView.setText(String.valueOf(points));
                        pointsView.setText("" +points);
                        break;
                        // }
                    case 40:
                        //if(time == 40){//7200
                      //  points += 10;
                        editor = sharedPreferences.edit();
                        editor.putInt("points", points+=10);
                        editor.commit();
                        pointsView.setText(String.valueOf(points));
                        pointsView.setText("" +points);
                        break;
                        //   }
                    case 50:
                        //  if(time == 50){//7200
                       // points += 10;
                        editor = sharedPreferences.edit();
                        editor.putInt("points", points+=10);
                        editor.commit();
                        pointsView.setText(String.valueOf(points));
                        pointsView.setText("" +points);
                        break;
                }
            }

           public void onFinish() {
               Toast.makeText(getApplicationContext(), "You reached 2 hours!", Toast.LENGTH_LONG).show();
           }
        }.start();
    }
    public void stopTime() {
        if (null != Timer) {
            Timer.cancel();
            Timer = null;
            WindowManager manager = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
            manager.removeView(blockingView);
        }
        if (points > 9) {
            if (apiClient.isConnected()) {
                Achievements.unlock(apiClient, getString(string.achievement_bier));
            } else Log.e(TAG, "is not connected");
        }
        if (points > 19) {
            if (apiClient.isConnected()) {
                Achievements.unlock(apiClient, getString(string.achievement_pizza));
            } else Log.e(TAG, "is not connected");
        }
        if (points > 29) {
            if (apiClient.isConnected()) {
                Achievements.unlock(apiClient, getString(string.achievement_schnitzel_und_pommes));
            } else Log.e(TAG, "is not connected");
        }
        if (points > 39) {
            if (apiClient.isConnected()) {
                Achievements.unlock(apiClient, getString(string.achievement_frozen_yogurt));
            } else Log.e(TAG, "is not connected");
        }
        if (points > 49) {
            if (apiClient.isConnected()) {
                Achievements.unlock(apiClient, getString(string.achievement_burger));
            }else Log.e(TAG, "is not connected");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case id.action_logout:
                firebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                AccessToken.setCurrentAccessToken(null);
                goLoginActivity();

                return true;
            case id.action_profile:
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);

                return true;
            case id.action_rewards:
                intent = new Intent(MainActivity.this, RewardActivity.class);
                startActivity(intent);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("points", points);
                editor.commit();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

// Notification for entering Beacon Range
    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[] { notifyIntent }, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }


    //Request for Permission
    private void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    private void goLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    //blocking back button by kiosk mode
    public void onBackPressed() {
        // … really
        // nothing to do here
    }

            // Eine Möglichkeit um den Navigationsbar zu unterbrechen
  /*  public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) {
            // Close every kind of system dialog
            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);

        }
    }
   //blocking volume buttons
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (blockedKeys.contains(event.getKeyCode())) {
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }*/

    private void startKioskService() { // ... and this method
        startService(new Intent(this, KioskService.class));
        createOverlay();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {

                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "Permission has been denied by user");
                } else {
                    Log.i(TAG, "Permission has been granted by user");
                }
                return;
            }
        }
    }

    //this class with the methode createOverlay is trying to block the navigation bar
    public class customViewGroup extends ViewGroup {

        public customViewGroup(Context context) {
            super(context);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            Log.v("customViewGroup", "**********Intercepted");
            return true;
        }
    }
        public void createOverlay(){

        WindowManager manager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        localLayoutParams.gravity = Gravity.TOP;
        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|

                // this is to enable the notification to receive touch events
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |

                // Draws over status bar
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        localLayoutParams.height = (int) (50 * getResources()
                .getDisplayMetrics().scaledDensity);
        localLayoutParams.format = PixelFormat.TRANSPARENT;

        blockingView = new customViewGroup(this);
        manager.addView(blockingView, localLayoutParams);

    }
    public void showAchievements(View v) {
       startActivityForResult(Achievements.getAchievementsIntent(apiClient), 1);
    }
}

