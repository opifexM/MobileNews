package com.company.mobilenews.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.company.mobilenews.News;
import com.company.mobilenews.NewsDB;
import com.company.mobilenews.R;
import com.company.mobilenews.WebActivity;
import com.company.mobilenews.databinding.FragmentNewsBinding;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewsFragment extends Fragment {

    private static final String API_KEY = "42146835e4f8462ab961b51c535b8c1c";
    private static final String[] categories = {"Business", "Entertainment", "General", "Health", "Science", "Sports", "Technology"};

    private FragmentNewsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.buttonSync.setOnClickListener(v -> fetchNewsJson());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void fetchNewsJson() {
        requireActivity().runOnUiThread(() -> {
            binding.recyclerViewNews.setVisibility(View.GONE);
            binding.textViewLoading.setVisibility(View.VISIBLE);
        });
        SharedPreferences preferences = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);

        List<String> activeCategories = new ArrayList<>();
        for (String category : categories) {
            if (preferences.getBoolean(category, false)) {
                activeCategories.add(category);
            }
        }

        if (activeCategories.isEmpty()) {
            requireActivity().runOnUiThread(() -> binding.textViewLoading.setText("No categories selected"));
            return;
        }

        for (String category : activeCategories) {
            String urlString = "https://newsapi.org/v2/top-headlines?country=us&category=" + category + "&apiKey=" + API_KEY;
            loadNewsForCategory(urlString);
        }
    }

    private void loadNewsForCategory(String urlString) {
        AsyncTask.execute(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(urlString);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("User-Agent", "Weather Mobile App");
                String finalOutput = getString(connection);
                requireActivity().runOnUiThread(() -> {
                    syncNews(finalOutput);
                    binding.recyclerViewNews.setVisibility(View.VISIBLE);
                    binding.textViewLoading.setVisibility(View.GONE);
                });
            } catch (Exception e) {
                e.printStackTrace();
                requireActivity().runOnUiThread(() -> binding.textViewLoading.setText("Error retrieving news data"));
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
    }

    @NonNull
    private static String getString(HttpURLConnection connection) throws IOException {
        InputStream stream = new BufferedInputStream(connection.getInputStream());
        InputStreamReader reader = new InputStreamReader(stream);
        BufferedReader streamReader = new BufferedReader(reader);
        StringBuilder output = new StringBuilder();
        String line = streamReader.readLine();
        while (line != null) {
            output.append(line);
            line = streamReader.readLine();
        }

        String finalOutput = output.toString();
        return finalOutput;
    }

    private String getAsStringOrDefault(JsonObject jsonObject, String memberName) {
        JsonElement element = jsonObject.get(memberName);
        return element != null && !element.isJsonNull() ? element.getAsString() : "";
    }

    private void syncNews(String jsonString) {
        NewsDB db = NewsDB.getMovieDB(requireContext());
        List<News> newsList = new ArrayList<>();

        JsonElement jsonElement = JsonParser.parseString(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray("articles");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        for (JsonElement articleElement : jsonArray) {
            JsonObject articleObject = articleElement.getAsJsonObject();
            JsonObject sourceObject = articleObject.getAsJsonObject("source");

            String source = getAsStringOrDefault(sourceObject, "name");
            String author = getAsStringOrDefault(articleObject, "author");
            String title = getAsStringOrDefault(articleObject, "title");
            String description = getAsStringOrDefault(articleObject, "description");
            String url = getAsStringOrDefault(articleObject, "url");
            String urlToImage = getAsStringOrDefault(articleObject, "urlToImage");
            String publishedAt = getAsStringOrDefault(articleObject, "publishedAt");
            String content = getAsStringOrDefault(articleObject, "content");

            Date date = null;
            if (!publishedAt.isEmpty()) {
                try {
                    date = sdf.parse(publishedAt);
                } catch (ParseException e) {
                    e.printStackTrace();
                    date = new Date();
                }
            }

            News news = new News(-1, source, author, title, description, url, urlToImage, date, content);
            newsList.add(news);

            boolean isExists = db.newsExists(title);
            if (!isExists) {
                db.addOrUpdateNews(news);
            }
        }

        requireActivity().runOnUiThread(() -> {
            RecyclerView.Adapter<NewsRecyclerViewAdapter.ViewHolder> adapter = new NewsRecyclerViewAdapter(requireContext(), newsList);
            RecyclerView recyclerView = requireView().findViewById(R.id.recyclerViewNews);
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            recyclerView.setAdapter(adapter);
        });
    }
}

class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.ViewHolder> {
    private final List<News> news;
    private final LayoutInflater inflater;

    public NewsRecyclerViewAdapter(Context context, List<News> news) {
        this.inflater = LayoutInflater.from(context);
        this.news = news;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.news_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        News selectedNews = this.news.get(position);
        holder.textViewNews.setText(selectedNews.toString());
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView textViewNews;

        ViewHolder(View itemView) {
            super(itemView);
            textViewNews = itemView.findViewById(R.id.textViewNews);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(context, WebActivity.class);
            String url = news.get(getAdapterPosition()).url;
            intent.putExtra("url", url);
            context.startActivity(intent);
        }
    }
}