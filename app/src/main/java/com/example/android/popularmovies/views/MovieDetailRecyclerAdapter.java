package com.example.android.popularmovies.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.models.Review;
import com.example.android.popularmovies.models.Trailer;
import com.example.android.popularmovies.utilities.IntentUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.android.popularmovies.utilities.Constants.MOVIE_PIC_BASE_PATH;

/**
 * Created by Pedram on 1/6/2017.
 */

public class MovieDetailRecyclerAdapter extends RecyclerView.Adapter<MovieDetailRecyclerAdapter.ViewHolder> {

    /*
     * Four different layouts are going to be integrated within this adapter
     */
    private static final int TYPE_OVERVIEW = 1;

    private static final int TYPE_TRAILER = 2;

    private static final int TYPE_REVIEW = 3;

    private static final int TYPE_HEADER = 4;

    Movie mMovie;
    List<Trailer> mTrailerList;
    List<Review> mReviewList;
    Context mContext;

    boolean mTrailerClosed;
    private boolean mReviewClosed;

    public MovieDetailRecyclerAdapter(Context context, Movie movie, List<Trailer> trailerList, List<Review> reviewList) {
        mContext = context;
        mMovie = movie;
        mTrailerList = trailerList;
        mReviewList = reviewList;
    }

    @Override
    public int getItemViewType(int position) {

        //Starting position
        position = (mMovie == null) ? position + 1 : position;

        //At position zero we can only have the overview layout
        if (position == 0)
            return TYPE_OVERVIEW;

        //Postion one is always a header regardless of whether trailers or reviews are available
        if (position == 1)
            return TYPE_HEADER;

        //Trailer secion is closed up so next index is going to be review header
        if (mTrailerClosed && position == 2)
            return TYPE_HEADER;

        //Trailers are closed up so only reviews remain
        if (mTrailerClosed)
            return TYPE_REVIEW;

        //Normal situation overview is shown, Trailer header is shown and it is time for trailers
        if (position <= mTrailerList.size() + 1)
            return TYPE_TRAILER;

        //Normal situation only reviews are remained to be shown
        if (position == mTrailerList.size() + 2)
            return TYPE_HEADER;

        return TYPE_REVIEW;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case TYPE_OVERVIEW:
                return new OverviewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_movie_detail_info, parent, false));
            case TYPE_TRAILER:
                return new TrailerViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_movie_detail_trailer, parent, false));
            case TYPE_REVIEW:
                return new ReviewViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_movie_detail_review, parent, false));
            case TYPE_HEADER:
                return new ContentHeaderViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_movie_content_header, parent, false));
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.bind(position);
    }

    /**
     * Depending on which data is provided for this adapter The counting is going to differ
     *
     * @return
     */
    @Override
    public int getItemCount() {
        int count = (mMovie == null) ? 0 : 1;

        //Trailers are available so we need a header
        count += (mTrailerList == null) ? 0 : 1;
        count += (mTrailerList == null || mTrailerClosed) ? 0 : mTrailerList.size();

        //Reviews are available so we need a header
        count += (mReviewList == null) ? 0 : 1;
        count += (mReviewList == null || mReviewClosed) ? 0 : mReviewList.size();
        return count;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(int position) {

        }
    }

    public class OverviewHolder extends ViewHolder {

        private ImageView mPosterImageView;
        private TextView mDateTextView;
        private TextView mRateTextView;
        private TextView mOverviewTextView;

        public OverviewHolder(View itemView) {
            super(itemView);
            mPosterImageView = (ImageView) itemView.findViewById(R.id.frag_detail_movie_img);
            mDateTextView = (TextView) itemView.findViewById(R.id.frag_detail_movie_tv_date);
            mRateTextView = (TextView) itemView.findViewById(R.id.frag_detail_movie_tv_rate);
            mOverviewTextView = (TextView) itemView.findViewById(R.id.frag_detail_movie_tv_overview);
        }

        @Override
        public void bind(int position) {
            Picasso.with(mContext).load(  mMovie.getPoster()).into(mPosterImageView);
            mDateTextView.setText(mMovie.getDate());
            mRateTextView.setText(mMovie.getVote() + "/10");
            mOverviewTextView.setText(mMovie.getOverview());
        }
    }

    public class TrailerViewHolder extends ViewHolder {

        TextView mNameTextView;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            mNameTextView = (TextView) itemView.findViewById(R.id.item_trailer_tv);

        }

        @Override
        public void bind(final int position) {
            super.bind(position);
            if (mTrailerClosed) {
                itemView.setVisibility(View.GONE);
                return;
            }
            itemView.setVisibility(View.VISIBLE);

            mNameTextView.setText(mTrailerList.get(getListIndex(position)).getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.startActivity(IntentUtils.getYouTubeIntent(mTrailerList.get(position).getKey()));
                }
            });
        }

        /**
         * Returns index equivalent of a position based on available data
         * @param position position on the adapter
         * @return
         */
        private int getListIndex(int position) {
            return (mMovie == null) ? position - 1 : position - 2;
        }
    }

    public class ReviewViewHolder extends ViewHolder {

        TextView mAuthorTextView;
        TextView mContentTextView;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            mAuthorTextView = (TextView) itemView.findViewById(R.id.item_review_tv_author);
            mContentTextView = (TextView) itemView.findViewById(R.id.item_review_tv_content);
        }

        @Override
        public void bind(int position) {
            super.bind(position);

            if (mReviewClosed) {
                itemView.setVisibility(View.GONE);
                return;
            }
            itemView.setVisibility(View.VISIBLE);

            mAuthorTextView.setText(mReviewList.get(getListIndex(position)).getAuthor());
            mContentTextView.setText(mReviewList.get(getListIndex(position)).getContent());
            mContentTextView.setMovementMethod(LinkMovementMethod.getInstance());
            mAuthorTextView.append(":");
        }

        /**
         * Returns index equivalent of a position based on available data
         * @param position position on the adapter
         * @return
         */
        private int getListIndex(int position) {
            int index = position-1;
            index -= (mMovie==null)? 0: 1;
            index -= (mTrailerList==null)?0:1+mTrailerList.size();

            return index;
        }
    }

    public class ContentHeaderViewHolder extends ViewHolder {

        ImageView mHeaderImageView;
        TextView mHeaderTextView;

        public ContentHeaderViewHolder(View itemView) {
            super(itemView);
            mHeaderImageView = (ImageView) itemView.findViewById(R.id.item_movie_header_img);
            mHeaderTextView = (TextView) itemView.findViewById(R.id.item_movie_tv_header_tv);

        }

        @Override
        public void bind( int position) {

            if (position == 0) {
                itemView.findViewById(R.id.divider).setVisibility(View.GONE);
                mHeaderImageView.setImageResource(R.drawable.ic_play);
                mHeaderTextView.setText(mContext.getResources().getString(R.string.trailers));
            } else if (position == 1 && !mTrailerClosed) {
                mHeaderImageView.setImageResource(R.drawable.ic_play);
                mHeaderTextView.setText(mContext.getResources().getString(R.string.trailers));
            } else {
                mHeaderImageView.setImageResource(R.drawable.ic_rate_review);
                mHeaderTextView.setText(mContext.getResources().getString(R.string.reviews));
            }
            mHeaderTextView.append(":");

            itemView.setTag(position);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (int) itemView.getTag();
                    if(position<=1) {
                        mTrailerClosed = !mTrailerClosed;
                        notifyItemRangeRemoved(2, mTrailerList.size());
                    }else{
                        mReviewClosed = !mReviewClosed;
                        notifyItemRangeRemoved(position+1,mReviewList.size());

                    }
                }
            });
        }
    }
}
