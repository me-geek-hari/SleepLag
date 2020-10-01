package com.hari.asus.sleep;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import de.hdodenhof.circleimageview.CircleImageView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class setupActivity extends AppCompatActivity {
    private EditText setup_username,start_date,end_date;
    private Button setup_submit;
    private CircleImageView setup_profile;

    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private Uri mainImageUri = null;
    private FrameLayout setup_bar;
    private  long valdate =-1;
    private  long valdate1 =-1;
    private String user_id,  downloadUri,currentdate ;
    StorageReference imagePath;
    DatabaseReference ref;
    FirebaseDatabase db;
    private Boolean isChanged = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

db= FirebaseDatabase.getInstance();
ref = db.getReference("status");
        setup_username = findViewById(R.id.setup_edittext);
        start_date = findViewById(R.id.editTextTextPasswordsetup);
        end_date = findViewById(R.id.setup_confirm);
        setup_submit = findViewById(R.id.setup_btn);
        setup_profile = findViewById(R.id.setup_circleImageView);
        setup_bar = findViewById(R.id.setup_progressbar);

        setup_bar.setVisibility(View.VISIBLE);
        setup_submit.setEnabled(false);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();


        user_id = firebaseAuth.getCurrentUser().getUid();


        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
             if(task.isSuccessful()){
                 if(task.getResult().exists()){
                  String name  = task.getResult().getString("name");
                  String image = task.getResult().getString("image");
                  String sdate = task.getResult().getString("start");
                  String edate = task.getResult().getString("end");
                  mainImageUri = Uri.parse(image);
                  setup_username.setText(name);
                  start_date.setText(sdate);
                  end_date.setText(edate);
                     RequestOptions requestOptions = new RequestOptions();
                     requestOptions.placeholder(R.drawable.user);
                  Glide.with(setupActivity.this).setDefaultRequestOptions(requestOptions).load(image).into(setup_profile);
                 }
             }else{
                 String error = task.getException().getMessage();
                 Toast.makeText(setupActivity.this, "error :" + error, Toast.LENGTH_SHORT).show();
             }
             setup_bar.setVisibility(View.INVISIBLE);
             setup_submit.setEnabled(true);
            }
        });

        setup_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = setup_username.getText().toString();
                String startdate = start_date.getText().toString();
                String enddate = end_date.getText().toString();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy");
                Date date1;
                Date date2;
                Date date3;
                try{
                    currentdate = simpleDateFormat.format(new Date());
                    date1 = simpleDateFormat.parse(startdate);
                    date2 = simpleDateFormat.parse(enddate);
                    date3 = simpleDateFormat.parse(currentdate);
                    valdate = compute(date1,date2);


                }catch (ParseException e){
                    Toast.makeText(setupActivity.this, "Dates wrong format", Toast.LENGTH_SHORT).show();
                }

                    if(!TextUtils.isEmpty(username)&& mainImageUri!=null&&!TextUtils.isEmpty(startdate)&&!TextUtils.isEmpty(enddate)&&valdate>=0){
                    setup_bar.setVisibility(View.VISIBLE);
                if(isChanged==true){



                     imagePath = storageReference.child("profile_images").child(user_id+".jpg");
                    imagePath.putFile(mainImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                       if(task.isSuccessful()){

                           storetofirestore(task,username,startdate,enddate);
                       }else{
                           String errormsg = task.getException().getMessage();
                           Toast.makeText(setupActivity.this, "error: " + errormsg, Toast.LENGTH_SHORT).show();
                           setup_bar.setVisibility(View.INVISIBLE);

                       }
                        }
                    });
                }else{
                    storetofirestore(null,username,startdate,enddate);

                }
            }else{
                    Toast.makeText(setupActivity.this, "Wrong fields found", Toast.LENGTH_SHORT).show();
                }}
        });

        setup_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){

                  if(ContextCompat.checkSelfPermission(setupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                      ActivityCompat.requestPermissions(setupActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                  }else{
                      CropImage.activity()
                              .setGuidelines(CropImageView.Guidelines.ON)
                              .setAspectRatio(1,1)
                              .start(setupActivity.this);
                  }
              }else{
                  CropImage.activity()
                          .setGuidelines(CropImageView.Guidelines.ON)
                          .setAspectRatio(1,1)
                          .start(setupActivity.this);
              }
            }
        });


    }

    private void storetofirestore(@NonNull Task<UploadTask.TaskSnapshot> task, String username, String startdate , String enddate) {
        if(task == null ){downloadUri = (mainImageUri).toString();
            Map<String,String> usermap = new HashMap<>();
            usermap.put("name",username);
            usermap.put("start",startdate);
            usermap.put("end",enddate);
            usermap.put("image",downloadUri);

            firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            String txt = task.getResult().getString("TEXT");
                            String es1 = task.getResult().getString("ES1");
                            String es2 = task.getResult().getString("ES2");
                            String es3 = task.getResult().getString("ES3");String es4 = task.getResult().getString("ES4");String es5 = task.getResult().getString("ES5");

                            String ep1 = task.getResult().getString("EP1");
                            String ep2 = task.getResult().getString("EP2");
                            String ep3 = task.getResult().getString("EP3");
                            String ep4 = task.getResult().getString("EP4");
                            String ep5 = task.getResult().getString("EP5");
                            String ep6 = task.getResult().getString("EP6");
                            String ep7 = task.getResult().getString("EP7");
                            String ep8 = task.getResult().getString("EP8");
                            String ss1 = task.getResult().getString("SS1");
                            String ss2 = task.getResult().getString("SS2");
                            String ss3 = task.getResult().getString("SS3");
                            String ss4 = task.getResult().getString("SS4");
                            String sp1 = task.getResult().getString("SP1");
                            String sp2 = task.getResult().getString("SP2");
                            String sp3 = task.getResult().getString("SP3");
                            String sp4 = task.getResult().getString("SP4");
                            String m1 = task.getResult().getString("M1");
                            String m2 = task.getResult().getString("M2");
                            String as1 = task.getResult().getString("AS1");
                            String ap1 = task.getResult().getString("AP1");
                            String ns = task.getResult().getString("NS");
                            String np = task.getResult().getString("NP");
                            String ls1 = task.getResult().getString("LS1");
                            String ls2 = task.getResult().getString("LS2");
                            String ls3 = task.getResult().getString("LS3");
                            String ls4 = task.getResult().getString("LS4");
                            String lp1 = task.getResult().getString("LP1");
                            String lp2 = task.getResult().getString("LP2");
                            String lp3 = task.getResult().getString("LP3");
                            String lp4 = task.getResult().getString("LP4");

                            usermap.put("ES1",es1);
                            usermap.put("ES2",es2);
                            usermap.put("ES3",es3);
                            usermap.put("ES4",es4);
                            usermap.put("ES5",es5);

                            usermap.put("EP1",ep1);
                            usermap.put("EP2",ep2);
                            usermap.put("EP3",ep3);
                            usermap.put("EP4",ep4);
                            usermap.put("EP5",ep5);
                            usermap.put("EP6",ep6);
                            usermap.put("EP7",ep7);
                            usermap.put("EP8",ep8);

                            usermap.put("SS1",ss1);
                            usermap.put("SS2",ss2);
                            usermap.put("SS3",ss3);
                            usermap.put("SS4",ss4);

                            usermap.put("SP1",sp1);
                            usermap.put("SP2",sp2);
                            usermap.put("SP3",sp3);
                            usermap.put("SP4",sp4);

                            usermap.put("M1",m1);
                            usermap.put("M2",m2);

                            usermap.put("AS1",as1);
                            usermap.put("AP1",ap1);

                            usermap.put("NS",ns);
                            usermap.put("NP",np);

                            usermap.put("LS1",ls1);
                            usermap.put("LS2",ls2);
                            usermap.put("LS3",ls3);
                            usermap.put("LS4",ls4);

                            usermap.put("LP1",lp1);
                            usermap.put("LP2",lp2);
                            usermap.put("LP3",lp3);
                            usermap.put("LP4",lp4);
                            usermap.put("TEXT",txt);


                            firebaseFirestore.collection("Users").document(user_id).set(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(setupActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                                        Intent in = new Intent(setupActivity.this,MainActivity.class);
                                        startActivity(in);
                                        finish();
                                    }else{
                                        String error = task.getException().getMessage();
                                        Toast.makeText(setupActivity.this, "error : " + error, Toast.LENGTH_SHORT).show();
                                    }
                                    setup_bar.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    }

                }
            });
        }else{
        final Task<Uri> firebaseUri = task.getResult().getStorage().getDownloadUrl();
        firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                downloadUri = uri.toString();
                Map<String,String> usermap = new HashMap<>();

                usermap.put("name",username);
                usermap.put("start",startdate);
                usermap.put("end",enddate);
                usermap.put("image",downloadUri);

                usermap.put("ES1","00:00");
                usermap.put("ES2","00:00");
                usermap.put("ES3","00:00");
                usermap.put("ES4","22:00");
                usermap.put("ES5","08:00");

                usermap.put("EP1","02:00");
                usermap.put("EP2","04:00");
                usermap.put("EP3","09:00");
                usermap.put("EP4","23:59");
                usermap.put("EP5","14:00");
                usermap.put("EP6","13:00");
                usermap.put("EP7","12:00");
                usermap.put("EP8","11:00");

                usermap.put("SS1","02:00");
                usermap.put("SS2","04:00");
                usermap.put("SS3","09:00");
                usermap.put("SS4","00:00");

                usermap.put("SP1","10:00");
                usermap.put("SP2","12:00");
                usermap.put("SP3","15:00");
                usermap.put("SP4","08:00");

                usermap.put("M1","09:00");
                usermap.put("M2","23:00");

                usermap.put("AS1","21:00");
                usermap.put("AP1","23:00");

                usermap.put("NS","18:00");
                usermap.put("NP","20:00");

                usermap.put("LS1","14:00");
                usermap.put("LS2","13:00");
                usermap.put("LS3","12:00");
                usermap.put("LS4","11:00");

                usermap.put("LP1","20:00");
                usermap.put("LP2","19:00");
                usermap.put("LP3","18:00");
                usermap.put("LP4","18:00");

                usermap.put("TEXT","I am your doctor back from earth, I will posting updates weekly, so try to follow your sleep schedule I will be weekly reviewing your status and will update you about your health status, dont worry there is always an eye watching over your health!");
ref.child(user_id).child(username).setValue(0);
                firebaseFirestore.collection("Users").document(user_id).set(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(setupActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                            Intent in = new Intent(setupActivity.this,MainActivity.class);
                            startActivity(in);
                            finish();
                        }else{
                            String error = task.getException().getMessage();
                            Toast.makeText(setupActivity.this, "error : " + error, Toast.LENGTH_SHORT).show();
                        }
                        setup_bar.setVisibility(View.INVISIBLE);
                    }

                });
            }
        });}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageUri= result.getUri();
                setup_profile.setImageURI(mainImageUri);
                isChanged=true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    private long compute(Date date1, Date date2) {
        long different = date2.getTime()-date1.getTime();
        long  differen  = different/(86400000);
        return (differen);
    }
}