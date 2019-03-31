package fr.mguigourez.projet_application;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        final LinearLayout listeWithScroll = findViewById(R.id.liste);

        Intent intent = getIntent();

        final String nom_studio = intent.getStringExtra("studio");
        final String date_sortie = intent.getStringExtra("date");
        final int genre_film = intent.getIntExtra("genre", 0);
        final int nombre_resultat = intent.getIntExtra("nombre", 0);

        /************** INITIALISATION *************/

        final Context c = this;
        final int finalNbr = 0;
        final int finalPage = 1;

        new Thread(new Runnable() {

            public void run() {

                int nbr = finalNbr;
                int page = finalPage;

                while( nbr < nombre_resultat ) {

                    page++;

                    JsonObject result = null;

                    try {
                        result = Ion.with(c)
                                .load("https://api.themoviedb.org/3/movie/popular?api_key=d9d52bd9b5ead14f7d1feb2111e99354&page=" + page)
                                .asJsonObject()
                                .get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    boolean bool_genre = false;
                    final Map<String, Integer> map = new HashMap<>();

                    try {
                        JSONObject reader = new JSONObject(result.toString());
                        JSONArray resultats = reader.getJSONArray("results");

                        if( resultats != null ){

                            for (int j = 0; j < resultats.length(); j++) {
                                final JSONObject json = resultats.getJSONObject(j);

                                if (nbr < nombre_resultat) {

                                    JSONArray genres = json.getJSONArray("genre_ids");

                                    for (int k = 0; k < genres.length(); k++) {
                                        int id = genres.getInt(k);
                                        if (genre_film == id) {
                                            bool_genre = true;
                                        }
                                    }

                                    String date = json.getString("release_date");
                                    String year = date.replaceAll("-\\d{2}-\\d{2}", "");

                                    if( !year.equals("") ){
                                        if (Integer.valueOf(year).equals(Integer.valueOf(date_sortie))) {
                                            if (bool_genre) {

                                                map.put(json.getString("title"), json.getInt("id"));

                                                final TextView film_tmp = new TextView(c);
                                                film_tmp.setText(json.getString("title"));
                                                film_tmp.setPadding(10, 40, 0, 0);
                                                film_tmp.setTextSize(30);

                                                nbr++;

                                                film_tmp.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        try {
                                                            Intent details = new Intent(getApplicationContext(), ThirdActivity.class);
                                                            details.putExtra("film", map.get(json.getString("title")));
                                                            startActivity(details);

                                                        } catch (JSONException e1) {
                                                            e1.printStackTrace();
                                                        }
                                                    }
                                                });

                                                runOnUiThread(new Runnable() {

                                                    public void run() {
                                                        listeWithScroll.addView(film_tmp);
                                                    }

                                                });
                                            }
                                        }
                                    }
                                } else {
                                    break;
                                }

                                bool_genre = false;

                            }
                        }

                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }

        }).start();

    }
}
