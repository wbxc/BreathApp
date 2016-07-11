package com.hhd.breath.app.tab.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.MyExpandableListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.hhd.breath.app.BaseActivity;
import com.hhd.breath.app.CommonValues;
import com.hhd.breath.app.R;
import com.hhd.breath.app.adapter.TrainExpandableAdapter;
import com.hhd.breath.app.db.TrainHisService;
import com.hhd.breath.app.imp.TrainRecordImp;
import com.hhd.breath.app.main.ui.TrainReportActivity;
import com.hhd.breath.app.model.BreathHisDataShow;
import com.hhd.breath.app.model.BreathHistoricalData;
import com.hhd.breath.app.model.BreathTempData;
import com.hhd.breath.app.model.RecordDayData;
import com.hhd.breath.app.model.RecordUnitData;
import com.hhd.breath.app.net.ManagerRequest;
import com.hhd.breath.app.utils.ConvertDateUtil;
import com.hhd.breath.app.utils.ShareUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HisTabActivity extends BaseActivity {

    private TextView hisTopTextView;
    private PullToRefreshExpandableListView history_recordContent;
    private List<RecordDayData> mTrainRecordDatas;
    private TrainExpandableAdapter mTrainExpandableAdapter;
    private MyExpandableListView mExpandableListView;
    private List<RecordUnitData> mRecordUnitDatas;
    private List<BreathHisDataShow> mShowRecordDayDatas;
    private int sumIndex = 1;  //跳过几行
    private RelativeLayout layoutContent;
    private String user_id;
    private List<BreathHisDataShow> breathHisDataShows;
    private List<BreathHisDataShow> breathResultShow  ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_his_tab);
        user_id = ShareUtils.getUserId(HisTabActivity.this);
        breathHisDataShows = new ArrayList<BreathHisDataShow>();
        breathResultShow = new ArrayList<BreathHisDataShow>() ;

        initData();
        initView();
        initEvent();
        startRequest();
    }

    private void initData() {

        mTrainRecordDatas = new ArrayList<RecordDayData>();
        mRecordUnitDatas = new ArrayList<RecordUnitData>();
        mShowRecordDayDatas = new ArrayList<BreathHisDataShow>();
        mTrainExpandableAdapter = new TrainExpandableAdapter(HisTabActivity.this, mShowRecordDayDatas);
    }

    @Override
    protected void initView() {
        hisTopTextView = (TextView) findViewById(R.id.topText);
        history_recordContent = (PullToRefreshExpandableListView) findViewById(R.id.history_recordContent);
        layoutContent = (RelativeLayout) findViewById(R.id.layout_content);
    }

    @Override
    protected void initEvent() {

        history_recordContent.setMode(PullToRefreshBase.Mode.BOTH);
        mExpandableListView = (MyExpandableListView) history_recordContent.getRefreshableView();
        mExpandableListView.setGroupIndicator(null);
        mExpandableListView.setDivider(null);
        mExpandableListView.setAdapter(mTrainExpandableAdapter);

        history_recordContent.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<MyExpandableListView>() {
            @Override
            public void onRefresh(final PullToRefreshBase<MyExpandableListView> refreshView) {
                if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
                    sumIndex++;
                    loadRequestMoreData(sumIndex);
                } else if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
                    startRequest();
                }
            }
        });
        for (int i = 0; i < mExpandableListView.getCount(); i++) {
            mExpandableListView.expandGroup(i);
        }
        mTrainExpandableAdapter.setmTrainRecordImp(new TrainRecordImp() {
            @Override
            public void setCurrentSelect(int groupId, int childernId) {
            }
        });
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (groupPosition < breathResultShow.size() && childPosition < breathResultShow.get(groupPosition).getBreathHistoricalDatas().size()) {
                    goNextUI(breathResultShow.get(groupPosition).getBreathHistoricalDatas().get(childPosition).getRecord_id());
                }
                return false;
            }
        });
    }


    /**
     * 加载更多
     * @param page_sum 加载的页数
     */
    private void loadRequestMoreData(final int page_sum){
        Map<String, String> moreParams = new HashMap<String, String>();
        moreParams.put("user_id", user_id);
        moreParams.put("page_num", String.valueOf(sumIndex));
        moreParams.put("page_few", "10");

        ManagerRequest.getInstance().getRequestNetApi().getBreathHisListMap(moreParams).enqueue(new Callback<BreathTempData>() {
            @Override
            public void onResponse(Call<BreathTempData> call, Response<BreathTempData> response) {


                if (page_sum==2){
                    breathResultShow.clear();
                    breathResultShow.addAll(breathHisDataShows) ;
                }

                List<BreathHistoricalData> breathHistoricalDatas = response.body().getData();
                if (breathHistoricalDatas.isEmpty()){
                    history_recordContent.onRefreshComplete();
                    Toast.makeText(HisTabActivity.this,"没有更多的数据了",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (breathHistoricalDatas.size()>0 ){

                    for (int i = 0; i < breathHistoricalDatas.size(); i++) {

                        if (breathHistoricalDatas.get(i).getTrain_time().length() == 10){
                            String measureTime = ConvertDateUtil.timestampToTime(breathHistoricalDatas.get(i).getTrain_time()).substring(0, 10);

                            boolean flag = false ;
                            for (int j=0 ; j< breathResultShow.size() ; j++){
                                if (breathResultShow.get(j).getMeasure_time().equals(measureTime))
                                    flag = true ;
                            }
                            if (!flag){
                                BreathHisDataShow breathHisDataShow = new BreathHisDataShow() ;
                                breathHisDataShow.setMeasure_time(measureTime);
                                breathResultShow.add(breathHisDataShow) ;
                            }
                        }
                    }

                    for (int i1 = 0; i1 < breathHistoricalDatas.size(); i1++){
                        if (breathHistoricalDatas.get(i1).getTrain_time().length() == 10){
                            String measureTime = ConvertDateUtil.timestampToTime(breathHistoricalDatas.get(i1).getTrain_time()).substring(0, 10);
                           for (int j1=0 ; j1<breathResultShow.size() ; j1++){
                               if (breathResultShow.get(j1).getMeasure_time().equals(measureTime))
                                   breathResultShow.get(j1).getBreathHistoricalDatas().add(breathHistoricalDatas.get(i1)) ;
                           }
                        }
                    }
                }
                mTrainExpandableAdapter.refresh(breathResultShow);
                for (int i = 0; i < mTrainExpandableAdapter.getGroupCount(); i++) {
                    mExpandableListView.expandGroup(i);
                }
                history_recordContent.onRefreshComplete();
            }

            @Override
            public void onFailure(Call<BreathTempData> call, Throwable t) {

            }
        });
    }
    private void startRequest() {

        Map<String, String> map = new HashMap<String, String>();
        sumIndex = 1;
        map.put("user_id", user_id);
        map.put("page_num", String.valueOf(sumIndex));
        map.put("page_few", "10");
        breathHisDataShows.clear();
        ManagerRequest.getInstance().getRequestNetApi().getBreathHisListMap(map).enqueue(new Callback<BreathTempData>() {
            @Override
            public void onResponse(Call<BreathTempData> call, Response<BreathTempData> response) {
                List<BreathHistoricalData> breathHistoricalDatas = response.body().getData();

                TrainHisService.getInstance(HisTabActivity.this).addList(breathHistoricalDatas);

                if (!breathHistoricalDatas.isEmpty()) {

                    for (int i = 0; i < breathHistoricalDatas.size(); i++) {

                        if (breathHistoricalDatas.get(i).getTrain_time().length() == 10) {
                            String measure_time = ConvertDateUtil.timestampToTime(breathHistoricalDatas.get(i).getTrain_time()).substring(0, 10);
                            if (breathHisDataShows.isEmpty() || breathHisDataShows.size() == 0) {
                                BreathHisDataShow breathHisDataShow = new BreathHisDataShow();
                                breathHisDataShow.setMeasure_time(measure_time);
                                //breathHisDataShow.getBreathHistoricalDatas().add(breathHistoricalDatas.get(i)) ;
                                breathHisDataShows.add(breathHisDataShow);
                            } else {
                                int breathSize = breathHisDataShows.size();

                                for (int j = 0; j < breathSize; j++) {
                                    if (breathHisDataShows.get(j).getMeasure_time().equals(measure_time)) {
                                        //breathHisDataShows.get(j).getBreathHistoricalDatas().add(breathHistoricalDatas.get(i)) ;
                                    } else {
                                        BreathHisDataShow breathHisDataShow = new BreathHisDataShow();
                                        breathHisDataShow.setMeasure_time(measure_time);
                                        breathHisDataShows.add(breathHisDataShow);
                                        breathSize++;
                                    }
                                }
                            }
                        }
                    }

                    for (int i1 = 0; i1 < breathHistoricalDatas.size(); i1++) {

                        if (breathHistoricalDatas.get(i1).getTrain_time().length() == 10) {

                            String measureTime = ConvertDateUtil.timestampToTime(breathHistoricalDatas.get(i1).getTrain_time()).substring(0, 10);
                            for (int j1=0 ; j1<breathHisDataShows.size() ; j1++){
                                if (breathHisDataShows.get(j1).getMeasure_time().equals(measureTime)){
                                    breathHisDataShows.get(j1).getBreathHistoricalDatas().add(breathHistoricalDatas.get(i1)) ;
                                }
                            }
                         }
                      }
                    }
                    breathResultShow.addAll(breathHisDataShows) ;
                    mTrainExpandableAdapter = new TrainExpandableAdapter(HisTabActivity.this, breathHisDataShows);
                    mExpandableListView.setAdapter(mTrainExpandableAdapter);
                    for (int i = 0; i < mTrainExpandableAdapter.getGroupCount(); i++) {
                        mExpandableListView.expandGroup(i);
                    }
                    history_recordContent.onRefreshComplete();
                }
                @Override
                public void onFailure (Call < BreathTempData > call, Throwable t){

                }
              }
            );
        }


    public void goNextUI(String recordUnitId) {

        CommonValues.check_his_report = true;
        TrainReportActivity.actionStart(HisTabActivity.this, recordUnitId);
    }


    @Override
    protected void onResume() {
        super.onResume();
        CommonValues.select_activity = 1;
        hisTopTextView.setText("训练历史");
        if (!CommonValues.check_his_report) {
            sumIndex = 0;
        } else {
            CommonValues.check_his_report = false;

        }
    }

}
