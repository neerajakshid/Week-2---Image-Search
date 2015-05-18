package com.neeraja.imagesearch.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;

import android.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;

import com.etsy.android.grid.StaggeredGridView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.neeraja.imagesearch.R;
import com.neeraja.imagesearch.helpers.EndlessScrollListener;
import com.neeraja.imagesearch.adapters.ImageResultsAdapter;
import com.neeraja.imagesearch.fragments.SettingsFragment;
import com.neeraja.imagesearch.helpers.NetworkManager;
import com.neeraja.imagesearch.helpers.RecentImageSearchSuggestions;
import com.neeraja.imagesearch.models.ImageResultsModel;
import com.neeraja.imagesearch.models.SettingsModel;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.AbstractList;
import java.util.ArrayList;


public class ImageSearchActivity extends ActionBarActivity implements SettingsFragment.OnDialogCompleteListener{
    private StaggeredGridView gvResults;
    public static final String BASE_URL = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8&q=";
    private ArrayList<ImageResultsModel> imageResults;
    private ImageResultsAdapter aImageResults;
    private SearchView svQuery;
    private SettingsModel settings;
    private String searchUrl;
    String settingsQuery="";
    MenuItem searchItem;

    private static int RESULT_SIZE = 8;
    private static int PAGE_ONE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);


        // setting logo in the actionbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        gvResults = (StaggeredGridView) findViewById(R.id.gvResults);
        handleIntent(getIntent());
        imageResults = new ArrayList<com.neeraja.imagesearch.models.ImageResultsModel>();
        aImageResults = new ImageResultsAdapter(this, imageResults);
        gvResults.setAdapter(aImageResults);
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // lauch the full screen image activity
                // creating an intent
                Intent i = new Intent(ImageSearchActivity.this, FullScreenImageActivity.class);
                ImageResultsModel result = imageResults.get(position);
                i.putExtra("result", result);
                startActivity(i);

            }
        });

        // Attach the listener to the AdapterView onCreate for Infinity scrolling
        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                fetchImages(svQuery.getQuery().toString(), page);
                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image_search, menu);
        searchItem = menu.findItem(R.id.action_search);
        svQuery = (SearchView) MenuItemCompat.getActionView(searchItem);
        svQuery.setQueryHint("Enter search term");
        svQuery.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d("Debug", "inside onQueryTextSubmit!");
                fetchImages(s,PAGE_ONE);
                // recent suggestions
                performQuery(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                 return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
// loading the suggstions
    public void performQuery(String q) {
        Log.i("Debug", "inside performQuery!");
        if (q == null || q == "") {
            return;
        } else {
            Log.i("Debug", "inside performQuery!");
            RecentImageSearchSuggestions.createRecentSuggestions(this)
                    .saveRecentQuery(q, null);
        }
    }
    public void fetchImages (String query, final int page) {
        if (NetworkManager.isNetworkAvailable(ImageSearchActivity.this)) {
            if (page < PAGE_ONE || page > RESULT_SIZE) {
                return;
            }
            performQuery(query);
            // soft keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(svQuery.getWindowToken(), 0);

            AsyncHttpClient client = new AsyncHttpClient();
            int items = RESULT_SIZE * (page - 1);
            if (settingsQuery != null || settingsQuery != "") {
                searchUrl = BASE_URL + query + "&start=" + items + settingsQuery;
            } else {
                searchUrl = BASE_URL + query + "&start=" + items;
            }
            client.get(searchUrl, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    JSONArray imageResultsJSON = null;
                    try {
                        imageResultsJSON = response.getJSONObject("responseData").getJSONArray("results");
                        if (page == 1) {
                            imageResults.clear(); // If new search
                            aImageResults.notifyDataSetChanged();
                        }
                        aImageResults.addAll(ImageResultsModel.parseJSON(imageResultsJSON));
                        aImageResults.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    if (NetworkManager.isNetworkAvailable(ImageSearchActivity.this)) {
                        Log.e("ImageSearchActivity", throwable.toString());
                    } else {
                        NetworkManager.showAlertDialog(ImageSearchActivity.this, getBaseContext().getString(R.string.network_error), false);
                    }
                }
            });

        } else {
            NetworkManager.showAlertDialog(ImageSearchActivity.this, getBaseContext().getString(R.string.network_error), false);
        }
    }


    public void onSettingsClick(MenuItem menuItem){
        FragmentManager fm = this.getFragmentManager();
        SettingsFragment alertDialog = SettingsFragment.newInstance(this.getString(R.string.advanced_search));
        alertDialog.show(fm, this.getString(R.string.advanced_search));

    }

    @Override
    public void onDialogComplete(SettingsModel settings) {
        settingsQuery = settings.settingsQueryParam();
        aImageResults.clear();
        fetchImages(svQuery.getQuery().toString(), PAGE_ONE);
}

    public SettingsModel getSelectedSetting(){
        return settings;
    }

// Handling user click on suggestions
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String queryFromSuggestions = intent.getStringExtra(SearchManager.QUERY);
            if ( searchItem != null) {
                 searchItem.getActionView().clearFocus();
            }
            svQuery.setQuery(queryFromSuggestions,true);
            if (NetworkManager.isNetworkAvailable(ImageSearchActivity.this)) {
                performQuery(queryFromSuggestions);
            } else {
                NetworkManager.showAlertDialog(ImageSearchActivity.this, this.getString(R.string.network_error), false);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }
}

