package com.water.helper.viewholder;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.water.helper.R;
import com.water.helper.base.AbsBaseApplication;
import com.water.helper.bean.ChuKuMxBean;
import com.water.helper.config.presenter.BackWashPresenter;
import com.wevey.selector.dialog.DialogOnClickListener;
import com.wevey.selector.dialog.MDAlertDialog;

/**
 * <p>
 * 出库明细
 * </p>
 * Created by Administrator on 2018/1/4 0004.
 */
@SuppressWarnings("ALL")
public class ChukuMxHolder extends RecyclerView.ViewHolder {

    private Context mContext;
    private View itemView;
    private BackWashPresenter mPresenter;

    private MDAlertDialog mdAlertDialog;

    /**
     * 编号
     */
    private TextView tvCode;

    /**
     * 类型
     */
    private TextView tvName;

    /**
     * 规格
     */
    private TextView tvSpec;

    /**
     * 数量
     */
    private TextView tvNumber;

    /**
     * 日期
     */
    private TextView tvRqi;

    /**
     * 回换
     */
    private TextView tvOperate;

    public static ChukuMxHolder create(Context context) {
        return new ChukuMxHolder(LayoutInflater.from(context).inflate(R.layout.item_chuku_mx, null), context);
    }

    private ChukuMxHolder(View itemView, final Context context) {
        super(itemView);
        this.mContext = context;
        this.itemView = itemView;
        initView();
    }

    /**
     * 初始化页面
     */
    private void initView() {
        tvCode = (TextView) itemView.findViewById(R.id.tv_back_wash_code);
        tvName = (TextView) itemView.findViewById(R.id.tv_back_wash_name);
        tvSpec = (TextView) itemView.findViewById(R.id.tv_back_wash_spec);
        tvNumber = (TextView) itemView.findViewById(R.id.tv_back_wash_shuliang);
        tvRqi = (TextView) itemView.findViewById(R.id.tv_back_wash_rqi);
        tvOperate = (TextView) itemView.findViewById(R.id.tv_back_wash_operate);
    }

    /**
     * 设置数据
     */
    public void setData(final ChuKuMxBean bean, BackWashPresenter presenter) {
        this.mPresenter = presenter;
        if (bean == null)
            return;
        tvCode.setText(bean.getKeyid());
        tvName.setText("类型：" + bean.getCpname());
        tvSpec.setText("备注：" + bean.getBeizhu());
        tvNumber.setText("数量：" + String.valueOf(bean.getShuLiang()) + "个");
        tvRqi.setText(bean.getRiqi());

        // 控制回洗按钮是否显示
        int mode = bean.getMode();
        tvOperate.setVisibility(mode == 1 ? View.VISIBLE : View.GONE);

        tvOperate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPresenter == null)
                    return;
                doHuihuan(bean.getId());
            }
        });
    }

    /**
     * 回换操作
     *
     * @param id 出库明细id
     */
    private void doHuihuan(final int id) {
        mdAlertDialog = new MDAlertDialog.Builder(mContext)
                .setHeight(0.25f)  //屏幕高度*0.3
                .setWidth(0.7f)  //屏幕宽度*0.7
                .setTitleVisible(true)
                .setTitleText("温馨提示")
                .setTitleTextColor(R.color.black_light)
                .setContentText("是否要提交收货信息？")
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
                        // 回换操作
                        String username = AbsBaseApplication.sApp.getUserInfo().getUsername();
                        mPresenter.doHuiHuan(id, username);
                    }
                })
                .build();
        if (!((Activity) mContext).isFinishing())
            mdAlertDialog.show();
    }
}
