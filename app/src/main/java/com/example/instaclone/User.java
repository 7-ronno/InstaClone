package com.example.instaclone;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class User extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {


    private ListView listview;
    private ArrayList<String> arraylist;
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

        listview.setOnItemClickListener(User.this);
        listview.setOnItemLongClickListener(User.this);

        return  view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent=new Intent(getContext(),ViewUser.class);
        intent.putExtra("username",arraylist.get(position));
        startActivity(intent);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        final String username=arraylist.get(position);
        ParseQuery<ParseUser> parseQuery=ParseUser.getQuery();
        parseQuery.whereEqualTo("username",username);

        parseQuery.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e==null && user!=null){
                    final PrettyDialog pretty=new PrettyDialog(getContext());
                    pretty.setTitle(user.getUsername()).
                            setMessage(user.get("Sport")+"\n"+user.get("Idol")).
                            setIcon(R.drawable.person).addButton("Ok", R.color.pdlg_color_red,R.color.pdlg_color_green, new PrettyDialogCallback() {
                        @Override
                        public void onClick() {
                            pretty.dismiss();
                        }
                    }).show();
                }
            }
        });




        return true;
    }
}
