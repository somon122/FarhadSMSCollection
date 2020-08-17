package com.farhad.sms_collection.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codesgood.views.JustifiedTextView;
import com.facebook.ads.AbstractAdListener;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.RewardedVideoAd;
import com.facebook.ads.RewardedVideoAdListener;
import com.farhad.sms_collection.MainActivity;
import com.farhad.sms_collection.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class QuotationsActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            if (interstitialAd.isAdLoaded()){
                interstitialAd.show();
            } {
                this.finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (interstitialAd.isAdLoaded()){
            interstitialAd.show();
        }else {
            this.finish();
        }
    }

    private TextView waitingTV,counterTV;
    private JustifiedTextView dataTV;
    private ProgressBar progressBar;
    private Button shareButton,nextButton;
    private FirebaseFirestore mfirestore;
    private List<ModelClass>dataList;
    private int size;
    private String currentData;
    private int updateCount;
    private static final long START_TIME_IN_MILLIS2 = 5000;
    private CountDownTimer mCountDownTimer2;
    private boolean mTimerRunning2;
    private long mTimeLeftInMillis2;
    private long mEndTime2;
    private InterstitialAd interstitialAd;
    private RewardedVideoAd rewardedVideoAd;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotations);

        Toolbar toolbar = findViewById(R.id.quotationsToolBar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        waitingTV = findViewById(R.id.quotationsWaiting_id);
        dataTV = findViewById(R.id.quotationsView_id);
        counterTV = findViewById(R.id.quotationsCounter_id);
        shareButton = findViewById(R.id.quotationsShare_id);
        nextButton = findViewById(R.id.quotationsNext_id);
        waitingTV.setVisibility(View.GONE);

        mfirestore = FirebaseFirestore.getInstance();
        dataList = new ArrayList<>();
        progressBar = findViewById(R.id.quotationProgressBar);
        AudienceNetworkAds.initialize(this);

        adView = new AdView(this, "1165469997177687_1165475257177161", AdSize.BANNER_HEIGHT_50);
        LinearLayout adContainer = findViewById(R.id.banner_container);
        adContainer.addView(adView);
        adView.loadAd();

        rewardedVideoAd = new RewardedVideoAd(this, "1165469997177687_1165470767177610");
        interstitialAd = new InterstitialAd(this, "1165469997177687_1165474733843880");

        rewardedVideoAd.setAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoCompleted() {
                dataShare(currentData);
                rewardedVideoAd.loadAd();
            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }

            @Override
            public void onRewardedVideoClosed() {
                rewardedVideoAd.loadAd();
            }

            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {

            }
        });
        rewardedVideoAd.loadAd();


        interstitialAd.setAdListener(new AbstractAdListener() {
            @Override
            public void onError(Ad ad, AdError error) {
                super.onError(ad, error);
            }

            @Override
            public void onAdLoaded(Ad ad) {
                super.onAdLoaded(ad);
            }

            @Override
            public void onAdClicked(Ad ad) {
                super.onAdClicked(ad);
            }

            @Override
            public void onInterstitialDisplayed(Ad ad) {
                super.onInterstitialDisplayed(ad);
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                super.onInterstitialDismissed(ad);

                finish();
                interstitialAd.loadAd();
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                super.onLoggingImpression(ad);
            }
        });
        interstitialAd.loadAd();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String value = bundle.getString("quotation");
            setTitle(value);
            loadData(value);
        }

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (updateCount < size ){
                        nextSMS();

                    }else {
                      dataStatus();
                    }

                }catch (Exception e){

                    dataStatus();
                }


            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dataShare(currentData);
            }
        });

    }

    private void nextSMS() {

        updateCount = updateCount+1;
        counterTV.setText(updateCount+"/"+size);
       /* waitingTV.setVisibility(View.VISIBLE);
        nextButton.setVisibility(View.GONE);*/
        updatedata(updateCount);
       // startTimer2();
    }

    private void dataShare(String body) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareSub = "Important Quotation";
        intent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
        intent.putExtra(Intent.EXTRA_TEXT,body);
        startActivity(Intent.createChooser(intent,"হাদিসের উক্তি"));


    }

    @Override
    protected void onDestroy() {
        if (rewardedVideoAd != null){
            rewardedVideoAd.destroy();
        } if (interstitialAd != null){
            interstitialAd.destroy();
        }
        super.onDestroy();
    }

    private void loadData(String value) {

        CollectionReference collectionReference = mfirestore.collection(value);
        collectionReference.orderBy("mTime", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){

                    if (task.isSuccessful()) {

                        dataList.clear();

                        for (DocumentSnapshot document : task.getResult()) {

                             ModelClass modelClass = new ModelClass(
                                    document.getString("mTime"),
                                    document.getString("mData")
                            );

                            dataList.add(modelClass);

                        }
                        progressBar.setVisibility(View.GONE);
                        updatedata(0);
                        size = dataList.size()-1;
                        counterTV.setText("0"+"/"+size);

                    } else {
                        Toast.makeText(QuotationsActivity.this, "Quiz Show is Field", Toast.LENGTH_SHORT).show();
                    }



                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private void updatedata(int i) {

        if (!dataList.isEmpty()){
            ModelClass modelClass = dataList.get(i);
            dataTV.setText(modelClass.getmData());
            currentData = modelClass.getmData();


        }else {
            Toast.makeText(this, "Data is not available", Toast.LENGTH_SHORT).show();
        }


    }

    private void dataStatus() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(QuotationsActivity.this);

        builder.setTitle("Congratulation!")
                .setMessage("You are completed all Quotation")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(QuotationsActivity.this, MainActivity.class));
                        finish();

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }


    private void startTimer2() {
        mEndTime2 = System.currentTimeMillis() + mTimeLeftInMillis2;
        mCountDownTimer2 = new CountDownTimer(mTimeLeftInMillis2, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis2 = millisUntilFinished;
                updateCountDownText2();
            }

            @Override
            public void onFinish() {
                mTimerRunning2 = false;
                waitingTV.setVisibility(View.GONE);
                nextButton.setVisibility(View.VISIBLE);
                resetTimer2();

            }
        }.start();

        mTimerRunning2 = true;
    }


    private void resetTimer2() {
        mTimeLeftInMillis2 = START_TIME_IN_MILLIS2;
        updateCountDownText2();

    }


    private void updateCountDownText2() {

        int seconds = (int) (mTimeLeftInMillis2 / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d", seconds);
        waitingTV.setText("Please Wait : "+timeLeftFormatted);


    }






}
