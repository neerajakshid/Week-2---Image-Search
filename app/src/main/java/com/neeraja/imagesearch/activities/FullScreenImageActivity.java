package com.neeraja.imagesearch.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.neeraja.imagesearch.R;
import com.neeraja.imagesearch.helpers.NetworkManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.neeraja.imagesearch.models.ImageResultsModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import uk.co.senab.photoview.PhotoViewAttacher;


public class FullScreenImageActivity extends ActionBarActivity {
    ProgressBar pbImage;
    PhotoViewAttacher mAttacher;
    ShareActionProvider miShareAction;
    ImageView ivImageItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);
        ImageResultsModel result =  getIntent().getParcelableExtra("result");
        ivImageItem = (ImageView) findViewById(R.id.ivImageItem);
        // setting title in the actionbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(Html.fromHtml("<font size='1' face='verdana'>"+result.title+"</font"));
        pbImage = (ProgressBar)findViewById(R.id.pbImage);
        Picasso.with(this).load(result.fullUrl).into(ivImageItem, new Callback() {
            @Override
            public void onSuccess() {
                setupShareIntent();
                if(mAttacher==null) {
                    mAttacher = new PhotoViewAttacher(ivImageItem);
                } else mAttacher.update();
                pbImage.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                if (NetworkManager.isNetworkAvailable(FullScreenImageActivity.this)) {
                    NetworkManager.showAlertDialog(FullScreenImageActivity.this, getBaseContext().getString(R.string.image_load_error), false);
                } else {
                    NetworkManager.showAlertDialog(FullScreenImageActivity.this, getBaseContext().getString(R.string.network_error), false);
                }
                pbImage.setVisibility(View.GONE);

            }
        });





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_full_screen_image, menu);
        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);
        // Fetch reference to the share action provider
         miShareAction = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        // Return true to display menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void setupShareIntent() {
        // Fetch Bitmap Uri locally
        ivImageItem = (ImageView) findViewById(R.id.ivImageItem);
        Uri bmpUri = getLocalBitmapUri(ivImageItem);
        // Create share intent as described above
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.setType("image/*");

        // Attach share event to the menu item provider
        miShareAction.setShareIntent(shareIntent);

    }

    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }

        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    // sharing images methods ends
 }
