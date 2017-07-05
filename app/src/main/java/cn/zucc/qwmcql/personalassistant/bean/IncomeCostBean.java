package cn.zucc.qwmcql.personalassistant.bean;

import java.io.Serializable;
/**
 * Created by angelroot on 2017/7/2.
 */

public class IncomeCostBean implements Serializable{
    private int id;
    private String incomeCostDate;
    private int incomeCostIv;
    private float money;
    private String source;

    public IncomeCostBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIncomeCostDate() {
        return incomeCostDate;
    }

    public void setIncomeCostDate(String incomeCostDate) {
        this.incomeCostDate = incomeCostDate;
    }

    public int getIncomeCostIv() {
        return incomeCostIv;
    }

    public void setIncomeCostIv(int incomeCostIv) {
        this.incomeCostIv = incomeCostIv;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
