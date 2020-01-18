package com.farhad.sms_collection.ui.policy;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.farhad.sms_collection.R;

public class PolicyFragment extends Fragment {


    Button policy;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_policy, container, false);


        policy = root.findViewById(R.id.privacyPolicy_id);

        policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://supernicetitle.blogspot.com/2020/01/blog-post.html?m=1&fbclid\n" +
                            "=IwAR3pbziKsHXlOsWXm8D6wnuLJixulJcHJe9qiMOpMYTrGuS66JHId8-4-bc")));

                }catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://supernicetitle.blogspot.com/2020/01/blog-post.html?m=1&fbclid\n" +
                            "=IwAR3pbziKsHXlOsWXm8D6wnuLJixulJcHJe9qiMOpMYTrGuS66JHId8-4-bc")));
                }


            }
        });

        return root;
    }
}