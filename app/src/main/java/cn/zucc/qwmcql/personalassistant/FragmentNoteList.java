package cn.zucc.qwmcql.personalassistant;

/**
 * Created by My PC on 2017/5/12.
 */

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import cn.zucc.qwmcql.personalassistant.bean.NoteBean;
import cn.zucc.qwmcql.personalassistant.db.DataBaseHelper;

public class FragmentNoteList extends Fragment implements CardsAdapter.InnerItemOnclickListener {
    private ListView mListView;
    private Cursor cursor;
    private SQLiteDatabase dbReader;
    private CardsAdapter cardsAdapter;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_note_plan,
                container, false);
        mListView = (ListView) rootView.findViewById(R.id.cards_list);
        mListView.setNestedScrollingEnabled(true);
        dbReader = DataBaseHelper.getInstance(getContext()).getReadableDatabase();
        selectDB();
        cardsAdapter = new CardsAdapter(getContext(), cursor);
        cardsAdapter.setOnInnerItemOnClickListener(this);
        mListView.setAdapter(cardsAdapter);
        return rootView;
    }

    public void selectDB() {
        cursor = dbReader.query("notes", null, null, null, null, null, null);
    }

    @Override
    public void itemClick(View v) {
        int position;
        position = (Integer) v.getTag();
        switch (v.getId()) {
            case R.id.card:
                cursor.moveToPosition(position);
                Activity mActivity = getActivity();
                Intent intent = new Intent(mActivity, NoteActivity.class);
                NoteBean note = new NoteBean();
                note.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                note.setContent(cursor.getString(cursor.getColumnIndex("content")));
                note.setTime(cursor.getString(cursor.getColumnIndex("time")));
                intent.putExtra("note", note);
                startActivity(intent);
                mActivity.finish();
                break;
            default:
                break;
        }
    }
}