package com.example.instaclone;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;


/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {


    public Profile() {
        // Required empty public constructor
    }

    private Button btnProfileUpdate;
    private EditText enProfileName,enProfileSport,enProfileIdol;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_profile, container, false);

        final ParseUser parseUser=ParseUser.getCurrentUser();

        btnProfileUpdate=view.findViewById(R.id.btnProfileUpdate);
        enProfileIdol=view.findViewById(R.id.enProfileIdol);
        enProfileName=view.findViewById(R.id.enProfileName);
        enProfileSport=view.findViewById(R.id.enProfileSport);


        btnProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseUser.put("Name",enProfileName.getText().toString());
                parseUser.put("Sport",enProfileSport.getText().toString());
                parseUser.put("Idol",enProfileIdol.getText().toString());

                parseUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            FancyToast.makeText(getContext(),"Update successful ", Toast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                        }
                        else{
                            FancyToast.makeText(getContext(),e.getMessage(), Toast.LENGTH_LONG,FancyToast.ERROR,true).show();
                        }
                    }
                });

            }
        });

        if(parseUser.get("Name")!=null)
            enProfileName.setText(parseUser.get("Name")+"");
        else
            enProfileName.setText("");
        if(parseUser.get("Sport")!=null)
        enProfileSport.setText(parseUser.get("Sport")+"");
        else
            enProfileSport.setText("");
        if(parseUser.get("Idol")!=null)
        enProfileIdol.setText(parseUser.get("Idol")+"");
        else
            enProfileIdol.setText("");




        return view;
    }

}
