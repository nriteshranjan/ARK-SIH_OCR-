package com.example.ml_vision;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Activity_2 extends AppCompatActivity {

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    CardView uploadButton, emailButton, Result_Lab;
    Uri imageUri = null;
    File attachment;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    public static List<String> pResult;
    public static List<String> parameter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle("Dashboard");
        setContentView(R.layout.activity_2);
        ActionBar toolbar = getSupportActionBar();
        assert toolbar != null;
        toolbar.setElevation(0);

        uploadButton = findViewById(R.id.uploadButton);
        emailButton = findViewById(R.id.emailButton);
        Result_Lab = findViewById(R.id.Result_Lab);

        Result_Lab.setVisibility(View.INVISIBLE);


        Result_Lab.setOnClickListener(v -> {
            Intent intent=new Intent(Activity_2.this,LabResults.class);
            startActivity(intent);
        });


        emailButton.setOnClickListener(v -> {
            final SharedPreferences mPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            String recipientList = (mPreference.getString("phoneKey", "xyz@gmail.com"));
            String[] recipients = recipientList.split(",");
            String subject ="Lab Reports";
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL, recipients);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_STREAM, imageUri);
            intent.setType("message/rfc822");
            startActivity(Intent.createChooser(intent, "Choose an email client"));
        });

        uploadButton.setOnClickListener(v -> {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                {
                    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    requestPermissions(permissions,PERMISSION_CODE);
                }
                else
                {
                    pickImageFromGallery();
                }
            }
            else
            {
                pickImageFromGallery();
            }
        });

        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final NavigationView nav_view=findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(menuItem -> {
            int id=menuItem.getItemId();

            if(id==R.id.dashboard)
            {
                Intent intent = new Intent(Activity_2.this,Activity_2.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }

            else if(id==R.id.plots)
            {
                Intent  intent = new Intent(Activity_2.this,Plots.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }

            else if(id==R.id.setting)
            {
                Intent  intent = new Intent(Activity_2.this,MyProfile.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
            else if(id==R.id.logout)
            {
                startActivity(new Intent(Activity_2.this, MainActivity.class));
            }
            else if(id==R.id.about)
            {
                Intent  intent = new Intent(Activity_2.this,About.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
            return true;
        });
    }

    private void pickImageFromGallery()
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case PERMISSION_CODE:
                {
                    if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    {
                        pickImageFromGallery();
                    }
                    else
                    {
                        Toast.makeText(this, "PERMISSION DENIED", Toast.LENGTH_SHORT).show();
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void upload()
    {
        // add another part within the multipart request
        String descriptionString = "file";
        RequestBody description = RequestBody.create(okhttp3.MultipartBody.FORM, descriptionString);

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), attachment);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", attachment.getName(), requestFile);

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(RetrofitClientInstance.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson));
        Retrofit retrofit = builder.build();

        //Get client and call object for the request
        RetrofitClientInstance client = retrofit.create(RetrofitClientInstance.class);

        //Execute the request
        Call<LabReport> call =  client.upload(body,description);
        call.enqueue(new Callback<LabReport>() {
            @Override
            public void onResponse(Call<LabReport> call, Response<LabReport> response) {
                Result_Lab.setVisibility(View.VISIBLE);
                if(response.isSuccessful())
                {
                    assert response.body() != null;
                    parameter = response.body().getProperties();
                    pResult = response.body().getValue();
                }
                else System.out.println("Response Failed!");
                Toast.makeText(Activity_2.this, "Successful!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<LabReport> call, Throwable t)
            {
                System.out.println(t.getMessage());
                Toast.makeText(Activity_2.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if(resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE)
        {
            imageUri = data.getData();
            System.out.println("Path--->"+imageUri.toString());
            attachment = new File(getPath(imageUri));
            upload();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =             cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish()
    {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
