package cn.zucc.qwmcql.personalassistant.db;

import java.util.ArrayList;
import android.content.Context;

import cn.zucc.qwmcql.personalassistant.bean.NoteBean;
import cn.zucc.qwmcql.personalassistant.bean.SchedulePlan;

public class DBServer {
    public static void addPlan(Context context, SchedulePlan note) {
        DataBaseDao dataBaseDao = DataBaseDao.getInstance(context);
        dataBaseDao.addSchedulePlan(note);
    }

    public static ArrayList<SchedulePlan> searchPlanByDate(Context context, String date) {
        DataBaseDao dataBaseDao = DataBaseDao.getInstance(context);
        return dataBaseDao.searchPlanByDate(date);
    }

    public static SchedulePlan searchPlanById(Context context, int id) {
        DataBaseDao dataBaseDao = DataBaseDao.getInstance(context);
        return dataBaseDao.searchSchedulePlanById(id);
    }

    public static void updatePlan(Context context, SchedulePlan note) {
        DataBaseDao dataBaseDao = DataBaseDao.getInstance(context);
        dataBaseDao.updateSchedulePlan(note);
    }

    public static void deletePlanById(Context context, int id) {
        DataBaseDao dataBaseDao = DataBaseDao.getInstance(context);
        dataBaseDao.deleteSchedulePlanById(id);
    }
    public  static void searchNote(Context context){
        DataBaseDao dataBaseDao = DataBaseDao.getInstance(context);
        dataBaseDao.searchNote();
    }
    public static void addNote(Context context, NoteBean note) {
        DataBaseDao dataBaseDao = DataBaseDao.getInstance(context);
        dataBaseDao.addNote(note);
    }
}
