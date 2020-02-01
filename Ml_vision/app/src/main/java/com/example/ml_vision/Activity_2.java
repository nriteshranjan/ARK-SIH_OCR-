package com.example.ml_vision;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
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

import java.io.File;
import java.lang.annotation.Retention;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Activity_2 extends AppCompatActivity {

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    ImageView imageView,image_r;
    Button upload, email;
    private DrawerLayout drawerLayout;
    Uri imageUri = null;
    File attachment;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle("Dashboard");
        setContentView(R.layout.activity_2);

        imageView = findViewById(R.id.imageView);
        image_r = findViewById(R.id.result);
        upload = findViewById(R.id.uploadButton);
        email = findViewById(R.id.sendEmail);

        email.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final SharedPreferences mPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                String va=(mPreference.getString("phoneKey", "xyz@gmail.com"));
                String recipientList = va;
                String[] recipients = recipientList.split(",");
                String subject ="Lab Reports";
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_STREAM, imageUri);
                intent.setType("message/rfc822");
                startActivity(Intent.createChooser(intent, "Choose an email client"));
            }
        });

        upload.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
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
            }
        });

        drawerLayout = findViewById(R.id.drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final NavigationView nav_view=findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
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
            }
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

    public void upload(Uri uri)
    {
        // add another part within the multipart request
        String descriptionString = "file";
        RequestBody description = RequestBody.create(okhttp3.MultipartBody.FORM, descriptionString);

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), attachment);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", attachment.getName(), requestFile);

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(RetrofitClientInstance.BASE_URL).addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        //Get client and call object for the request
        RetrofitClientInstance client = retrofit.create(RetrofitClientInstance.class);

        //Execute the request
        Call<String> call =  client.upload(body,description);
        call.enqueue(new Callback<String>()
        {
            @Override
            public void onResponse(Call<String> call, Response<String> response)
            {
                Toast.makeText(Activity_2.this, "Successfully uploaded file", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t)
            {
                System.out.println(t.getMessage());
                Toast.makeText(Activity_2.this, "Error uploading File", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if(resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE)
        {
            imageView.setImageURI(data.getData());
            image_r.setImageResource(R.drawable.api_result);
            imageUri = data.getData();
            System.out.println("Path--->"+imageUri.toString());
            attachment = new File(getPath(imageUri));

            ////**////
            upload(imageUri);
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
