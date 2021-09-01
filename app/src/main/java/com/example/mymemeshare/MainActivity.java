package com.example.mymemeshare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ImageView memeImage,shareButton;
    ProgressBar progressBar;

    String url1 = "";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        memeImage = (ImageView) findViewById(R.id.memeImage);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        shareButton = (ImageView) findViewById(R.id.shareButton);

        loadMeme();

        memeImage.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {


            public void onSwipeLeft() {
                //Toast.makeText(MainActivity.this, "Loading new meme", Toast.LENGTH_SHORT).show();
                loadMeme();
            }


        });

    }


        public  void loadMeme(){



            progressBar.setVisibility(View.VISIBLE);
            //RequestQueue queue = Volley.newRequestQueue(this);
            String url = "https://meme-api.herokuapp.com/gimme";


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("json parsed","WORKED");

                            try {
                                url1 = response.getString("url");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Glide.with(MainActivity.this)
                                    .load(url1)
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(MainActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
                                            memeImage.setImageResource(R.drawable.ic_baseline_image_not_supported_24);
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                            progressBar.setVisibility(View.GONE);
                                            return false;
                                        }
                                    })
                                    .error(R.drawable.ic_baseline_image_not_supported_24)
                                    .into(memeImage);



                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });


            // queue.add(jsonObjectRequest);
            MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

        }


    public void shareMeme(View view){

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,"Hi.. Check out this cool meme   "+ url1);
        intent.setType("text/plain");
        Intent chooser = Intent.createChooser(intent,"share with :");
        startActivity(chooser);


    }

}

