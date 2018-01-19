package com.water.helper;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.ClearEditText;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.water.helper.adapter.MemberAdapter;
import com.water.helper.app.AbsAppComponent;
import com.water.helper.base.AbsBaseActivity;
import com.water.helper.bean.MemberBean;
import com.water.helper.config.component.DaggerMemberSelectPresenterComponent;
import com.water.helper.config.contract.MemberSelectContract;
import com.water.helper.config.module.MemberSelectModule;
import com.water.helper.config.presenter.MembersPresenter;
import com.water.helper.webservice.RequestType;
import com.wevey.selector.dialog.DialogOnClickListener;
import com.wevey.selector.dialog.MDAlertDialog;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * <p>
 * 成员信息列表
 * </p>
 * Created by Administrator on 2018/1/18 0018.
 */
public class MemberSelectActivity extends AbsBaseActivity
        implements MemberSelectContract.View,
        MemberAdapter.OnSeletedListener,
        XRecyclerView.LoadingListener {

    @Bind(R.id.ntb)
    NormalTitleBar ntb;

    @Bind(R.id.recyclerview)
    XRecyclerView mRecyclerView;
    @Bind(R.id.et_search)
    ClearEditText editSearch;

    private int mCurrentPage = 1;

    private MemberAdapter memberAdapter;

    @Inject
    MembersPresenter presenter;

    @Override
    protected int initLayoutResID() {
        return R.layout.activity_member_select;
    }

    @Override
    protected void parseIntent() {

    }

    @Override
    protected void setupComponent(AbsAppComponent component) {
        DaggerMemberSelectPresenterComponent.builder()
                .absAppComponent(component)
                .memberSelectModule(new MemberSelectModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initUi() {
        ntb.setTitleText("成员选择");
        // 绑定页面
        presenter.attachView(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
    }

    @Override
    protected void initDatas() {
        memberAdapter = new MemberAdapter(this);
        mRecyclerView.setAdapter(memberAdapter);
        mRecyclerView.setLoadingListener(this);

        mRecyclerView.setRefreshing(true);
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

    @OnClick({R.id.btn_member_confirm})
    void confirm(View view) {
        onRefresh();
    }

    @Override
    public void onRefresh() {
        mCurrentPage = 1;
        loadData(mCurrentPage);
    }

    @Override
    public void onLoadMore() {
        mCurrentPage += 1;
        loadData(mCurrentPage);
    }

    private void loadData(int pageindex) {
        String searchname = editSearch.getText().toString().trim();
        presenter.membersList(searchname, pageindex);
    }

    @Override
    public void membersList(List<MemberBean> membersList) {
        // 停止加载
        mRecyclerView.refreshComplete();
        mRecyclerView.loadMoreComplete();

        if (mCurrentPage == 1) {
            memberAdapter.resetData(membersList);
        } else {
            memberAdapter.addAllData(membersList);
        }
    }

    @Override
    public void onFailureCallback(Throwable throwable) {
        // 停止加载
        mRecyclerView.refreshComplete();
        mRecyclerView.loadMoreComplete();
        // 提示消息
        ToastUitl.showShort("请检查网络连接");
    }

    @Override
    public void onFailureCallback(int errorCode, String errorMsg) {
        // 停止加载
        mRecyclerView.refreshComplete();
        mRecyclerView.loadMoreComplete();
        // 提示消息
        ToastUitl.showShort(errorMsg);
    }

    private MDAlertDialog mdAlertDialog;

    @Override
    public void onSelected(final MemberBean memberBean) {
        mdAlertDialog = new MDAlertDialog.Builder(MemberSelectActivity.this)
                .setHeight(0.25f)  //屏幕高度*0.3
                .setWidth(0.7f)  //屏幕宽度*0.7
                .setTitleVisible(true)
                .setTitleText("温馨提示")
                .setTitleTextColor(R.color.black_light)
                .setContentText("确定要选择？")
                .setContentTextColor(R.color.black_light)
                .setLeftButtonText("取消")
                .setLeftButtonTextColor(R.color.black_light)
                .setRightButtonText("确认")
                .setRightButtonTextColor(R.color.gray)
                .setTitleTextSize(16)
                .setContentTextSize(14)
                .setButtonTextSize(14)
                .setOnclickListener(new DialogOnClickListener() {

                    @Override
                    public void clickLeftButton(View view) {
                        mdAlertDialog.dismiss();
                    }

                    @Override
                    public void clickRightButton(View view) {
                        mdAlertDialog.dismiss();
                        // 选中一个员工，返回
                        Intent intent = new Intent();
                        intent.putExtra("name", memberBean.getUsername());
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                })
                .build();
        if (!isFinishing())
            mdAlertDialog.show();
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onLoadSuccessCallBack(String dataJson, RequestType type) {

    }
}
