package com.itculturalfestival.smartcampus.ui.activity.team;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.itculturalfestival.smartcampus.R;
import com.itculturalfestival.smartcampus.ui.base.BaseActivity;

/**
 * @creation_time: 2017/4/12
 * @author: Vegen
 * @e-mail: vegenhu@163.com
 * @describe: 团队介绍页
 */

public class TeamIntroduceActivity extends BaseActivity {

    private TextView tv_information;

    @Override
    protected void onCreate() {
        initLayout(R.layout.activity_team_introduce);
        tv_information = getView(R.id.tv_information);
        tv_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TeamInformationActivity.actionStart(getContext(), 1);
            }
        });
    }

    /**
     * 暴露的跳转方法
     * @param context
     * @param team_id
     */
    public static void actionStart(Context context, int team_id){
        Intent intent = new Intent(context, TeamIntroduceActivity.class);
        intent.putExtra("team_id", team_id);
        context.startActivity(intent);
    }
}
