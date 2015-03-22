package com.zsb.lostandfound;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zsb.lostandfound.bean.Found;
import com.zsb.lostandfound.bean.Lost;

import cn.bmob.v3.listener.SaveListener;

/**
 * 添加失物招领信息界面
 * Created by zsb on 2015/3/22
 */
public class AddActivity extends BaseActivity implements View.OnClickListener {
    EditText edit_title, edit_phone, edit_describe;
    Button btn_back, btn_true;

    TextView tv_add;
    String from = "";

    String old_title = "";
    String old_describe = "";
    String old_phone = "";

    String title = "";
    String describe = "";
    String phone = "";

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_add);
    }

    @Override
    public void initViews() {
        tv_add = (TextView) findViewById(R.id.tv_add);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_true = (Button) findViewById(R.id.btn_true);
        edit_phone = (EditText) findViewById(R.id.edit_phone);
        edit_describe = (EditText) findViewById(R.id.edit_describe);
        edit_title = (EditText) findViewById(R.id.edit_title);
    }

    /**
     * 进行数据初始化
     */
    @Override
    public void initData() {
        from = getIntent().getStringExtra("from");
        old_title = getIntent().getStringExtra("title");
        old_describe = getIntent().getStringExtra("describe");
        old_phone = getIntent().getStringExtra("phone");

        edit_title.setText(old_title);
        edit_describe.setText(old_describe);
        edit_phone.setText(old_phone);

        if (from.equals("Lost"))
            tv_add.setText("添加失物信息");
        else
            tv_add.setText("添加招领信息");
    }

    @Override
    public void initListeners() {
        btn_back.setOnClickListener(this);
        btn_true.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_true)
            addByType();
        else if (v == btn_back)
            finish();
    }

    /**
     * 根据类型添加失物/招领
     */
    private void addByType() {
        title = edit_title.getText().toString();
        describe = edit_describe.getText().toString();
        phone = edit_phone.getText().toString();

        if (TextUtils.isEmpty(title)) {
            showToast("请填写标题");
            return;
        }
        if (TextUtils.isEmpty(describe)) {
            showToast("请填写描述");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            showToast("请填写手机");
            return;
        }

        if (from.equals("Lost")) {
            addLost();
        } else {
            addFound();
        }
    }

    /**
     * 添加招领信息
     */
    private void addFound() {
        Found found = new Found();
        found.setDescribe(describe);
        found.setPhone(phone);
        found.setTitle(title);
        found.save(this, new SaveListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                showToast("招领信息添加成功!");
                //return success to the caller 
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(int code, String arg0) {
                // TODO Auto-generated method stub
                showToast("添加失败:" + arg0);
            }
        });
    }

    /**
     * 添加失物信息
     */
    private void addLost() {
        Lost lost = new Lost();
        lost.setDescribe(describe);
        lost.setPhone(phone);
        lost.setTitle(title);
        lost.save(this, new SaveListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                showToast("失物信息添加成功!");
                //return success to the caller 
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(int code, String arg0) {
                // TODO Auto-generated method stub
                showToast("添加失败:" + arg0);
            }
        });
    }
}
