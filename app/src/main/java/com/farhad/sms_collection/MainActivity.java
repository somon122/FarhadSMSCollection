package com.farhad.sms_collection;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.farhad.sms_collection.ui.home.DataSubmitActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_policy, R.id.nav_share, R.id.nav_RateUs)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_exit) {
            finishAffinity();
        }

        if (id == R.id.adminPanel_id) {

           adminPanel("122farhad#");

        }
        if (id == R.id.action_refresh) {

          startActivity(new Intent(MainActivity.this,MainActivity.class));
          finish();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    private void adminPanel(final String password) {


        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view1 = getLayoutInflater().inflate(R.layout.admin_control, null);

        final EditText passwordET = view1.findViewById(R.id.adminCheckPassword_id);
        Button submit = view1.findViewById(R.id.adminSubmit_id);

        builder.setTitle("Admin Panel");
        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mPassword = passwordET.getText().toString();

                if (mPassword.isEmpty()) {

                    passwordET.setError("Please enter password");

                } else {

                    if (mPassword.equals(password)) {

                        Toast.makeText(MainActivity.this, "Password is matches", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, DataSubmitActivity.class));


                    } else {

                        Toast.makeText(MainActivity.this, "Password is not matches", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        builder.setView(view1);
        AlertDialog dialog = builder.create();
        dialog.show();


    }
}
