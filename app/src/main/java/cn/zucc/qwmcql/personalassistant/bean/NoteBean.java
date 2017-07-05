package cn.zucc.qwmcql.personalassistant.bean;

import java.io.Serializable;

/**
 * Created by angelroot on 2017/7/5.
 */

public class NoteBean implements Serializable{
    private String content;
    private int id;
    private String time;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
