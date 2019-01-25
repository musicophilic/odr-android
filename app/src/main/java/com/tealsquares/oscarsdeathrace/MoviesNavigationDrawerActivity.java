package com.tealsquares.oscarsdeathrace;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tealsquares.oscarsdeathrace.entities.Movie;
import com.tealsquares.oscarsdeathrace.entities.WatchedMovie;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MoviesNavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference movieDatabaseReference;
    private DatabaseReference seenMoviesDatabaseReference;
    private static final int currentYear = 2019;
    private Map<String, Movie> idMovieMap;
    private Map<String, WatchedMovie> watchedMovieMap;
    ValueEventListener movieListener;
    ValueEventListener seenMovieListener;
    boolean loadData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_navigation_drawer);

        checkForLogin();

        // For overcoming loading the movies twice
        loadData = true;
        watchedMovieMap = new HashMap<>();
        idMovieMap = new HashMap<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        getSupportActionBar().setTitle("Oscars Death Race");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        loadUserDataInNavigationMenu();

        firebaseDatabase = FirebaseDatabase.getInstance();
        movieDatabaseReference = firebaseDatabase.getReference().
                child("movies/" + Integer.toString(currentYear));
        seenMoviesDatabaseReference = firebaseDatabase.getReference()
                .child("watchedMovies/" + getCurrentUser().getUid() + "/" + currentYear);

        movieListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot movieSnapshot : dataSnapshot.getChildren()) {
                    Movie movie = movieSnapshot.getValue(Movie.class);
                    idMovieMap.put(movieSnapshot.getKey(), movie);
                }
                addMoviesToGrid(idMovieMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        seenMovieListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot watchedMovieSnapshot : dataSnapshot.getChildren()) {
                    WatchedMovie watchedMovie = watchedMovieSnapshot.getValue(WatchedMovie.class);
                    watchedMovieMap.put(watchedMovieSnapshot.getKey(), watchedMovie);
                }
                movieDatabaseReference.addListenerForSingleValueEvent(movieListener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

    }

    private void loadUserDataInNavigationMenu() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header=navigationView.getHeaderView(0);

        FirebaseUser user = getCurrentUser();
        Uri photoUri = user.getPhotoUrl();
        String name = user.getDisplayName();
        String email = user.getEmail();

        ImageView userImageView = header.findViewById(R.id.nav_user_image_view);
        TextView userName = header.findViewById(R.id.nav_user_name);
        TextView userEmail = header.findViewById(R.id.nav_user_email);

        if(photoUri != null && userImageView != null) {
            Glide.with(this)
                    .load(photoUri)
                    .into(userImageView);
        }

        userName.setText(name);
        userEmail.setText(email);

    }

    private void checkForLogin() {
        FirebaseUser user = getCurrentUser();
        if(user != null) {
            return;
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private FirebaseUser getCurrentUser() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        return user;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_logout) {
            AuthUI.getInstance().signOut(this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForLogin();
        if(loadData) {
            seenMoviesDatabaseReference.addListenerForSingleValueEvent(seenMovieListener);
            loadData = false;
        }
    }

    private void addMoviesToGrid(Map<String,Movie> idMovieMap) {

        GridLayout movieGrid = findViewById(R.id.movies_grid);
        Set<String> movieIds = idMovieMap.keySet();
        View movieView;
        for(String id : movieIds) {
            Movie movie = idMovieMap.get(id);
            movieView = getLayoutInflater()
                    .inflate(R.layout.activity_movie_tile, null);
            TextView movieNameTextview = movieView.findViewById(R.id.movie_name_textview);
            movieNameTextview.setText(movie.getMovieName());
            ImageView movieImageView = movieView.findViewById(R.id.movie_poster_imageview);
            CheckBox checkBox = movieView.findViewById(R.id.seen_checkbox);
            if(watchedMovieMap.containsKey(id)) {
                checkBox.setChecked(true);
            }
            checkBox.setTag(id);
            checkBox.setOnCheckedChangeListener(checkBoxListener);
            Glide.with(this)
                    .load(movie.getPosterLink())
                    .into(movieImageView);
            movieGrid.addView(movieView);
        }
    }

    CompoundButton.OnCheckedChangeListener checkBoxListener =
            new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            String movieId = (String)buttonView.getTag();
            //System.out.println(movieId + " is " + (isChecked ? "checked" : "unchecked"));
            if(isChecked) {
                WatchedMovie watchedMovie = new WatchedMovie();
                watchedMovie.setWatchedTimestamp(System.currentTimeMillis());
                Map<String, Object> watchedMovieValues = watchedMovie.toMap();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put(movieId, watchedMovieValues);
                seenMoviesDatabaseReference.updateChildren(childUpdates);
            } else {
                // Delete from seen database
                seenMoviesDatabaseReference.child(movieId).removeValue();
            }

        }
    };



}
