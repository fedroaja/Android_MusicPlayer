package umn.ac.id.uts2020_mobile_cl_00000021661_fedro_musique;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText etUsername,etPassword;
    Button btLogin;
    int KODE_MENU_UTAMA = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etUsername = (EditText) findViewById(R.id.username);
        etPassword = (EditText) findViewById(R.id.password);
        btLogin    = (Button)   findViewById(R.id.Login);

        //login
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //login validation and send msg for new intent
                if (etUsername.getText().toString().equals("musique") && etPassword.getText().toString().equals("musique123")) {
                    Toast.makeText(getApplicationContext(),
                            Html.fromHtml("<font color='#FFFFFF' > Redicrecting ...</font>"), Toast.LENGTH_SHORT).show();
                    Intent intentMenuUtama = new Intent(MainActivity.this, MenuUtama.class);
                    String isiPsn = "Fedro \n00000021661";
                    intentMenuUtama.putExtra("AccName",isiPsn);
                    startActivityForResult(intentMenuUtama,KODE_MENU_UTAMA);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),
                            Html.fromHtml("<font color='#c90404' > Username atau Password Salah !</font>"), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
