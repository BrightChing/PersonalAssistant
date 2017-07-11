package cn.zucc.qwmcql.personalassistant;

/**
 * Created by My PC on 2017/5/22.
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.zucc.qwmcql.personalassistant.bean.NoteBean;
import cn.zucc.qwmcql.personalassistant.db.DBServer;

public class EditNoteActivity extends AppCompatActivity {
    private EditText editText;
    CoordinatorLayout container;
    ImageView content_img;
    private File phoneFile;
    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        container = (CoordinatorLayout) findViewById(R.id.coor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        toolbar.setTitle("笔记");
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(EditNoteActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        intView();
    }

    private void intView() {
        flag = getIntent().getIntExtra("flag", 0);
        editText = (EditText) findViewById(R.id.add_etv);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().equals("")) {
                    Snackbar.make(container, "真的不写一点东西吗？", Snackbar.LENGTH_LONG).setAction("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addNote();
                        }
                    }).show();
                } else {
                    addNote();
                }
            }
        });
        content_img = (ImageView) findViewById(R.id.content_img);
        content_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Camera();
            }
        });
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (flag == 1) {

        } else if (flag == 2) {
            Camera();
        }

    }

    private void addNote() {
        NoteBean noteBean = new NoteBean();
        noteBean.setTime(getTime());
        noteBean.setContent(editText.getText().toString());
        noteBean.setPath(phoneFile + "");
        DBServer.addNote(this, noteBean);
        Snackbar.make(container, "保存成功", Snackbar.LENGTH_LONG).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(EditNoteActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        }).start();
    }

    public String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyy年MM月dd日 HH:mm:ss");
        Date curDate = new Date();
        String str = format.format(curDate);
        return str;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent(EditNoteActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(phoneFile
                    .getAbsolutePath());
            content_img.setImageBitmap(bitmap);
        } else
            phoneFile = null;
    }
    public void Camera(){
        Intent img = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        phoneFile = new File(Environment.getExternalStorageDirectory()
                .getAbsoluteFile() + "/" + getTime() + ".jpg");
        img.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(phoneFile));
        startActivityForResult(img, 0);
    }
}
