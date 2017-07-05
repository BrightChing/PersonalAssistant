package cn.zucc.qwmcql.personalassistant;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import cn.zucc.qwmcql.personalassistant.bean.IncomeCostBean;
import cn.zucc.qwmcql.personalassistant.db.DataBaseDao;
import cn.zucc.qwmcql.personalassistant.util.RecyclerViewAdapter;

public class FragmentIncomeCost extends Fragment {
    private ImageButton addIncomeCostBtn;
    RecyclerView mRecyclerView;
    RecyclerViewAdapter mRecyclerViewAdapter;
    public List<IncomeCostBean> mList;
    SimpleDateFormat sdf;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_income_cost, container, false);
        initData();
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.income_cost_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerViewAdapter = new RecyclerViewAdapter<IncomeCostBean>(getContext(),
                R.layout.item_income_cost, mList) {
            @Override
            protected void convert(ViewHolder holder, IncomeCostBean incomeCostBean) {
                holder.setImageResource(R.id.income_cost_iv, R.drawable.bookshelf);
                holder.setText(R.id.source, incomeCostBean.getSource());
                holder.setText(R.id.ic_money, String.valueOf(incomeCostBean.getMoney()));
                holder.setText(R.id.income_cost_date, incomeCostBean.getIncomeCostDate());
            }
        };
        addIncomeCostBtn = (ImageButton) rootView.findViewById(R.id.addIncomeCostBtn);
        addIncomeCostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIncomeCostDialog("添加收支");
            }
        });
        //设置recycleView监听
        mRecyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                // Toast.makeText(getContext(), position + "", Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerViewAdapter.setOnItemLongClickListener(new RecyclerViewAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(RecyclerView parent, View view, int position) {
                IncomeCostBean incomeCostBean = mList.get(position);
                DataBaseDao.getInstance(getContext()).deleteIncomeCost(incomeCostBean.getId());
                mList.remove(position);
                mRecyclerViewAdapter.notifyItemRemoved(position);
            }
        });
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        return rootView;
    }

    /**
     * 收支对话框
     *
     * @param title 对话框的标题
     */
    private void addIncomeCostDialog(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View viewDialog = inflater.inflate(R.layout.income_cost_dialog, null);
        final DatePicker datePicker = (DatePicker) viewDialog.findViewById(R.id.datePicker);
        final TextView money = (TextView) viewDialog.findViewById(R.id.money);
        final TextView source = (TextView) viewDialog.findViewById(R.id.source);
        builder.setView(viewDialog)
                .setTitle(title)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        IncomeCostBean incomeCostBean = new IncomeCostBean();
                        incomeCostBean.setIncomeCostDate(datePicker.getYear() + "-" +
                                (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth());
                        incomeCostBean.setSource(source.getText().toString());
                        if (money == null || "".equals(money.getText().toString()))
                            incomeCostBean.setMoney(Integer.parseInt("0"));
                        else
                            incomeCostBean.setMoney(Float.parseFloat(money.getText().toString()));
                        DataBaseDao dataBaseDao = DataBaseDao.getInstance(getContext());
                        dataBaseDao.addIncomeCost(incomeCostBean);
                        mList.add(incomeCostBean);
                        mRecyclerViewAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .create()
                .show();
    }

    private void initData() {
        DataBaseDao dataBaseDao = DataBaseDao.getInstance(getContext());
        mList = dataBaseDao.searchIncomeCost();
    }
}