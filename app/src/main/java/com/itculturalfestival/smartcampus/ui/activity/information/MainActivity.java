package com.itculturalfestival.smartcampus.ui.activity.information;

import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.itculturalfestival.smartcampus.R;
import com.itculturalfestival.smartcampus.adapter.RecyclerAdapter;
import com.itculturalfestival.smartcampus.entity.InformationDB;
import com.itculturalfestival.smartcampus.other.loader.GlideImageLoader;
import com.itculturalfestival.smartcampus.ui.base.BaseActivity;
import com.itculturalfestival.smartcampus.ui.view.ArcPopupWindow;
import com.itculturalfestival.smartcampus.ui.view.RecyclerDecoration;
import com.itculturalfestival.smartcampus.util.LoginUtil;
import com.itculturalfestival.smartcampus.util.T;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @creation_time: 2017/3/27
 * @author: Vegen
 * @e-mail: vegenhu@163.com
 * @describe: MainActivity
 */

public class MainActivity extends BaseActivity {

    private boolean mIsExit;                //是否退出
    private Banner banner;                  //轮播控件
    private List<Integer> images;           //temp轮播图
    private List<String> titles;            //temp轮播配文
    private List<InformationDB> informationList;
    private RecyclerView rv_information;
    private SwipeRefreshLayout swipe_information;
    private RecyclerAdapter adapter;
//    private Toolbar toolbar;
    private boolean isLoading;              //是否在加载中
    private int end;                        //temp变量
    private FloatingActionButton fa_menu;
    private ArcPopupWindow arcPopupWindow;

    @Override
    protected void onCreate() {
        initLayout(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView(){
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
        banner = getView(R.id.banner);
        rv_information = getView(R.id.rv_information);
        swipe_information = getView(R.id.swipe_information);
        fa_menu = getView(R.id.fa_menu);
    }

    private void initData(){
//        initToolBar();
        initBanner();
        initRecyclerView();
        initSwipe();
        initMenu();
    }

//    private void initToolBar(){
//        setSupportActionBar(toolbar);
//        toolbar.setTitle(R.string.app_name);
//}

    @Override
    protected void onStart() {
        super.onStart();
        banner.startAutoPlay();     //开始轮播
    }

    @Override
    protected void onStop() {
        super.onStop();
        banner.stopAutoPlay();      //结束轮播
    }

    /**
     * 初始化菜单
     */
    private void initMenu(){
        fa_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (arcPopupWindow == null) {
                    arcPopupWindow = new ArcPopupWindow(getContext(), getActivity());
//                }
                arcPopupWindow.show();
            }
        });
    }

    private void initSwipe(){
        swipe_information.setColorSchemeResources(R.color.colorTheme);
        swipe_information.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshInformation();
            }
        });
    }

    /**
     * 下拉刷新数据
     */
    private void refreshInformation(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 10; i >=0; i --){
                            InformationDB informationBean = new InformationDB(LoginUtil.getUserId(getContext()),"音乐协会专场表演音乐协会专场表演音乐协会专场表演" + (-i)
                                    ,"helldhsdhasd", "http://1b6093f923.51mypc.cn/res/2017/02/14872303862009c0e1b55.jpg", "",
                                    new Date(), "音乐爱好者协会高音部", 1, "体育类");
                            informationList.add(0, informationBean);
                        }
                        adapter.notifyDataSetChanged();
//                        adapter.notifyItemRemoved(adapter.getItemCount());
                        swipe_information.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    /**
     * 上拉加载更多
     */
    private void loadMore(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = end; i < end + 10; i ++){
                            InformationDB informationBean = new InformationDB(LoginUtil.getUserId(getContext()),"音乐协会专场表演音乐协会专场表演音乐协会专场表演" + (i + 20)
                                    ,"helldhsdhasd", "http://1b6093f923.51mypc.cn/res/2017/02/14872303862009c0e1b55.jpg", "",
                                    new Date(), "音乐爱好者协会钢琴组", 1, "体育类");
                            informationList.add(informationBean);
                        }
                        end += 10;
                        Log.e("RecyclerView", "load more completed, end = " + end);
                        isLoading = false;
                        adapter.notifyDataSetChanged();
                        swipe_information.setRefreshing(false);
                        adapter.notifyItemRemoved(adapter.getItemCount());
                    }
                });
            }
        }).start();
    }

    private void initRecyclerView(){
        initInformationList();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_information.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(getContext(), informationList);
        rv_information.setAdapter(adapter);
        rv_information.addItemDecoration(new RecyclerDecoration(getContext(), RecyclerDecoration.VERTICAL_LIST));

        //上拉加载更多
        rv_information.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                Log.e("RecyclerView", "StateChanged = " + newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                Log.e("onScrolled", "lastVisibleItemPosition+1=" + (1+lastVisibleItemPosition) + "  adapter.getItemCount()=" + adapter.getItemCount());
                if (lastVisibleItemPosition + 1 == adapter.getItemCount()) {
                    Log.d("test", "loading executed");
                    boolean isRefreshing = swipe_information.isRefreshing();
                    if (isRefreshing) {
                        adapter.notifyItemRemoved(adapter.getItemCount());
                        return;
                    }
                    if (!isLoading) {
                        isLoading = true;
                        loadMore();
                    }
                }
            }
        });

        //item的点击事件
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                T.showShort(getContext(),"item position = " + position);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    /**
     * 初始化资讯数据
     */
    private void initInformationList(){
        informationList = new ArrayList<>();
        end = 0;
        for (int i = end; i < end + 10; i ++){
            InformationDB informationBean = new InformationDB(LoginUtil.getUserId(getContext()),"音乐协会专场表演音乐协会专场表演音乐协会专场表演" + (i)
                    ,"helldhsdhasd", "http://1b6093f923.51mypc.cn/res/2017/02/14872303862009c0e1b55.jpg", "",
                    new Date(), "音乐爱好者协会流行音乐组", 1, "体育类");
            informationList.add(informationBean);
        }
        end += 10;
    }

    /**
     * 初始化轮播图
     */
    private void initBanner(){
        images = new ArrayList<>();
        images.add(R.mipmap.banner1);
        images.add(R.mipmap.banner2);
        images.add(R.mipmap.banner3);
        images.add(R.mipmap.banner4);
        images.add(R.mipmap.banner5);
        titles = new ArrayList<>();
        titles.add("音乐协会专场表演");
        titles.add("摄影协会招新");
        titles.add("读书协会讲座");
        titles.add("话剧协会");
        titles.add("大学生记者团");
//        informationBeanList = new ArrayList<>();
//        for (int i = 0; i < 5; i ++){
//            InformationDB informationBean = new InformationDB();
//            informationBean.setTitle("");
//            informationBean.setIllustration("");
//        }

        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Default);
        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(4000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        //banner的点击事件
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                T.showShort(getContext(), "你点击了第" + (position + 1) + "张轮播图，标题：" + titles.get(position));
            }
        });
    }

    /**
     * 提示再按一次退出
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mIsExit) {
                this.finish();
            } else {
                T.showShort(getContext(), getRString(R.string.exit_tip));
                mIsExit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIsExit = false;
                    }
                }, 2000);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
