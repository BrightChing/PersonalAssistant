package cn.zucc.qwmcql.personalassistant;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.DateIntervalFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.List;

import cn.zucc.qwmcql.personalassistant.bean.IncomeCostBean;
import cn.zucc.qwmcql.personalassistant.db.DBServer;
import cn.zucc.qwmcql.personalassistant.util.RecyclerViewAdapter;

public class FragmentIncomeCost extends Fragment {
    RecyclerView mRecyclerView;
    RecyclerViewAdapter mRecyclerViewAdapter;
    public List<IncomeCostBean> mList;
    private FloatingActionButton fab;

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
                if (incomeCostBean.getIncomeCostType() == 0)
                    holder.setImageResource(R.id.income_cost_iv, R.drawable.android_pay);
                else
                    holder.setImageResource(R.id.income_cost_iv, R.drawable.android_income);
                holder.setText(R.id.source, incomeCostBean.getSource());
                holder.setText(R.id.ic_money, String.valueOf(incomeCostBean.getMoney()));
                holder.setText(R.id.income_cost_date, incomeCostBean.getIncomeCostDate());
            }
        };
        //设置recycleView监听
        mRecyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                incomeCostDialog("修改", mList.get(position), position);
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_incomeCost);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
            }
        });
    }

    /**
     * 收支对话框
     *
     * @param title    对话框的标题
     * @param cost     当修改收支时传入要修改的对象
     * @param position 要修改对象的位置
     */
    private void incomeCostDialog(String title, final IncomeCostBean cost, final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final View viewDialog = LayoutInflater.from(getContext()).inflate(R.layout.income_cost_dialog, null);
        setDialogData(viewDialog, cost);
        builder.setView(viewDialog)
                .setTitle(title)
                .setPositiveButton("确定", null).
                setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                IncomeCostBean incomeCostBean = checkIncomeCost(viewDialog);
                if (incomeCostBean != null) {
                    if (cost == null) {
                        DBServer.addIncomeCost(getContext(), incomeCostBean);
                        incomeCostBean.setId(DBServer.getIncomeCostId(getContext()));
                        mList.add(getPosition(incomeCostBean.getIncomeCostDate()), incomeCostBean);
                    } else {
                        incomeCostBean.setId(cost.getId());
                        DBServer.updataIncomeCost(getContext(), incomeCostBean);
                        mList.set(position, incomeCostBean);
                    }
                    mRecyclerViewAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        });
    }

    public void showBottomSheetDialog() {
        final BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.bottomsheet_dialog_cost, null);
        TextView tvAdd = (TextView) dialogView.findViewById(R.id.tv_add);
        TextView tvChart = (TextView) dialogView.findViewById(R.id.tv_chart);
        TextView tvCancel = (TextView) dialogView.findViewById(R.id.tv_cancel);
        tvChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(getActivity(), ChartActivity.class);
                startActivity(intent);

            }
        });
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomeCostDialog("添加收支", null, 0);
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

    private void initData() {
        mList = DBServer.searchIncomeCost(getContext());
    }

    //查找插入数据在列表中的位置
    private int getPosition(String str) {
        int n = mList.size();
        for (int i = 0; i < n; i++) {
            if (mList.get(i).getIncomeCostDate().compareTo(str) > 0)
                return i;
        }
        return n;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private IncomeCostBean checkIncomeCost(View view){
        DatePicker datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        TextView money = (TextView) view.findViewById(R.id.money);
        TextView source = (TextView) view.findViewById(R.id.source);
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        int n = datePicker.getMonth() + 1;
        String m = n > 9 ? n + "" : "0" + n;
        n = datePicker.getDayOfMonth();
        String d = n > 9 ? n + "" : "0" + n;
        int y = datePicker.getYear();
        String date = y + "-" + m + "-" + d;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (new Date().compareTo(simpleDateFormat.parse(date)) < 0) {
                errorDialog("您写入的时间不合法，写了未来的某个时间");
                return null;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if ("".equals(source.getText().toString())) {
            errorDialog("请输入收支备注(来源)");
            return null;
        }
        if ("".equals(money.getText().toString())) {
            errorDialog("请输入收支金额");
            return null;
        }
        IncomeCostBean cost = new IncomeCostBean();
        if (spinner.getSelectedItemPosition() == 0) {
            cost.setIncomeCostType(0);
            cost.setMoney(-Float.parseFloat(money.getText().toString()));
        } else {
            cost.setIncomeCostType(1);
            cost.setMoney(Float.parseFloat(money.getText().toString()));
        }
        cost.setSource(source.getText().toString());
        cost.setIncomeCostDate(date);
        return cost;
    }

    private void setDialogData(View view, IncomeCostBean cost) {
        DatePicker datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        TextView money = (TextView) view.findViewById(R.id.money);
        TextView source = (TextView) view.findViewById(R.id.source);
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        if (cost != null) {
            if (cost.getIncomeCostType() == 0)
                money.setText(String.valueOf(-cost.getMoney()));
            else
                money.setText(String.valueOf(cost.getMoney()));
            source.setText(cost.getSource());
            String[] str = cost.getIncomeCostDate().split("-");
            datePicker.updateDate(Integer.valueOf(str[0]), Integer.valueOf(str[1]), Integer.valueOf(str[2]));
            spinner.setSelection(cost.getIncomeCostType());
        }
    }

    private void errorDialog(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("提示").setPositiveButton("OK", null).setMessage(str).create().show();
    }
}