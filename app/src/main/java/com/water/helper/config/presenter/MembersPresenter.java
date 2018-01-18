package com.water.helper.config.presenter;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.water.helper.bean.BaseModel;
import com.water.helper.bean.MemberBean;
import com.water.helper.config.contract.MemberSelectContract;
import com.water.helper.config.httpclient.HttpPost;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * <p>
 * 成员列表
 * </p>
 * Created by Administrator on 2018/1/18 0018.
 */
public class MembersPresenter implements MemberSelectContract.Presenter {

    private MemberSelectContract.View view;
    private Gson mGson = new Gson();

    @Inject
    public MembersPresenter() {

    }

    @Override
    public void membersList(String searchname, int pageindex) {
        HttpPost httpPost = new HttpPost();
        httpPost.members(searchname, pageindex, new Subscriber<BaseModel>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.onFailureCallback(e);
            }

            @Override
            public void onNext(BaseModel baseModel) {
                if (baseModel == null) {
                    view.onFailureCallback(1002, "成员信息加载失败");
                    return;
                }
                if (!baseModel.isSuccess()) {
                    int code = baseModel.getCode();
                    String message = baseModel.getMessage();
                    view.onFailureCallback(code, message);
                    return;
                }

                Type type = new TypeToken<List<MemberBean>>() {
                }.getType();
                List<MemberBean> members = mGson.fromJson(baseModel.getData(), type);

                // 解析成员信息
                view.membersList(members);
            }
        });
    }

    @Override
    public void attachView(@NonNull MemberSelectContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }
}
