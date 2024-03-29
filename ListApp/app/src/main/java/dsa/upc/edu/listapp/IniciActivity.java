package dsa.upc.edu.listapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import java.util.Locale;

public class IniciActivity extends AppCompatActivity {
    private TextInputEditText usuari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inici_usuaris);
        //btnToggleMode.setOn
        usuari=findViewById(R.id.usuari);
    }

    public void button2Click(View view){
        String nomUsuari = usuari.getText().toString();
        //usuari=findViewById(R.id.usuari);
        Intent intent = new Intent(this, ReposActivity.class);
        intent.putExtra("nomUsuari", nomUsuari);
        startActivity(intent);
    }

}
