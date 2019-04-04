package fr.mguigourez.projet_application;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ThirdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        final TextView movie_titre = findViewById(R.id.title);
        final ImageView movie_pic = findViewById(R.id.affiche);
        final TextView movie_description = findViewById(R.id.description);
        final RatingBar movie_note = findViewById(R.id.rating);
        final TextView movie_studio = findViewById(R.id.nom_studio);
        final TextView movie_date = findViewById(R.id.date_film);
        final TextView movie_genre = findViewById(R.id.list_genre);

        /* ************* INITIALISATION ************ */

        final Context c = this;

        Intent intent = getIntent();

        final int id = intent.getIntExtra("film", 0 );

        /* ************* DATAS ************ */

        Ion.with(c)
                .load("https://api.themoviedb.org/3/movie/" + String.valueOf(id) + "?api_key=d9d52bd9b5ead14f7d1feb2111e99354")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        try {

                            JSONObject reader = new JSONObject(result.toString());

                            String genre = "";
                            String studio = "";

                            String titre = reader.getString("original_title");
                            final String img = reader.getString("poster_path");
                            String description = reader.getString("overview");
                            double note = reader.getDouble("vote_average");
                            JSONArray studios = reader.getJSONArray("production_companies");
                            for (int i = 0; i < studios.length(); i++) {
                                JSONObject json = studios.getJSONObject(i);
                                studio += " - " + json.getString("name") + "\n";
                            }
                            String date = reader.getString("release_date");
                            JSONArray genres = reader.getJSONArray("genres");
                            for (int i = 0; i < genres.length(); i++) {
                                JSONObject json = genres.getJSONObject(i);
                                genre += " - " + json.getString("name") + "\n";
                            }

                            new Thread(new Runnable() {

                                public void run() {

                                    try {

                                        URL urlPic = new URL("https://image.tmdb.org/t/p/w500" + img);
                                        HttpURLConnection connection = (HttpURLConnection) urlPic.openConnection();
                                        InputStream inputStream = connection.getInputStream();

                                        final Bitmap bm = BitmapFactory.decodeStream(inputStream);

                                        runOnUiThread(new Runnable() {

                                            public void run() {
                                                movie_pic.setImageBitmap(bm);
                                            }

                                        });

                                    } catch (MalformedURLException e2) {
                                        e2.printStackTrace();
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }

                                }

                            }).start();

                            movie_titre.setText(titre);
                            movie_description.setText(description);
                            movie_note.setRating((float) note / 2);
                            movie_studio.setText(studio);
                            movie_date.setText(date);
                            movie_genre.setText(genre);

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }
                });

    }
}
