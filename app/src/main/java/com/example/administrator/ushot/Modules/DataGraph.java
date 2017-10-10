package com.example.administrator.ushot.Modules;

/**
 * Created by Administrator on 2017/10/10 0010.
 */

public class DataGraph {

    private String[] attributes;
    private float[] entries;
    private int dataType = -1;
    /*
    1 ArcProgressStack
    2 BarChart
     */
    public void setEntries(float[] entry) {
        this.entries = entry;
    }


    public float[] getEntries() {
        return this.entries;
    }


    public void setAttributes(String[] names) {
        this.attributes = names;
    }


    public String[] getAttributes() {
        return this.attributes;
    }


    public void setDataType(int type) {
        this.dataType = type;
    }

    public int getDataType() {
        return this.dataType;
    }


    public DataGraph(int type, String[] attr, float[] entry) {
        this.dataType = type;
        this.attributes = attr;
        this.entries = entry;
    }
}
