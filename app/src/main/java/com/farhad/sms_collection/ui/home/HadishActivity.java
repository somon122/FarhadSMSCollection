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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codesgood.views.JustifiedTextView;
import com.farhad.sms_collection.MainActivity;
import com.farhad.sms_collection.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
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

public class HadishActivity extends AppCompatActivity implements RewardedVideoAdListener {

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            if (mInterstitialAd.isLoaded()){
                mInterstitialAd.show();
            } {
                this.finish();

            }

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
        }else {

            this.finish();

        }
    }

    private TextView waitingTV,counterTV;
    private JustifiedTextView dataTV;
    private Button shareButton,nextButton;
    private AdView mAdView;
    private RewardedVideoAd mRewardedVideoAd;
    private InterstitialAd mInterstitialAd;


    private ProgressBar progressBar;

    private FirebaseFirestore mfirestore;
    private List<ModelClass> dataList;
    private int size;
    private String currentData;
    private int updateCount;

    private static final long START_TIME_IN_MILLIS2 = 5000;
    private CountDownTimer mCountDownTimer2;
    private boolean mTimerRunning2;
    private long mTimeLeftInMillis2;
    private long mEndTime2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hadish);

        Toolbar toolbar = findViewById(R.id.smsToolBar_id2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        waitingTV = findViewById(R.id.smsWaiting2_id2);
        dataTV = findViewById(R.id.smsView_id2);
        counterTV = findViewById(R.id.smsCounter_id2);
        shareButton = findViewById(R.id.smsShare_id2);
        nextButton = findViewById(R.id.smsNext_id2);
        waitingTV.setVisibility(View.GONE);

        mfirestore = FirebaseFirestore.getInstance();
        dataList = new ArrayList<>();
        progressBar = findViewById(R.id.smsProgressBar2);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String value = bundle.getString("quotation");
            setTitle(value);
            loadData(value);
        }

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id2));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();


        mAdView = findViewById(R.id.smsAdView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {

            }

            @Override
            public void onAdOpened() {

            }

            @Override
            public void onAdClicked() {

            }

            @Override
            public void onAdLeftApplication() {

            }

            @Override
            public void onAdClosed() {
               finish();
            }
        });


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

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd(getString(R.string.videoReward_ad_unit_id2),
                new AdRequest.Builder().build());
    }



    private void dataShare(String body) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareSub = "সহীহ হাদিসের উক্তি";
        intent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
        intent.putExtra(Intent.EXTRA_TEXT,body);
        startActivity(Intent.createChooser(intent,"হাদিসের উক্তি"));


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
                        Toast.makeText(HadishActivity.this, "Quiz Show is Field", Toast.LENGTH_SHORT).show();
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

        final AlertDialog.Builder builder = new AlertDialog.Builder(HadishActivity.this);

        builder.setTitle("Congratulation!")
                .setMessage("You are completed all SMS")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(HadishActivity.this, MainActivity.class));
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

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

        dataShare(currentData);
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

        dataShare(currentData);
    }
}
