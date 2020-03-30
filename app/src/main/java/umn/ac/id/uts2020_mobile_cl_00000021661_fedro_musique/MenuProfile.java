package umn.ac.id.uts2020_mobile_cl_00000021661_fedro_musique;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MenuProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_profile);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
