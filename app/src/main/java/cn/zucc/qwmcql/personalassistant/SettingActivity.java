package cn.zucc.qwmcql.personalassistant;

/**
 * Created by My PC on 2017/5/20.
 */

import android.app.UiModeManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {
    private Switch mSwitch;
    private UiModeManager mUiModeManager = null;
    int flag = 0;
    LinearLayout lay;
    LinearLayout lay1;
    String pass;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    EditText editText1, editText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        toolbar.setTitle("设置");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mUiModeManager = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);
        mSwitch = (Switch) findViewById(R.id.sw1);
        lay = (LinearLayout) findViewById(R.id.lay4);
        lay.setOnClickListener(new View.OnClickListener() {
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
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setView(dialog);
                builder.setTitle("设置登录密码");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String commit = preferences.getString("password", "");
                        if (editText1.getText().toString().equals(commit)) {
                            pass = editText2.getText().toString();
                            editor.putString("password", pass);
                            editor.apply();
                            Toast.makeText(SettingActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SettingActivity.this, "旧密码错误！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                editor.putInt("count", ++count);
                builder.show();
            }
        });
        lay1 = (LinearLayout) findViewById(R.id.lay3);
        lay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, HelpActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                if (isChecked) {
                    mUiModeManager.setNightMode(UiModeManager.MODE_NIGHT_YES);
                    flag = 1;
                } else {
                    mUiModeManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
                    flag = 0;
                }
                editor.putInt("flag", flag);
                editor.apply();
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent(SettingActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        int flag = preferences.getInt("flag", 2);
        if (flag == 1) {
            mSwitch.setChecked(true);
        } else
            mSwitch.setChecked(false);
    }
}
