package com.example.instaclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin,btnSignup;
    private EditText enEmail,enUsername,enPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        setTitle("SignUp");

        btnLogin=findViewById(R.id.btnLogin);
        btnSignup=findViewById(R.id.btnSignUp);

        enEmail=findViewById(R.id.enEmail);
        enUsername=findViewById(R.id.enUsername);
        enPassword=findViewById(R.id.enPassword);

        enPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN){
                    onClick(btnSignup);
                }
                return false;
            }
        });

        btnSignup.setOnClickListener(SignUp.this);
        btnLogin.setOnClickListener(SignUp.this);

        if(ParseUser.getCurrentUser()!=null){
           tohome();
       }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnLogin:
                Intent intent=new Intent(SignUp.this,Login.class);
                startActivity(intent);
                break;

            case R.id.btnSignUp:

                final ParseUser newUser= new ParseUser();
                newUser.setEmail(enEmail.getText().toString());
                newUser.setUsername(enUsername.getText().toString());
                newUser.setPassword(enPassword.getText().toString());

                final ProgressDialog progress=new ProgressDialog(this);
                progress.setMessage("Signing Up"+ enUsername.getText().toString());
                progress.show();
                newUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            FancyToast.makeText(SignUp.this,"SignUp successful "+newUser.getUsername(), Toast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                            tohome();
                        }
                        else{
                            FancyToast.makeText(SignUp.this,e.getMessage(), Toast.LENGTH_LONG,FancyToast.ERROR,true).show();
                        }
                        progress.dismiss();
                    }
                });

                break;
        }
    }

    public void RootLayoutTapped(View view) {
        try {
            InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }catch (Exception e){
            e.fillInStackTrace();
        }
    }

    public void tohome(){
        Intent intent=new Intent(SignUp.this,Home.class);
        startActivity(intent);
    }
}
