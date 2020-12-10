package com.example.movieapplication.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapplication.fragments.ItemDetailFragment;
import com.example.movieapplication.R;
import com.example.movieapplication.activities.MainActivity;
import com.example.movieapplication.model.Movie;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private List<Movie> movieList;
    private Context context;

    public RecyclerAdapter ( Context ctx, List<Movie> values) {
        context = ctx;
        movieList = values;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView movieTitle;
        public TextView movieReleaseDate;
        public ImageView movieImage;

        public MyViewHolder(View v){

            super(v);
            movieTitle = (TextView)v.findViewById(R.id.movie_text);
            movieReleaseDate = (TextView)v.findViewById(R.id.release_date_text);
            movieImage = v.findViewById(R.id.movie_full_poster);
            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            ItemDetailFragment newFragment = ItemDetailFragment.newInstance();
            MainActivity mainActivity = (MainActivity)itemView.getContext();
            Bundle bundle = new Bundle();
            String path = movieList.get(getAdapterPosition()).getMoviePoster();
            String title = movieList.get(getAdapterPosition()).getName();
            String overview = movieList.get(getAdapterPosition()).getOverview();
            ArrayList<String> genresList = movieList.get(getAdapterPosition()).getGenres();
            bundle.putString("Poster Path", path);
            bundle.putString("Title", title);
            bundle.putString("Overview", overview);
            bundle.putStringArrayList("Genres", genresList);
            newFragment.setArguments(bundle);
            newFragment.show(mainActivity.getSupportFragmentManager(), "fragment_edit_name");
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_layout, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final String name = movieList.get(position).getName();
        Log.i("NAME" ,name);
        holder.movieTitle.setText(name);
        holder.movieReleaseDate.setText("Released on: "+movieList.get(position).getReleaseDate());
        String val = movieList.get(position).getImage();
        if(val!= "null") {
            Picasso.with(context).load(val).into(holder.movieImage); // load image
        }

    }
    @Override
    public int getItemCount() {
        return movieList.size();
    }

}
