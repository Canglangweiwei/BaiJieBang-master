package com.water.helper;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;
import com.water.helper.adapter.TagAdapter;
import com.water.helper.app.AbsAppComponent;
import com.water.helper.base.AbsBaseActivity;
import com.water.helper.bean.HotelBean;
import com.water.helper.config.component.DaggerCheckUserInfoPresenterComponent;
import com.water.helper.config.contract.CheckUserInfoContract;
import com.water.helper.config.module.CheckUserInfoModule;
import com.water.helper.config.presenter.CheckUserInfoPresenter;
import com.water.helper.webservice.RequestType;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * <p>
 * 验证用户信息
 * </p>
 * Created by Administrator on 2018/1/10 0010.
 */
public class CheckUserInfoActivity extends AbsBaseActivity implements CheckUserInfoContract.View {

    @Bind(R.id.ntb)
    NormalTitleBar ntb;
    @Bind(R.id.gridview)
    GridView gridView;

    @Bind(R.id.et_check_user_input)
    EditText editText;

    private TagAdapter mTagAdapter;

    private List<HotelBean> selectSource = new ArrayList<>();

    private String checkName;

    @Inject
    CheckUserInfoPresenter mPresenter;

    @Override
    protected int initLayoutResID() {
        return R.layout.activity_check_userinfo;
    }

    @Override
    protected void parseIntent() {

    }

    @Override
    protected void setupComponent(AbsAppComponent component) {
        DaggerCheckUserInfoPresenterComponent.builder()
                .absAppComponent(component)
                .checkUserInfoModule(new CheckUserInfoModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initUi() {
        ntb.setTitleText("用户验证");

        mTagAdapter = new TagAdapter(this);
        gridView.setAdapter(mTagAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (mTagAdapter.getTheListSelect(position)) {
                    case 0:
                        mTagAdapter.setTheListSelect(position, 1);
                        break;
                    case 1:
                        mTagAdapter.setTheListSelect(position, 0);
                        break;
                    default:
                        break;
                }
                HotelBean hotel = (HotelBean) mTagAdapter.getItem(position);
                updateSelected(hotel);
                mTagAdapter.notifyDataSetChanged();
            }
        });
    }

    private void updateSelected(HotelBean hotel) {
        if (selectSource.contains(hotel)) {
            selectSource.remove(hotel);
        } else {
            selectSource.add(hotel);
        }
    }

    @Override
    protected void initDatas() {
        startProgressDialog();
        mPresenter.attachView(this);
        mPresenter.getBinGuanInfo("");
    }

    @Override
    protected void initListener() {
        ntb.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @OnClick({R.id.btn_confirm})
    void confirm(View view) {
        checkName = editText.getText().toString().trim();
        if (TextUtils.isEmpty(checkName)) {
            ToastUitl.showShort("请输入用户名称");
            return;
        }
        if (selectSource == null || selectSource.size() == 0) {
            ToastUitl.showShort("请至少选择一个自己负责的宾馆");
            return;
        }
        // 兴趣爱好
        StringBuilder builder = new StringBuilder();
        builder.delete(0, builder.length());
        for (int i = 0; i < selectSource.size(); i++) {
            if (i == selectSource.size() - 1) {
                builder.append(selectSource.get(i).getId());
            } else {
                builder.append(selectSource.get(i).getId()).append(",");
            }
        }
        startProgressDialog();
        mPresenter.checkUinfo(checkName, builder.toString());
    }

    @Override
    public void getBinGuanInfo(List<HotelBean> bins) {
        stopProgressDialog();
        if (bins == null || bins.size() == 0)
            return;
        mTagAdapter.resetData(bins);
    }

    @Override
    public void getCheckUinfoResult(String message) {
        // 取消加载框显示
        stopProgressDialog();
        // 提示消息
        ToastUitl.showShort(message);
        Bundle bundle = new Bundle();
        bundle.putString("check_name", checkName);
        startNextActivity(bundle, ResetPassActivity.class, true);
    }

    @Override
    public void onFailureCallback(Throwable throwable) {
        // 取消加载框显示
        stopProgressDialog();
        // 提示消息
        ToastUitl.showShort("请检查网络连接");
    }

    @Override
    public void onFailureCallback(int errorCode, String errorMsg) {
        // 取消加载框显示
        stopProgressDialog();
        // 提示消息
        ToastUitl.showShort(errorMsg);
    }

    @Override
    public void onLoadSuccessCallBack(String dataJson, RequestType type) {

    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }
}
