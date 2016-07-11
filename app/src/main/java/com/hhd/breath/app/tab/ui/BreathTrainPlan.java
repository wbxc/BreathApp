package com.hhd.breath.app.tab.ui;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhd.breath.app.BaseActivity;
import com.hhd.breath.app.BreathApplication;
import com.hhd.breath.app.CommonValues;
import com.hhd.breath.app.R;
import com.hhd.breath.app.db.TrainPlanService;
import com.hhd.breath.app.main.ui.BreathTrainActivity;
import com.hhd.breath.app.model.TrainPlan;
import com.hhd.breath.app.utils.ShareUtils;
import com.hhd.breath.app.view.RecycleViewDivider;
import com.hhd.breath.app.view.ui.MeDetailsActivity;
import com.hhd.breath.app.view.ui.TrainPlanAdd;
import com.hhd.breath.app.view.viewHolder.TrainPlanSwipeAdapter;
import com.hhd.breath.app.wchusbdriver.Global340Driver;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BreathTrainPlan extends BaseActivity {

    @Bind(R.id.listViewSwipe)
    RecyclerView listViewSwipe ;
    @Bind(R.id.layoutSwipe)
    SwipeRefreshLayout layoutSwipe ;
    @Bind(R.id.topText)
    TextView topText ;
    @Bind(R.id.layoutMeDetails)
    RelativeLayout layoutMeDetails ;
    private Handler handler = new Handler() ;
    private List<TrainPlan> trainPlans ;
    private TrainPlanSwipeAdapter trainPlanSwipeAdapter ;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breath_train_plan);
        ButterKnife.bind(this);
        trainPlans = new ArrayList<TrainPlan>() ;
        LinearLayoutManager  manager = new LinearLayoutManager(this) ;
        listViewSwipe.setHasFixedSize(true);
        manager.setOrientation(OrientationHelper.VERTICAL);
        listViewSwipe.setLayoutManager(manager);
        //listViewSwipe.addItemDecoration(new RecycleViewDivider(BreathTrainPlan.this, LinearLayoutManager.HORIZONTAL, 3, getResources().getColor(R.color.common_color_cbcbcb)));
        //listViewSwipe.addItemDecoration(new RecycleViewDivider(BreathTrainPlan.this, LinearLayoutManager.HORIZONTAL));
        initData();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonValues.select_activity = 0 ;
    }

    private void initData(){
        trainPlans.clear();


        TrainPlanService.getInstance(BreathTrainPlan.this).find() ;
        TrainPlan trainPlan = new TrainPlan() ;
        trainPlan.setName("缩唇呼吸训练(初级)");
        trainPlan.setControl("一级(13)");
        trainPlan.setControlLevel("13");
        trainPlan.setStrength("初级(15)");
        trainPlan.setStrengthLevel("15");
        trainPlan.setPersistent("一级(3)");
        trainPlan.setPersistentLevel("3");
        trainPlan.setGroupNumber("10");
        trainPlans.add(trainPlan) ;



        TrainPlan trainPlan1 = new TrainPlan() ;
        trainPlan1.setName("缩唇呼吸训练(中级)");
        trainPlan1.setControl("二级(10)");
        trainPlan1.setControlLevel("10");
        trainPlan1.setStrength("中级(25)");
        trainPlan1.setStrengthLevel("25");
        trainPlan1.setPersistent("二级(4)");
        trainPlan1.setPersistentLevel("4");
        trainPlan1.setGroupNumber("10");
        trainPlans.add(trainPlan1) ;

        TrainPlan trainPlan2 = new TrainPlan() ;
        trainPlan2.setName("缩唇呼吸训练(高级)");
        trainPlan2.setControl("三级(13)");
        trainPlan2.setControlLevel("13");
        trainPlan2.setStrength("高级(32)");
        trainPlan2.setStrengthLevel("32");
        trainPlan2.setPersistent("三级(3)");
        trainPlan2.setPersistentLevel("3");
        trainPlan2.setGroupNumber("10");
        trainPlans.add(trainPlan2) ;


        TrainPlan trainPlan3 = new TrainPlan() ;
        trainPlan3.setName("缩唇呼吸训练(高级40)");
        trainPlan3.setControl("三级(7)");
        trainPlan3.setControlLevel("7");
        trainPlan3.setStrength("高级(40)");
        trainPlan3.setStrengthLevel("40");
        trainPlan3.setPersistent("三级(3)");
        trainPlan3.setPersistentLevel("3");
        trainPlan3.setGroupNumber("10");
        trainPlans.add(trainPlan3) ;


        trainPlans.add(new TrainPlan()) ;
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        trainPlanSwipeAdapter = new TrainPlanSwipeAdapter(BreathTrainPlan.this, trainPlans) ;
        trainPlanSwipeAdapter.setOnRecyclerItemClickListener(new TrainPlanSwipeAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position == (trainPlans.size() - 1)) {
                    BreathApplication.toast(BreathTrainPlan.this, "训练计划添加功能暂未实现");
                    Intent intent = new Intent() ;
                    intent.setClass(BreathTrainPlan.this, TrainPlanAdd.class) ;
                    startActivity(intent);
                } else {

                    if (!isNotEmpty(ShareUtils.getSerialNumber(BreathTrainPlan.this))) {
                        String result = Global340Driver.getInstance(BreathTrainPlan.this).readSerial() ;
                        BreathApplication.toast(BreathTrainPlan.this, result+"序列号");
                        ShareUtils.setSerialNumber(BreathTrainPlan.this,result);

                    }
                    Global340Driver.getInstance(BreathTrainPlan.this).setEnableRead(false); ;
                    CommonValues.SKY_STRENGTH_VALE = Integer.parseInt(trainPlans.get(position).getStrengthLevel()) * 5;
                    CommonValues.CONTROLLER_VALUE = Integer.parseInt(trainPlans.get(position).getControlLevel()) * 5;
                    CommonValues.CENTER_HEIGHT = CommonValues.SKY_BIRD_ALLOW - CommonValues.SKY_STRENGTH_VALE;
                    CommonValues.TOP_PAIR_HEIGHT = CommonValues.CENTER_HEIGHT - CommonValues.CONTROLLER_VALUE - CommonValues.PAIR_WIDTH_CENTER_HEIGHT;
                    ShareUtils.setBrathTime(BreathTrainPlan.this, Integer.parseInt(trainPlans.get(position).getPersistentLevel()));

                    if (Global340Driver.getInstance(BreathTrainPlan.this).checkUsbStatus() == 1) {
                        BreathTrainActivity.actionStart(BreathTrainPlan.this, "缩唇呼吸训练", String.valueOf(3), String.valueOf(102), true);
                    } else {
                        BreathApplication.toast(BreathTrainPlan.this, getString(R.string.string_health_no_connect));
                        BreathTrainActivity.actionStart(BreathTrainPlan.this, "缩唇呼吸训练", String.valueOf(3), String.valueOf(102), false);
                    }
                }
            }
        });
        listViewSwipe.setAdapter(trainPlanSwipeAdapter);

        layoutSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                        layoutSwipe.setRefreshing(false);
                    }
                }, 500);
            }
        });

        layoutMeDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent() ;
                intent.setClass(BreathTrainPlan.this, MeDetailsActivity.class) ;
                startActivity(intent);
            }
        });
    }
}
