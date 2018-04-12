package com.example.phihai.ShutPhone.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phihai.ShutPhone.Adapter.RewardAdapter;
import com.example.phihai.ShutPhone.Helper.Achievements;
import com.example.phihai.ShutPhone.R;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Phi Hai on 07.11.2017.
 */

public class RewardActivity extends AppCompatActivity {
    private static final String TAG = "RewardActivity" ;
    private ListView lvAchievement;
    private RewardAdapter adapter;
    private List<Achievements> mAchievementsList;
    SharedPreferences sharedPreferences;
    TextView pointsView2;
    TextView showPoints2;
    public int points;
    FirebaseAuth firebaseAuth;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);
        pointsView2 = (TextView) findViewById(R.id.points_view2);
        showPoints2 = (TextView) findViewById(R.id.showPoints2);
        Context context = this;
        sharedPreferences = context.getSharedPreferences(
                "ShutPhone", Context.MODE_PRIVATE);
        points = sharedPreferences.getInt("points", 0);

        pointsView2.setText(String.valueOf(points));
        lvAchievement = (ListView) findViewById(R.id.listview_achievements);
        mAchievementsList = new ArrayList<>();
        mAchievementsList.add(new Achievements(1, "Bier", "reach 2 hours!", 10 ));
        mAchievementsList.add(new Achievements(2, "Pizza", "reach 4 hours!", 20 ));
        mAchievementsList.add(new Achievements(3, "Burger", "reach 6 hours!", 30 ));
        mAchievementsList.add(new Achievements(4, "Schnitzel und Pommes", "reach 8 hours!", 40 ));
        mAchievementsList.add(new Achievements(5, "Eis nach Wahl", "reach 10 hours!", 50 ));

        //Init adapter
        adapter = new RewardAdapter(getApplicationContext(), mAchievementsList);
        lvAchievement.setAdapter(adapter);
        lvAchievement.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object viewTag = view.getTag();
                Integer tagi = (Integer) viewTag;

                if(tagi == 1 && points >= 10){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("points", points-=10);
                    editor.commit();
                    pointsView2.setText("" +points);
                    Toast.makeText(getApplicationContext(), "You got Bier!", Toast.LENGTH_LONG).show();
                }else if (tagi == 2 && points >= 20){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("points", points-=20);
                    editor.commit();
                    pointsView2.setText(""+points );
                    Toast.makeText(getApplicationContext(), "You got Pizza!", Toast.LENGTH_LONG).show();
                }else if (tagi == 3 && points >= 30){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("points", points-=30);
                    editor.commit();
                    pointsView2.setText("" +points);
                    Toast.makeText(getApplicationContext(), "You got Burger!", Toast.LENGTH_LONG).show();
                }else if (tagi == 4 && points >= 40){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("points", points-=40);
                    editor.commit();
                    pointsView2.setText("" +points);
                    Toast.makeText(getApplicationContext(), "You got Schnitzel und Pommes!", Toast.LENGTH_LONG).show();
                }else if(tagi== 5 && points >= 50){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("points", points-=50);
                    editor.commit();
                    pointsView2.setText("" +points);
                    Toast.makeText(getApplicationContext(), "You got Eis nach Wahl!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "You have not enough Points.", Toast.LENGTH_LONG).show();
                }
                Log.e(TAG, "points: " +points);
            }
        });

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
                Intent intent = new Intent(RewardActivity.this, ProfileActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_timer:
                intent = new Intent(RewardActivity.this, MainActivity.class);
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
