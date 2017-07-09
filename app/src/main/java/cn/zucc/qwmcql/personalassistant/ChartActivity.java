package cn.zucc.qwmcql.personalassistant;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.zucc.qwmcql.personalassistant.bean.IncomeCostBean;
import cn.zucc.qwmcql.personalassistant.db.DBServer;
import cn.zucc.qwmcql.personalassistant.util.RecyclerViewAdapter;
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

public class ChartActivity extends AppCompatActivity {

    private PieChartData data;
    private boolean hasLabels = true;
    private boolean hasLabelsOutside = true;
    private boolean hasCenterCircle = true;
    private boolean hasCenterText1 = true;//环形中间的文字1
    private boolean hasCenterText2 = false;
    private boolean isExploded = true;
    private boolean hasLabelForSelected = true;
    private FloatingActionButton btn;
    private PieChartView chart;
    private float[] array;
    private List<IncomeCostBean> mList1, mList2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        initViews();
        initList();
        initDates();
    }

    private void initList() {
        mList1 = DBServer.searchIncome(this);
        mList2 = DBServer.searchCost(this);
    }

    private void initDates() {
        generateData();
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_chart);
        toolbar.setTitle("收入支出图标");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ChartActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        chart = (PieChartView) findViewById(R.id.chart);
        chart.setOnValueTouchListener(new ValueTouchListener());//添加点击事件
        chart.setCircleFillRatio(0.8f);//设置图所占整个view的比例  当有外面的数据时使用，防止数据显示不全
    }

    /**
     * 生成数据
     */
    private void generateData() {
        array = new float[]{sumIncomeCost(mList1),sumIncomeCost(mList2)};
        int numValues = 2;//分成的块数
        List<SliceValue> values = new ArrayList<>();
        for (int i = 0; i < numValues;i++ ) {
            SliceValue sliceValue = new SliceValue(array[i], ChartUtils.pickColor());//每一块的值和颜色，图标根据值自动进行比例分配
            values.add(sliceValue);
        }
        data = new PieChartData(values);
        data.setHasLabels(true);//显示数据
        data.setHasLabelsOnlyForSelected(false);//不用点击显示占的百分比
        data.setHasLabelsOutside(true);//占的百分比是否显示在饼图外面
        data.setHasCenterCircle(true);//是否是环形显示
        data.setCenterCircleScale(0.5f);////设置环形的大小级别
        data.setValueLabelBackgroundColor(Color.TRANSPARENT);////设置值得背景透明
        data.setValueLabelBackgroundEnabled(false);//数据背景不显示
        data.setValueLabelsTextColor(Color.BLACK);
        data.setValues(values);//填充数据
        if (isExploded) {
            data.setSlicesSpacing(1);//设置间隔为0
        }

        if (hasCenterText1) {
            data.setCenterText1("收支比!");
            data.setCenterText1FontSize(18);
            data.setCenterText1Color(Color.BLACK);////设置值得颜色*/
        }
        if (hasCenterText2) {
            data.setCenterText2("Charts (Roboto Italic)");
        }
        chart.setPieChartData(data);
    }

    private class ValueTouchListener implements PieChartOnValueSelectListener {

        @Override
        public void onValueSelected(int arcIndex, SliceValue value) {
            if (arcIndex == 0)
                showBottomSheetDialog("支出",mList1);
            else showBottomSheetDialog("收入",mList2);
        }

        @Override
        public void onValueDeselected() {

        }
    }

    public void showBottomSheetDialog(String str,List<IncomeCostBean> mList) {
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.chart_bottom_list, null);
        TextView textView = (TextView) dialogView.findViewById(R.id.text_ic);
        textView.setText(str);
        RecyclerView mRecyclerView = (RecyclerView) dialogView.findViewById(R.id.income_cost_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        RecyclerViewAdapter<IncomeCostBean> mRecyclerViewAdapter = new RecyclerViewAdapter<IncomeCostBean>(this,
                R.layout.item_income_cost, mList) {
            @Override
            protected void convert(ViewHolder holder, IncomeCostBean incomeCostBean) {
                holder.setImageResource(R.id.income_cost_iv, R.drawable.bookshelf);
                holder.setText(R.id.source, incomeCostBean.getSource());
                holder.setText(R.id.ic_money, String.valueOf(incomeCostBean.getMoney()));
                holder.setText(R.id.income_cost_date, incomeCostBean.getIncomeCostDate());
            }
        };
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        dialog.setContentView(dialogView);
        dialog.show();
    }

    private float sumIncomeCost(List<IncomeCostBean> mList) {
        float sum = 0;
        for (int i = 0, n = mList.size(); i < n; i++) {
            sum += mList.get(i).getMoney();
        }
        return sum;
    }
}
