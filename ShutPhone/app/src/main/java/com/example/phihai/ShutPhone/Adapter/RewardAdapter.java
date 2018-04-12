package com.example.phihai.ShutPhone.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.phihai.ShutPhone.Helper.Achievements;
import com.example.phihai.ShutPhone.R;

import java.util.List;

/**
 * Created by Phi Hai on 27.12.2017.
 */

public class RewardAdapter extends BaseAdapter {

    private Context mContext;
    private List<Achievements> mAchievementList;


    //Construtor
    public RewardAdapter(Context mContext, List<Achievements> mAchievementList) {
        this.mContext = mContext;
        this.mAchievementList = mAchievementList;
    }

    @Override
    public int getCount() {
        return mAchievementList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAchievementList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.item_reward_list, null);
        TextView achievementName = (TextView)v.findViewById(R.id.achievementName);
        TextView achievementDescription = (TextView)v.findViewById(R.id.achievementDescription);
        TextView achievementPoints = (TextView)v.findViewById(R.id.achievementPoints);

        //Set Text for TextView
        achievementName.setText(mAchievementList.get(position).getName());
        achievementDescription.setText(mAchievementList.get(position).getDescription());
        achievementPoints.setText(String.valueOf(mAchievementList.get(position).getGotPoints()) +  " Points");

        v.setTag(mAchievementList.get(position).getId());

        return v;
    }
}
