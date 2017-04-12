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
 * @describe:
 */

public class CreateTeamActivity2 extends BaseActivity {

    private TextView tv_end_step;

    @Override
    protected void onCreate() {
        initLayout(R.layout.activity_create_team_end_step);
        tv_end_step = getView(R.id.tv_end_step);
        tv_end_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("创建团队成功");
                startActivity(new Intent(getContext(), TeamActivity.class));
            }
        });
    }
}
