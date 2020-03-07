package anamapp.pro.belajar;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import anamapp.pro.belajar.crud.DataActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class BasicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button tombolCrud = findViewById(R.id.tombolCrud);
        tombolCrud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DataActivity.class);
                startActivity(intent);
            }
        });
        Button nav = findViewById(R.id.navigationDrawer);
        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        Toast.makeText(this, "INI START", Toast.LENGTH_LONG).show();
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Toast.makeText(this, "INI RESTART", Toast.LENGTH_LONG).show();
        super.onRestart();
    }

    @Override
    protected void onStop() {
        Toast.makeText(this, "INI STOP", Toast.LENGTH_LONG).show();
        super.onStop();
    }

    @Override
    protected void onResume() {
        Toast.makeText(this, "INI RESUME", Toast.LENGTH_LONG).show();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Toast.makeText(this, "INI DESTROY", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Toast.makeText(this, "INI PAUSE", Toast.LENGTH_LONG).show();
        super.onPause();
    }
}
