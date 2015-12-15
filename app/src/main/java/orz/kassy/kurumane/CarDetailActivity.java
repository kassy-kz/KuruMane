package orz.kassy.kurumane;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CarDetailActivity extends AppCompatActivity {

    private Object[] activities = {
            "自動車登録番号又は車両番号", "名古屋 305 つ 6292",
            "車名", "ニッサン",
            "表板の枚数及び大きさ", "小板 2枚 ペイント",
            "車台番号", "CWEWN-127084",
            "型式", "DBA-CWEFWN",
            "初度登録年月", "2012年6月",
            "有効期間の満了する日", "2017年7月27日",
            "車両重量", "1540kg",
    };

    private CustomListAdapter mListAdapter;
    private ListView mListView1;
    private ArrayList<CarDetailItem> mItemList1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);

        mListView1 = (ListView) findViewById(R.id.listCarDetail);
        mItemList1 = new ArrayList<CarDetailItem>();
        mListAdapter = new CustomListAdapter(this, R.layout.list_row_car_detail, mItemList1);

        for (int i = 0; i < activities.length ; i+=2) {
            mItemList1.add(new CarDetailItem((String)activities[i], (String)activities[i+1]));
        }

        mListView1.setAdapter(mListAdapter);

        ButterKnife.inject(this);
    }


    private class CustomListAdapter extends BaseAdapter {

        List<CarDetailItem> mDataList;
        int mLayoutResId;
        Context mContext;

        /**
         * コンストラクタ
         * @param context
         * @param layoutResId
         * @param dataList
         */
        public CustomListAdapter(Context context, int layoutResId, List<CarDetailItem> dataList) {
            super();
            mDataList = dataList;
            mLayoutResId = layoutResId;
            mContext = context;
        }

        public int getCount() {
            return mDataList.size();
        }

        public Object getItem(int position) {
            return mDataList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(mLayoutResId, null);
            }
            CarDetailItem item = (CarDetailItem) getItem(position);
            if(item == null) {
                return null;
            }
            ((TextView)convertView.findViewById(R.id.textCarDetailTitle)).setText(item.getTitle());
            ((TextView)convertView.findViewById(R.id.textCarDetailDetail)).setText(item.getDetail());
            return convertView;
        }
    }

    @OnClick(R.id.btnRegisterCarDetail)
    void onClickButton1() {
        Intent data = new Intent();
        setResult(RESULT_CANCELED, data);
        finish();
    }
}
