package com.example.quizapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.quizapp.R;

public class HighscoreActivity extends AppCompatDialogFragment {
    private EditText editTextName;
    private HighScoreActivityListener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.highscore_dialog, null);

        builder.setView(view)
                .setTitle("Congrats Champion!!")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = editTextName.getText().toString();
                        listener.applyText(name);
                    }
                });
        editTextName = view.findViewById(R.id.edit_text_hignscore_name);
                return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (HighScoreActivityListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+
                    "must implement ExampleDialogListener");
        }
    }

    public interface HighScoreActivityListener{
        void applyText(String name);
    }
}
