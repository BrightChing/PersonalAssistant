package cn.zucc.qwmcql.personalassistant;

/**
 * Created by My PC on 2017/5/19.
 */

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;

import cn.zucc.qwmcql.personalassistant.db.DBServer;
import cn.zucc.qwmcql.personalassistant.bean.SchedulePlan;
import cn.zucc.qwmcql.personalassistant.alarmReceive.*;

public class EditPlanActivity extends Activity {

    private RelativeLayout etNoteLayout = null;
    private EditText etNoteTitle = null;
    private Button btnSave = null;
    private Button btnDelete = null;
    private Button btnEdit = null;
    private String planTitle = "", planDate = "", planHour = "", planMinutes = "", planPost = "";
    private SchedulePlan currPlan = null;
    private String uiType = "";
    private ImageButton imageButton;
    private TimePicker timePicker;
    private TextView dateTitle, picker, timeShower, postScript;
    private CheckBox checkBox;
    private int hour, minutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit_note);
        initView();
        uiType = getIntent().getStringExtra("uiType");
        switch (uiType) {
            case "newPlan":
                showNewNoteUI();
                currPlan=new SchedulePlan();
                break;
            case "showPlan":
                showTextNoteUI();
                break;
            default:
                break;
        }
    }

    private void showTextNoteUI() {
        currPlan = (SchedulePlan) getIntent().getSerializableExtra("plan");
        etNoteLayout.setVisibility(View.VISIBLE);
        etNoteTitle.setText(currPlan.getTitle());
        etNoteTitle.setEnabled(false);
        postScript.setEnabled(false);
        postScript.setText(currPlan.getPostScript());
        timePicker.setVisibility(View.GONE);
        if (Integer.valueOf(currPlan.getMinutes()).intValue() < 10) {
            timeShower.setText(currPlan.getHour() + ":0" + currPlan.getMinutes());
        } else
            timeShower.setText(currPlan.getHour() + ":" + currPlan.getMinutes());
        btnSave.setVisibility(View.GONE);
        btnDelete.setVisibility(View.VISIBLE);
        btnEdit.setVisibility(View.VISIBLE);

        dateTitle.setVisibility(View.VISIBLE);
        picker.setVisibility(View.VISIBLE);
        checkBox.setEnabled(false);
    }

    private void showNewNoteUI() {
        etNoteLayout.setVisibility(View.VISIBLE);
        btnSave.setVisibility(View.VISIBLE);
        btnDelete.setVisibility(View.GONE);
        btnEdit.setVisibility(View.GONE);
        timePicker.setVisibility(View.VISIBLE);
        timePicker.setEnabled(true);
        timeShower.setVisibility(View.GONE);
        timePicker.setIs24HourView(Boolean.TRUE);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minutes) {
                EditPlanActivity.this.hour = hour;
                EditPlanActivity.this.minutes = minutes;
            }
        });
    }

    private void showEditNoteUI() {
        etNoteLayout.setVisibility(View.VISIBLE);
        btnSave.setVisibility(View.VISIBLE);
        btnDelete.setVisibility(View.GONE);
        btnEdit.setVisibility(View.GONE);
        timePicker.setCurrentHour(Integer.valueOf(currPlan.getHour()));
        timePicker.setCurrentMinute(Integer.valueOf(currPlan.getMinutes()));
        timePicker.setIs24HourView(Boolean.TRUE);
        timePicker.setVisibility(View.VISIBLE);
        etNoteTitle.setEnabled(true);
        postScript.setEnabled(true);
        checkBox.setEnabled(true);
    }


    private void initView() {
        etNoteLayout = (RelativeLayout) findViewById(R.id.etNoteLayout);
        etNoteTitle = (EditText) findViewById(R.id.etNoteTitle);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnEdit = (Button) findViewById(R.id.btnEdit);
        imageButton = (ImageButton) findViewById(R.id.imb2);
        timeShower = (TextView) findViewById(R.id.timeshower);
        timePicker = (TimePicker) findViewById(R.id.timepicker);
        dateTitle = (TextView) findViewById(R.id.datetitle);
        picker = (TextView) findViewById(R.id.datecontent);
        postScript = (TextView) findViewById(R.id.postEdit);
        checkBox = (CheckBox) findViewById(R.id.checkBox);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                    savePlan();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                deletePlan(currPlan);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showEditNoteUI();
            }
        });
    }
    
    private void savePlan() {
        planDate = getIntent().getStringExtra("planDate");
        planTitle = etNoteTitle.getText().toString();
        planHour = timePicker.getCurrentHour().toString();
        planMinutes = timePicker.getCurrentMinute().toString();
        planPost = postScript.getText().toString();
        if (!planTitle.equals("")) {
            currPlan.setTitle(planTitle);
            currPlan.setHour(planHour);
            currPlan.setMinutes(planMinutes);
            currPlan.setPostScript(planPost);
            if(planDate!=null&&!"".equals(planDate))
            {
                currPlan.setDate(planDate);
            DBServer.addPlan(this, currPlan);}
            else DBServer.updatePlan(this,currPlan);
            AlarmSetting();
            EditPlanActivity.this.finish();
        } else {
            Toast.makeText(getApplicationContext(), R.string.empty, Toast.LENGTH_SHORT).show();
        }
    }

    private void deletePlan(SchedulePlan plan) {
        DBServer.deletePlanById(this, plan.getId());
        cancelAlarm();
        EditPlanActivity.this.finish();
    }
    
    private void AlarmSetting() {
        Intent intent = new Intent(EditPlanActivity.this, AlarmReceiver.class);
        intent.putExtra("planTitle", currPlan.getTitle());
        intent.putExtra("planId", currPlan.getId());
        PendingIntent sender = PendingIntent.getBroadcast(EditPlanActivity.this, currPlan.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long firstTime = SystemClock.elapsedRealtime();    // 开机之后到现在的运行时间(包括睡眠时间)
        long systemTime = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8")); // 这里时区需要设置一下，不然会有8个小时的时间差
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        // 选择的每天定时时间
        long selectTime = calendar.getTimeInMillis();
        // 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
        if (systemTime > selectTime) {
            Toast.makeText(EditPlanActivity.this, "设置的时间小于当前时间", Toast.LENGTH_SHORT).show();
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            selectTime = calendar.getTimeInMillis();
        }
        // 计算现在时间到设定时间的时间差
        long time = selectTime - systemTime;
        firstTime += time;
        // 进行闹铃注册
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (checkBox.isChecked()) {
            manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    firstTime, 5 * 1000, sender);
            Toast.makeText(EditPlanActivity.this, "设置重复闹铃成功! ", Toast.LENGTH_LONG).show();
        } else {
            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, sender);
            Toast.makeText(EditPlanActivity.this, "设置闹铃成功! ", Toast.LENGTH_LONG).show();
        }
    }

    public void cancelAlarm() {
        Intent intent = new Intent(EditPlanActivity.this, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(EditPlanActivity.this,
                currPlan.getId(), intent, 0);
        // 取消闹铃
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(sender);
    }
}
