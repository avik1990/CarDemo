package com.app.carcharging.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.app.carcharging.R;
import com.app.carcharging.common.ClickEventLisener;
import com.app.carcharging.databinding.JobsRowBinding;
import com.app.carcharging.pojo.Jobs;

import java.util.List;

/**
 * Created by user2 on 15-05-2018.
 */

public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.SimpleObjectHolder> {

    JobsRowBinding binding;
    List<Jobs> item;
    Context context;
    ClickEventLisener clickEventLisener;

    public JobsAdapter(Context mContext, List<Jobs> list, ClickEventLisener clickEventLisener) {
        super();
        this.item = list;
        this.context = mContext;
    }

    @NonNull
    @Override
    public SimpleObjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.jobs_row, parent, false);
        return new SimpleObjectHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleObjectHolder holder, int position) {
        holder.bindConnection(item.get(position));
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class SimpleObjectHolder extends RecyclerView.ViewHolder {
        JobsRowBinding binding;

        public SimpleObjectHolder(JobsRowBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void bindConnection(Jobs obj) {
            binding.setJobs(obj);
        }
    }
}