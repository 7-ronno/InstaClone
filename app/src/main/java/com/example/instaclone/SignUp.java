package com.example.instaclone;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private Button save;
    private EditText name,power,speed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        save=findViewById(R.id.save);
        name=findViewById(R.id.name);
        power=findViewById(R.id.power);
        speed=findViewById(R.id.speed);

        save.setOnClickListener(SignUp.this);
    }

    @Override
    public void onClick(View v) {
        try {
            final ParseObject boxer = new ParseObject("Boxers");
            boxer.put("name", name.getText().toString());
            boxer.put("power", Integer.parseInt(power.getText().toString()));
            boxer.put("speed", Integer.parseInt(speed.getText().toString()));

            boxer.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        FancyToast.makeText(SignUp.this, boxer.get("name") + " has been saved to server", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                    } else {
                        FancyToast.makeText(SignUp.this, e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                    }
                }
            });
        }
        catch (Exception e)
        {
            FancyToast.makeText(SignUp.this, e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
        }
    }
}
