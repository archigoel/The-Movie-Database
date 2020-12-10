package com.example.movieapplication.fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;

import com.example.movieapplication.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class ItemDetailFragment extends DialogFragment {

    public ItemDetailFragment() {
    }

    public static ItemDetailFragment newInstance() {
        ItemDetailFragment fragment = new ItemDetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_item_detail, container, false);

        Bundle bundle = getArguments();
        String path = bundle.getString("Poster Path");
        ImageView itemView = v.findViewById(R.id.movie_image);
        TextView tv = v.findViewById(R.id.movie_text);
        TextView overview= v.findViewById(R.id.overview_text);
        TextView genresText= v.findViewById(R.id.genres_text);
        if(path!= "null") {
            Picasso.with(getActivity()).load(path).into(itemView); // load image
        }
        tv.setText( bundle.getString("Title"));
        overview.setText(bundle.getString("Overview"));
        ArrayList<String> list = bundle.getStringArrayList("Genres");
        String genres= "";
        for(String s: list){
            genres = genres + s;
            if(list.indexOf(s) != list.size()-1){
                genres = genres + ", ";
            }
        }
        String first = "<font color='#9e9d9d'>Genres:</font>";
        genresText.setText(Html.fromHtml(first +" "+genres));

        return v;
    }

}