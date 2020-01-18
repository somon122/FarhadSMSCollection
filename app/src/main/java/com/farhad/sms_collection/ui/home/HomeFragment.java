package com.farhad.sms_collection.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.farhad.sms_collection.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class HomeFragment extends Fragment {

    private ImageButton englishHadish, banglaHadish2, englishHadish2, banglaHadish;
    private AdView mAdView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        englishHadish = root.findViewById(R.id.englishHadish_id);
        banglaHadish2 = root.findViewById(R.id.banglaHadish2_id);
        englishHadish2 = root.findViewById(R.id.englishHadish2_id);
        banglaHadish = root.findViewById(R.id.banglaHadish_id);

        mAdView = root.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

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



        return root;
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


}