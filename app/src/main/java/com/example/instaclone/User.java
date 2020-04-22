package com.example.instaclone;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class User extends Fragment {


    private ListView listview;
    private ArrayList arraylist;
    private ArrayAdapter arrayadapter;
    private TextView txtLoading;


    public User() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_user, container, false);

        listview=view.findViewById(R.id.listview);
        txtLoading=view.findViewById(R.id.txtUserLoading);
        arraylist=new ArrayList();
        arrayadapter=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,arraylist);

        ParseQuery<ParseUser> parseUser=ParseUser.getQuery();

        parseUser.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());

        parseUser.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if(e==null){
                    if(users.size()>0){
                        for(ParseUser user:users){

                            arraylist.add(user.getUsername());
                        }
                        listview.setAdapter(arrayadapter);
                        txtLoading.animate().alpha(0).setDuration(1000);
                        listview.setVisibility(View.VISIBLE);
                    }
                }
            }
        });



        return  view;
    }

}
