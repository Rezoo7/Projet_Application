package fr.mguigourez.projet_application;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.List;
import java.util.concurrent.ExecutionException;

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

        this.studio = findViewById(R.id.nom_studio);
        this.date = findViewById(R.id.date);
        this.genre = findViewById(R.id.genre);
        this.nombreR = findViewById(R.id.nombre_resultats);
        this.affichageR = findViewById(R.id.affichage_resultats);
        final ImageButton search = findViewById(R.id.recherche);

        this.nombreR.setMax(100);
        affichageR.setText("5");

        this.nombreR.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar bs, int progress, boolean fromUser) {
                if (progress < 5) {
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

        studio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                studio.clearListSelection();

                String studio_search = studio.getText().toString();

                if( !studio_search.isEmpty() ) {

                    studio_search = studio_search.replaceAll(" ", "%20");

                    Ion.with(c)
                            .load("https://api.themoviedb.org/3/search/company?api_key=d9d52bd9b5ead14f7d1feb2111e99354&query=" + studio_search)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {

                                    if (result != null) {

                                        try {
                                            JSONObject reader = new JSONObject(result.toString());

                                            JSONArray resultats = reader.getJSONArray("results");

                                            if (resultats != null) {

                                                String[] studios = new String[resultats.length()];

                                                for (int i = 0; i < resultats.length(); i++) {

                                                    JSONObject json = resultats.getJSONObject(i);
                                                    studios[i] = json.getString("name");
                                                }

                                                ArrayAdapter<String> adapter = new ArrayAdapter<>(c, android.R.layout.simple_dropdown_item_1line, studios);
                                                studio.setAdapter(adapter);
                                            }

                                        } catch (JSONException e1) {
                                            e1.printStackTrace();
                                        }

                                    }

                                }

                            });
                }
            }
        });

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

                        try {

                            final JSONObject reader = new JSONObject(result.toString());
                            JSONArray genres = reader.getJSONArray("genres");

                            for (int i = 0; i < genres.length(); i++) {
                                JSONObject json = genres.getJSONObject(i);
                                list_genre.add(json.getString("name"));
                            }

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(c, android.R.layout.simple_spinner_item, list_genre);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            genre.setAdapter(dataAdapter);

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                });

        /************** RECHERCHE *************/

        search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {

                    JsonObject resultGenre = Ion.with(c)
                            .load("https://api.themoviedb.org/3/genre/movie/list?api_key=d9d52bd9b5ead14f7d1feb2111e99354")
                            .asJsonObject()
                            .get();

                    int gen = 0;

                    try {

                        final JSONObject reader = new JSONObject(resultGenre.toString());
                        JSONArray genres = reader.getJSONArray("genres");

                        for (int i = 0; i < genres.length(); i++) {
                            JSONObject json = genres.getJSONObject(i);

                            if(genre.getSelectedItem().toString().equals(json.getString("name"))){

                                gen = json.getInt("id");

                                break;

                            }
                        }

                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                    String nomstudio = studio.getText().toString();

                    if( !nomstudio.isEmpty() ) {

                        nomstudio = nomstudio.replaceAll(" ", "%20");

                        JsonObject result = Ion.with(c)
                                .load("https://api.themoviedb.org/3/search/company?api_key=d9d52bd9b5ead14f7d1feb2111e99354&query=" + nomstudio)
                                .asJsonObject()
                                .get();

                        try {
                            JSONObject reader = new JSONObject(result.toString());

                            JSONArray resultats = reader.getJSONArray("results");

                            if (resultats != null) {

                                if (resultats.length() == 1) {

                                    JSONObject json = resultats.getJSONObject(0);

                                    String stu = json.getString("id");
                                    String da = date.getSelectedItem().toString();
                                    int nb = nombreR.getProgress();
                                    if (nb < 5) {
                                        nb = 5;
                                    }

                                    Intent affichageRes = new Intent(getApplicationContext(), SecondActivity.class);
                                    affichageRes.putExtra("studio", stu);
                                    affichageRes.putExtra("date", da);
                                    affichageRes.putExtra("genre", gen);
                                    affichageRes.putExtra("nombre", nb);
                                    startActivity(affichageRes);

                                }

                            }

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    } else {

                        String stu = "";
                        String da = date.getSelectedItem().toString();
                        int nb = nombreR.getProgress();
                        if (nb < 5) {
                            nb = 5;
                        }

                        Intent affichageRes = new Intent(getApplicationContext(), SecondActivity.class);
                        affichageRes.putExtra("studio", stu);
                        affichageRes.putExtra("date", da);
                        affichageRes.putExtra("genre", gen);
                        affichageRes.putExtra("nombre", nb);
                        startActivity(affichageRes);

                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

    }
}
