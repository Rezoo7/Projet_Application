package fr.mguigourez.projet_application;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import fr.mguigourez.projet_application.Listeners.BarNombrePagesListener;

public class MainActivity extends AppCompatActivity {

    private EditText studio;
    private EditText date;
    private Spinner genre;
    private SeekBar nombreR;
    private TextView affichageR;
    private ImageButton search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.studio = findViewById(R.id.studio);
        this.date = findViewById(R.id.date);
        this.genre = findViewById(R.id.genre);
        this.nombreR = findViewById(R.id.nombre_resultats);
        this.affichageR = findViewById(R.id.affichage_resultats);
        this.search = findViewById(R.id.recherche);


        this.nombreR.setMax(150);
        this.nombreR.setOnSeekBarChangeListener(new BarNombrePagesListener(this.affichageR));





        /* -------------------  BOUTON SEARCH => SecondActivity   //TODO Faire fonctionner ! ----------------------- */
        this.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultats = new Intent(getApplicationContext(),SecondActivity.class);
                startActivity(resultats);
            }
        });


    }
}
