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
        int nbr = 0;
        int page = 0;
        int last_nombre = 0;
        int timeout = 0;

        while( nbr < nombre_resultat ) {

            page++;

            try {
                JsonObject result = Ion.with(c)
                        .load("https://api.themoviedb.org/3/discover/movie?api_key=d9d52bd9b5ead14f7d1feb2111e99354&with_companies=" + nom_studio + "&primary_release_year=" + date_sortie + "&with_genres=" + genre_film + "&sort_by=popularity.desc&page=" + page)
                        .asJsonObject()
                        .get();

                JSONObject reader = null;
                try {
                    reader = new JSONObject(result.toString());
                    JSONArray resultats = reader.getJSONArray("results");

                    if (resultats != null) {

                        for (int j = 0; j < resultats.length(); j++) {

                            Map<String, Integer> map = new HashMap<>();
                            final JSONObject json = resultats.getJSONObject(j);

                            if (nbr < nombre_resultat) {

                                map.put(json.getString("title"), json.getInt("id"));

                                final TextView film_tmp = new TextView(c);
                                film_tmp.setText(json.getString("title"));
                                film_tmp.setPadding(10, 40, 0, 0);
                                film_tmp.setTextSize(30);

                                nbr++;

                                final Map<String, Integer> finalMap = map;

                                film_tmp.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            Intent details = new Intent(getApplicationContext(), ThirdActivity.class);
                                            details.putExtra("film", finalMap.get(json.getString("title")));
                                            startActivity(details);

                                        } catch (JSONException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                });

                                listeWithScroll.addView(film_tmp);

                            } else {
                                break;
                            }

                        }
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if( nbr == last_nombre ){
                timeout++;
                if( timeout == 10 ){
                    break;
                }
            } else {
                timeout = 0;
                last_nombre = nbr;
            }

        }

    }
}
