package fr.mguigourez.projet_application;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView studio;
    private Spinner date;
    private Spinner genre;
    private SeekBar nombreR;
    private TextView affichageR;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.studio = findViewById(R.id.studio);
        this.date = findViewById(R.id.date);
        this.genre = findViewById(R.id.genre);
        this.nombreR = findViewById(R.id.nombre_resultats);
        this.affichageR = findViewById(R.id.affichage_resultats);
        final ImageButton search = findViewById(R.id.recherche);

        this.nombreR.setMax(100);
        affichageR.setText("5");

        this.nombreR.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar bs, int progress, boolean fromUser)
            {
                if( progress < 5 ){
                    progress = 5;
                }

                affichageR.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });

        /* -------------------  GET DATA ----------------------- */

        /************** INITIALISATION *************/

        final Context c = this;

        /************** STUDIO *************/

        final String[] STUDIO = new String[]{
                "Warner Bros",
                "Sony Pictures Motion Picture Group",
                "Walt Disney Studios",
                "Universal Pictures",
                "20th Century Fox",
                "Paramount Pictures",
                "Lionsgate Films",
                "The Weinstein Company",
                "Paramount Pictures Corporation",
                "MTV Films",
                "DC Entertainment",
                "New Line Cinema",
                "Home Box Office",
                "Castle Rock Entertainment",
                "Blue Sky Studios Inc.",
                "Columbia Pictures",
                "Screen Gems",
                "TriStar",
                "Pixar Animation Studios",
                "Marvel Entertainment",
                "Lucasfilm Limited",
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(c, android.R.layout.simple_dropdown_item_1line, STUDIO);
        studio.setAdapter(adapter);

        /************** DATE *************/

        List<String> list = new ArrayList<>();

        for (int i = 2019; i > 1950; i--) {
            list.add(String.valueOf(i));
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(c, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        date.setAdapter(dataAdapter);

        /************** GENRE *************/

        Ion.with(c)
                .load("https://api.themoviedb.org/3/genre/movie/list?api_key=d9d52bd9b5ead14f7d1feb2111e99354")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        List<String> list_genre = new ArrayList<>();
                        Map<String, Integer> map = new HashMap<>();

                        try {

                            JSONObject reader = new JSONObject(result.toString());
                            JSONArray genres = reader.getJSONArray("genres");

                            for (int i = 0; i < genres.length(); i++) {
                                JSONObject json = genres.getJSONObject(i);
                                list_genre.add(json.getString("name"));
                                map.put( json.getString("name"), json.getInt("id") );
                            }

                            final Map<String, Integer> finalMap = map;

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(c, android.R.layout.simple_spinner_item, list_genre);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            genre.setAdapter(dataAdapter);

                            search.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {

                                    String stu = studio.getText().toString();
                                    String da = date.getSelectedItem().toString();
                                    String gen = genre.getSelectedItem().toString();
                                    int nb = nombreR.getProgress();
                                    if( nb < 5 ){ nb = 5; }

                                    Intent resultats = new Intent(getApplicationContext(), SecondActivity.class);
                                    resultats.putExtra("studio", stu);
                                    resultats.putExtra("date", da);
                                    resultats.putExtra("genre", finalMap.get(gen) );
                                    resultats.putExtra("nombre", nb);
                                    startActivity(resultats);

                                }

                            });

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                });

    }
}
