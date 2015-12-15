package orz.kassy.kurumane;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class GsDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gs_detail);
        ButterKnife.inject(this);
    }

    @InjectView(R.id.txtGoodJob)
    TextView mTxtGoodJob;

    @OnClick(R.id.btnGoodJob)
    void onClickB() {
        mTxtGoodJob.setText("524");
    }
}
