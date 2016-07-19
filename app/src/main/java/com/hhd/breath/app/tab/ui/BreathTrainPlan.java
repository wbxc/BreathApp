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
        //layoutSwipe.setRefreshing(false);
        manager.setOrientation(OrientationHelper.VERTICAL);
        listViewSwipe.setLayoutManager(manager);
        //listViewSwipe.addItemDecoration(new RecycleViewDivider(BreathTrainPlan.this, LinearLayoutManager.HORIZONTAL, 3, getResources().getColor(R.color.common_color_cbcbcb)));
        //listViewSwipe.addItemDecoration(new RecycleViewDivider(BreathTrainPlan.this, LinearLayoutManager.HORIZONTAL));

        if (!TrainPlanService.getInstance(BreathTrainPlan.this).exits(ShareUtils.getUserId(BreathTrainPlan.this),"0")){
            TrainPlan trainPlan = new TrainPlan() ;
            trainPlan.setTrainType("0");  // 循序渐进呼气类型
            trainPlan.setName("循序渐进训练");   // 训练的名称
            trainPlan.setInspirerTime("3");   // 吸气时间 就是暂停时间


            trainPlan.setGroupNumber("10");    // 呼吸训练的组数
            trainPlan.setTimes("1");           // 完成多少次可以晋级
            trainPlan.setControlLevel("13");     // 控制强度
            trainPlan.setControl("1");
            trainPlan.setStrength("1");
            trainPlan.setCurrentControl("1");
            trainPlan.setCurrentStrength("1");
            trainPlan.setCurrentPersistent("1");


            trainPlan.setStrengthLevel("15");
            trainPlan.setPersistent("1");
            trainPlan.setPersistentLevel("3");   // 吸气时间
            trainPlan.setUserId(ShareUtils.getUserId(BreathTrainPlan.this));
            trainPlan.setCumulativeTime("0");   // 训练累计时间
            trainPlan.setCreateTime("");

            TrainPlanService.getInstance(BreathTrainPlan.this).add(trainPlan) ;
        }

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
        trainPlans = TrainPlanService.getInstance(BreathTrainPlan.this).getTrainPlans(ShareUtils.getUserId(BreathTrainPlan.this)) ;
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
                    Intent intent = new Intent() ;
                    intent.setClass(BreathTrainPlan.this, TrainPlanAdd.class) ;
                    //startActivity(intent);
                    startActivityForResult(intent,11);

                } else {

                    if (!isNotEmpty(ShareUtils.getSerialNumber(BreathTrainPlan.this))) {
                        String result = Global340Driver.getInstance(BreathTrainPlan.this).readSerial() ;
                       // BreathApplication.toast(BreathTrainPlan.this, result+"序列号");
                        ShareUtils.setSerialNumber(BreathTrainPlan.this,result);

                    }
                    Global340Driver.getInstance(BreathTrainPlan.this).setEnableRead(false); ;
                    CommonValues.SKY_STRENGTH_VALE = Integer.parseInt(trainPlans.get(position).getStrengthLevel()) * 5;
                    CommonValues.CONTROLLER_VALUE = Integer.parseInt(trainPlans.get(position).getControlLevel()) * 5;
                    CommonValues.CENTER_HEIGHT = CommonValues.SKY_BIRD_ALLOW - CommonValues.SKY_STRENGTH_VALE;
                    CommonValues.TOP_PAIR_HEIGHT = CommonValues.CENTER_HEIGHT - CommonValues.CONTROLLER_VALUE - CommonValues.PAIR_WIDTH_CENTER_HEIGHT;
                    ShareUtils.setBrathTime(BreathTrainPlan.this, Integer.parseInt(trainPlans.get(position).getPersistentLevel()));

                    if (Global340Driver.getInstance(BreathTrainPlan.this).checkUsbStatus() == 1) {
                        BreathTrainActivity.actionStart(BreathTrainPlan.this, "缩唇呼吸训练", String.valueOf(3), String.valueOf(102), true,trainPlans.get(position));
                    } else {
                        BreathTrainActivity.actionStart(BreathTrainPlan.this, "缩唇呼吸训练", String.valueOf(3), String.valueOf(102), false,trainPlans.get(position));
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
                        //initData();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 11:
                initData();
                trainPlanSwipeAdapter.setTrainPlans(trainPlans);
                break;
        }
    }
}
