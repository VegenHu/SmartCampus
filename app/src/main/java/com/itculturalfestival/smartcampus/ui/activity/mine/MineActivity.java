package com.itculturalfestival.smartcampus.ui.activity.mine;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.itculturalfestival.smartcampus.R;
import com.itculturalfestival.smartcampus.entity.TeamDB;
import com.itculturalfestival.smartcampus.ui.activity.information.MainActivity;
import com.itculturalfestival.smartcampus.ui.activity.team.TeamIntroduceActivity;
import com.itculturalfestival.smartcampus.ui.base.BaseActivity;
import com.itculturalfestival.smartcampus.ui.view.ArcPopupWindow;
import com.itculturalfestival.smartcampus.ui.view.CircleImageView;
import com.itculturalfestival.smartcampus.ui.view.RecyclerDecoration;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @creation_time: 2017/4/10
 * @author: Vegen
 * @e-mail: vegenhu@163.com
 * @describe: 我的的首页
 */

public class MineActivity extends BaseActivity {

    private FloatingActionButton fa_menu;
    private ArcPopupWindow arcPopupWindow;
    private RecyclerView rv_team_list;
    private List<TeamDB> teamList;
    private LinearLayout my_data;
    private CircleImageView civ_head;

    @Override
    protected void onCreate() {
        initLayout(R.layout.activity_mine);
        initView();
        initData();
    }

    private void initView(){
        fa_menu = getView(R.id.fa_menu);
        rv_team_list = getView(R.id.rv_team_list);
        my_data = getView(R.id.my_data);
        civ_head = getView(R.id.civ_head);
    }

    private void initData(){
        Glide.with(getContext()).load("http://1b6093f923.51mypc.cn/res/2017/02/14872303862009c0e1b55.jpg").into(civ_head);

        initMenu();
        initRecyclerView();

        my_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MyDataActivity.class));
            }
        });
    }

    private void initRecyclerView(){
        teamList = new ArrayList<>();
//        teamList.add(new TeamDB("岭师计协", ));
//        teamList.add(new TeamDB("岭师教协", 1));
//        teamList.add(new TeamDB("信息学院宣传工作委员会", 2));
//        teamList.add(new TeamDB("14网络班", 3));
//        teamList.add(new TeamDB("15数媒班" ,4));
//        teamList.add(new TeamDB("信息工程学院", 5));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_team_list.setLayoutManager(layoutManager);
        final CommonAdapter commonAdapter =  new CommonAdapter<TeamDB>(this, R.layout.view_team_type, teamList) {
            @Override
            protected void convert(ViewHolder holder, TeamDB teamBean, int position) {
                holder.setText(R.id.tv_type, String.valueOf(teamBean.getId()));
            }
        };
        rv_team_list.setAdapter(commonAdapter);
        rv_team_list.addItemDecoration(new RecyclerDecoration(getContext(), RecyclerDecoration.VERTICAL_LIST));
        commonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                TeamIntroduceActivity.actionStart(getContext(), teamList.get(position).getId());
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * 初始化菜单
     */
    private void initMenu(){
        fa_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arcPopupWindow = new ArcPopupWindow(getContext(), getActivity());
                arcPopupWindow.show();
            }
        });
    }

//    public class TeamDB{
//        private String team_name;
//        private int team_number;
//
//        public TeamDB(){}
//
//        public TeamDB(String team_name, int team_number){
//            this.team_name = team_name;
//            this.team_number = team_number;
//        }
//
//        public String getTeam_name() {
//            return team_name;
//        }
//
//        public void setTeam_name(String team_name) {
//            this.team_name = team_name;
//        }
//
//        public int getTeam_number() {
//            return team_number;
//        }
//
//        public void setTeam_number(int team_number) {
//            this.team_number = team_number;
//        }
//    }
}
