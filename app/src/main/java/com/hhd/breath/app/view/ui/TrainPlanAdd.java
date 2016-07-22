package com.hhd.breath.app.view.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhd.breath.app.BaseActivity;
import com.hhd.breath.app.BreathApplication;
import com.hhd.breath.app.R;
import com.hhd.breath.app.db.TrainPlanService;
import com.hhd.breath.app.model.SysDataModel;
import com.hhd.breath.app.model.TrainPlan;
import com.hhd.breath.app.net.ThreadPoolWrap;
import com.hhd.breath.app.utils.ShareUtils;
import com.hhd.breath.app.widget.WheelView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TrainPlanAdd extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.layoutInspired)
    RelativeLayout  layoutInspired ;
    @Bind(R.id.layoutBreathGroups)
    RelativeLayout layoutBreathGroups;
    @Bind(R.id.layoutTrainTimes)
    RelativeLayout layoutTrainTimes;
    @Bind(R.id.layoutBreathTime)
    RelativeLayout layoutBreathTime;
    @Bind(R.id.layoutBreathStrength)
    RelativeLayout layoutBreathStrength;
    @Bind(R.id.layoutBreathControl)
    RelativeLayout layoutBreathControl;
    @Bind(R.id.layoutTrainName)
    RelativeLayout layoutTrainName ;
    @Bind(R.id.tvTrainName)
    TextView tvTrainName ;

    @Bind(R.id.tvInspired)
    TextView  tvInspired;
    @Bind(R.id.tvBreathGroups)
    TextView  tvBreathGroups;

    @Bind(R.id.tvTrainTimes)
    TextView  tvTrainTimes;

    @Bind(R.id.tvBreathTime)
    TextView  tvBreathTime;
    @Bind(R.id.tvBreathStrength)
    TextView  tvBreathStrength;
    @Bind(R.id.tvBreathControl)
    TextView  tvBreathControl;
    @Bind(R.id.back_re)
    RelativeLayout layoutBack ;
    @Bind(R.id.topText)
    TextView topText ;
    @Bind(R.id.btnCreateTrainPlan)
    Button btnCreateTrainPlan ;

    private SelectDialog layoutInspiredDialog ;
    private SelectDialog layoutBreathGroupsDialog  ;
    private SelectDialog layoutTrainTimesDialog ;
    private SelectDialog layoutBreathTimeDialog ;
    private SelectDialog layoutBreathStrengthDialog ;
    private SelectDialog layoutBreathControlDialog ;


    @Bind(R.id.controlBar)
    RatingBar controlBar ;
    @Bind(R.id.strengthBar)
    RatingBar strengthBar ;
    @Bind(R.id.prensenterBar)
    RatingBar prensenterBar ;

    private TrainPlan trainPlan ;
    @Bind(R.id.tvRight)
    TextView tvRight ;

    @Bind(R.id.layout_right)
    RelativeLayout layout_right ;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_plan_add);
        ButterKnife.bind(this);
        initData();
        initEvent();
    }

    private List<SysDataModel> layoutInspiredModels ;
    private List<SysDataModel> layoutBreathGroupsModels ;
    private List<SysDataModel> layoutTrainTimesModels ;
    private List<SysDataModel> layoutBreathTimeModels ;
    private List<SysDataModel> layoutBreathStrengthModels ;
    private List<SysDataModel> layoutBreathControlModels ;


    private void  initData(){
        trainPlan = new TrainPlan() ;

        layoutInspiredModels = new ArrayList<SysDataModel>() ;
        layoutBreathGroupsModels = new ArrayList<SysDataModel>() ;
        layoutTrainTimesModels = new ArrayList<SysDataModel>() ;
        layoutBreathTimeModels = new ArrayList<SysDataModel>() ;

        layoutBreathStrengthModels = new ArrayList<SysDataModel>() ;
        layoutBreathControlModels = new ArrayList<SysDataModel>() ;


        for (int i=0 ; i<3 ; i++){
            SysDataModel sysDataModel = new SysDataModel() ;
            sysDataModel.setValue((2+i)+"");
            sysDataModel.setId((1+i)+"");
            sysDataModel.setName((2+i)+"秒")  ;
            layoutInspiredModels.add(sysDataModel) ;
        }

        for (int i=0 ; i<3 ; i++){
            SysDataModel sysDataModel = new SysDataModel() ;
            sysDataModel.setValue(((1+i)*5)+"");
            sysDataModel.setId((1+i)+"");
            sysDataModel.setName(((1+i)*5)+"组")  ;
            layoutBreathGroupsModels.add(sysDataModel) ;
        }

        for (int i=0 ; i<5 ; i++){
            SysDataModel sysDataModel = new SysDataModel() ;
            sysDataModel.setValue((1+i)+"");
            sysDataModel.setId((1+i)+"");
            sysDataModel.setName((1+i)+"次")  ;
            layoutTrainTimesModels.add(sysDataModel) ;
        }

        for (int i=0 ; i<3 ; i++){
            SysDataModel sysDataModel = new SysDataModel() ;
            sysDataModel.setValue((3+i)+"");
            sysDataModel.setId((i+1)+"");
            switch (i){
                case 0:
                    sysDataModel.setName("初级")  ;
                    break;
                case 1:
                    sysDataModel.setName("中级")  ;
                    break;
                case 2:
                    sysDataModel.setName("高级")  ;
                    break;
            }

            layoutBreathTimeModels.add(sysDataModel) ;
        }

        SysDataModel sysDataModel1 = new SysDataModel();
        sysDataModel1.setId(1+"");
        sysDataModel1.setValue(15+"");
        sysDataModel1.setName("初级");
        layoutBreathStrengthModels.add(sysDataModel1) ;

        SysDataModel sysDataModel2 = new SysDataModel();
        sysDataModel2.setId(2+"");
        sysDataModel2.setValue(25+"");
        sysDataModel2.setName("中级");

        layoutBreathStrengthModels.add(sysDataModel2) ;

        SysDataModel sysDataModel3 = new SysDataModel();
        sysDataModel3.setId(3+"");
        sysDataModel3.setValue(35+"");
        sysDataModel3.setName("高级");
        layoutBreathStrengthModels.add(sysDataModel3) ;


        SysDataModel sysDataModel11 = new SysDataModel();
        sysDataModel11.setId(1+"");
        sysDataModel11.setValue(13+"");
        sysDataModel11.setName("初级");
        layoutBreathControlModels.add(sysDataModel11) ;

        SysDataModel sysDataMode22 = new SysDataModel();
        sysDataMode22.setId(2+"");
        sysDataMode22.setValue(10+"");
        sysDataMode22.setName("中级");

        layoutBreathControlModels.add(sysDataMode22) ;

        SysDataModel sysDataMode33 = new SysDataModel();
        sysDataMode33.setId(3+"");
        sysDataMode33.setValue(7+"");
        sysDataMode33.setName("高级");
        layoutBreathControlModels.add(sysDataMode33) ;









    }
    @Override
    protected void initEvent() {
        super.initEvent();
        layout_right.setVisibility(View.VISIBLE);
        tvRight.setText("说明");

        topText.setText(getResources().getString(R.string.string_create_myself_train));
        layoutBack.setOnClickListener(this);
        layoutInspired.setOnClickListener(this);
        layoutBreathControl.setOnClickListener(this);
        layoutBreathGroups.setOnClickListener(this);
        layoutBreathTime.setOnClickListener(this);
        layoutTrainTimes.setOnClickListener(this);
        layoutBreathStrength.setOnClickListener(this);
        btnCreateTrainPlan.setOnClickListener(this);
        layoutTrainName.setOnClickListener(this);
        layout_right.setOnClickListener(this);





        layoutInspiredDialog = new SelectDialog(TrainPlanAdd.this, new SelectInterface() {
            @Override
            public void setValue(final String value) {
                trainPlan.setInspirerTime(value);
            }

            @Override
            public void setOnClick(final int position) {
                tvInspired.setText(layoutInspiredModels.get(position).getName());
                trainPlan.setInspirerTime(layoutInspiredModels.get(position).getValue());
                layoutInspiredDialog.dimissDialog();
            }
        },layoutInspiredModels) ;

        layoutBreathGroupsDialog = new SelectDialog(TrainPlanAdd.this, new SelectInterface() {
            @Override
            public void setValue(String value) {
                trainPlan.setGroupNumber(value);
            }

            @Override
            public void setOnClick(int result) {

                trainPlan.setGroupNumber(layoutBreathGroupsModels.get(result).getValue());
                tvBreathGroups.setText(layoutBreathGroupsModels.get(result).getName());
                layoutBreathGroupsDialog.dimissDialog();
            }
        },layoutBreathGroupsModels) ;


        layoutTrainTimesDialog = new SelectDialog(TrainPlanAdd.this, new SelectInterface() {
            @Override
            public void setValue(String value) {
                trainPlan.setTimes(value);
            }

            @Override
            public void setOnClick(int result) {
                tvTrainTimes.setText(layoutTrainTimesModels.get(result).getName()+"满分");
                trainPlan.setTimes(layoutTrainTimesModels.get(result).getValue());
                layoutTrainTimesDialog.dimissDialog();
            }
        },layoutTrainTimesModels) ;

        layoutBreathTimeDialog = new SelectDialog(TrainPlanAdd.this, new SelectInterface() {
            @Override
            public void setValue(String value) {
               trainPlan.setBreathTime(value);
                trainPlan.setPersistentLevel(value);

            }

            @Override
            public void setOnClick(int result) {
               // tvBreathTime.setText(layoutBreathTimeModels.get(result).getName());
                prensenterBar.setRating(Float.valueOf(layoutBreathTimeModels.get(result).getId()));
                trainPlan.setPersistent(layoutBreathTimeModels.get(result).getId());
                trainPlan.setPersistentLevel(layoutBreathTimeModels.get(result).getValue());
                trainPlan.setCurrentPersistent(layoutBreathTimeModels.get(result).getId());
                layoutBreathTimeDialog.dimissDialog();
            }
        },layoutBreathTimeModels) ;


        layoutBreathStrengthDialog = new SelectDialog(TrainPlanAdd.this, new SelectInterface() {
            @Override
            public void setValue(String value) {
                trainPlan.setStrengthLevel(value);
            }

            @Override
            public void setOnClick(int result) {

                trainPlan.setStrength(layoutBreathStrengthModels.get(result).getId());
                strengthBar.setRating(Float.valueOf(layoutBreathStrengthModels.get(result).getId()));
                trainPlan.setCurrentStrength(layoutBreathStrengthModels.get(result).getId());

                trainPlan.setStrengthLevel(layoutBreathStrengthModels.get(result).getValue());
                layoutBreathStrengthDialog.dimissDialog();
            }
        },layoutBreathStrengthModels) ;


        layoutBreathControlDialog = new SelectDialog(TrainPlanAdd.this, new SelectInterface() {
            @Override
            public void setValue(String value) {
                trainPlan.setControlLevel(value);
            }

            @Override
            public void setOnClick(int result) {

         /*       tvBreathControl.setText(layoutBreathControlModels.get(result).getName());*/

                trainPlan.setControl(layoutBreathControlModels.get(result).getId());
                trainPlan.setControlLevel(layoutBreathControlModels.get(result).getValue());
                controlBar.setRating(Float.valueOf(layoutBreathControlModels.get(result).getId()));
                trainPlan.setCurrentControl(layoutBreathControlModels.get(result).getId());


                layoutBreathControlDialog.dimissDialog();
            }
        },layoutBreathControlModels) ;


    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_re:
                TrainPlanAdd.this.finish();
                break;
            case R.id.layoutInspired:
                if (layoutInspiredDialog!=null && !layoutInspiredDialog.isShowing()){
                    layoutInspiredDialog.showDialog();
                }
                break;
            case R.id.layoutBreathGroups:
                if (layoutBreathGroupsDialog!=null && !layoutBreathGroupsDialog.isShowing()){
                    layoutBreathGroupsDialog.showDialog();
                }
                break;
            case R.id.layoutTrainTimes:
                if (layoutTrainTimesDialog!=null && !layoutTrainTimesDialog.isShowing()){
                    layoutTrainTimesDialog.showDialog();
                }
                break;
            case R.id.layoutBreathTime:
                if (layoutBreathTimeDialog!=null && !layoutBreathTimeDialog.isShowing()){
                    layoutBreathTimeDialog.showDialog();
                }
                break;
            case R.id.layoutBreathStrength:

                if (layoutBreathStrengthDialog!=null && !layoutBreathStrengthDialog.isShowing()){
                    layoutBreathStrengthDialog.showDialog();
                }
                break;
            case R.id.layoutBreathControl:
                if (layoutBreathControlDialog!=null && !layoutBreathControlDialog.isShowing()){
                    layoutBreathControlDialog.showDialog();
                }
                break;
            case R.id.btnCreateTrainPlan:
                if (trainPlan.isNotEmpty(trainPlan)){
                    ThreadPoolWrap.getThreadPool().executeTask(createTrainPlan);
                }else {

                    BreathApplication.toast(TrainPlanAdd.this,"训练参数不能为空");
                }
                break;
            case R.id.layoutTrainName:
                Intent intent = new Intent(TrainPlanAdd.this,TrainAddName.class) ;
                startActivityForResult(intent,10);
                break;
            case R.id.layout_right:
                Intent intent1 = new Intent() ;
                intent1.setClass(TrainPlanAdd.this,CreateTrainInstruction.class) ;
                Bundle bundle = new Bundle() ;
                bundle.putString("str_top_text","训练模式说明");
                bundle.putString("request_url","http://101.201.39.122/ftpuser01/app/shuoming.html");
                intent1.putExtras(bundle) ;
                startActivity(intent1);
                break;

        }
    }

    private Runnable createTrainPlan = new Runnable() {
        @Override
        public void run() {
            String createTime = String.valueOf(System.currentTimeMillis()/1000) ;
            trainPlan.setCreateTime(createTime);
            trainPlan.setCumulativeTime("0");
            int result = TrainPlanService.getInstance(TrainPlanAdd.this).countTrainPlan(ShareUtils.getUserId(TrainPlanAdd.this)) ;
            trainPlan.setTrainType(String.valueOf(result+1));
            trainPlan.setUserId(ShareUtils.getUserId(TrainPlanAdd.this));



            trainPlan.setUserId(ShareUtils.getUserId(TrainPlanAdd.this));
            if (TrainPlanService.getInstance(TrainPlanAdd.this).add(trainPlan)){
                setResult(11,null);
                TrainPlanAdd.this.finish();
            }else {
                BreathApplication.toast(TrainPlanAdd.this,"创建异常");
            }
        }
    } ;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 10 :
                String trainName = data.getStringExtra("trainName") ;
                if (isNotEmpty(trainName)){
                    tvTrainName.setText(trainName);
                    trainPlan.setName(trainName);
                }
                break;
        }
    }

    private class SelectDialog extends Dialog{

        private Button button ;
        private WheelView mWheelView  ;
        private SelectInterface selectInterface ;
        private List<String> list ;
        private List<SysDataModel> sysDataModels ;
        private int position = 0 ;

        public void setSelectInterface(SelectInterface selectInterface){
            this.selectInterface = selectInterface ;
        }

        public SelectDialog(Context context , SelectInterface selectInterface, List<SysDataModel> sysDataModels) {
            super(context,R.style.common_dialog);
            this.setCancelable(false);
            this.selectInterface = selectInterface ;
            list = new ArrayList<String>() ;
            this.sysDataModels = sysDataModels ;
            if (sysDataModels!=null && !sysDataModels.isEmpty()){
                for (int i=0 ; i<sysDataModels.size() ; i++){
                    list.add(sysDataModels.get(i).getName()) ;
                }
            }
            init(context);
        }
        private void  init(Context context){
            View view = LayoutInflater.from(context).inflate(R.layout.popu_system_dialog,null) ;
            button = (Button) view.findViewById(R.id.queding) ;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectInterface.setOnClick(position);
                }
            });
            mWheelView = (WheelView)view.findViewById(R.id.wheel_view_wv) ;
            mWheelView.setItems(list);
            mWheelView.setSeletion(0);
            mWheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener(){

                @Override
                public void onSelected(int selectedIndex, String item) {
                    super.onSelected(selectedIndex, item);
                    if (selectedIndex<sysDataModels.size()){
                        position = selectedIndex ;
                        selectInterface.setValue(sysDataModels.get(selectedIndex).getValue());
                    }
                }
            });
            ViewGroup.LayoutParams  mParmas = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT) ;
            setContentView(view,mParmas);
        }


        public void  showDialog(){
            this.show();
        }

        public void  dimissDialog(){
            this.dismiss();
        }
    }



    public interface SelectInterface{

        public void setValue(String value) ;

        public void setOnClick(int result) ;
    }
}
