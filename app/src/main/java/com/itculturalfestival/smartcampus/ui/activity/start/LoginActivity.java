package com.itculturalfestival.smartcampus.ui.activity.start;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.itculturalfestival.smartcampus.R;
import com.itculturalfestival.smartcampus.entity.UserDB;
import com.itculturalfestival.smartcampus.ui.activity.information.MainActivity;
import com.itculturalfestival.smartcampus.ui.base.BaseActivity;
import com.itculturalfestival.smartcampus.ui.view.CleanEditText;
import com.itculturalfestival.smartcampus.util.DateUtil;
import com.itculturalfestival.smartcampus.util.RegexUtil;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * @creation_time: 2017/4/11
 * @author: Vegen
 * @e-mail: vegenhu@163.com
 * @describe: 登录
 */

public class LoginActivity extends BaseActivity {
    private CleanEditText et_email_phone;
    private CleanEditText et_password;
    private Button btn_login;
    private List<UserDB> userList;
    private TextView tv_create_account;

    @Override
    protected void onCreate() {
        initLayout(R.layout.activity_login);
        initView();
    }

    private void initView(){
        et_email_phone = getView(R.id.et_email_phone);
        et_password = getView(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_create_account = getView(R.id.tv_create_account);
        et_email_phone.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        et_email_phone.setTransformationMethod(HideReturnsTransformationMethod
                .getInstance());

        et_password.setImeOptions(EditorInfo.IME_ACTION_DONE);
        et_password.setImeOptions(EditorInfo.IME_ACTION_GO);
        et_password.setTransformationMethod(PasswordTransformationMethod
                .getInstance());
        et_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_GO) {
                    clickLogin();
                }
                return false;
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickLogin();
            }
        });

        tv_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SignUpActivity.class));
            }
        });
    }

    private void clickLogin() {
        String account = et_email_phone.getText().toString();
        String password = et_password.getText().toString();
        if (checkInput(account, password)) {
            //登录账号验证
            //判断是否已存在
            boolean isDBExist = !DataSupport.limit(1).find(UserDB.class).isEmpty();
            if (isDBExist){
                userList = DataSupport.where("tel = ? and password = ?", account ,password).find(UserDB.class);
                if (!userList.isEmpty()){
                    SharedPreferences.Editor editor = getContext().getSharedPreferences("Login", MODE_PRIVATE).edit();
                    editor.putInt("id", userList.get(0).getId());
                    editor.putString("name", userList.get(0).getName());
                    editor.putString("sex", userList.get(0).getSex());
                    editor.putString("school", userList.get(0).getSchool());
                    editor.putString("faculty", userList.get(0).getFaculty());
                    editor.putString("major", userList.get(0).getMajor());
                    editor.putString("education", userList.get(0).getEducation());
                    editor.putString("e_mail", userList.get(0).getE_mail());
                    editor.putString("tel", userList.get(0).getTel());
                    editor.putString("enrollment_Year", userList.get(0).getEnrollment_Year());
                    editor.putString("login_time", DateUtil.textForNow2());
                    editor.commit();
                    startActivity(new Intent(getContext(), MainActivity.class));
                    finish();
                }else {
                    showToast("登录失败，手机号或密码错误");
                }
            }else {
                showToast("登录失败，手机号或密码错误");
            }
        }
    }

    /**
     * 检查输入
     *
     * @param account
     * @param password
     * @return
     */
    public boolean checkInput(String account, String password) {
        // 账号为空时提示
        if (account == null || account.trim().equals("")) {
            Toast.makeText(this, R.string.tip_account_empty, Toast.LENGTH_LONG)
                    .show();
        } else {
            // 账号不匹配手机号格式（11位数字且以1开头）
            if ( !RegexUtil.checkMobile(account)) {
                Toast.makeText(this, R.string.tip_account_regex_not_right,
                        Toast.LENGTH_LONG).show();
            } else if (password == null || password.trim().equals("")) {
                Toast.makeText(this, R.string.tip_password_can_not_be_empty,
                        Toast.LENGTH_LONG).show();
            } else {
                return true;
            }
        }

        return false;
    }

}
