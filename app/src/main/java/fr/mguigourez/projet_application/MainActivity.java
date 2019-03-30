package fr.mguigourez.projet_application;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import java.util.Arrays;
import java.util.List;

import fr.mguigourez.projet_application.Listeners.BarNombrePagesListener;

public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView studio;
    private Spinner date;
    private Spinner genre;
    private SeekBar nombreR;
    private TextView affichageR;
    private ImageButton search;

    private String[][] listGenres;
    private String[][] listMovies;

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
        this.search = findViewById(R.id.recherche);

        this.nombreR.setMax(150);
        this.nombreR.setOnSeekBarChangeListener(new BarNombrePagesListener(this.affichageR));

        /* -------------------  GET DATA ----------------------- */

        /************** INITIALISATION *************/

        final Context c = this;
        listMovies = new String[950][14];

        /************** MOVIES *************/

        for (int i = 0; i < 50; i++) {
            Ion.with(c)
                    .load("https://api.themoviedb.org/3/movie/popular?api_key=d9d52bd9b5ead14f7d1feb2111e99354&page=" + i)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {

                            try {

                                JSONObject reader = new JSONObject(result.toString());
                                if (reader.has("results")) {

                                    JSONArray movies = reader.getJSONArray("results");

                                    for (int j = 0; j < movies.length(); j++) {

                                        JSONObject json = movies.getJSONObject(j);
                                        /*listMovies[i*j][0] = json.getString("vote_count");
                                        listMovies[i*j][1] = String.valueOf(json.getInt("id"));
                                        listMovies[i*j][2] = String.valueOf(json.getBoolean("video"));
                                        listMovies[i*j][3] = String.valueOf(json.getDouble("vote_average"));
                                        listMovies[i*j][4] =json.getString("title");
                                        listMovies[i*j][5] = String.valueOf(json.getDouble("popularity"));
                                        listMovies[i*j][6] = json.getString("poster_path");
                                        listMovies[i*j][7] = json.getString("original_language");
                                        listMovies[i*j][8] = json.getString("original_title");
                                        listMovies[i*j][9] = String.valueOf(json.getJSONArray("genre_ids"));
                                        listMovies[i*j][10] = json.getString("backdrop_path");
                                        listMovies[i*j][11] = String.valueOf(json.getBoolean("adult"));
                                        listMovies[i*j][12] = json.getString("overview");
                                        listMovies[i*j][13] = json.getString("release_date");*/

                                    }

                                }

                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    });
        }

        Log.d("DATA", Arrays.toString(listMovies[0]));


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

                        List<String> list = new ArrayList<>();

                        try {

                            JSONObject reader = new JSONObject(result.toString());
                            JSONArray genres = reader.getJSONArray("genres");

                            // listGenres = new String[genres.length()][2];

                            for (int i = 0; i < genres.length(); i++) {
                                JSONObject json = genres.getJSONObject(i);
                                list.add(json.getString("name"));
                                /*listGenres[i][0] = String.valueOf(json.getInt("id"));
                                listGenres[i][1] = json.getString("name");*/
                            }

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(c, android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            genre.setAdapter(dataAdapter);

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                });

        this.search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String stu = studio.getText().toString();

                if (!stu.matches("")) {


                    String da = date.getSelectedItem().toString();
                    String gen = genre.getSelectedItem().toString();
                    int nb = nombreR.getProgress();

                    Intent resultats = new Intent(getApplicationContext(), SecondActivity.class);
                    resultats.putExtra("studio",stu);
                    resultats.putExtra("date",da);
                    resultats.putExtra("genre",gen);
                    resultats.putExtra("nombre",nb);
                    startActivity(resultats);


                } else {
                    Log.d("DATA", "null" );
                }


            }

        });


    }
}
