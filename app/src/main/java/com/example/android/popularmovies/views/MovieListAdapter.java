package com.example.android.popularmovies.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.utilities.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.zip.Inflater;

import static com.example.android.popularmovies.utilities.Constants.MOVIE_PIC_BASE_PATH;

/**
 * Created by Pedram on 12/11/2016.
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

    List<Movie> mItems;
    InteractionListener mListener;
    Context mContext;

    public interface InteractionListener {
        void onItemClickListener(long id);
    }

    public MovieListAdapter(List<Movie> items, InteractionListener listener) {

        mItems = items;
        mListener = listener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        holder.bind(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView posterImageView;
        TextView titleTextView;
        TextView rateTextView;
        TextView dateTextView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            posterImageView = (ImageView) itemView.findViewById(R.id.item_movie_img);
            titleTextView = (TextView) itemView.findViewById(R.id.item_movie_tv_title);
            rateTextView = (TextView) itemView.findViewById(R.id.item_movie_tv_rate);
            dateTextView = (TextView) itemView.findViewById(R.id.item_movie_tv_date);
            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {

            Picasso.with(mContext).load(MOVIE_PIC_BASE_PATH + movie.getPoster()).into(posterImageView);
            titleTextView.setText(movie.getTitle());
            rateTextView.setText(String.valueOf(movie.getVote()));
        }

        @Override
        public void onClick(View view) {
            mListener.onItemClickListener(mItems.get(getAdapterPosition()).getId());
        }
    }
}
