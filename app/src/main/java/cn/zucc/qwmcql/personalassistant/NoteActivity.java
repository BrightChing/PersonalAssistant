package cn.zucc.qwmcql.personalassistant;

/**
 * Created by My PC on 2017/5/17.
 */

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.zucc.qwmcql.personalassistant.db.DataBaseHelper;

public class NoteActivity extends AppCompatActivity {
    private TextView s_tv;
    private TextView t_tv;
    private LinearLayout linearLayout;
    private SQLiteDatabase dbWriter;
    CoordinatorLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        container = (CoordinatorLayout) findViewById(R.id.coor1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(NoteActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        dbWriter = DataBaseHelper.getInstance(this).getWritableDatabase();
        s_tv = (TextView) findViewById(R.id.textview2);
        t_tv = (TextView) findViewById(R.id.textView1);
        linearLayout = (LinearLayout) findViewById(R.id.lay1);
        s_tv.setText(getIntent().getStringExtra("content"));
        t_tv.setText(getIntent().getStringExtra("time"));
        linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                editdialog();
                return true;
            }
        });
    }

    private void deleteDate() {
        dbWriter.delete("notes", "_id=" + getIntent().getIntExtra("_id", 0), null);
    }

    public void showDialog() {
        final BottomSheetDialog dialog = new BottomSheetDialog(NoteActivity.this);
        View dialogView = LayoutInflater.from(NoteActivity.this)
                .inflate(R.layout.bottomsheetdialog, null);
        TextView tvedit = (TextView) dialogView.findViewById(R.id.tv_edit);
        TextView tvdelete = (TextView) dialogView.findViewById(R.id.tv_delete);
        TextView tvCancel = (TextView) dialogView.findViewById(R.id.tv_cancel);
        tvdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(NoteActivity.this);
                builder.setTitle("删除提示");
                builder.setMessage("真的要删除吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteDate();
                        Snackbar.make(container, "删除成功", Snackbar.LENGTH_LONG).show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Intent intent = new Intent(NoteActivity.this, MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                finish();
                            }
                        }).start();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();
                dialog.dismiss();
            }
        });
        tvedit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                editdialog();
                dialog.dismiss();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(dialogView);
        dialog.show();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent(NoteActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void editdialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialog1 = inflater.inflate(R.layout.editdialog, (ViewGroup) findViewById(R.id.dialog));
        final EditText editText = (EditText) dialog1.findViewById(R.id.et);
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(NoteActivity.this);
        builder.setTitle("修改笔记");
        builder.setView(dialog1);
        editText.setText(getIntent().getStringExtra("content"));
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ContentValues cv = new ContentValues();
                cv.put("", editText.getText().toString());
                dbWriter.update("", cv, "_id=?", new String[]{String.valueOf(getIntent().getIntExtra("_id",0))});
                Snackbar.make(container, "修改成功", Snackbar.LENGTH_LONG).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(NoteActivity.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    }
                }).start();
            }
        });
        builder.show();
    }
}
