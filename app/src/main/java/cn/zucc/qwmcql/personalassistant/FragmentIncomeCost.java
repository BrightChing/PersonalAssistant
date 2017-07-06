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

import java.util.List;

import cn.zucc.qwmcql.personalassistant.bean.IncomeCostBean;
import cn.zucc.qwmcql.personalassistant.db.DBServer;
import cn.zucc.qwmcql.personalassistant.util.RecyclerViewAdapter;

public class FragmentIncomeCost extends Fragment {
    private ImageButton addIncomeCostBtn;
    RecyclerView mRecyclerView;
    RecyclerViewAdapter mRecyclerViewAdapter;
    public List<IncomeCostBean> mList;

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
                incomeCostDialog("添加收支",null,0);
            }
        });
        //设置recycleView监听
        mRecyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
               incomeCostDialog("修改",mList.get(position),position);
            }
        });
        mRecyclerViewAdapter.setOnItemLongClickListener(new RecyclerViewAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(RecyclerView parent, View view, int position) {
                IncomeCostBean incomeCostBean = mList.get(position);
                DBServer.deleteIncomeCost(getContext(), incomeCostBean.getId());
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
    private void incomeCostDialog(String title, final IncomeCostBean cost, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View viewDialog = inflater.inflate(R.layout.income_cost_dialog, null);
        final DatePicker datePicker = (DatePicker) viewDialog.findViewById(R.id.datePicker);
        final TextView money = (TextView) viewDialog.findViewById(R.id.money);
        final TextView source = (TextView) viewDialog.findViewById(R.id.source);
        if (cost != null) {
            money.setText(cost.getMoney() + "");
            source.setText(cost.getSource());
            String[] str = cost.getIncomeCostDate().split("-");
           datePicker.updateDate(Integer.valueOf(str[0]),Integer.valueOf(str[1]),Integer.valueOf(str[2]));
        }
        builder.setView(viewDialog)
                .setTitle(title)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        IncomeCostBean incomeCostBean = new IncomeCostBean();
                        int n = datePicker.getMonth() + 1;
                        String m = n > 9 ? n + "" : "0" + n;
                        n = datePicker.getDayOfMonth();
                        String d = n > 9 ? n + "" : "0" + n;
                        incomeCostBean.setIncomeCostDate(datePicker.getYear() + "-" + m + "-" + d);
                        incomeCostBean.setSource(source.getText().toString());
                        if (money == null || "".equals(money.getText().toString()))
                            incomeCostBean.setMoney(Integer.parseInt("0"));
                        else
                            incomeCostBean.setMoney(Float.parseFloat(money.getText().toString()));
                        if(cost==null) {
                            DBServer.addIncomeCost(getContext(), incomeCostBean);
                            incomeCostBean.setId(DBServer.getIncomeCostId(getContext()));
                            mList.add(getPositon(incomeCostBean.getIncomeCostDate()), incomeCostBean);
                        }else{
                            DBServer.updataIncomeCost(getContext(), incomeCostBean);
                            incomeCostBean.setId(DBServer.getIncomeCostId(getContext()));
                            mList.set(position, incomeCostBean);
                        }
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
        mList = DBServer.searchIncomeCost(getContext());
    }

    private int getPositon(String str) {
        int n = mList.size();
        for (int i = 0; i < n; i++) {
            if (mList.get(i).getIncomeCostDate().compareTo(str) > 0)
                return i;
        }
        return n;
    }
}