package orz.kassy.kurumane.flow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import orz.kassy.kurumane.MyCaptureActivity;
import orz.kassy.kurumane.R;


public class FlowRootActivity extends AppCompatActivity {

    private static final String question = "整備は完ぺきにやってほしい";

    @InjectView(R.id.txtFlow)
    TextView mTextFlow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_root);
        ButterKnife.inject(this);
        mTextFlow.setText(question);
    }

    @OnClick(R.id.btnNo)
    void onClickNo() {
        Intent intent = new Intent(this, FlowAnswerActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btnYes)
    void onClickYes() {
        Intent intent = new Intent(this, FlowAnswerActivity.class);
        startActivity(intent);
    }


}
