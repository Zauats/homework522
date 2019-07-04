package com.example.homework341;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class AutorizeActivity extends AppCompatActivity {

    EditText password;
    EditText login;
    Button entry;
    Button registration;
    Boolean saveFile;

    String SETTING_NAME = "checkBox";
    SharedPreferences checkSettining;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autorize);

        checkSettining = getSharedPreferences(SETTING_NAME, Context.MODE_PRIVATE);

        final SharedPreferences.Editor editor = checkSettining.edit();
        if(!(checkSettining.contains(SETTING_NAME))) {
            editor.putString(SETTING_NAME, "false");
            editor.apply();
        }

        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        entry = findViewById(R.id.entry);
        registration = findViewById(R.id.registration);

        entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFile = Boolean.parseBoolean(checkSettining.getString(SETTING_NAME, "false"));
                String loginAndPassword = "";

                if (!saveFile){
                    loginAndPassword = readInternalFile();
                }else{
                    loginAndPassword = readExternalFile();
                }

                if (loginAndPassword.equals(login.getText().toString() + " " + password.getText().toString())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(AutorizeActivity.this);
                    builder.setTitle("Важное сообщение!")
                            .setMessage("вы вошли!")
                            .setCancelable(false)
                            .setNegativeButton("ОК",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
    }

    //обработка кнопки регистрации
    public void regClick(View view){
        if (password.getText().toString().equals("") | login.getText().toString().equals("")){
            Toast toast = Toast.makeText(this, "Введите что-нибудь", Toast.LENGTH_LONG);
            toast.show();
        }else{
            saveFile = Boolean.parseBoolean(checkSettining.getString(SETTING_NAME, "false"));
            if (!saveFile){
                saveInternalFile();
            }else{
                saveExternalFile();
            }
            Toast toast = Toast.makeText(this, "Регистрация прошла успешно", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    // кнопка открытия настроек
    public void openSetting(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        // меняю тему авторизации тут
        int theme = Integer.parseInt(checkSettining.getString("theme", "2"));
        int margin = Integer.parseInt(checkSettining.getString("margin", "2"));
        Utils.changeToTheme(AutorizeActivity.this, theme, margin);
    }

    // считывает внутренний файл
    private String readInternalFile(){
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = openFileInput("login_and_password");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String loginAndPassword = "";
        try {
            loginAndPassword = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loginAndPassword;
    }

    // сохраняет внутренний файл
    private void saveInternalFile(){
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = openFileOutput("login_and_password", MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        BufferedWriter bw = new BufferedWriter(outputStreamWriter);
        try {
            bw.write(login.getText().toString() + " " + password.getText().toString());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // считывает внешний файл
    private String readExternalFile(){
        if(!checkPermissions()){
            return "";
        }
        String text = "";
        FileInputStream fin = null;
        File file = getExternalPath();

        if(!file.exists()) return "";
        try {
            fin =  new FileInputStream(file);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            text = new String (bytes);
        }
        catch(IOException ex) {

            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally{

            try{
                if(fin!=null)
                    fin.close();
            }
            catch(IOException ex){

                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        return text;
    }

    // сохраняет внешний файл
    private void saveExternalFile(){
        if(!checkPermissions()){
            return;
        }
        FileOutputStream fos = null;
        try {
            String text = login.getText().toString() + " " + password.getText().toString();
            fos = new FileOutputStream(getExternalPath());
            fos.write(text.getBytes());
        }
        catch(IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally{
            try{
                if(fos!=null)
                    fos.close();
            }
            catch(IOException ex){
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkPermissions(){

        if(!isExternalStorageReadable() || !isExternalStorageWriteable()){
            Toast.makeText(this, "Внешнее хранилище не доступно", Toast.LENGTH_LONG).show();
            return false;
        }
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissionCheck!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
            return false;
        }
        return true;
    }

    // проверяем, доступно ли внешнее хранилище для чтения и записи
    private boolean isExternalStorageReadable(){
        String state = Environment.getExternalStorageState();
        return  (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }

    // проверяем, доступно ли внешнее хранилище хотя бы только для чтения
    private boolean isExternalStorageWriteable(){
        String state = Environment.getExternalStorageState();
        return  Environment.MEDIA_MOUNTED.equals(state);
    }

    // получение пути к файлу
    private File getExternalPath() {
        return(new File(Environment.getExternalStorageDirectory(), "login_and_password"));
    }
}
