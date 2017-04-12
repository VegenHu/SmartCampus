package com.itculturalfestival.smartcampus.ui.activity.team;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hdl.mricheditor.bean.CamaraRequestCode;
import com.hdl.mricheditor.view.MRichEditor;
import com.itculturalfestival.smartcampus.R;
import com.itculturalfestival.smartcampus.ui.base.BaseActivity;
import com.itculturalfestival.smartcampus.util.runtimepermissions.PermissionsManager;
import com.itculturalfestival.smartcampus.util.runtimepermissions.PermissionsResultAction;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * @creation_time: 2017/4/12
 * @author: Vegen
 * @e-mail: vegenhu@163.com
 * @describe: 编辑资讯页
 */

public class WriteInformationActivity extends BaseActivity {

    private MRichEditor editor;
    private EditText etTitle;//文章标题对象
    private static final String IMG_URL = "/data/";//文件存放的路径

    @Override
    protected void onCreate() {
        initLayout(R.layout.activity_write_information);
        editor = getView(R.id.mre_editor);
        /**
         * 请求所有必要的权限----android6.0必须要动态申请权限,否则选择照片和拍照功能 用不了哦
         */
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {//权限通过了
            }

            @Override
            public void onDenied(String permission) {//权限拒绝了

            }
        });
        initMRichEditor();//初始化编辑器
    }

    /**
     * 初始化富文本编辑器
     */
    private void initMRichEditor() {
        etTitle = (EditText) findViewById(R.id.et_main_title);
        editor = (MRichEditor) findViewById(R.id.mre_editor);
        editor.setServerImgDir(IMG_URL);//设置图片存放的路径
        editor.setOnPreviewBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priview();//预览
            }
        });
    }

    /**
     * 预览
     */
    private void priview() {
        editor.setHtmlTitle(etTitle.getText().toString().trim());//设置html的标题
        String htmlStr = editor.createHtmlStr();//创建html字符串
        AlertDialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = View.inflate(getContext(), R.layout.dialog_preiview_html, null);
        final WebView wvPreiview = (WebView) view.findViewById(R.id.wv_dialog_preiview_html);
        ImageView ivClose = (ImageView) view.findViewById(R.id.iv_dialog_close);
        ImageView ivRefresh = (ImageView) view.findViewById(R.id.iv_dialog_refresh);
        ivRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wvPreiview.reload();
            }
        });
        wvPreiview.loadData(htmlStr, "text/html; charset=UTF-8", null);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
        final AlertDialog finalDialog = dialog;
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalDialog.dismiss();
            }
        });
        uploadImg();//上传图片
    }

    /**
     * 需要重写这个方法,并且加上下面的判断(照写即可)
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "取消操作", Toast.LENGTH_LONG).show();
            return;
        }
        if (requestCode == CamaraRequestCode.CAMARA_GET_IMG) {
            editor.insertImg(data.getData());
        } else if (requestCode == CamaraRequestCode.CAMARA_TAKE_PHOTO) {
            editor.insertImg(data);
        }
    }

    /**
     * 完成按钮---将文件和图片提交到服务器
     *
     * @param view
     */
    public void onFinished(View view) {
        editor.setHtmlTitle(etTitle.getText().toString().trim());//设置html的标题,在创建html文件之前,需要将文章的标题(即title标签)设置进去,之后设置无效.
        editor.createHtmlStr();//创建html字符串,会返回一个html字符串.[必须调用,否则内容为空]
        File file = editor.getHtmlFile("sdcard/test.html");//创建html文件,并设置保存的路径
        //添加List<File>的文件列表,用于MyHttpUtils多文件上传的参数.
        List<File> fileList = new ArrayList<>();
        fileList.add(file);
        for (String filePath : editor.getImgPath()) {
            fileList.add(new File(filePath));
        }
        showToast("发布成功");
        finish();
//        new MyHttpUtils()
//                .url(BASE_URL)//文件上传的接口 (url)
//                .addUploadFiles(fileList)//需上传的多个文件
//                .setJavaBean(UploadResult.class)//上传完成返回的json格式的数据对应的javabean对象
//                .uploadFileMult(new CommCallback<UploadResult>() {//执行多文件上传任务
//                    @Override
//                    public void onSucess(UploadResult uploadResult) {//成功之后回调
//                        Toast.makeText(getContext(), uploadResult.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onFailed(String msg) {//失败时候回调
//                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
//                    }
//                });
    }

    /**
     * 上传图片(这里用于实时预览,上传了图片才可以预览哦,否则看不到图片,只能看见文字)
     */
    private void uploadImg() {
        List<File> fileList = new ArrayList<>();
        for (String filePath : editor.getImgPath()) {
            fileList.add(new File(filePath));
        }
//        new MyHttpUtils()
//                .url(BASE_URL)
//                .addUploadFiles(fileList)
//                .setJavaBean(UploadResult.class)
//                .uploadFileMult(new CommCallback<UploadResult>() {
//                    @Override
//                    public void onSucess(UploadResult uploadResult) {
//                        Toast.makeText(getContext(), uploadResult.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onFailed(String msg) {
//                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
//                    }
//                });
        Log.e("MRichEditorDemo", editor.getHtmlStr());
    }

    /**
     * 打开帮助页面
     *
     * @param view
     */
    public void onHelp(View view) {
        Toast.makeText(this, "              操作手册\n点击-->修改(图片除外),长按-->删除", Toast.LENGTH_LONG).show();
    }
}
