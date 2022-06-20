package com.bnx.ntart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.bnx.ntart.creator.activities.canvas.CanvasActivity;
import com.bnx.ntart.data.StringConstants;
import com.bnx.ntart.databinding.ActivityIpAppPurchaseBinding;
import com.bnx.ntart.utils.Prefs;

import java.util.ArrayList;
import java.util.List;

public class IpAppPurchaseActivity extends AppCompatActivity {

    ActivityIpAppPurchaseBinding binding;
    TextView txt_price;
    String TAG = "SubTest1";
    Activity activity;
    Prefs prefs;
    private BillingClient billingClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIpAppPurchaseBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());


        initViews();

        activity = this;

        prefs = new Prefs(this);

        billingClient = BillingClient.newBuilder(this)
                .enablePendingPurchases()
                .setListener(
                        new PurchasesUpdatedListener() {
                            @Override
                            public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
                                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {

                                    for (Purchase purchase : list) {
                                        verifySubPurchase(purchase);
                                    }
                                }

                            }
                        }
                ).build();

        //start the connection after initializing the billing client
        establishConnection();

        binding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
    }


    void establishConnection() {

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    showProducts();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                establishConnection();
            }
        });
    }

    void showProducts() {

        List<String> skuList = new ArrayList<>();
        skuList.add("nt_art_6");
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
        billingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSkuDetailsResponse(@NonNull BillingResult billingResult,
                                                     List<SkuDetails> skuDetailsList) {
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                            // Process the result.
                            for (SkuDetails skuDetails : skuDetailsList) {
                                if (skuDetails.getSku().equals("nt_art_6")) {
                                    //Now update the UI
                                    txt_price.setText("3 Day’s Free Trial Then " + skuDetails.getPrice() + " 6 Month Cancel Anytime");
                                    binding.tvBuyNow.setOnClickListener(view -> {
                                        launchPurchaseFlow(skuDetails);
                                    });
                                }
                            }
                        }
                    }
                });
    }

    void launchPurchaseFlow(SkuDetails skuDetails) {
        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(skuDetails)
                .build();
        billingClient.launchBillingFlow(IpAppPurchaseActivity.this, billingFlowParams);
    }


    void verifySubPurchase(Purchase purchases) {

        AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams
                .newBuilder()
                .setPurchaseToken(purchases.getPurchaseToken())
                .build();

        billingClient.acknowledgePurchase(acknowledgePurchaseParams, new AcknowledgePurchaseResponseListener() {
            @Override
            public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    //Toast.makeText(SubscriptionActivity.this, "Item Consumed", Toast.LENGTH_SHORT).show();
                    // Handle the success of the consume operation.
                    //user prefs to set premium
                    Toast.makeText(IpAppPurchaseActivity.this, "You are a premium user now", Toast.LENGTH_SHORT).show();
                    //updateUser();
                    //Setting premium to 1
                    // 1 - premium
                    //0 - no premium
                    prefs.setPremium(1);
                    startActivity(
                            new Intent(IpAppPurchaseActivity.this, CanvasActivity.class)
                                    .putExtra(StringConstants.SPAN_COUNT_EXTRA, ActivityHome.Companion.getSpanCnt()));
                }else {
                    Toast.makeText(IpAppPurchaseActivity.this, "You are not premium user", Toast.LENGTH_SHORT).show();

                }
            }
        });

        Log.d(TAG, "Purchase Token: " + purchases.getPurchaseToken());
        Log.d(TAG, "Purchase Time: " + purchases.getPurchaseTime());
        Log.d(TAG, "Purchase OrderID: " + purchases.getOrderId());
    }

    private void initViews() {
        txt_price = findViewById(R.id.tv_price1);
    }


    protected void onResume() {
        super.onResume();

        billingClient.queryPurchasesAsync(
                BillingClient.SkuType.SUBS,
                new PurchasesResponseListener() {
                    @Override
                    public void onQueryPurchasesResponse(@NonNull BillingResult billingResult, @NonNull List<Purchase> list) {
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                            for (Purchase purchase : list) {
                                if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged()) {
                                    verifySubPurchase(purchase);
                                }
                            }
                        }
                    }
                }
        );

    }
}