package com.example.administrator.ushot.Views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import devlight.io.library.ArcProgressStackView;

import static com.example.administrator.ushot.Configs.GlobalConfig.ARC_TYPE;
import static com.example.administrator.ushot.Configs.GlobalConfig.BAR_TYPE;

/**
 * Created by Administrator on 2017/10/10 0010.
 */

public class GraphAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<DataGraph> mGraphList;

    public static class ArcGraphViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rec_progressstack_score)
        ArcProgressStackView arcProgressStackView;
        CardView rootView;

        public ArcGraphViewHolder(View view) {
            super(view);
            rootView = (CardView) view;
            ButterKnife.bind(this, view);
        }
    }

    public static class BarGraphViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rec_barchart)
        BarChart barChart;
        CardView rootView;

        public BarGraphViewHolder(View view) {
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
        return mGraphList.get(position).getDataType();
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
            default:
                break;
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        DataGraph graph = mGraphList.get(position);
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
                final ArrayList<ArcProgressStackView.Model> models = new ArrayList<>();
                for (int i = 0; i < entries.length; i++)
                    models.add(new ArcProgressStackView.Model(attributes[i], entries[i], bgColor, ftColors[i]));

                viewHolder_arc.arcProgressStackView.setModels(models);
                break;
            case BAR_TYPE:
                BarGraphViewHolder viewHolder_bar = (BarGraphViewHolder) holder;
                int[] iColors = {R.color.aliceblue, R.color.aquamarine, R.color.mediumturquoise, R.color.deepskyblue,
                R.color.dodgerblue, R.color.powderblue};
                for (int i = 0; i < entries.length; i++)
                    viewHolder_bar.barChart.addBar(new BarModel(attributes[i], entries[i], iColors[i]));

                viewHolder_bar.barChart.startAnimation();
                break;
        }

    }

    @Override
    public int getItemCount() {
        return mGraphList.size();
    }
}
