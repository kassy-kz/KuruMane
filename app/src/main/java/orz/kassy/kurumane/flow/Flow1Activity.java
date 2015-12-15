package orz.kassy.kurumane.flow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import orz.kassy.kurumane.R;


public class Flow1Activity extends AppCompatActivity {

    private static final String question = "コストは気にしない";

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
//        Intent intent = new Intent(this, Flow3Activity.class);
//        startActivity(intent);
    }

    @OnClick(R.id.btnYes)
    void onClickYes() {
//        Intent intent = new Intent(this, FlowGoalAActivity.class);
//        startActivity(intent);
    }


}
