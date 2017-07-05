package cn.zucc.qwmcql.personalassistant;

/**
 * Created by My PC on 2017/5/12.
 */

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
        dbReader =  DataBaseHelper.getInstance(this.getActivity()).getReadableDatabase();
        selectDB();
        cardsAdapter.setOnInnerItemOnClickListener(this);
        mListView.setAdapter(cardsAdapter);
        return rootView;
    }

    public void selectDB() {
        cursor = dbReader.query("notes", null, null, null, null, null, null);
        cardsAdapter = new CardsAdapter(this.getActivity(), cursor);
    }

    @Override
    public void itemClick(View v) {
        int position;
        position = (Integer) v.getTag();
        switch (v.getId()) {
            case R.id.card:
                cursor.moveToPosition(position);
                Intent intent = new Intent(this.getActivity(), NoteActivity.class);

                intent.putExtra("_id", cursor.getInt(cursor.getColumnIndex("_id")));
                intent.putExtra("content", cursor.getString(cursor.getColumnIndex("content")));
                intent.putExtra("time", cursor.getString(cursor.getColumnIndex("time")));
                startActivity(intent);
                getActivity().finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // selectDB();
    }
}