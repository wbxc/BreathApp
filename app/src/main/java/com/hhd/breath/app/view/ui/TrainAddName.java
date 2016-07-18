package com.hhd.breath.app.view.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhd.breath.app.BaseActivity;
import com.hhd.breath.app.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TrainAddName extends BaseActivity {

    @Bind(R.id.back_re)
    RelativeLayout layoutBack ;
    @Bind(R.id.topText)
    TextView topText ;
    @Bind(R.id.edtTrainName)
    EditText edtTrainName ;
    @Bind(R.id.layout_right)
    RelativeLayout tvRight ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_add_name);
        ButterKnife.bind(this);
        initEvent();
    }

    @Override
    protected void initEvent() {
        topText.setText(getResources().getString(R.string.string_create_train_name));
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrainAddName.this,TrainPlanAdd.class) ;
                intent.putExtra("trainName",edtTrainName.getText().toString().trim()) ;
                setResult(10,intent);

                TrainAddName.this.finish();
            }
        });

        layoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrainAddName.this,TrainPlanAdd.class) ;
                intent.putExtra("trainName",edtTrainName.getText().toString().trim()) ;
                setResult(10,intent);

                TrainAddName.this.finish();
            }
        });

        edtTrainName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE){
                    Intent intent = new Intent(TrainAddName.this,TrainPlanAdd.class) ;
                    intent.putExtra("trainName",edtTrainName.getText().toString().trim()) ;
                    setResult(10,intent);
                    TrainAddName.this.finish();
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(TrainAddName.this,TrainPlanAdd.class) ;
        intent.putExtra("trainName",edtTrainName.getText().toString().trim()) ;
        setResult(10,intent);
        super.onBackPressed();
    }
}
