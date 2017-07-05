package cn.zucc.qwmcql.personalassistant.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import cn.zucc.qwmcql.personalassistant.bean.IncomeCostBean;
import cn.zucc.qwmcql.personalassistant.bean.NoteBean;
import cn.zucc.qwmcql.personalassistant.bean.SchedulePlan;

public class DBServer {

    // 日程规划数据操作
    public static void addPlan(Context context, SchedulePlan note) {
        DataBaseDao dataBaseDao = DataBaseDao.getInstance(context);
        dataBaseDao.addSchedulePlan(note);
    }

    public static void deletePlanById(Context context, int id) {
        DataBaseDao dataBaseDao = DataBaseDao.getInstance(context);
        dataBaseDao.deleteSchedulePlanById(id);
    }

    public static void updatePlan(Context context, SchedulePlan plan) {
        DataBaseDao dataBaseDao = DataBaseDao.getInstance(context);
        dataBaseDao.updateSchedulePlan(plan);
    }

    public static ArrayList<SchedulePlan> searchPlanByDate(Context context, String date) {
        DataBaseDao dataBaseDao = DataBaseDao.getInstance(context);
        return dataBaseDao.searchPlanByDate(date);
    }

    public static SchedulePlan searchPlanById(Context context, int id) {
        DataBaseDao dataBaseDao = DataBaseDao.getInstance(context);
        return dataBaseDao.searchSchedulePlanById(id);
    }

    //笔记数据操作
    public static void addNote(Context context, NoteBean note) {
        DataBaseDao dataBaseDao = DataBaseDao.getInstance(context);
        dataBaseDao.addNote(note);
    }

    public static void deleteNote(Context context, int id) {
        DataBaseDao dataBaseDao = DataBaseDao.getInstance(context);
        dataBaseDao.deleteNote(id);
    }

    public static void updataNote(Context context, NoteBean note) {
        DataBaseDao dataBaseDao = DataBaseDao.getInstance(context);
        dataBaseDao.updateNote(note);
    }

    public static void searchNote(Context context) {
        DataBaseDao dataBaseDao = DataBaseDao.getInstance(context);
        dataBaseDao.searchNote();
    }

    public static ArrayList<SchedulePlan> searchByIndistinct(Context context, String plan) {
        DataBaseDao dataBaseDao = DataBaseDao.getInstance(context);
        return dataBaseDao.searchByIndistinct(plan);
    }

    //取得收支的id
    public static int getIncomeCostId(Context context) {
        DataBaseDao dataBaseDao = DataBaseDao.getInstance(context);
        return dataBaseDao.getIncomeCostId();
    }

    //收支数据操作
    public static void addIncomeCost(Context context, IncomeCostBean cost) {
        DataBaseDao dataBaseDao = DataBaseDao.getInstance(context);
        dataBaseDao.addIncomeCost(cost);
    }

    public static void deleteIncomeCost(Context context, int id) {
        DataBaseDao dataBaseDao = DataBaseDao.getInstance(context);
        dataBaseDao.deleteIncomeCost(id);
    }

    public static void updataIncomeCost(Context context, IncomeCostBean cost) {
        DataBaseDao dataBaseDao = DataBaseDao.getInstance(context);
        dataBaseDao.updateIncomeCost(cost);
    }

    public static List<IncomeCostBean> searchIncomeCost(Context context) {
        DataBaseDao dataBaseDao = DataBaseDao.getInstance(context);
        return dataBaseDao.searchIncomeCost();
    }
}
