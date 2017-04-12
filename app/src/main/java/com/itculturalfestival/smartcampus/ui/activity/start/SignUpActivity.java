package com.itculturalfestival.smartcampus.ui.activity.start;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itculturalfestival.smartcampus.R;
import com.itculturalfestival.smartcampus.entity.RecordDB;
import com.itculturalfestival.smartcampus.entity.UserDB;
import com.itculturalfestival.smartcampus.ui.base.BaseActivity;
import com.itculturalfestival.smartcampus.ui.view.CleanEditText;
import com.itculturalfestival.smartcampus.util.RegexUtil;
import com.itculturalfestival.smartcampus.util.T;
import com.itculturalfestival.smartcampus.util.VerifyCodeManager;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * @creation_time: 2017/4/11
 * @author: Vegen
 * @e-mail: vegenhu@163.com
 * @describe: 注册界面,一般会使用手机登录，通过获取手机验证码，跟服务器交互完成注册
 */

public class SignUpActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "SignupActivity";
    // 界面控件
    private CleanEditText phoneEdit;
    private CleanEditText passwordEdit;
    private CleanEditText verifyCodeEdit;
    private Button getVerifiCodeButton;
    private CleanEditText et_nickname;
    private VerifyCodeManager codeManager;
    private List<UserDB> userList;
    private LinearLayout ll_school;
    private TextView tv_school;

    @Override
    protected void onCreate() {
        setContentView(R.layout.activity_signup);
        initViews();
        codeManager = new VerifyCodeManager(this, phoneEdit, getVerifiCodeButton);
    }

    private void initViews() {
        et_nickname = getView(R.id.et_nickname);
        ll_school = getView(R.id.ll_school);
        tv_school = getView(R.id.tv_school);
        getVerifiCodeButton = getView(R.id.btn_send_verifi_code);
        getVerifiCodeButton.setOnClickListener(this);
        phoneEdit = getView(R.id.et_phone);
        phoneEdit.setImeOptions(EditorInfo.IME_ACTION_NEXT);// 下一步
        verifyCodeEdit = getView(R.id.et_verifiCode);
        verifyCodeEdit.setImeOptions(EditorInfo.IME_ACTION_NEXT);// 下一步
        passwordEdit = getView(R.id.et_password);
        passwordEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
        passwordEdit.setImeOptions(EditorInfo.IME_ACTION_GO);
        passwordEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                // 点击虚拟键盘的done
                if (actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_GO) {
                    commit();
                }
                return false;
            }
        });

        tv_school.setText("岭南师范学院");
        ll_school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("暂只开放岭南师范学院");
            }
        });
    }

    private void commit() {
        String phone = phoneEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();
        String code = verifyCodeEdit.getText().toString().trim();

        if (checkInput(phone, password, code)) {
            // TODO:请求服务端注册账号
            showToast("注册成功");
            UserDB user = new UserDB("", et_nickname.getText().toString(),"" , tv_school.getText().toString(), "", "", "",  "", phone, password, "");
            user.saveThrows();
            finish();
        }
    }

    private boolean checkInput(String phone, String password, String code) {
        boolean isDBExist = !DataSupport.limit(1).find(UserDB.class).isEmpty();
        if (isDBExist) {
            userList = DataSupport.where("tel = ? ", phone).find(UserDB.class);
            if (!userList.isEmpty()){
                showToast("该手机号已注册");
                return false;
            }
        }


        if (TextUtils.isEmpty(phone)) { // 电话号码为空
            T.showShort(this, R.string.tip_phone_can_not_be_empty);
        } else {
            if (!RegexUtil.checkMobile(phone)) { // 电话号码格式有误
                T.showShort(this, R.string.tip_phone_regex_not_right);
            } else if (TextUtils.isEmpty(code)) { // 验证码不正确
                T.showShort(this, R.string.tip_please_input_code);
            } else if (password.length() < 6 || password.length() > 32
                    || TextUtils.isEmpty(password)) { // 密码格式
                T.showShort(this,
                        R.string.tip_please_input_6_32_password);
            } else if (!verifyCodeEdit.getText().toString().equals("1234")){
                showToast("验证码不正确");
            }else if(TextUtils.isEmpty(et_nickname.getText().toString())){
                showToast("昵称不能为空");
            } else if(TextUtils.isEmpty(tv_school.getText().toString())){
                showToast("请选择学校");
            }else {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_verifi_code:
                // TODO 请求接口发送验证码

                if (codeManager.getVerifyCode(VerifyCodeManager.REGISTER)){
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
                                    showToast("收到验证码：1234");
                                    verifyCodeEdit.setText("1234");
                                }
                            });
                        }
                    }).start();
                }

                break;
            case R.id.btn_create_account:
                commit();
                break;
            default:
                break;
        }
    }

}
