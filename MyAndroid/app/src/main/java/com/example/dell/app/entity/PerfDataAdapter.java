package com.example.dell.app.entity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dell.app.R;

import java.util.List;

public class PerfDataAdapter extends RecyclerView.Adapter<PerfDataAdapter.ViewHolder>{
    private List<CollectedPoint> mPerfList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView collectedTime_tv;
        TextView value_tv;
        public ViewHolder(View view){
            super(view);
            collectedTime_tv = view.findViewById(R.id.perf_time);
            value_tv = view.findViewById(R.id.perf_value);
        }
    }

    public  PerfDataAdapter(List<CollectedPoint> dataList){
        this.mPerfList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.perf_data_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CollectedPoint cp = mPerfList.get(position);
        holder.collectedTime_tv.setText(cp.getCollectedTime());
        holder.value_tv.setText(cp.getDataList().toString());
    }

    @Override
    public int getItemCount() {
        return mPerfList.size();
    }
}
