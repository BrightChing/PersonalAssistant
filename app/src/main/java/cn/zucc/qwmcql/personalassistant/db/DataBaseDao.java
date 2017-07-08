package cn.zucc.qwmcql.personalassistant.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.zucc.qwmcql.personalassistant.bean.IncomeCostBean;
import cn.zucc.qwmcql.personalassistant.bean.NoteBean;
import cn.zucc.qwmcql.personalassistant.bean.SchedulePlan;

/**
 * Created by angelroot on 2017/7/3.
 */

public class DataBaseDao {
    private DataBaseHelper helper;
    private static DataBaseDao dataBaseDao;

    private DataBaseDao(Context context) {
        this.helper = DataBaseHelper.getInstance(context);
    }

    public static DataBaseDao getInstance(Context context) {
        if (dataBaseDao == null)
            dataBaseDao = new DataBaseDao(context);
        return dataBaseDao;

    }

    public void addIncomeCost(IncomeCostBean incomeCostBean) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("money", incomeCostBean.getMoney());
        cv.put("incomeCostDate", incomeCostBean.getIncomeCostDate());
        cv.put("incomeCostType", incomeCostBean.getIncomeCostType());
        cv.put("source", incomeCostBean.getSource());
        db.insert("cost", null, cv);
        db.close();
    }

    public void updateIncomeCost(IncomeCostBean incomeCostBean) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("money", incomeCostBean.getMoney());
        cv.put("incomeCostDate", incomeCostBean.getIncomeCostDate());
        cv.put("incomeCostType", incomeCostBean.getIncomeCostType());
        cv.put("source", incomeCostBean.getSource());
        db.update("cost", cv, "_id = ?", new String[]{String.valueOf(incomeCostBean.getId())});
        db.close();
    }

    public void deleteIncomeCost(int id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("cost", "_id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public List<IncomeCostBean> searchIncomeCost() {
        SQLiteDatabase db = helper.getWritableDatabase();
        List<IncomeCostBean> list = new ArrayList<>();
        Cursor cur = db.query("cost", null, null, null, null, null, "incomeCostDate asc");
        while (cur.moveToNext()) {
            IncomeCostBean ic = new IncomeCostBean();
            ic.setId(cur.getInt(cur.getColumnIndex("_id")));
            ic.setMoney(cur.getFloat(cur.getColumnIndex("money")));
            ic.setIncomeCostType(cur.getInt(cur.getColumnIndex("incomeCostType")));
            ic.setSource(cur.getString(cur.getColumnIndex("source")));
            ic.setIncomeCostDate(cur.getString(cur.getColumnIndex("incomeCostDate")));
            list.add(ic);
        }
        db.close();
        cur.close();
        return list;
    }
    public List<IncomeCostBean> searchIncome() {
        SQLiteDatabase db = helper.getWritableDatabase();
        List<IncomeCostBean> list = new ArrayList<>();
        Cursor cur = db.query("cost", null, "money > 0", null, null, null, "incomeCostDate asc");
        while (cur.moveToNext()) {
            IncomeCostBean ic = new IncomeCostBean();
            ic.setId(cur.getInt(cur.getColumnIndex("_id")));
            ic.setMoney(cur.getFloat(cur.getColumnIndex("money")));
            ic.setSource(cur.getString(cur.getColumnIndex("source")));
            ic.setIncomeCostDate(cur.getString(cur.getColumnIndex("incomeCostDate")));
            list.add(ic);
        }
        db.close();
        cur.close();
        return list;
    }

    public List<IncomeCostBean> searchCost() {
        SQLiteDatabase db = helper.getWritableDatabase();
        List<IncomeCostBean> list = new ArrayList<>();
        Cursor cur = db.query("cost", null, "money < 0", null, null, null, "incomeCostDate asc");
        while (cur.moveToNext()) {
            IncomeCostBean ic = new IncomeCostBean();
            ic.setId(cur.getInt(cur.getColumnIndex("_id")));
            ic.setMoney(cur.getFloat(cur.getColumnIndex("money")));
            ic.setSource(cur.getString(cur.getColumnIndex("source")));
            ic.setIncomeCostDate(cur.getString(cur.getColumnIndex("incomeCostDate")));
            list.add(ic);
        }
        db.close();
        cur.close();
        return list;
    }

    public void addSchedulePlan(SchedulePlan plan) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", plan.getTitle());
        values.put("date", plan.getDate());
        values.put("hour", plan.getHour());
        values.put("minutes", plan.getMinutes());
        values.put("postscript", plan.getPostScript());
        db.insert("plan", "_id", values);
        db.close();
    }

    public ArrayList<SchedulePlan> searchPlanByDate(String date) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("plan", new String[]{"_id", "date", "title", "hour", "minutes", "postscript"},
                " date = ?", new String[]{date}, null, null, "_id asc");
        ArrayList<SchedulePlan> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            SchedulePlan plan = new SchedulePlan();
            plan.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            plan.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            plan.setDate(cursor.getString(cursor.getColumnIndex("date")));
            plan.setHour(cursor.getString(cursor.getColumnIndex("hour")));
            plan.setMinutes(cursor.getString(cursor.getColumnIndex("minutes")));
            plan.setPostScript(cursor.getString(cursor.getColumnIndex("postscript")));
            list.add(plan);
        }
        db.close();
        cursor.close();
        return list;
    }

    public SchedulePlan searchSchedulePlanById(int id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("plan", new String[]{"_id", "date", "title", "hour", "minutes",
                "postscript"}, " _id = ?", new String[]{String.valueOf(id)}, null, null, "_id asc");
        SchedulePlan plan = null;
        if (cursor.moveToNext()) {
            plan = new SchedulePlan();
            plan.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            plan.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            plan.setDate(cursor.getString(cursor.getColumnIndex("date")));
            plan.setHour(cursor.getString(cursor.getColumnIndex("hour")));
            plan.setMinutes(cursor.getString(cursor.getColumnIndex("minutes")));
            plan.setPostScript(cursor.getString(cursor.getColumnIndex("postscript")));
        }
        db.close();
        cursor.close();
        return plan;
    }

    public void deleteSchedulePlanById(int id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("plan", "_id=?", new String[]{String.valueOf(id)});
    }

    public void updateSchedulePlan(SchedulePlan plan) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("title", plan.getTitle());
        cv.put("date", plan.getDate());
        cv.put("hour", plan.getHour());
        cv.put("minutes", plan.getMinutes());
        cv.put("postscript", plan.getPostScript());
        db.update("plan", cv, "_id = ?", new String[]{String.valueOf(plan.getId())});
        Log.e("time",plan.getId()+" "+plan.getHour()+":"+plan.getMinutes());
        db.close();
    }

    public void addNote(NoteBean note) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("content", note.getContent());
        cv.put("time", note.getTime());
        db.insert("notes", null, cv);
        db.close();
    }

    public void deleteNote(int id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("notes", "_id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void updateNote(NoteBean note) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("content", note.getContent());
        cv.put("time", note.getTime());
        db.update("notes", cv, "_id=?", new String[]{String.valueOf(note.getId())});
        db.close();
    }

    public List<NoteBean> searchNote() {
        SQLiteDatabase db = helper.getWritableDatabase();
        List<NoteBean> list = new ArrayList<>();
        Cursor cur = db.query("notes", null, null, null, null, null, null);
        while (cur.moveToNext()) {
            NoteBean note = new NoteBean();
            note.setId(cur.getInt(cur.getColumnIndex("_id")));
            note.setContent(cur.getString(cur.getColumnIndex("content")));
            note.setTime(cur.getString(cur.getColumnIndex("time")));
            list.add(note);
        }
        db.close();
        cur.close();
        return list;
    }

    public ArrayList<SchedulePlan> searchByIndistinct(String title) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("plan", new String[]{"_id", "date", "title", "hour", "minutes", "postscript"},
                "title like '%" + title + "%'" + " or postscript like '%" + title + "%'", null, null, null, "_id asc");
        ArrayList<SchedulePlan> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            SchedulePlan plan = new SchedulePlan();
            plan.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            plan.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            plan.setDate(cursor.getString(cursor.getColumnIndex("date")));
            plan.setHour(cursor.getString(cursor.getColumnIndex("hour")));
            plan.setMinutes(cursor.getString(cursor.getColumnIndex("minutes")));
            plan.setPostScript(cursor.getString(cursor.getColumnIndex("postscript")));
            list.add(plan);
        }
        db.close();
        cursor.close();
        return list;
    }
    public int getIncomeCostId(){
       String sql="select * from cost order by _id desc limit 0,1";
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToNext();
        return cursor.getInt(cursor.getColumnIndex("_id"));
    }
}
