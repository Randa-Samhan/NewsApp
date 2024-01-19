package com.example.newsapp;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final String API_KEY = "c356bd2fea984bbaa35a50929f3a2978";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionManager sessionManager = new SessionManager(MainActivity.this);
        if (!sessionManager.isLoggedIn()) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.newsRecyclerView);
        RecyclerView recyclerView2 = findViewById(R.id.newsRecyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        Button editProfileButton = findViewById(R.id.editProfileButton);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditProfileActivity();
            }
        });
        List<NewsItem> newsList = new ArrayList<>();
        NewsAdapter adapter = new NewsAdapter(newsList);
        recyclerView.setAdapter(adapter);
        recyclerView2.setAdapter(adapter);
        checkInternetConnection();
        displayUserName();
        GetBusinessNews();
        GetSportsNews();
    }

    private void openEditProfileActivity() {
        Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);
        startActivity(intent);
    }

    private void displayUserName() {
        SharedPreferences preferences = getSharedPreferences("UserInformation", MODE_PRIVATE);
        String firstName = preferences.getString("firstName", "");
        String lastName = preferences.getString("lastName", "");

        // Set the label with the user's name
        String userName = String.format("Welcome, %s %s!", firstName, lastName);
        TextView newsLabel = findViewById(R.id.newsLabel);
        newsLabel.setText(userName);
    }

    private void GetSportsNews(){
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        RequestQueue requestQueue = new RequestQueue(cache, network);
        requestQueue.start();
        String newsUrl  = "https://newsapi.org/v2/top-headlines?country=us&category=sport&sortBy=popularity";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                newsUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray newsArticles = response.getJSONArray("articles");
                            List<NewsItem> newsList = new ArrayList<>();
                            for (int i = 0; i < 20; i++) {
                                JSONObject article = newsArticles.getJSONObject(i);
                                String title = article.getString("title");
                                String description = article.getString("description");
                                String date = article.getString("publishedAt");
                                String urlToImage = article.getString("urlToImage");
                                NewsItem item1 = new NewsItem(title,description);
                                newsList.add(item1);
                            }
                            updateRecyclerView2(newsList);
                        } catch (JSONException e) {
                            Log.e("Volley Error", "Error fetching news data", e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleVolleyError(error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("x-api-key", API_KEY);
                headers.put("user-agent","mozilla/5.0");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private void GetBusinessNews(){
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());
        RequestQueue requestQueue = new RequestQueue(cache, network);
        requestQueue.start();
        String newsUrl  = "https://newsapi.org/v2/top-headlines?country=us&category=business&sortBy=popularity";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                newsUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray newsArticles = response.getJSONArray("articles");
                            List<NewsItem> newsList = new ArrayList<>();
                            for (int i = 0; i < 20; i++) {
                                JSONObject article = newsArticles.getJSONObject(i);
                                String title = article.getString("title");
                                String description = article.getString("description");
                                String date = article.getString("publishedAt");
                                String urlToImage = article.getString("urlToImage");
                                NewsItem item1 = new NewsItem(title,description);
                                newsList.add(item1);
                            }
                            updateRecyclerView(newsList);
                        } catch (JSONException e) {
                            Log.e("Volley Error", "Error fetching news data", e);
                        }

                       }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleVolleyError(error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("x-api-key", API_KEY);
                headers.put("user-agent","mozilla/5.0");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private void handleVolleyError(VolleyError error) {
        // Handle Volley errors
        if (error instanceof AuthFailureError) {
            // Handle authentication failure
            Toast.makeText(MainActivity.this, "Authentication failure. Check your API key.", Toast.LENGTH_SHORT).show();
        } else {
            // Handle other types of errors
            Toast.makeText(MainActivity.this, "Error fetching news data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        }
        Log.e("Volley Error", "Error fetching news data", error);
    }

    private void updateRecyclerView(List<NewsItem> newsList) {
        RecyclerView recyclerView = findViewById(R.id.newsRecyclerView);
        NewsAdapter adapter = new NewsAdapter(newsList);
        recyclerView.setAdapter(adapter);
    }

    private void updateRecyclerView2(List<NewsItem> newsList) {
        RecyclerView recyclerView = findViewById(R.id.newsRecyclerView2);
        NewsAdapter adapter = new NewsAdapter(newsList);
        recyclerView.setAdapter(adapter);
    }

    private String GetFullName() {
        SharedPreferences preferences = getSharedPreferences("UserInformation", MODE_PRIVATE);
        return preferences.getString("firstName", "") + preferences.getString("lastName", "");
    }

    private void checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void logout() {
        SessionManager sessionManager = new SessionManager(MainActivity.this);
        sessionManager.logout();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }
}