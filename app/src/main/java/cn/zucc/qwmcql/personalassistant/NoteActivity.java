package cn.zucc.qwmcql.personalassistant;

/**
 * Created by My PC on 2017/5/17.
 */

import android.content.DialogInterface;
import android.content.Intent;
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

import cn.zucc.qwmcql.personalassistant.bean.NoteBean;
import cn.zucc.qwmcql.personalassistant.db.DBServer;

public class NoteActivity extends AppCompatActivity {
    private TextView s_tv;
    private TextView t_tv;
    private LinearLayout linearLayout;
    private NoteBean note;
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

        s_tv = (TextView) findViewById(R.id.content_text);
        t_tv = (TextView) findViewById(R.id.textView1);
        linearLayout = (LinearLayout) findViewById(R.id.lay1);
        note = (NoteBean) getIntent().getSerializableExtra("note");
        s_tv.setText(note.getContent());
        t_tv.setText(note.getTime());
        linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                editDialog();
                return true;
            }
        });
    }

    public void showDialog() {
        final BottomSheetDialog dialog = new BottomSheetDialog(NoteActivity.this);
        View dialogView = LayoutInflater.from(NoteActivity.this).inflate(R.layout.bottomsheet_dialog_note, null);
        TextView tvEdit = (TextView) dialogView.findViewById(R.id.tv_edit);
        TextView tvDelete = (TextView) dialogView.findViewById(R.id.tv_delete);
        TextView tvCancel = (TextView) dialogView.findViewById(R.id.tv_cancel);
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v7.app.AlertDialog.Builder builder =
                        new android.support.v7.app.AlertDialog.Builder(NoteActivity.this);
                builder.setTitle("删除提示");
                builder.setMessage("真的要删除吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBServer.deleteNote(NoteActivity.this, note.getId());//删除数据
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
        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog();
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

    public void editDialog() {
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
                note.setContent(editText.getText().toString());
                DBServer.updataNote(NoteActivity.this, note);//更新数据
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
