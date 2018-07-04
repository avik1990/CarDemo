package com.app.carcharging;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.app.carcharging.adapter.JobsAdapter;
import com.app.carcharging.common.ClickEventLisener;
import com.app.carcharging.common.RecyclerViewMargin;
import com.app.carcharging.databinding.ActivityJobsBinding;
import com.app.carcharging.pojo.Jobs;

import java.util.ArrayList;
import java.util.List;

public class ActivtyJobListing extends AppCompatActivity implements View.OnClickListener, ClickEventLisener {

    Context mContext;
    ActivityJobsBinding binding;
    String type = "";
    List<Jobs> reportCardBeanList;
    JobsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        initview();
        showList();
    }

    private void initview() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_jobs);
        binding.toolbar.tvTitle.setText("My Job List");

    }

    @Override
    public void onClick(View v) {
      /*  if (v == binding.btnSignin) {
            if (type.equalsIgnoreCase("technician")) {
                Intent i = new Intent(mContext, TechnicianVerification.class);
                startActivity(i);
            } else {
                Intent i = new Intent(mContext, Dashboard.class);
                startActivity(i);
            }
        }*/
    }


    private void showList() {
        adapter = new JobsAdapter(this, dataEntry(), this);
        binding.rvRecycler.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewMargin decoration = new RecyclerViewMargin(15, dataEntry().size());
        binding.rvRecycler.addItemDecoration(decoration);
        binding.rvRecycler.setAdapter(adapter);
    }

    //String id, String job_id, String job_heading, String job_details, String job_image
    private List<Jobs> dataEntry() {
        reportCardBeanList = new ArrayList<>();
        reportCardBeanList.add(new Jobs("1", "Job#ID -1000A", "Heading of job", "Job details", ""));
        reportCardBeanList.add(new Jobs("1", "Job#ID -1000A", "Heading of job", "Job details", ""));
        reportCardBeanList.add(new Jobs("1", "Job#ID -1000A", "Heading of job", "Job details", ""));
        reportCardBeanList.add(new Jobs("1", "Job#ID -1000A", "Heading of job", "Job details", ""));
        reportCardBeanList.add(new Jobs("1", "Job#ID -1000A", "Heading of job", "Job details", ""));
        reportCardBeanList.add(new Jobs("1", "Job#ID -1000A", "Heading of job", "Job details", ""));
        reportCardBeanList.add(new Jobs("1", "Job#ID -1000A", "Heading of job", "Job details", ""));
        reportCardBeanList.add(new Jobs("1", "Job#ID -1000A", "Heading of job", "Job details", ""));
        reportCardBeanList.add(new Jobs("1", "Job#ID -1000A", "Heading of job", "Job details", ""));
        reportCardBeanList.add(new Jobs("1", "Job#ID -1000A", "Heading of job", "Job details", ""));
        reportCardBeanList.add(new Jobs("1", "Job#ID -1000A", "Heading of job", "Job details", ""));
        reportCardBeanList.add(new Jobs("1", "Job#ID -1000A", "Heading of job", "Job details", ""));
        return reportCardBeanList;
    }


    @Override
    public void clickTrigger(View v, int position) {

    }
}
