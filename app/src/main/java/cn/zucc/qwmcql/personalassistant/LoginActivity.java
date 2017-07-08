package cn.zucc.qwmcql.personalassistant;

/**
 * Created by My PC on 2017/5/26.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {
    Button butttonSign, buttonedit;
    EditText editText, editText1, editText2;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;
    String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        butttonSign = (Button) findViewById(R.id.sign_in_button);
        buttonedit = (Button) findViewById(R.id.editpass);
        editText = (EditText) findViewById(R.id.password2);
        butttonSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferences = getSharedPreferences("password", Context.MODE_PRIVATE);
                String commit = preferences.getString("password", "");
                if (editText.getText().toString().equals(commit)) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();
                        }
                    }).start();
                    Toast.makeText(LoginActivity.this, "登陆成功！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "密码错误！请重试", Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View dialog = inflater.inflate(R.layout.dialog_password, (ViewGroup) findViewById(R.id.pass));
                preferences = getSharedPreferences("password", Context.MODE_PRIVATE);
                editor = preferences.edit();
                int count = preferences.getInt("count", 0);
                if (count == 0) {
                    editor.putString("password", "");
                }
                editText1 = (EditText) dialog.findViewById(R.id.password);
                editText2 = (EditText) dialog.findViewById(R.id.repassword);

                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(LoginActivity.this);
                builder.setView(dialog);
                builder.setTitle("设置登录密码");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String commit = preferences.getString("password", "");
                        if (editText1.getText().toString().equals(commit)) {
                            pass = editText2.getText().toString();
                            editor.putString("password", pass);
                            editor.commit();
                            Toast.makeText(LoginActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "旧密码错误！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                editor.putInt("count", ++count);
                builder.show();
            }
        });

    }
}

