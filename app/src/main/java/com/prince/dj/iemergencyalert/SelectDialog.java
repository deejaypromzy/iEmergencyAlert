package com.prince.dj.iemergencyalert;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SelectDialog extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback,
        PermissionUtils.PermissionResultCallback  {
    final int PERMS_REQUEST_CODE = 123;
    final static int REQUEST_VIDEO_CAPTURE = 178;
    private String mFileName;
    private String timestamp=null;
    private String videofilename;
    private DateFormat df ;
    final static int Gallery_Pick = 1;
    boolean isPermissionGranted;
    Date date;
    private SharedPreferences prefs;
    String fullName, snmBody,lastLocation,myLong,mylat;
    String ec1,ec2,ec3;
    private String loc;
    private StorageReference UserVideoFileRef;
    private FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference mref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setFinishOnTouchOutside(false);
        setContentView(R.layout.activity_select_dialog);
        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date= new Date();
        UserVideoFileRef = FirebaseStorage.getInstance().getReference();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        lastLocation= prefs.getString("location",  "Oyibi-Accra @ Valley View Uni.");


        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mref = mfirebaseDatabase.getReference();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allowed = true;
        switch (requestCode) {
            case PERMS_REQUEST_CODE:
                for (int res : grantResults)
                    allowed = allowed && res == PackageManager.PERMISSION_GRANTED;
                break;

            default:
                allowed = false;
                break;


        }
        if (allowed)
            Toast.makeText(SelectDialog.this, "Permission Granted", Toast.LENGTH_SHORT).show();
        else {
            Toast.makeText(SelectDialog.this, " Permission Denied!!!", Toast.LENGTH_SHORT).show();

        }
    }
    private void requestPermission() {
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.CALL_PHONE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            requestPermissions(permissions, PERMS_REQUEST_CODE);
    }

    boolean NoPermissions() {
        int res = 0;
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.CALL_PHONE};

        for (String s : permissions) {
            res = SelectDialog.this.checkCallingOrSelfPermission(s);
            if (!(res == PackageManager.PERMISSION_GRANTED))
                return true;
        }
        return false;

    }
    public File getFilePath() {
        timestamp = df.format(date);
        videofilename = Environment.getExternalStorageDirectory().getAbsolutePath();
        videofilename += "/video"+".mp4";

        return new File(videofilename);
    }






    //video Recording Code
    private void captureVideo() {
        if (checkCameraHardware(SelectDialog.this)){
            Intent captureVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            if (captureVideoIntent.resolveActivity(getPackageManager()) != null) {
//                File videoPath = getFilePath();
//                Uri video_uri = Uri.fromFile(videoPath);
//                captureVideoIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, video_uri);
                captureVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 3);
                captureVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
                startActivityForResult(captureVideoIntent, REQUEST_VIDEO_CAPTURE);

            }
        }

    }
    private boolean checkCameraHardware(Context context) {
        // this device has a camera
// no camera on this device
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public void fire(View view) {
        switch (view.getId()){
            case R.id.video:
                Toast.makeText(SelectDialog.this,"Video Clicked",Toast.LENGTH_LONG).show();
                if(NoPermissions()) {
                    requestPermission();

                }else {
                    captureVideo();
                }

                break;
            case R.id.call:
                if(NoPermissions()) {
                    requestPermission();

                }else {
                    if (lastLocation.toLowerCase().trim().contains("adeiso")){
                        // ec1="0299207853";
                        ec1="0240741137";
                        loc="Adeiso";
                    }else if (lastLocation.toLowerCase().trim().contains("dodowa")){
                        // ec1="0245206702";
                        loc="dodowa";
                    }else if (lastLocation.toLowerCase().trim().contains("asamankese")){
                        // ec1="0245206702";
                        ec1="0240741137";
                        loc="asamankese";
                    }

                    final String user = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
                    timestamp = df.format(date);
                    Database dbUser = new Database(
                            "SOS BY PHONE CALL",
                            lastLocation,
                            timestamp.toString(),
                            user
                    );
                    mref.child("reports").push().setValue(dbUser);

                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ec1));
                    startActivity(intent);
                    Toast.makeText(SelectDialog.this,"Calling "+loc+" police station",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.sms:
                if(NoPermissions()) {
               requestPermission();
                }else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"Select Picture"), Gallery_Pick);

                    final String user = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
                    timestamp = df.format(date);
                    Database dbUser = new Database(
                            "SOS by SMS",
                            lastLocation,
                            timestamp.toString(),
                            user
                    );
                    mref.child("reports").push().setValue(dbUser);
//                    Intent galleryIntent = new Intent();
//                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
//                    galleryIntent.setType("image/*");
//                    galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                    startActivityForResult(galleryIntent, Gallery_Pick);
//                   // startService(new Intent(SelectDialog.this, smsService.class));
                    Toast.makeText(SelectDialog.this, "Sending Images", Toast.LENGTH_LONG).show();
                    //finish();
                }
                break;
        }
    }
    //Video Uploading Code
    private void uploadVideo() {
        final String user = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();

        timestamp = df.format(date);
        final StorageReference filepath = UserVideoFileRef.child(timestamp).child("zee_video.mp4");
        final Uri uri = Uri.fromFile(new File(videofilename));
        StorageTask<UploadTask.TaskSnapshot> storageTask = filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                                                          @Override
                                                                                                          public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                                                              timestamp = df.format(date);
                                                                                                              Database dbUser = new Database(
                                                                                                                      "SOS BY VIDEO",
                                                                                                                      lastLocation,
                                                                                                                      timestamp.toString(),
                                                                                                                      user
                                                                                                              );
                                                                                                              mref.child("reports").push().setValue(dbUser);
                                                                                                          }



                                                                                                      }
        );

    }







    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
        if (requestCode==REQUEST_VIDEO_CAPTURE && resultCode==RESULT_OK){
            File videoPath = getFilePath();
                Uri video_uri = Uri.fromFile(videoPath);
                data.putExtra(MediaStore.EXTRA_OUTPUT, video_uri);
                Toast.makeText(getApplicationContext(), "Video recorded Successfully", Toast.LENGTH_SHORT).show();
                uploadVideo();

        }else   if(requestCode == Gallery_Pick) {
            if(resultCode == Activity.RESULT_OK) {
                if(data.getClipData() != null) {
                    int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                    for(int i = 0; i < count; i++){
                          Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    }
                    //do something with the image (save it to some directory or whatever you need to do with it here)
                }
            }

            else if(data.getData() != null) {
                String imagePath = data.getData().getPath();
                //do something with the image (save it to some directory or whatever you need to do with it here)
            }




        }else {
            super.onActivityResult(requestCode, resultCode, data);

        }   }catch (Exception es){
            Toast.makeText(getApplicationContext(), "No input", Toast.LENGTH_SHORT).show();

    }
    }

    // Permission check functions

    @Override
    public void PermissionGranted(int request_code) {
        Log.i("PERMISSION","GRANTED");
        isPermissionGranted=true;
    }

    @Override
    public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {
        Log.i("PERMISSION PARTIALLY","GRANTED");
    }

    @Override
    public void PermissionDenied(int request_code) {
        Log.i("PERMISSION","DENIED");
    }

    @Override
    public void NeverAskAgain(int request_code) {
        Log.i("PERMISSION","NEVER ASK AGAIN");
    }
}
