package com.techease.grubsup.views.fragments.profileFragments;


import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.techease.grubsup.R;
import com.techease.grubsup.adapter.MyOrderAdapter;
import com.techease.grubsup.model.getOrderModel.GetOrderDataModel;
import com.techease.grubsup.model.getOrderModel.GetOrderResponseModel;
import com.techease.grubsup.networking.BaseNetworking;
import com.techease.grubsup.utils.AlertUtils;
import com.techease.grubsup.utils.GeneralUtills;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyOrderListFragment extends Fragment implements View.OnClickListener {

    View parentView;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    @BindView(R.id.rv_my_order)
    RecyclerView rvMyOrder;

    @BindView(R.id.tv_no_order_found)
    TextView tvNoOrderFound;

    MyOrderAdapter myOrderAdapter;
    List<GetOrderDataModel> getOrderDataModels = new ArrayList<>();

    Dialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_my_order_list, container, false);
        ButterKnife.bind(this, parentView);
        ivBack.setOnClickListener(this);

        alertDialog = AlertUtils.createProgressDialog(getActivity());


        rvMyOrder.setHasFixedSize(true);
        rvMyOrder.setLayoutManager(new LinearLayoutManager(getActivity()));

        myOrderAdapter = new MyOrderAdapter(getActivity(), getOrderDataModels);
        rvMyOrder.setAdapter(myOrderAdapter);


        apiCallMyOrder();


        return parentView;
    }

    private void apiCallMyOrder() {

        alertDialog.show();

        Call<GetOrderResponseModel> getOrderResponseModelCall = BaseNetworking.apiServices(GeneralUtills.getApiToken(getActivity())).getMyOrder();
        getOrderResponseModelCall.enqueue(new Callback<GetOrderResponseModel>() {
            @Override
            public void onResponse(Call<GetOrderResponseModel> call, Response<GetOrderResponseModel> response) {

                alertDialog.dismiss();

                getOrderDataModels.addAll(response.body().getData());
                myOrderAdapter.notifyDataSetChanged();

                if (getOrderDataModels.size() == 0) {
                    tvNoOrderFound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<GetOrderResponseModel> call, Throwable t) {

                alertDialog.dismiss();
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                Objects.requireNonNull(getActivity()).finish();
                break;

        }
    }

}
