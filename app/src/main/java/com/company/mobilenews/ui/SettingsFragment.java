package com.company.mobilenews.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.company.mobilenews.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.buttonSave.setOnClickListener(v -> saveCheckBoxStates());
        restoreCheckBoxStates();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void saveCheckBoxStates() {
        SharedPreferences preferences = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean("Business", binding.checkBoxBusiness.isChecked());
        editor.putBoolean("Entertainment", binding.checkBoxEntertainment.isChecked());
        editor.putBoolean("General", binding.checkBoxGeneral.isChecked());
        editor.putBoolean("Health", binding.checkBoxHealth.isChecked());
        editor.putBoolean("Science", binding.checkBoxScience.isChecked());
        editor.putBoolean("Sports", binding.checkBoxSports.isChecked());
        editor.putBoolean("Technology", binding.checkBoxTechnology.isChecked());

        editor.apply();
    }


    private void restoreCheckBoxStates() {
        SharedPreferences preferences = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);

        binding.checkBoxBusiness.setChecked(preferences.getBoolean("Business", false));
        binding.checkBoxEntertainment.setChecked(preferences.getBoolean("Entertainment", false));
        binding.checkBoxGeneral.setChecked(preferences.getBoolean("General", false));
        binding.checkBoxHealth.setChecked(preferences.getBoolean("Health", false));
        binding.checkBoxScience.setChecked(preferences.getBoolean("Science", false));
        binding.checkBoxSports.setChecked(preferences.getBoolean("Sports", false));
        binding.checkBoxTechnology.setChecked(preferences.getBoolean("Technology", false));
    }
}