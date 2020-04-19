package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.base.BaseActivity;
import com.example.base.DBOpenHelper;

/**
 * @author
 * @Date 2020-04-19 13:49
 * 功能：登录
 */
public class LoginActivity extends BaseActivity {


    private TextView mTvLogin;
    private EditText mEdtAddress;
    private EditText mEdtPassword;
    private TextView mTvPassword;

    private SQLiteDatabase db;
    private Cursor c;

    /*
     * 绑定布局
     * */
    @Override
    public int getLayout() {
        return R.layout.activity_login;
    }

    /*
     * 初始化控件
     * */
    @Override
    public void onBindView() {

        initView();

        /*
        * 创建数据库实例的地方，可以放到Application中
        * */
        DBOpenHelper helper = new DBOpenHelper(this, "poster");
        //获取只读数据库对象
        db = helper.getReadableDatabase();

    }

    private void initView() {
        mTvLogin = findViewById(R.id.tv_login);
        mEdtAddress = findViewById(R.id.edt_address);
        mTvPassword = findViewById(R.id.tv_password);
        mEdtPassword = findViewById(R.id.edt_password);


        mEdtAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                /*
                * 查询账号是否为已注册账号
                * */
                c = db.rawQuery("select * from personal_info", null);
                while (c.moveToNext()) {
                    if (c.getString(c.getColumnIndex("email_address")).equals(mEdtAddress.getText().toString())){
                        mTvPassword.setVisibility(View.VISIBLE);
                        mEdtPassword.setVisibility(View.VISIBLE);
                    }else {
                        mTvPassword.setVisibility(View.GONE);
                        mEdtPassword.setVisibility(View.GONE);
                    }
                }
                c.close();
            }
        });


        mTvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEdtAddress.getText().toString().equals("")){
                    Toast.makeText(getApplication(),"Please enter E-mail address !",Toast.LENGTH_SHORT).show();
                    return;
                }
                /*
                * 检测邮箱是否为正确格式
                * */
                if (!mEdtAddress.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z0-9]+.[a-z]+")){
                    Toast.makeText(getApplication(),"Please enter the correct format of E-mail address !",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mTvPassword.getVisibility()==View.VISIBLE){
                    /*
                    * 验证密码
                    * */
                    c = db.rawQuery("select password from personal_info where email_address='"+mEdtAddress.getText().toString()+"'",null);
                    String password = "";
                    while (c.moveToNext()){
                        password = c.getString(c.getColumnIndex("password"));
                    }
                    c.close();
                    if (password.equals("")){
                        Toast.makeText(getApplication(),"No query password !",Toast.LENGTH_SHORT).show();
                    }else {
                        if (password.equals(mEdtPassword.getText().toString())){
                            Intent intent = new Intent(getApplication(),MainActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(getApplication(),"Landing......",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplication(),"Password Error !",Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    /*
                    * 传address到注册页面
                    * */
                    Bundle bundle = new Bundle();
                    bundle.putString("address",mEdtAddress.getText().toString());
                    Intent intent = new Intent(getApplication(), SignUpActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭数据库连接对象，避免内存泄漏
        db.close();
    }
}
