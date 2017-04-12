package com.itculturalfestival.smartcampus.ui.activity.team;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.itculturalfestival.smartcampus.R;
import com.itculturalfestival.smartcampus.ui.base.BaseActivity;

/**
 * @creation_time: 2017/4/10
 * @author: Vegen
 * @e-mail: vegenhu@163.com
 * @describe: 创建团队
 */

public class CreateTeamActivity extends BaseActivity {

    private TextView tv_next_step;

    @Override
    protected void onCreate() {
        initLayout(R.layout.activity_create_team_first_step);
        tv_next_step = getView(R.id.tv_next_step);
        tv_next_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), CreateTeamActivity2.class));
            }
        });
    }
}
