package cn.zucc.qwmcql.personalassistant;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.zucc.qwmcql.personalassistant.db.DBServer;
import cn.zucc.qwmcql.personalassistant.bean.SchedulePlan;

public class SearchActivity extends AppCompatActivity {
    ArrayList<SchedulePlan> planData = new ArrayList<>();
    String SearchPlan = "";
    ListView searchView;
    SearchView searchBox;
    TextView dialogTitle, dialogDate, dialogPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_serach);
        toolbar.setTitle("日程搜索");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        initView();
        refreshNoteList();
    }

    private void initView() {
        searchView = (ListView) findViewById(R.id.serach_results);
        searchBox = (SearchView) findViewById(R.id.search_box);
        searchView.setTextFilterEnabled(true);
        searchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)) {
                    SearchPlan = newText;
                    refreshNoteList();
                }
                if (TextUtils.isEmpty(newText)) {
                    SearchPlan = newText;
                    refreshNoteList();
                } else {
                    searchView.clearTextFilter();
                }
                return false;
            }
        });
        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialog = inflater.inflate(R.layout.date_resource, (ViewGroup) findViewById(R.id.dateResource));
                dialogTitle = (TextView) dialog.findViewById(R.id.dialogTitle);
                dialogDate = (TextView) dialog.findViewById(R.id.dialogDate);
                dialogPost = (TextView) dialog.findViewById(R.id.dialogPost);
                dialogTitle.setText(planData.get(position).getTitle());
                if (Integer.valueOf(planData.get(position).getMinutes()) <= 10)
                    dialogDate.setText(planData.get(position).getDate() + "  " + planData.get(position).getHour() + ":0" + planData.get(position).getMinutes());
                else
                    dialogDate.setText(planData.get(position).getDate() + "  " + planData.get(position).getHour() + ":" + planData.get(position).getMinutes());
                dialogPost.setText(planData.get(position).getPostScript());
                builder.setTitle("日程信息");
                builder.setView(dialog);
                builder.show();
            }
        });
    }


    private void loadData() {
        planData = DBServer.searchByIndistinct(getApplicationContext(), SearchPlan);
    }

    private void refreshNoteList() {
        loadData();
        ListAdapter adapter = new ListAdapter(getApplicationContext());
        searchView.setAdapter(adapter);
    }

    public class ListAdapter extends BaseAdapter {

        private Context mContext = null;
        private LayoutInflater mInflater = null;

        public ListAdapter(Context c) {
            mContext = c;
            mInflater = LayoutInflater.from(this.mContext);
        }

        @Override
        public int getCount() {
            return planData.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListViewHolder holder;
            if (convertView == null) {
                holder = new ListViewHolder();
                convertView = mInflater.inflate(R.layout.list_item, null);
                holder.txvName = (TextView) convertView.findViewById(R.id.txvName);
                holder.subLine = (TextView) convertView.findViewById(R.id.subLine);
                convertView.setTag(holder);
            } else {
                holder = (ListAdapter.ListViewHolder) convertView.getTag();
            }
            holder.txvName.setText(planData.get(position).getTitle());
            holder.subLine.setText(planData.get(position).getPostScript());
            return convertView;
        }

        class ListViewHolder {
            TextView txvName, subLine;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent(SearchActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
