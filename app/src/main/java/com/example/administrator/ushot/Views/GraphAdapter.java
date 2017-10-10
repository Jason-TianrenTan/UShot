package com.example.administrator.ushot.Views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.ushot.Modules.DataGraph;
import com.example.administrator.ushot.R;
import com.example.administrator.ushot.Tools.DataProcessor;
import com.github.mikephil.charting.charts.BubbleChart;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import devlight.io.library.ArcProgressStackView;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.BubbleChartData;
import lecho.lib.hellocharts.model.BubbleValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.BubbleChartView;

import static com.example.administrator.ushot.Configs.GlobalConfig.ARC_TYPE;
import static com.example.administrator.ushot.Configs.GlobalConfig.BAR_TYPE;
import static com.example.administrator.ushot.Configs.GlobalConfig.BUBBLE_TYPE;

/**
 * Created by Administrator on 2017/10/10 0010.
 */

public class GraphAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<DataGraph> mGraphList;

    public class ArcGraphViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rec_progressstack_score)
        ArcProgressStackView arcProgressStackView;
        CardView rootView;

        public ArcGraphViewHolder(View view) {
            super(view);
            rootView = (CardView) view;
            ButterKnife.bind(this, view);
        }
    }

    public class BarGraphViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rec_barchart)
        BarChart barChart;
        CardView rootView;

        public BarGraphViewHolder(View view) {
            super(view);
            rootView = (CardView) view;
            ButterKnife.bind(this, view);
        }
    }

    public class BubbleGraphViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rec_bubblechart)
        BubbleChartView bubbleChart;
        CardView rootView;

        public BubbleGraphViewHolder(View view) {
            super(view);
            rootView = (CardView) view;
            ButterKnife.bind(this, view);
        }
    }


    public GraphAdapter(ArrayList<DataGraph> cList) {
        this.mGraphList = cList;
    }


    @Override
    public int getItemViewType(int position) {
        return mGraphList.get(position % mGraphList.size()).getDataType();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view;
        switch (viewType) {
            case ARC_TYPE:
                view = LayoutInflater.from(mContext).inflate(R.layout.arc_chart_item, parent, false);
                return new ArcGraphViewHolder(view);
            case BAR_TYPE:
                view = LayoutInflater.from(mContext).inflate(R.layout.bar_chart_item, parent, false);
                return new BarGraphViewHolder(view);
            case BUBBLE_TYPE:
                view = LayoutInflater.from(mContext).inflate(R.layout.bubble_chart_item, parent, false);
                return new BubbleGraphViewHolder(view);
            default:
                break;
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        DataGraph graph = mGraphList.get(position % mGraphList.size());
        int type = graph.getDataType();
        float[] entries = graph.getEntries();
        String[] attributes = graph.getAttributes();
        switch (type) {
            case ARC_TYPE:
                ArcGraphViewHolder viewHolder_arc = (ArcGraphViewHolder) holder;
                String bgColor_str = "#f5f5f5";
                int bgColor = Color.parseColor(bgColor_str);
                String[] colors = {"#1e90ff", "#00bfff", "#87ceeb", "#afeeee"};
                int[] ftColors = new int[colors.length];
                for (int i = 0; i < colors.length; i++)
                    ftColors[i] = Color.parseColor(colors[i]);
                ArrayList<ArcProgressStackView.Model> models = new ArrayList<>();
                for (int i = 0; i < entries.length; i++)
                    models.add(new ArcProgressStackView.Model(attributes[i], DataProcessor.process(entries[i]), bgColor, ftColors[i]));

                viewHolder_arc.arcProgressStackView.setModels(models);
                break;
            case BAR_TYPE:
                BarGraphViewHolder viewHolder_bar = (BarGraphViewHolder) holder;
                int[] iColors = {R.color.aliceblue, R.color.aquamarine, R.color.mediumturquoise, R.color.deepskyblue,
                        R.color.dodgerblue, R.color.powderblue};
                if (viewHolder_bar.barChart.getData().size() < entries.length) {
                    for (int i = 0; i < entries.length; i++)
                        viewHolder_bar.barChart.addBar(new BarModel(attributes[i], DataProcessor.process(entries[i]),
                                ContextCompat.getColor(mContext, iColors[i])));
                }
                viewHolder_bar.barChart.startAnimation();
                break;
            case BUBBLE_TYPE:
                BubbleGraphViewHolder viewHolder_bubble = (BubbleGraphViewHolder) holder;
                List<BubbleValue> pointValues = new ArrayList<BubbleValue>();// 节点数据结合
                Axis axisY = new Axis().setHasLines(true);
                Axis axisX = new Axis();
                axisX.setName("色彩标准");
                ArrayList<AxisValue> axisValuesX = new ArrayList<AxisValue>();
                //ArrayList<AxisValue> axisValuesY = new ArrayList<AxisValue>();
                axisX.setValues(axisValuesX);
                axisX.setLineColor(Color.BLACK);
                axisY.setLineColor(Color.BLACK);
                axisX.setTextColor(Color.GRAY);
                axisY.setTextColor(Color.GRAY);
                axisX.setTextSize(14);
                axisX.setTypeface(Typeface.DEFAULT);
                axisX.setHasTiltedLabels(true);
                axisX.setHasLines(true);
                axisY.setHasLines(true);
                axisX.setHasSeparationLine(true);
                axisX.setInside(true);

                int[] bubble_colors = {R.color.fuchsia, R.color.paleturquoise, R.color.lawngreen,
                R.color.skyblue, R.color.lightcyan};
                for (int i=0;i<5;i++) {
                    BubbleValue v = new BubbleValue();
                    float x = DataProcessor.process(entries[i])/10;
                    v.set(x*1.5f, x*1.5f, x*0.8f);
                    v.setColor(ContextCompat.getColor(mContext, bubble_colors[i]));
                    v.setLabel("Label");
                    v.setShape(ValueShape.CIRCLE);
                    v.setLabel(attributes[i]);
                    pointValues.add(v);
                }

                BubbleChartData bubbleData=new BubbleChartData(pointValues);//定义气泡图的数据对象
                bubbleData.setBubbleScale(0.2f);//设置气泡的比例大小
                bubbleData.setHasLabelsOnlyForSelected(false);//设置文本只有当点击时显示
                bubbleData.setMinBubbleRadius(1);//设置气泡的最小半径
                bubbleData.setValueLabelsTextColor(Color.BLACK);// 设置数据文字颜色
                bubbleData.setValueLabelTextSize(15);// 设置数据文字大小
                bubbleData.setValueLabelTypeface(Typeface.MONOSPACE);// 设置数据文字样式
                bubbleData.setAxisYLeft(axisY);// 将Y轴属性设置到左边
                bubbleData.setAxisXBottom(axisX);// 将X轴属性设置到底部
                viewHolder_bubble.bubbleChart.setBubbleChartData(bubbleData);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }
}
