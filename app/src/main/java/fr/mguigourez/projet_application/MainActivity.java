package fr.mguigourez.projet_application;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText studio;
    private EditText date;
    private Spinner genre;
    private SeekBar nombreP;
    private TextView affichageP;
    private ImageButton search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.studio = findViewById(R.id.studio);
        this.date = findViewById(R.id.date);
        this.genre = findViewById(R.id.genre);
        this.nombreP = findViewById(R.id.nombre_pages);
        this.affichageP = findViewById(R.id.affichage_pages);
        this.search = findViewById(R.id.recherche);



    }
}
