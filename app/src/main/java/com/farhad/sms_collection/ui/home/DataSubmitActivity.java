package com.farhad.sms_collection.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.farhad.sms_collection.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSubmitActivity extends AppCompatActivity {


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private Button submitButton;
    private EditText dataET;
    private Spinner spinner;
    private String spinnerValue;
    private FirebaseFirestore mfirestore;
    private ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_submit);

        Toolbar toolbar = findViewById(R.id.SubmitToolbar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Admin Panel");
        mfirestore = FirebaseFirestore.getInstance();

        List<String> dataCategory = new ArrayList<String>();
        dataCategory.add("ইংলিশ_হাদিস-1");
        dataCategory.add("ইংলিশ_হাদিস-2");
        dataCategory.add("বাংলা_হাদিস-১");
        dataCategory.add("বাংলা_হাদিস-২");
        dataCategory.add("Slide_images");


        submitButton = findViewById(R.id.submitButton_id);
        dataET = findViewById(R.id.submitEditText_id);
        spinner = findViewById(R.id.submitSpinner_id);
        progressBar = findViewById(R.id.submitProgressBar);
        progressBar.setVisibility(View.GONE);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dataCategory);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerValue = spinner.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();
            }
        });

    }

    private void submitData() {

        String dataText = dataET.getText().toString();

        if (dataText.isEmpty()){

            dataET.setError("Please enter some data");
        }else {

            progressBar.setVisibility(View.VISIBLE);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
            String currentDateAndTime = sdf.format(new Date());

            Map<String,Object>dataPost= new HashMap<>();
            dataPost.put("mTime",currentDateAndTime);
            dataPost.put("mData",dataText);

            mfirestore.collection(spinnerValue).add(dataPost).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()){
                        progressBar.setVisibility(View.GONE);
                        dataET.setText("");
                        Toast.makeText(DataSubmitActivity.this, "Data Submit Success", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dataET.setText("");
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(DataSubmitActivity.this, "Data Submit Field", Toast.LENGTH_SHORT).show();
                }
            });


        }
    }



}
