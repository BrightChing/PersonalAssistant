package cn.zucc.qwmcql.personalassistant;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.zucc.qwmcql.personalassistant.util.RecyclerViewAdapter;

public class InterestActivity extends AppCompatActivity {
    private Spinner yearSpinner;
    private Spinner typeSpinner;
    private EditText money;
    private TextView interest;
    private Button mButton;
    private List<Deposit> mList;
    RecyclerView mRecyclerView;
    RecyclerViewAdapter mRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insterest);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("利息计算");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        initView();
        initData();
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1)
                    yearSpinner.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mRecyclerViewAdapter = new RecyclerViewAdapter<Deposit>(this, R.layout.interest_item, mList) {
            @Override
            protected void convert(ViewHolder holder, Deposit deposit) {
                holder.setText(R.id.interest, deposit.getYear());
                holder.setText(R.id.years, deposit.getRate() + "%");
            }
        };
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (money.getText().toString().equals("")) {
                    errorDialog("金额不能为空");
                } else
                    interest.setText(Calculation().toString());
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        mList = new ArrayList<>();
        mList.add(new Deposit("半年", new BigDecimal(1 + Math.random() * 0.3).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
        mList.add(new Deposit("一年", new BigDecimal(1.5 + Math.random() * 0.1).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
        mList.add(new Deposit("两年", new BigDecimal(2 + Math.random() * 0.1).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
        mList.add(new Deposit("五年", new BigDecimal(3 + Math.random() * 0.3).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
        mList.add(new Deposit("十年", new BigDecimal(4 + Math.random() * 0.3).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
        mList.add(new Deposit("活期", new BigDecimal(0.5 + Math.random() * 0.5).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
    }

    private void initView() {
        yearSpinner = (Spinner) findViewById(R.id.years);
        typeSpinner = (Spinner) findViewById(R.id.type);
        mRecyclerView = (RecyclerView) findViewById(R.id.interest_list);
        money = (EditText) findViewById(R.id.money);
        interest = (TextView) findViewById(R.id.interest);
        mButton = (Button) findViewById(R.id.button);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private Double Calculation() {
        double rate = Double.parseDouble(money.getText().toString());
        double[] array = new double[]{0.5, 1.0, 2.0, 5.0, 10.0};
        if (typeSpinner.getSelectedItemPosition() == 1) {
            int i = yearSpinner.getSelectedItemPosition();
            rate = rate * array[i] * mList.get(i).getRate() / 100;
        } else {
            rate = rate * mList.get(5).getRate() / 100;
        }

        return new BigDecimal(rate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    class Deposit {
        private String year;
        private double rate;

        public Deposit(String year, double rate) {
            this.year = year;
            this.rate = rate;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }
    }

    private void errorDialog(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示").setPositiveButton("OK", null).setMessage(str).create().show();
    }
}
