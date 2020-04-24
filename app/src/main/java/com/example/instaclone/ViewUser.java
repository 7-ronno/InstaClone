package com.example.instaclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class ViewUser extends AppCompatActivity {

    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);

        mLinearLayout=findViewById(R.id.linearr);

        Intent recievedIntent=getIntent();
        String username=recievedIntent.getStringExtra("username");
        FancyToast.makeText(ViewUser.this,username, Toast.LENGTH_SHORT,
                FancyToast.SUCCESS,true).show();

        setTitle(username +"'s posts");

        ParseQuery<ParseObject> parseQuery=new ParseQuery<ParseObject>("Photo");
        parseQuery.whereEqualTo("username",username);
        parseQuery.orderByDescending("createdAt");

        final ProgressDialog dialog=new ProgressDialog(ViewUser.this);
        dialog.setMessage("Loading");
        dialog.show();

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(objects.size()>0 && e==null){
                    for (ParseObject post:objects){
                        final TextView img_des=new TextView(ViewUser.this);
                        if (post.get("info")==null){
                            img_des.setText("");
                        }else{
                            img_des.setText(post.get("info")+"");
                        }
                        ParseFile post_pic=post.getParseFile("pic");
                        post_pic.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if(e==null && data!=null){
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(data,0,data.length);
                                    ImageView pic=new ImageView(ViewUser.this);
                                    LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT);
                                    params.setMargins(5,5,5,5);
                                    pic.setLayoutParams(params);
                                    pic.setScaleType(ImageView.ScaleType.FIT_XY);
                                    pic.setImageBitmap(bitmap);

                                    LinearLayout.LayoutParams des_params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT);
                                    params.setMargins(5,5,5,5);
                                    img_des.setLayoutParams(params);
                                    img_des.setGravity(Gravity.CENTER);
                                    img_des.setBackgroundColor(Color.BLUE);
                                    img_des.setTextColor(Color.WHITE);
                                    img_des.setTextSize(30f);

                                    mLinearLayout.addView(pic);
                                    mLinearLayout.addView(img_des);
                                }
                                else{
                                    FancyToast.makeText(ViewUser.this,e.getMessage(),
                                            Toast.LENGTH_LONG,FancyToast.ERROR,true).show();
                                }
                            }
                        });
                    }

                }else if(objects.size()==0){
                        TextView nothing=new TextView(ViewUser.this);
                        nothing.setText("User hasnt uploaded");
                        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.
                                MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(5,5,5,5);
                        nothing.setGravity(Gravity.CENTER);
                        params.weight=1f;
                        nothing.setLayoutParams(params);
                        nothing.setTextColor(Color.BLACK);
                        mLinearLayout.addView(nothing);
                }else{
                    FancyToast.makeText(ViewUser.this,e.getMessage(),
                            Toast.LENGTH_LONG,FancyToast.ERROR,true).show();
                    finish();
                }
                dialog.dismiss();
            }
        });
    }
}
