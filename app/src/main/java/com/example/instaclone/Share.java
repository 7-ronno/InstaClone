package com.example.instaclone;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class Share extends Fragment implements View.OnClickListener{

    private EditText enInfo;
    private ImageView img;
    private Button btnShare;
    private Bitmap imageBitmap,rotatedimageBitmap;


    public Share() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_share, container, false);


        enInfo=view.findViewById(R.id.enShareInfo);
        btnShare=view.findViewById(R.id.btnShare);
        img=view.findViewById(R.id.imgShareFoto);

        img.setOnClickListener(Share.this);
        btnShare.setOnClickListener(Share.this);






        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnShare:
                if(imageBitmap!=null){

                    if(enInfo.getText().toString().equals("")){
                        FancyToast.makeText(getContext(),"Enter Info", Toast.LENGTH_LONG,FancyToast.ERROR,true).show();
                    }
                    else{
                        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                        imageBitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                        byte[] bytes=byteArrayOutputStream.toByteArray();
                        ParseFile parseFile=new ParseFile("img.png",bytes);
                        ParseObject parseObject=new ParseObject("Photo");
                        parseObject.put("pic",parseFile);
                        parseObject.put("info",enInfo.getText().toString());
                        parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                        final ProgressDialog dialog=new ProgressDialog(getContext());
                        dialog.setMessage("Sharing");
                        dialog.show();
                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e==null){
                                    FancyToast.makeText(getContext(),"Image Shared", Toast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                                }else{
                                    FancyToast.makeText(getContext(),e.getMessage(), Toast.LENGTH_LONG,FancyToast.ERROR,true).show();
                                }
                                dialog.dismiss();
                            }
                        });
                    }

                }else {
                    FancyToast.makeText(getContext(),"Upload Image", Toast.LENGTH_LONG,FancyToast.ERROR,true).show();
                }

                break;
            case R.id.imgShareFoto:if(Build.VERSION.SDK_INT>=23 && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1000);
            }
            else {
                getImage();
            }

                break;
        }

    }

    private void getImage() {

        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,3000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1000){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getImage();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==3000){
            if(resultCode== Activity.RESULT_OK){
                try {
                    Uri selImage=data.getData();
                    String[] filePathColumn={MediaStore.Images.Media.DATA};
                    Cursor cursor=getActivity().getContentResolver().query(selImage,filePathColumn,null,null,null);
                    cursor.moveToFirst();
                    int coloumnindex=cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath=cursor.getString(coloumnindex);
                    cursor.close();

                    imageBitmap= BitmapFactory.decodeFile(picturePath);
                    ExifInterface ei = new ExifInterface(picturePath);
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);

                    switch(orientation) {

                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotatedimageBitmap = rotateImage(imageBitmap, 90);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotatedimageBitmap = rotateImage(imageBitmap, 180);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotatedimageBitmap = rotateImage(imageBitmap, 270);
                            break;

                        case ExifInterface.ORIENTATION_NORMAL:
                        default:
                            rotatedimageBitmap = imageBitmap;
                    }



                    img.setImageBitmap(rotatedimageBitmap);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

    }

    private Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

}
