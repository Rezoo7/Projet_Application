package fr.mguigourez.projet_application;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

        /* -------------------  GET DATA ----------------------- */ //TODO

        /************** GENRE *************/

        final Context c = this;

        Ion.with(this)
                .load("https://api.themoviedb.org/3/genre/movie/list?api_key=d9d52bd9b5ead14f7d1feb2111e99354")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        List<String> list = new ArrayList<>();

                        try {

                            JSONObject reader = new JSONObject(result.toString());
                            JSONArray genres  = reader.getJSONArray("genres");
                            for( int i = 0; i < genres.length(); i++ ) {
                                JSONObject json = genres.getJSONObject(i);
                                list.add( json.getString("name"));
                            }

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(c, android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            genre.setAdapter(dataAdapter);

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                });


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
