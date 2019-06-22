package com.example.homework341;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Spinner languageSpinner;
    Spinner colorSpinner;
    Spinner indentSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        languageSpinner = findViewById(R.id.languageChange);
        colorSpinner = findViewById((R.id.colorChange));
        indentSpinner = findViewById((R.id.indentChange));
    }

    public void setlanguage(String language){
        Locale locale = new Locale(language=language);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        recreate();
    }
    public void Save(View v) {
        String[] languages = getResources().getStringArray(R.array.languages);
        String[] colors = getResources().getStringArray(R.array.colors);
        String[] indents = getResources().getStringArray(R.array.indents);
        String selectedLanguage = languageSpinner.getSelectedItem().toString();
        String selectedColor = colorSpinner.getSelectedItem().toString();
        String selectedIndent = indentSpinner.getSelectedItem().toString();

        if (selectedLanguage.equals(languages[0]))
            setlanguage("ru");
        else if (selectedLanguage.equals(languages[1]))
            setlanguage("en");
    }
}
