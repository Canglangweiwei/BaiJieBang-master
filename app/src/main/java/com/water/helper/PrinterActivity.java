package com.water.helper;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jaydenxiao.common.commonutils.DateTimeUtil;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.LoadingDialog;
import com.jaydenxiao.common.commonwidget.NoScrollListView;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;
import com.water.helper.adapter.PrinterListAdapter;
import com.water.helper.app.AbsAppComponent;
import com.water.helper.base.AbsBaseActivity;
import com.water.helper.base.AbsBaseApplication;
import com.water.helper.bean.GoodsModel;
import com.water.helper.manager.PrintfManager;
import com.water.helper.webservice.RequestType;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * <p>
 * 蓝牙打印机
 * </p>
 * Created by Administrator on 2017/11/28 0028.
 */
public class PrinterActivity extends AbsBaseActivity {

    @Bind(R.id.ntb)
    NormalTitleBar ntb;

    @Bind(R.id.tv_printer_hotel)
    TextView mTvhotel;                  // 宾馆
    @Bind(R.id.tv_printer_hotel_lc)
    TextView mTvhotellc;                // 楼层
    @Bind(R.id.tv_printer_person)
    TextView mTvperson;                 // 操作人
    @Bind(R.id.tv_printer_date)
    TextView mTvdate;                   // 操作日期
    @Bind(R.id.tv_printer_beizhu)
    TextView mTvbeizhu;                 // 备注

    @Bind(R.id.printer_list)
    NoScrollListView printerListview;       // 打印列表

    private String hotelName, hotelLcName, beizhu;// 宾馆、楼层、备注信息
    private ArrayList<GoodsModel> printerList = new ArrayList<>();// 正式打印列表

    // 打印机相关
    private PrintfManager printfManager;

    @Override
    protected int initLayoutResID() {
        return R.layout.activity_printer;
    }

    @Override
    protected void parseIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null)
            return;
        beizhu = bundle.getString("beizhu");
        hotelName = bundle.getString("hotel");
        hotelLcName = bundle.getString("hotelLc");
        ArrayList<GoodsModel> arrayList = bundle.getParcelableArrayList("printer_list");

        // 去除冗余数据
        if (arrayList != null && arrayList.size() > 0) {
            for (GoodsModel model : arrayList) {
                if (model.getNum() != 0
                        || model.getHuixiNum() != 0) {
                    printerList.add(model);
                }
            }
        }
    }

    @Override
    protected void setupComponent(AbsAppComponent component) {

    }

    @Override
    protected void initUi() {
        ntb.setTitleText("票据打印");
        mTvhotel.setText(hotelName);
        mTvhotellc.setText(hotelLcName);
        mTvperson.setText(mBaseUserBean.getUsername());
        mTvbeizhu.setText(beizhu);
        mTvdate.setText(DateTimeUtil.getClientDateFormat("yyyy年M月d日 HH:mm:ss"));

        if (printerList != null) {
            PrinterListAdapter adapter = new PrinterListAdapter(this, printerList);
            printerListview.setAdapter(adapter);
        }
    }

    @Override
    protected void initDatas() {
        printfManager = PrintfManager.getInstance();
        printfManager.defaultConnection();
    }

//    private void test() {
//        Map<String, String> map = new HashMap<>();
//        map.put("username", "123");
//        map.put("password", "123");
//        Call<BaseBean> response = client.testJson(map);
//        sendRequest1(response, RequestType.TEST, false);
//    }

    @Override
    protected void initListener() {
        ntb.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ntb.setRightTitleVisibility(true);
        ntb.setRightTitle("未连接蓝牙");
        ntb.setOnRightTextListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNextActivity(null, PrintfBlueListActivity.class);
            }
        });

        printfManager.addBluetoothChangLister(new PrintfManager.BluetoothChangLister() {
            @Override
            public void chang(String name) {
                if (TextUtils.isEmpty(name)) {
                    return;
                }
                if (ntb == null) {
                    return;
                }
                ntb.setRightTitle(name);
            }
        });
    }

    @OnClick({R.id.btn_print})
    void print(View view) {
        if (printerList == null || printerList.size() == 0) {
            ToastUitl.showShort("没有需要打印的票据");
            return;
        }
        if (printfManager.isConnect()) {
            LoadingDialog.showDialogForLoading(this, "正在打印，请稍后...", false);
            AbsBaseApplication.sApp.getCachedThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    printfManager.printf(hotelName, hotelLcName, mBaseUserBean.getUsername(), beizhu, printerList);
                }
            });
        } else {
            startNextActivity(null, PrintfBlueListActivity.class);
        }
    }

    @Override
    public void onLoadSuccessCallBack(String dataJson, RequestType type) {
        ToastUitl.showShort(dataJson);
    }

    @Override
    protected void onDestroy() {
        printfManager.removeAllMessage();
        super.onDestroy();
    }
}
