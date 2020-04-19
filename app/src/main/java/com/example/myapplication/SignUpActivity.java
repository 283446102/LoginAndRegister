package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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
public class SignUpActivity extends BaseActivity {


    private EditText mEdtAddress;
    private EditText mEdtPassword;
    private EditText mEdtName;
    private EditText mEdtOrganization;
    private EditText mEdtPermission;
    private TextView mTvSignUp;


    private SQLiteDatabase db;

    /*
     * 绑定布局
     * */
    @Override
    public int getLayout() {
        return R.layout.activity_sign_up;
    }

    /*
     * 初始化控件
     * */
    @Override
    public void onBindView() {
        initView();
        /*
        * 获取登录页面传来的address，并将address填入地址编辑
        * */
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mEdtAddress.setText(bundle.getString("address"));
        }
        DBOpenHelper helper = new DBOpenHelper(this, "poster");
        db = helper.getWritableDatabase();//获取读写的数据库

    }

    private void initView() {
        mEdtAddress = findViewById(R.id.edt_address);
        mEdtPassword = findViewById(R.id.edt_password);
        mEdtName = findViewById(R.id.edt_name);
        mEdtOrganization = findViewById(R.id.edt_organization);
        mEdtPermission = findViewById(R.id.edt_permission);
        mTvSignUp = findViewById(R.id.tv_sign_up);


        mTvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEdtAddress.getText().toString().equals("")) {
                    Toast.makeText(getApplication(), "E-mail address is null", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!mEdtAddress.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z0-9]+.[a-z]+")) {
                    Toast.makeText(getApplication(), "Please enter the correct format of E-mail address !", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mEdtPassword.getText().toString().equals("")) {
                    Toast.makeText(getApplication(), "Password is null", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mEdtName.getText().toString().equals("")) {
                    Toast.makeText(getApplication(), "Name is null", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mEdtOrganization.getText().toString().equals("")) {
                    Toast.makeText(getApplication(), "Organization is null", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mEdtPermission.getText().toString().equals("")) {
                    Toast.makeText(getApplication(), "Posting Permission is null", Toast.LENGTH_SHORT).show();
                    return;
                }

                /*
                * 第一次查询是为了查询当前address是否为已注册，若为已注册，则不能继续进行注册
                * */
                Cursor c = db.rawQuery("select * from personal_info", null);
                boolean flag = false;
                while (c.moveToNext()) {
                    if (c.getString(c.getColumnIndex("email_address")).equals(mEdtAddress.getText().toString())){
                        flag = true;
                        break;
                    }
                }
                c.close();
                if (flag){
                    Toast.makeText(getApplication(), "The E-mail is Already registered !", Toast.LENGTH_SHORT).show();
                    return;
                }

                /*
                * 将账号信息保存到数据库
                * */
                db.execSQL("insert into personal_info(email_address,password,name,organization,posting_permission)" +
                        "values('" + mEdtAddress.getText().toString() + "'," +
                        "'" + mEdtPassword.getText().toString() + "'," +
                        "'" + mEdtName.getText().toString() + "'," +
                        "'" + mEdtOrganization.getText().toString() + "'," +
                        "'" + mEdtPermission.getText().toString() + "')");

                /*
                * 第二次查询是为了查询该账号信息是否插入到数据库里面
                * */
                Cursor c1 = db.rawQuery("select * from personal_info", null);
                boolean flag1 = false;
                while (c1.moveToNext()) {
                    if (c1.getString(c1.getColumnIndex("email_address")).equals(mEdtAddress.getText().toString())){
                        flag1 = true;
                        break;
                    }
                }
                c1.close();
                if (flag1){
                    Toast.makeText(getApplication(), "Sign up successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplication(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplication(), "Sign up failed", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
