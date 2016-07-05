package com.hhd.breath.app.tab.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hhd.breath.app.BaseActivity;
import com.hhd.breath.app.BreathApplication;
import com.hhd.breath.app.CommonValues;
import com.hhd.breath.app.R;
import com.hhd.breath.app.db.HealthDataService;
import com.hhd.breath.app.main.ui.HealthCheck;
import com.hhd.breath.app.model.HealthData;
import com.hhd.breath.app.service.GlobalUsbService;
import com.hhd.breath.app.utils.ShareUtils;
import com.hhd.breath.app.wchusbdriver.Global340Driver;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.ComboLineColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ComboLineColumnChartView;

public class BreathCheck extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.topText)
    TextView tvTop ;
    @Bind(R.id.btnStartHealthTest)
    Button btnStartHealthTest ;
    @Bind(R.id.tvShowSuggestion)
    TextView tvShowSuggestion ;
    @Bind(R.id.chart)
    ComboLineColumnChartView chart ;
    @Bind(R.id.btnCompositeValue)
    Button btnCompositeValue ;
    @Bind(R.id.btnOneSecondValue)
    Button btnOneSecondValue ;
    @Bind(R.id.btnWindSpeedValue)
    Button btnWindSpeedValue ;

    private String showSuggestion = "定期测量呼吸健康指数可以有效估评呼吸训练的效果。建议每周进行一次呼吸健康评估测试" ;
    private int maxNumberOfLines = 1 ;
    private int numberOfPoints = 7 ;

    private ComboLineColumnChartData data ;
    float[][] randomNumbersTab ;

    float[][] randomNumbersTab2  ;
    float[][] randomNumbersTab3  ;
    private List<HealthData> healthDatas ;
    private String[] time ;
    private Handler handler = new Handler() ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breath_report);
        ButterKnife.bind(this);
        healthDatas = new ArrayList<HealthData>() ;
        healthDatas = HealthDataService.getInstance(BreathCheck.this).findHealths(ShareUtils.getUserId(BreathCheck.this)) ;
        numberOfPoints = healthDatas.size() ;
        randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];
        randomNumbersTab2 = new float[maxNumberOfLines][numberOfPoints] ;
        randomNumbersTab3 = new float[maxNumberOfLines][numberOfPoints] ;
        time = new String[numberOfPoints] ;
        initView();
        initEvent();
        generateValues();
        getValue(1);
        changeBtn(1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonValues.select_activity = 2 ;
    }

    @Override
    protected void initView() {
        tvTop.setText(getResources().getString(R.string.string_breath_health_check));
        tvShowSuggestion.setText(showSuggestion);
    }

    // 竖直方向的值
    private void generateValues(){

        if (healthDatas!=null && !healthDatas.isEmpty()){

            for (int i=0 ; i< maxNumberOfLines ; ++i){

                for (int j=0 ; j< healthDatas.size() ; j++){
                    Log.e(this.getClass().getName(), healthDatas.get(j).toString() + ">>>>>") ;
                    randomNumbersTab[i][j] = Float.parseFloat(healthDatas.get(j).getMaxRate().trim()) ; //
                    randomNumbersTab2[i][j] =  Float.parseFloat(healthDatas.get(j).getSecondValue().trim()) ; //
                    randomNumbersTab3[i][j] = Float.parseFloat(healthDatas.get(j).getCompValue().trim()) ; //
                    time[j] = healthDatas.get(j).getTime() ;
                }
            }
        }

    }




    private void generateData(){
        data = new ComboLineColumnChartData(null,generateLineData()) ;
        List<AxisValue> axisValues = new ArrayList<AxisValue>() ;
        for (int i=0 ; i<time.length ; i++){
            AxisValue axisValue = new AxisValue(i+1).setLabel(time[i]) ;
            axisValues.add(axisValue) ;
        }


        Axis axisX = new Axis() ;
        axisX.setValues(axisValues) ;
        axisX.setTextSize(10) ;
        axisX.setHasLines(false) ;
        axisX.setInside(false) ;
        axisX.setMaxLabelChars(5) ;
        axisX.setHasSeparationLine(true) ;

        Axis axisY = new Axis().setHasLines(false).setMaxLabelChars(6) ;


        data.setAxisYLeft(axisY);
        data.setAxisXBottom(axisX);
        chart.setComboLineColumnChartData(data);
        chart.setZoomType(ZoomType.HORIZONTAL);
    }

    private void getValue(int position){
        switch (position){
            case 1:
                generateData();
                resetViewport(3000) ;
                break;
            case 2:
                generateData2();
                resetViewport(100) ;
                break;
            case 3:
                generateData3();
                resetViewport(30000) ;
                break;
        }
    }



    private void generateData2(){
        data = new ComboLineColumnChartData(null, generateLineData2()) ;
        List<AxisValue> axisValues = new ArrayList<AxisValue>() ;
        for (int i=0 ; i<time.length ; i++){
            AxisValue axisValue = new AxisValue(i+1).setLabel(time[i]) ;
            axisValues.add(axisValue) ;
        }


        Axis axisX = new Axis() ;
        axisX.setValues(axisValues) ;
        axisX.setTextSize(10) ;
        axisX.setHasLines(false) ;
        axisX.setInside(false);
        axisX.setMaxLabelChars(5) ;
        axisX.setHasSeparationLine(true) ;


        Axis axisY = new Axis().setHasLines(false) ;
        axisY.setMaxLabelChars(6) ;

        data.setAxisYLeft(axisY);
        data.setAxisXBottom(axisX);
        chart.setComboLineColumnChartData(data);

        chart.setZoomType(ZoomType.HORIZONTAL);
    }
    private void generateData3(){
        data = new ComboLineColumnChartData(null, generateLineData3()) ;
        List<AxisValue> axisValues = new ArrayList<AxisValue>() ;
        for (int i=0 ; i<time.length ; i++){
            AxisValue axisValue = new AxisValue(i+1).setLabel(time[i]) ;

            axisValues.add(axisValue) ;
        }
        Axis axisX = new Axis();
        axisX.setValues(axisValues) ;
        axisX.setTextSize(10);
        axisX.setHasLines(false) ;
        axisX.setInside(false);
        axisX.setMaxLabelChars(5) ;
        axisX.setHasSeparationLine(true) ;


        Axis axisY = new Axis().setHasLines(false).setMaxLabelChars(6) ;

        data.setAxisYLeft(axisY);
        data.setAxisXBottom(axisX);
        chart.setComboLineColumnChartData(data);
        chart.setZoomType(ZoomType.HORIZONTAL);
    }




    public final static String[] days = new String[]{"Mon", "Tue", "Wen", "Thu", "Fri", "Sat", "Sun",};

    private LineChartData generateLineData(){

        List<Line> lines = new ArrayList<Line>() ;
        for (int i=0 ; i<maxNumberOfLines ; ++i){
            List<PointValue> values = new ArrayList<PointValue>() ;
            for (int j=1 ; j<numberOfPoints+1 ; ++j){
                values.add(new PointValue(j,randomNumbersTab[i][j-1])) ;
            }

            Line line = new Line(values) ;

            line.setColor(ChartUtils.COLORS[i]) ;
            line.setHasLabels(true) ;
            line.setCubic(true) ;
            line.setStrokeWidth(2) ;
            line.setAreaTransparency(30) ;
            lines.add(line) ;

        }
        LineChartData lineChartData = new LineChartData(lines) ;
        return lineChartData ;
    }


    private LineChartData generateLineData2(){
        List<Line> lines = new ArrayList<Line>() ;
        for (int i=0 ; i<maxNumberOfLines ; ++i){
            List<PointValue> values = new ArrayList<PointValue>() ;
            for (int j=1 ; j<numberOfPoints+1 ; ++j){
                values.add(new PointValue(j,randomNumbersTab2[i][j-1])) ;
            }

            Line line = new Line(values) ;
            line.setColor(ChartUtils.COLORS[i]) ;
            line.setHasLabels(true) ;
            line.setCubic(true) ;
            line.setStrokeWidth(2) ;
            line.setAreaTransparency(30) ;
            lines.add(line) ;

        }
        LineChartData lineChartData = new LineChartData(lines) ;
        return lineChartData ;
    }

    private LineChartData generateLineData3(){
        List<Line> lines = new ArrayList<Line>() ;
        for (int i=0 ; i<maxNumberOfLines ; ++i){
            List<PointValue> values = new ArrayList<PointValue>() ;
            for (int j=1 ; j<numberOfPoints+1 ; ++j){
                values.add(new PointValue(j,randomNumbersTab3[i][j-1])) ;
            }

            Line line = new Line(values) ;
            line.setColor(ChartUtils.COLORS[i]) ;
            line.setHasLabels(true) ;
            line.setCubic(true) ;
            line.setStrokeWidth(2) ;
            line.setAreaTransparency(30) ;
            lines.add(line) ;

        }
        LineChartData lineChartData = new LineChartData(lines) ;
        return lineChartData ;
    }


    private void resetViewport(int maxValue) {
        // Reset viewport height range to (0,100)
        final Viewport v = new Viewport(chart.getMaximumViewport());
        v.bottom = 0;
        v.top = maxValue;
        v.left = 0;
        v.right = healthDatas.size()+1 ;
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
    }






    @Override
    protected void initEvent() {

        btnStartHealthTest.setOnClickListener(this);
        btnCompositeValue.setOnClickListener(this);
        btnOneSecondValue.setOnClickListener(this);
        btnWindSpeedValue.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_re:
                BreathCheck.this.finish();
                break;
            case R.id.btnStartHealthTest:

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (GlobalUsbService.isOpenBreath){
                            Intent intent = new Intent() ;
                            intent.setClass(BreathCheck.this, HealthCheck.class) ;
                            startActivityForResult(intent,10);
                        }else {
                            try {
                                if (Global340Driver.getInstance(BreathCheck.this).send("2")){
                                    GlobalUsbService.isOpenBreath = true ;
                                    Intent intent = new Intent() ;
                                    intent.setClass(BreathCheck.this, HealthCheck.class) ;
                                    startActivityForResult(intent, 10);
                                }else {
                                    BreathApplication.toastTest(BreathCheck.this, getString(R.string.string_health_error_init));
                                }
                            }catch (Exception e){

                                Intent intent = new Intent() ;
                                intent.setClass(BreathCheck.this, HealthCheck.class) ;
                                startActivityForResult(intent,10);

                                BreathApplication.toast(BreathCheck.this, getString(R.string.string_health_no_connect));
                            }

                        }
                    }
                },500) ;

                break;
            case R.id.btnOneSecondValue:
                changeBtn(2);
                break;
            case R.id.btnCompositeValue:
                changeBtn(3);
                break;
            case R.id.btnWindSpeedValue:
                changeBtn(1);
                break;
        }
    }

    private void changeBtn(int flag){
        switch (flag){
            case 1:
                btnOneSecondValue.setBackgroundResource(R.drawable.btn_health_ovl);
                btnOneSecondValue.setTextColor(getResources().getColor(R.color.white));
                btnCompositeValue.setBackgroundResource(R.drawable.btn_health_ovl);
                btnCompositeValue.setTextColor(getResources().getColor(R.color.white));
                btnWindSpeedValue.setBackgroundResource(R.drawable.btn_health_ovl_select);
                btnWindSpeedValue.setTextColor(getResources().getColor(R.color.btn_heath_text_select));

                getValue(1);
                break;
            case 2:
                btnWindSpeedValue.setBackgroundResource(R.drawable.btn_health_ovl);
                btnWindSpeedValue.setTextColor(getResources().getColor(R.color.white));
                btnCompositeValue.setBackgroundResource(R.drawable.btn_health_ovl);
                btnCompositeValue.setTextColor(getResources().getColor(R.color.white));

                btnOneSecondValue.setBackgroundResource(R.drawable.btn_health_ovl_select);
                btnOneSecondValue.setTextColor(getResources().getColor(R.color.btn_heath_text_select));
                getValue(2);
                break;
            case 3:
                btnOneSecondValue.setBackgroundResource(R.drawable.btn_health_ovl);
                btnOneSecondValue.setTextColor(getResources().getColor(R.color.white));
                btnWindSpeedValue.setBackgroundResource(R.drawable.btn_health_ovl);
                btnWindSpeedValue.setTextColor(getResources().getColor(R.color.white));
                btnCompositeValue.setBackgroundResource(R.drawable.btn_health_ovl_select);
                btnCompositeValue.setTextColor(getResources().getColor(R.color.btn_heath_text_select));
                getValue(3);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 12:
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        healthDatas = HealthDataService.getInstance(BreathCheck.this).findHealths(ShareUtils.getUserId(BreathCheck.this));
                        numberOfPoints = healthDatas.size();
                        randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];
                        randomNumbersTab2 = new float[maxNumberOfLines][numberOfPoints] ;
                        randomNumbersTab3 = new float[maxNumberOfLines][numberOfPoints] ;
                        time = new String[numberOfPoints] ;
                        generateValues();
                        getValue(1);
                        changeBtn(1);
                        BreathApplication.toast(BreathCheck.this, "数据保存成功");
                    }
                }) ;
                break;
            case 11:
                BreathApplication.toast(BreathCheck.this,"数据保存失败");
                break;
            case 13:
                BreathApplication.toast(BreathCheck.this,"测试数据");
                break;
        }
    }
}
