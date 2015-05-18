package com.neeraja.imagesearch.helpers;

import android.content.Context;
import android.content.SearchRecentSuggestionsProvider;
import android.provider.SearchRecentSuggestions;

public class RecentImageSearchSuggestions extends SearchRecentSuggestionsProvider {
            public final static String AUTHORITY = RecentImageSearchSuggestions.class.getName();
            public final static int MODE = DATABASE_MODE_QUERIES;

            public RecentImageSearchSuggestions() {
                setupSuggestions(AUTHORITY, MODE);
            }

    public static SearchRecentSuggestions createRecentSuggestions(final Context context) {
        return new SearchRecentSuggestions(context, RecentImageSearchSuggestions.class.getName(), DATABASE_MODE_QUERIES);
    }
        }


