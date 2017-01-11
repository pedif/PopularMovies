package com.example.android.popularmovies.data.db;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by Pedram on 1/5/2017.
 * Database equivalent of Movie Table structure
 */

public interface MovieColumns {

    @DataType(DataType.Type.INTEGER)
    @PrimaryKey
    @NotNull
    public static final String _ID = "_id";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String TITLE = "title";

    @DataType(DataType.Type.INTEGER)
    public static final String VOTE = "vote";

    @DataType(DataType.Type.INTEGER)
    public static final String POPULARITY = "popularity";

    @DataType(DataType.Type.TEXT)
    public static final String GENRE = "genre";

    @DataType(DataType.Type.TEXT)
    public static final String DATE = "date";

    @DataType(DataType.Type.TEXT)
    public static final String POSTER = "poster";

    @DataType(DataType.Type.TEXT)
    public static final String OVERVIEW = "overview";


    String[] projection=

    {
        _ID, TITLE, VOTE, POPULARITY, GENRE, DATE, POSTER, OVERVIEW
    };



    int ID_INDEX = 0;
    int TITLE_INDEX = 1;
    int VOTE_INDEX = 2;
    int POPULARITY_INDEX = 3;
    int GENRE_INDEX = 4;
    int DATE_INDEX = 5;
    int POSTER_INDEX = 6;
    int OVERVIEW_INDEX = 7;


}
