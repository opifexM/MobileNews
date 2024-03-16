package com.company.mobilenews.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.company.mobilenews.News;
import com.company.mobilenews.NewsDB;
import com.company.mobilenews.R;
import com.company.mobilenews.databinding.FragmentArchiveBinding;

import java.util.ArrayList;
import java.util.List;

public class ArchiveFragment extends Fragment {

    private FragmentArchiveBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentArchiveBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        List<News> newsList = NewsDB.getMovieDB(requireContext()).getAllNews();

        RecyclerView.Adapter adapter = new NewsRecyclerViewAdapter(requireContext(), newsList);
        RecyclerView recyclerView = binding.recyclerViewNewsArchive;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}