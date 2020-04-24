package com.example.instaclone;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;

public class Home extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewpager;
    private TabAdapter tabadapter;
    private TabLayout tablayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setTitle("Welcome "+ParseUser.getCurrentUser().getUsername());


        toolbar=findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        viewpager=findViewById(R.id.myviewpager);
        tabadapter=new TabAdapter(getSupportFragmentManager());
        viewpager.setAdapter(tabadapter);

        tablayout=findViewById(R.id.myTabLayout);
        tablayout.setupWithViewPager(viewpager,false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.menlogout){
            ParseUser.getCurrentUser().logOut();
            Intent intent=new Intent(Home.this,SignUp.class);
            startActivity(intent);
            finish();
        }
        else if(item.getItemId()==R.id.menPhoto) {
            if (Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(Home.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
            }
            else{
                getImage();
            }
        }


        return super.onOptionsItemSelected(item);
    }

        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if(requestCode==1000){
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    getImage();
                }
            }
        }

    private void getImage() {

        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,3000);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==3000){
            if(resultCode== Activity.RESULT_OK ){
                try {
                    Uri selImage=data.getData();
                    Bitmap bitmap=MediaStore.Images.Media.getBitmap
                            (this.getContentResolver(),selImage);
                    ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                    byte[] bytes=byteArrayOutputStream.toByteArray();

                    ParseFile parseFile=new ParseFile("img.png",bytes);
                    ParseObject parseObject=new ParseObject("Photo");
                    parseObject.put("pic",parseFile);
                    parseObject.put("username",ParseUser.getCurrentUser().getUsername());
                    parseObject.put("info","..");
                    final ProgressDialog progressDialog=new ProgressDialog(Home.this);
                    progressDialog.setMessage("Uploading");
                    progressDialog.show();
                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e==null){
                                FancyToast.makeText(Home.this,"Uploaded", Toast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                            }
                            else{
                                FancyToast.makeText(Home.this,e.getMessage(), Toast.LENGTH_LONG,FancyToast.ERROR,true).show();
                            }
                            progressDialog.dismiss();
                        }
                    });

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

    }

}
