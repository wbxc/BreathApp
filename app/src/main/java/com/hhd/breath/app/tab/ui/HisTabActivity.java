package com.hhd.breath.app.tab.ui;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import com.hhd.breath.app.model.HisPopupModel;
import com.hhd.breath.app.model.RecordDayData;
import com.hhd.breath.app.model.RecordUnitData;
import com.hhd.breath.app.net.ManagerRequest;
import com.hhd.breath.app.utils.ConvertDateUtil;
import com.hhd.breath.app.utils.ShareUtils;
import com.hhd.breath.app.utils.UiUtils;
import com.hhd.breath.app.utils.Utility;
import com.hhd.breath.app.utils.Utils;
import com.hhd.breath.app.widget.DrawableCenterTextView;

import org.andengine.entity.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HisTabActivity extends BaseActivity {

    private DrawableCenterTextView hisTopTextView;
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
    private List<BreathHisDataShow> breathResultShow;
    private RelativeLayout layoutTop ;
    private PopupWindow popupWindow = null;

    private ListView popupListView = null;
    private PopupAdapter popupAdapter;
    private List<HisPopupModel> hisPopupModels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_his_tab);
        hisPopupModels = new ArrayList<HisPopupModel>() ;
        user_id = ShareUtils.getUserId(HisTabActivity.this);
        breathHisDataShows = new ArrayList<BreathHisDataShow>();
        breathResultShow = new ArrayList<BreathHisDataShow>();

        initData();
        initView();
        initEvent();
        startRequest();
    }

    private void initData() {

        HisPopupModel hisPopupModel1 = new HisPopupModel() ;
        hisPopupModel1.setFlag("0");
        hisPopupModel1.setName("全部训练");
        hisPopupModels.add(hisPopupModel1) ;


        HisPopupModel hisPopupModel2 = new HisPopupModel() ;
        hisPopupModel2.setFlag("0");
        hisPopupModel2.setName("循环渐进训练计划");
        hisPopupModels.add(hisPopupModel2) ;









        mTrainRecordDatas = new ArrayList<RecordDayData>();
        mRecordUnitDatas = new ArrayList<RecordUnitData>();
        mShowRecordDayDatas = new ArrayList<BreathHisDataShow>();
        mTrainExpandableAdapter = new TrainExpandableAdapter(HisTabActivity.this, mShowRecordDayDatas);
    }

    @Override
    protected void initView() {
        hisTopTextView = (DrawableCenterTextView) findViewById(R.id.topText);
        history_recordContent = (PullToRefreshExpandableListView) findViewById(R.id.history_recordContent);
        layoutContent = (RelativeLayout) findViewById(R.id.layout_content);
        layoutTop = (RelativeLayout)findViewById(R.id.top) ;
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

        hisTopTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hisTopTextView.setDra
                Drawable drawable = getResources().getDrawable(R.mipmap.icon_his_up);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                hisTopTextView.setCompoundDrawables(null, null, drawable, null);
                showPopupWindow();
            }
        });
    }


    /**
     * 加载更多
     *
     * @param page_sum 加载的页数
     */
    private void loadRequestMoreData(final int page_sum) {
        Map<String, String> moreParams = new HashMap<String, String>();
        moreParams.put("user_id", user_id);
        moreParams.put("page_num", String.valueOf(sumIndex));
        moreParams.put("page_few", "10");

        ManagerRequest.getInstance().getRequestNetApi().getBreathHisListMap(moreParams).enqueue(new Callback<BreathTempData>() {
            @Override
            public void onResponse(Call<BreathTempData> call, Response<BreathTempData> response) {


                if (page_sum == 2) {
                    breathResultShow.clear();
                    breathResultShow.addAll(breathHisDataShows);
                }

                List<BreathHistoricalData> breathHistoricalDatas = response.body().getData();
                if (breathHistoricalDatas.isEmpty()) {
                    history_recordContent.onRefreshComplete();
                    Toast.makeText(HisTabActivity.this, "没有更多的数据了", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (breathHistoricalDatas.size() > 0) {

                    for (int i = 0; i < breathHistoricalDatas.size(); i++) {

                        if (breathHistoricalDatas.get(i).getTrain_time().length() == 10) {
                            String measureTime = ConvertDateUtil.timestampToTime(breathHistoricalDatas.get(i).getTrain_time()).substring(0, 10);

                            boolean flag = false;
                            for (int j = 0; j < breathResultShow.size(); j++) {
                                if (breathResultShow.get(j).getMeasure_time().equals(measureTime))
                                    flag = true;
                            }
                            if (!flag) {
                                BreathHisDataShow breathHisDataShow = new BreathHisDataShow();
                                breathHisDataShow.setMeasure_time(measureTime);
                                breathResultShow.add(breathHisDataShow);
                            }
                        }
                    }

                    for (int i1 = 0; i1 < breathHistoricalDatas.size(); i1++) {
                        if (breathHistoricalDatas.get(i1).getTrain_time().length() == 10) {
                            String measureTime = ConvertDateUtil.timestampToTime(breathHistoricalDatas.get(i1).getTrain_time()).substring(0, 10);
                            for (int j1 = 0; j1 < breathResultShow.size(); j1++) {
                                if (breathResultShow.get(j1).getMeasure_time().equals(measureTime))
                                    breathResultShow.get(j1).getBreathHistoricalDatas().add(breathHistoricalDatas.get(i1));
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
        ManagerRequest.getInstance().getRequestNetApi()
                .getBreathHisListMap(map)
                .enqueue(new Callback<BreathTempData>() {
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
                                             for (int j1 = 0; j1 < breathHisDataShows.size(); j1++) {
                                                 if (breathHisDataShows.get(j1).getMeasure_time().equals(measureTime)) {
                                                     breathHisDataShows.get(j1).getBreathHistoricalDatas().add(breathHistoricalDatas.get(i1));
                                                 }
                                             }
                                         }
                                     }
                                 }
                                 breathResultShow.addAll(breathHisDataShows);
                                 mTrainExpandableAdapter = new TrainExpandableAdapter(HisTabActivity.this, breathHisDataShows);
                                 mExpandableListView.setAdapter(mTrainExpandableAdapter);
                                 for (int i = 0; i < mTrainExpandableAdapter.getGroupCount(); i++) {
                                     mExpandableListView.expandGroup(i);
                                 }
                                 history_recordContent.onRefreshComplete();
                             }

                             @Override
                             public void onFailure(Call<BreathTempData> call, Throwable t) {

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
        hisTopTextView.setText("全部训练历史");
        if (!CommonValues.check_his_report) {
            sumIndex = 0;
        } else {
            CommonValues.check_his_report = false;

        }
    }




    private void showPopupWindow() {
        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.layout_his_popu, null);
            popupListView = (ListView) view.findViewById(R.id.listViewPopu);
            popupAdapter = new PopupAdapter(HisTabActivity.this, hisPopupModels);
            popupListView.setAdapter(popupAdapter);
            //UiUtils.dip2px(HisTabActivity.this,213)
            popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,true);
        }

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        WindowManager windowManager = (WindowManager)getSystemService(Context.WINDOW_SERVICE) ;

        int posx = windowManager.getDefaultDisplay().getWidth()/2 - popupWindow.getWidth()/2 ;
        backgroundAlpha(0.7f) ;
        popupWindow.showAsDropDown(layoutTop,posx,-40);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
                Drawable drawable = getResources().getDrawable(R.mipmap.icon_his_down);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                hisTopTextView.setCompoundDrawables(null, null, drawable, null);
            }
        });



        popupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (popupWindow!=null && popupWindow.isShowing()){

                    for (int i=0 ; i<hisPopupModels.size() ; i++){
                        hisPopupModels.get(i).setFlag("0");
                    }
                    hisPopupModels.get(position).setFlag("1");
                    popupAdapter.notifyDataSetChanged();
                    hisTopTextView.setText(hisPopupModels.get(position).getName());

                    popupWindow.dismiss();
                }
            }
        });
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
         lp.alpha = bgAlpha; //0.0-1.0
        HisTabActivity.this.getWindow().setAttributes(lp);
    }


    protected class PopupAdapter extends BaseAdapter {

        private Context context;
        private List<HisPopupModel> hisPopupModels;

        public PopupAdapter(Context context, List<HisPopupModel> popupModels) {
            this.context = context;
            this.hisPopupModels = popupModels;
        }


        public void setPopupHisModels(List<HisPopupModel> popupHisModels){

            this.hisPopupModels = hisPopupModels ;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (hisPopupModels != null && !hisPopupModels.isEmpty())
                return hisPopupModels.size();
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return hisPopupModels.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.layout_popup_listview, null);
                viewHolder = new ViewHolder();
                viewHolder.imgSelect = (ImageView) convertView.findViewById(R.id.imgSelect);
                viewHolder.tvItemName = (TextView) convertView.findViewById(R.id.tvItemName);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if (position < hisPopupModels.size()) {

                if (hisPopupModels.get(position).getFlag().equals("1")) {
                    viewHolder.imgSelect.setVisibility(View.VISIBLE);
                    viewHolder.tvItemName.setText(hisPopupModels.get(position).getName());
                    viewHolder.tvItemName.setTextColor(getResources().getColor(R.color.common_top_color));
                } else {
                    viewHolder.imgSelect.setVisibility(View.GONE);
                    viewHolder.tvItemName.setText(hisPopupModels.get(position).getName());
                    viewHolder.tvItemName.setTextColor(getResources().getColor(R.color.common_color_9A9A9A));
                }
            }


            return convertView;
        }
    }

    class ViewHolder {

        TextView tvItemName;
        ImageView imgSelect;
    }


}
