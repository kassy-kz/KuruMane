package orz.kassy.kurumane;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

    }

    @OnClick(R.id.btnLogin)
    void onClickButton2() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btnFirstRegister)
    void onClickButton1() {
        Log.i(TAG, "onClickButton");
//        IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
//        integrator.setCaptureActivity(MyCaptureActivity.class);
//        integrator.setOrientationLocked(false);
//        integrator.initiateScan();
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Intent intent = new Intent(this, CarDetailActivity.class);
//        startActivity(intent);
    }
}
