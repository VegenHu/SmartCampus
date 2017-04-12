package com.itculturalfestival.smartcampus.ui.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

/**
 * @creation_time: 2017/4/3
 * @author: Vegen
 * @e-mail: vegenhu@163.com
 * @describe: 封装FragmentActivity
 */

public class BaseFragmentActivity extends FragmentActivity {
    public Context getContext(){
        return this;
    }

    public <T extends View> T getView(int viewId){
        return (T) findViewById(viewId);
    }

    public void setClickListener(int viewId, View.OnClickListener l){
        View view = getView(viewId);
        if (view != null) view.setOnClickListener(l);
    }

    /**
     * 切换Fragment
     */
    protected void switchFragment(int id, Fragment from, Fragment to){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (from != to){
            if (from != null) {
                transaction.hide(from);
            }
            if (!to.isAdded()){
                transaction.add(id, to);
            }else {
                transaction.show(to);
            }
        }
        transaction.commit();
    }
}
