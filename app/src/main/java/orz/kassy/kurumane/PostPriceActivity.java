package orz.kassy.kurumane;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class PostPriceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_price);
        ButterKnife.inject(this);
    }

    @InjectView(R.id.txtAutoIput)
    EditText mEditText;

    @OnClick(R.id.btnAutoInput)
    void onClickAuto() {
        mEditText.setText("出光GS栄新町支店");
    }

    @OnClick(R.id.btnPostPriceFire)
    void onClickFire() {
        Intent intent = new Intent(this,GsDetailActivity.class);
        startActivity(intent);
        finish();
    }
}
