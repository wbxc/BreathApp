package com.hhd.breath.app.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhd.breath.app.R;
import com.hhd.breath.app.imp.TrainRecordImp;
import com.hhd.breath.app.model.BreathHisDataShow;
import com.hhd.breath.app.model.BreathHistoricalData;
import com.hhd.breath.app.model.RecordUnitData;
import com.hhd.breath.app.model.RecordDayData;
import com.hhd.breath.app.utils.ConvertDateUtil;

import java.util.List;

/**
 * Created by familylove on 2015/12/14.
 */
public class TrainExpandableAdapter extends BaseExpandableListAdapter {

    private Context mContext ;
    private List<BreathHisDataShow> mTrainRecordDatas ;
    private LayoutInflater mLayoutInflater ;
    private View.OnClickListener mOnclick ;
    private TrainRecordImp mTrainRecordImp ;




    public TrainExpandableAdapter(Context mContext,List<BreathHisDataShow> mTrainRecordDatas){
        this.mContext = mContext ;
        this.mTrainRecordDatas = mTrainRecordDatas ;
        mLayoutInflater = LayoutInflater.from(mContext) ;
    }
    public void setmOnclick(View.OnClickListener onclick){
        this.mOnclick = onclick ;
    }
    public void setmTrainRecordImp(TrainRecordImp mTrainRecordImp){
        this.mTrainRecordImp = mTrainRecordImp ;
    }
    @Override
    public int getGroupCount() {
        if (mTrainRecordDatas!=null && !mTrainRecordDatas.isEmpty())
            return mTrainRecordDatas.size() ;
        return 0;
    }



    public void refresh(List<BreathHisDataShow> mTrainRecordDatas){
        this.mTrainRecordDatas = mTrainRecordDatas ;
        this.notifyDataSetChanged();
    }
    // 显示用于分组的视图
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        ViewFirst mViewFirst ;

        if (convertView==null){
            convertView = mLayoutInflater.inflate(R.layout.adapter_first_expand,null) ;
            mViewFirst = new ViewFirst() ;
            mViewFirst.mTodayTime = (TextView)convertView.findViewById(R.id.today_time) ;
            convertView.setTag(mViewFirst);
        }else{
            mViewFirst = (ViewFirst)convertView.getTag() ;
        }
        if (groupPosition < mTrainRecordDatas.size()){
            mViewFirst.mTodayTime.setText(mTrainRecordDatas.get(groupPosition).getMeasure_time());
        }
        return convertView;
    }


    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mTrainRecordDatas.get(groupPosition).getBreathHistoricalDatas().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mTrainRecordDatas.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mTrainRecordDatas.get(groupPosition).getBreathHistoricalDatas().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {

        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView( int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final  ViewSecond mViewSecond ;

        if (convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.adapter_second_expand,null) ;
            mViewSecond = new ViewSecond() ;
            mViewSecond.layoutContent = (RelativeLayout)convertView.findViewById(R.id.layout_content) ;
            mViewSecond.recordName = (TextView)convertView.findViewById(R.id.record_name) ;
            mViewSecond.starandRate = (TextView)convertView.findViewById(R.id.strandRate) ;
            mViewSecond.recordTime = (TextView)convertView.findViewById(R.id.record_time) ;
            mViewSecond.second_bootom_view = (View)convertView.findViewById(R.id.second_bootom_view) ;
            mViewSecond.second_bootom_view1 = (View)convertView.findViewById(R.id.second_bootom_view1) ;
            convertView.setTag(mViewSecond);
        }else{
            mViewSecond = (ViewSecond)convertView.getTag() ;
        }
        if (mTrainRecordDatas!=null && groupPosition<mTrainRecordDatas.size()){

            BreathHistoricalData mRecordUnitData = mTrainRecordDatas.get(groupPosition).getBreathHistoricalDatas().get(childPosition) ;




            mViewSecond.recordTime.setText(ConvertDateUtil.timestampToTime(mRecordUnitData.getTrain_time()));
            mViewSecond.recordName.setText("缩唇呼吸训练");
            mViewSecond.starandRate.setText(mRecordUnitData.getDifficulty());

            if (isLastChild){
                mViewSecond.second_bootom_view.setVisibility(View.GONE);
                mViewSecond.second_bootom_view1.setVisibility(View.VISIBLE);
            }else{
                mViewSecond.second_bootom_view1.setVisibility(View.GONE);
                mViewSecond.second_bootom_view.setVisibility(View.VISIBLE);
            }
        }

        return convertView;
    }

    public void setRefresh(){
        this.notifyDataSetChanged();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    class ViewFirst{
        TextView mTodayTime ;
    }
    class ViewSecond{
        TextView recordTime ;
        TextView recordName ;
        TextView starandRate ;
        View second_bootom_view ;
        View second_bootom_view1 ;
        RelativeLayout layoutContent ;
    }
}
