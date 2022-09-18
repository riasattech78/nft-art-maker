package com.bnx.ntart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.QueryPurchasesParams;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.bnx.ntart.creator.activities.canvas.CanvasActivity;
import com.bnx.ntart.data.StringConstants;
import com.bnx.ntart.databinding.ActivityIpAppPurchaseBinding;
import com.bnx.ntart.databinding.ProductItemBinding;
import com.bnx.ntart.utils.Prefs;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IpAppPurchaseActivity extends AppCompatActivity {


    String TAG = "TestINAPP";
    Activity activity;
    private BillingClient billingClient;
    List<ProductDetails> productDetailsList;
    Handler handler;
    ProductDetailsAdapter adapter;
    private ActivityIpAppPurchaseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIpAppPurchaseBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        //Initialize a BillingClient with PurchasesUpdatedListener onCreate method


        billingClient = BillingClient.newBuilder(this)
                .enablePendingPurchases()
                .setListener(
                        new PurchasesUpdatedListener() {
                            @Override
                            public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
                                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
                                    for (Purchase purchase : list) {
                                        verifyPurchase(purchase);
                                    }
                                }
                            }
                        }
                ).build();

        //start the connection after initializing the billing client
        connectGooglePlayBilling();


    }

    void connectGooglePlayBilling() {
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {
                connectGooglePlayBilling();
            }

            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    showProducts();
                }
            }
        });

    }

    @SuppressLint("SetTextI18n")
    void showProducts() {

        ImmutableList<QueryProductDetailsParams.Product> productList = ImmutableList.of(
                //Product 1
                QueryProductDetailsParams.Product.newBuilder()
                        .setProductId("product_item_app2_1")
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build(),

                //Product 2
                QueryProductDetailsParams.Product.newBuilder()
                        .setProductId("product_item_app2_2")
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build(),

                //Product 3
                QueryProductDetailsParams.Product.newBuilder()
                        .setProductId("product_item_app2_3")
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build(),

                //Product 4
                QueryProductDetailsParams.Product.newBuilder()
                        .setProductId("product_item_app2_4")
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()
        );

        QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder()
                .setProductList(productList)
                .build();

        billingClient.queryProductDetailsAsync(params, (billingResult, list) -> {
            productDetailsList.clear();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "posted delayed");
                    binding.loadProducts.setVisibility(View.INVISIBLE); //
                    productDetailsList.addAll(list);
                    Log.d(TAG, productDetailsList.size() + " number of products");
                    adapter = new ProductDetailsAdapter(getApplicationContext(), productDetailsList, new ProductDetailsAdapter.OnItemClick() {
                        @Override
                        public void onItemClick(int pos) {
                            launchPurchaseFlow(productDetailsList.get(pos));
                        }
                    });
                    binding.recyclerview.setHasFixedSize(true);
                    binding. recyclerview.setLayoutManager(new LinearLayoutManager(IpAppPurchaseActivity.this, LinearLayoutManager.VERTICAL, false));
                    binding.recyclerview.setAdapter(adapter);
                }
            }, 2000);
        });
    }


    void launchPurchaseFlow(ProductDetails productDetails) {

        ImmutableList<BillingFlowParams.ProductDetailsParams> productDetailsParamsList =
                ImmutableList.of(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                                .setProductDetails(productDetails)
                                .build()
                );
        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)
                .build();

        BillingResult billingResult = billingClient.launchBillingFlow(activity, billingFlowParams);
    }


    void verifyPurchase(Purchase purchase) {
        ConsumeParams consumeParams = ConsumeParams.newBuilder()
                .setPurchaseToken(purchase.getPurchaseToken())
                .build();
        ConsumeResponseListener listener = (billingResult, s) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                giveUserCoins(purchase);
            }
        };
        billingClient.consumeAsync(consumeParams, listener);
    }


    protected void onResume() {
        super.onResume();
        billingClient.queryPurchasesAsync(
                QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.INAPP).build(),
                (billingResult, list) -> {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        for (Purchase purchase : list) {
                            if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged()) {
                                verifyPurchase(purchase);
                            }
                        }
                    }
                }
        );

    }


    @SuppressLint("SetTextI18n")
    void giveUserCoins(Purchase purchase) {

        Log.d("TestINAPP", purchase.getProducts().get(0));
        Log.d("TestINAPP", purchase.getQuantity() + " Quantity");


    }

    public static class ProductDetailsAdapter extends RecyclerView.Adapter<ProductDetailsAdapter.ViewHolderClas> {

        Context mContext;
        List<ProductDetails> productDetailsList;
        OnItemClick onItemClick;

        public ProductDetailsAdapter(Context mContext, List<ProductDetails> productDetailsList, OnItemClick onItemClick) {

            this.mContext = mContext;
            this.productDetailsList = productDetailsList;
            this.onItemClick = onItemClick;
        }

        @NonNull
        @Override
        public ViewHolderClas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(mContext).inflate(R.layout.product_item, parent, false);
            return new ViewHolderClas(view);
        }

        @Override
        public int getItemCount() {
            return productDetailsList.size();
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolderClas holder, @SuppressLint("RecyclerView") int position) {

            holder.productItemBinding.productName.setText(productDetailsList.get(position).getName());

            holder.productItemBinding.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick.onItemClick(position);
                }
            });
        }

        public static class ViewHolderClas extends RecyclerView.ViewHolder {

            ProductItemBinding productItemBinding;

            public ViewHolderClas(@NonNull View itemView) {
                super(itemView);
                productItemBinding = ProductItemBinding.bind(itemView);
            }
        }


        public interface OnItemClick {
            void onItemClick(int pos);
        }

    }

}
