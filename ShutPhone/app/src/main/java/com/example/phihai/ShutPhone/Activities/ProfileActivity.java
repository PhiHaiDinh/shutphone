package com.example.phihai.ShutPhone.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.phihai.ShutPhone.R;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

//import com.facebook.AccessToken;
//import com.facebook.login.LoginManager;

/**
 * Created by Phi Hai on 07.11.2017.
 */

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity" ;
    FirebaseAuth firebaseAuth;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_logout:
                firebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                AccessToken.setCurrentAccessToken(null);
                goLoginActivity();

                return true;
            case R.id.action_profile:
                Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                startActivity(intent);

                return true;
            case R.id.action_rewards:
                intent = new Intent(ProfileActivity.this, RewardActivity.class);
                startActivity(intent);

                return true;

            case R.id.action_timer:
                intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void goLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
