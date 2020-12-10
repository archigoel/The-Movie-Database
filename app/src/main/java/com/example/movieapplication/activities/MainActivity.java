package com.example.movieapplication.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapplication.HttpHandler;
import com.example.movieapplication.model.Movie;
import com.example.movieapplication.MovieItemDecoration;
import com.example.movieapplication.R;
import com.example.movieapplication.adapters.RecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Movie> movieList;
    private static String webUrl = "https://api.themoviedb.org/3/movie/popular?api_key=0eedabe2e4f05e9bd4268899648901dd&language=en-US&page=1";
    private static String genresUrl = "https://api.themoviedb.org/3/genre/movie/list?api_key=0eedabe2e4f05e9bd4268899648901dd&language=en-US";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new MovieInfo().execute();
    }
   // return genres name with genre id as key
    private HashMap<String, String> getGenres(String str){
        HashMap<String, String> genresMap = new HashMap<>();

        if(str != null) {
            try {
                JSONObject jsonObject = new JSONObject(str);
                JSONArray results = jsonObject.getJSONArray("genres");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject temp = results.getJSONObject(i);
                    genresMap.put(temp.getString("id"), temp.getString("name"));
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return genresMap;
    }

    private class MovieInfo extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            movieList = new ArrayList<>();
            HttpHandler handler = new HttpHandler();
            String jsonStr = handler.makeServiceCall(webUrl);
            //get genres list
            String genreStr = handler.makeServiceCall(genresUrl);
            HashMap<String, String> genresMap = getGenres(genreStr);

            if (jsonStr != null) {
                try {
                    String path,posterPath;
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONArray results = jsonObject.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        ArrayList<String> genresList = new ArrayList<>();
                        JSONObject temp = results.getJSONObject(i);
                        Movie movie = new Movie();
                        movie.setOverview(temp.getString("overview"));
                        movie.setReleaseDate(temp.getString("release_date"));
                        movie.setName(temp.getString("title"));
                        if(temp.getString("poster_path") == "null")
                            posterPath = "null";
                        else
                            posterPath = "https://image.tmdb.org/t/p/w500/" + temp.getString("poster_path");
                        if(temp.getString("backdrop_path") == "null")
                            path = "null";
                        else
                            path = "https://image.tmdb.org/t/p/w400/" + temp.getString("backdrop_path");

                        movie.setImage(path);
                        movie.setMoviePoster(posterPath);
                        movie.setOverview(temp.getString("overview"));
                        JSONArray genresId = temp.getJSONArray("genre_ids");
                        for (int j = 0; j < genresId.length(); j++) {
                            genresList.add(genresMap.get(String.valueOf(genresId.get(j))));
                        }
                        movie.setGenres(genresList);
                        movieList.add(movie);
                    }

                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Sorry no data found: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }

                    });

                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }

                });

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);

            recyclerView = findViewById(R.id.simple_recyclerview);
            layoutManager = new LinearLayoutManager(MainActivity.this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.addItemDecoration(new MovieItemDecoration(MainActivity.this));
            mAdapter = new RecyclerAdapter(MainActivity.this, movieList);
            recyclerView.setAdapter(mAdapter);
        }
    }

}