package com.example.yassine.singers;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by yassine on 17.04.16.
 */

/* The activity that shows details about the selected item in the main activity*/
public class DetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView imageView;
    private TextView textView;
    private Artist artist;

    private boolean isLandscape = false;

    private TextView biographyTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        /*Get references to our Views objects */
        toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        DisplayMetrics dMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
        float density = dMetrics.density;
        int width = Math.round(dMetrics.widthPixels / density);
        int height = Math.round(dMetrics.heightPixels / density);

        /*check the screen orientation for a more convenient size for the image.*/
        if (width < height) {
            isLandscape = true;
        }

        imageView = (ImageView) findViewById(R.id.detail_image);
        textView = (TextView) findViewById(R.id.detail_info_music);
        biographyTextView = (TextView) findViewById(R.id.detail_biography);

        /*Get reference to the Artist object sent by the main activity */
        artist = (Artist) getIntent().getSerializableExtra("artist");

        /*Set the content*/
        contentSetter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Toolbar tb = (Toolbar) findViewById(R.id.detail_toolbar);
        tb.setTitle(spannableTitleString(artist.getName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // we handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to main activity
        }

        return super.onOptionsItemSelected(item);
    }

    private void contentSetter() {

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        if (isLandscape)
            Picasso.with(DetailActivity.this).load(artist.getBigCover()).resize(size.x, size.y / 2).into(imageView);
        else
            Picasso.with(DetailActivity.this).load(artist.getBigCover()).resize(size.x, 2 * size.y / 3).into(imageView);

        ArrayList<String> genres = artist.getGenres();

        toolbar.setTitle(spannableTitleString(artist.getName()));

        String _genresPhrase = "";


        for (String genre : genres) {
            _genresPhrase += genre;
            if (!genres.get(genres.size() - 1).equals(genre))
                _genresPhrase += ", ";

        }

        textView.setText(_genresPhrase);
        biographyTextView.append(artist.getDescription());
        String albumsAndSongs = "\n\n" + artist.getAlbums() + " альбомов" + "  -  " + artist.getTracks() + " песни";
        textView.append(albumsAndSongs);

        Button caption = (Button) findViewById(R.id.caption_image);
        caption.setText(R.string.activity_detail_view_button);
        caption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(artist.getLink()));
                startActivity(launchBrowser);
            }
        });
    }


    /*Method that reformats at the runtime the text to be shown as the toolbar's title */
    private SpannableString spannableTitleString(String str) {
        SpannableString whiteSpannable = new SpannableString(str);
        whiteSpannable.setSpan(new ForegroundColorSpan(Color.WHITE), 0, str.length(), 0);

        return whiteSpannable;
    }

}
