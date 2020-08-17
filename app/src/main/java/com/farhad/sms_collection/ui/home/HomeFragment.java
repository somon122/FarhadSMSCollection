package com.farhad.sms_collection.ui.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.farhad.sms_collection.MainActivity;
import com.farhad.sms_collection.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private ImageButton englishHadish, banglaHadish2, englishHadish2, banglaHadish;
    private AdView mAdView;

    private ImageView slideImages;
    TextView next, previous,slideCount;

    private FirebaseFirestore mfirestore;
    private List<ModelClass> dataList;
    private int size;
    private String currentData;
    private int updateCount;
    private int touchCount;

    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        englishHadish = root.findViewById(R.id.englishHadish_id);
        banglaHadish2 = root.findViewById(R.id.banglaHadish2_id);
        englishHadish2 = root.findViewById(R.id.englishHadish2_id);
        banglaHadish = root.findViewById(R.id.banglaHadish_id);
        slideImages = root.findViewById(R.id.slideImages_id);
        next = root.findViewById(R.id.slideNext_id);
        previous = root.findViewById(R.id.slidePrevious_id);
        slideCount = root.findViewById(R.id.slideCount_id);
        next.setVisibility(View.GONE);
        previous.setVisibility(View.GONE);
        scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());

        mfirestore = FirebaseFirestore.getInstance();
        dataList = new ArrayList<>();

        mAdView = root.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        loadData("Slide_images");

        englishHadish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               sent2("ইংলিশ_হাদিস-1");

            }
        });

         englishHadish2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sent2("ইংলিশ_হাদিস-2");

            }
        });

        banglaHadish2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sent("বাংলা_হাদিস-২");

            }
        });

         banglaHadish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sent3("বাংলা_হাদিস-১");
            }
        });


         next.setOnClickListener(new View.OnClickListener() {
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


         previous.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 try {
                     if (updateCount >= 1 ){
                         previousSMS();
                     }else {
                         dataStatus();
                     }
                 }catch (Exception e){
                     dataStatus();
                 }
             }
         });

         slideImages.setOnTouchListener(new View.OnTouchListener() {
             @Override
             public boolean onTouch(View v, MotionEvent event) {

                 scaleGestureDetector.onTouchEvent(event);

                 if (touchCount >= 1){
                     touchCount=0;
                     next.setVisibility(View.GONE);
                     previous.setVisibility(View.GONE);

                 }else {
                     touchCount =touchCount+1;
                     next.setVisibility(View.VISIBLE);
                     previous.setVisibility(View.VISIBLE);
                 }
                 return false;
             }
         });


        return root;
    }

    private void updatedata(int i) {

        if (!dataList.isEmpty()){
            slideCount.setVisibility(View.VISIBLE);
            ModelClass modelClass = dataList.get(i);
            currentData = modelClass.getmData();
            Picasso.get().load(currentData).fit().centerCrop().placeholder(R.drawable.hadish1).into(slideImages);


        }else {
            Toast.makeText(getContext(), "Data is not available", Toast.LENGTH_SHORT).show();
            slideCount.setVisibility(View.GONE);
            Picasso.get().load(currentData).fit().centerCrop().placeholder(R.drawable.hadish1).into(slideImages);
        }


    }

    private void dataStatus() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Alert!")
                .setMessage("You are completed all Images")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void nextSMS() {
        updateCount = updateCount+1;
        slideCount.setText(updateCount+"/"+size);
        updatedata(updateCount);

    }

 private void previousSMS() {
        updateCount = updateCount-1;
        slideCount.setText(updateCount+"/"+size);
        updatedata(updateCount);

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
                        updatedata(0);
                        size = dataList.size()-1;
                        slideCount.setText("0"+"/"+size);

                    } else {
                        Toast.makeText(getContext(), "Image Show is Field", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private void sent(String value) {

        Intent intent = new Intent(getContext(),Sms_Poem_Activity.class);
        intent.putExtra("quotation",value);
        startActivity(intent);


    }
     private void sent2(String value) {

            Intent intent = new Intent(getContext(),QuotationsActivity.class);
            intent.putExtra("quotation",value);
            startActivity(intent);


        }
 private void sent3(String value) {

            Intent intent = new Intent(getContext(),HadishActivity.class);
            intent.putExtra("quotation",value);
            startActivity(intent);


        }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            slideImages.setScaleX(mScaleFactor);
            slideImages.setScaleY(mScaleFactor);
            return true;
        }
    }
}