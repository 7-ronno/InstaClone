package com.example.instaclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class Login extends AppCompatActivity {

    private Button btnlogin,btnsignup;
    private EditText enusername,enpassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_layout);
        setTitle("Log In "+ParseUser.getCurrentUser().getUsername());

        btnlogin=findViewById(R.id.btnLoginLog);
        enpassword=findViewById(R.id.enPasswordLog);
        enusername=findViewById(R.id.enUsernameLog);
        btnsignup=findViewById(R.id.btnSignuplog);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logInInBackground(enusername.getText().toString(), enpassword.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(e==null && user!=null){
                            FancyToast.makeText(Login.this,"Login successful "+ParseUser.getCurrentUser().getUsername(), Toast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                            tohome();
                        }
                        else if(e!=null){
                            FancyToast.makeText(Login.this,e.getMessage(), Toast.LENGTH_LONG,FancyToast.ERROR,true).show();
                        }
                        else{
                            FancyToast.makeText(Login.this,"SignUp first", Toast.LENGTH_LONG,FancyToast.ERROR,true).show();

                        }
                    }
                });
            }
        });

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ParseUser.getCurrentUser()!=null){
                    FancyToast.makeText(Login.this,"Logging out "+ParseUser.getCurrentUser().getUsername(), Toast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                    ParseUser.getCurrentUser().logOut();
                }
                finish();
            }
        });

        if(ParseUser.getCurrentUser()!=null){
           tohome();
        }
    }


        public void RootLayoutTapped (View view) {
            try {
                InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }catch (Exception e){
                e.fillInStackTrace();
            }
        }

    public void tohome(){
        Intent intent=new Intent(Login.this,Home.class);
        startActivity(intent);
    }
}


