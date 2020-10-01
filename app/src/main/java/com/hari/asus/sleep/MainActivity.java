package com.hari.asus.sleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;


import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
private Toolbar maintoolbar;
private FirebaseAuth mAuth;
    private FirebaseAuth firebaseAuth;
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    private FirebaseFirestore firebaseFirestore;
FrameLayout f1; Calendar calendar;
    private String user_id,cdate;
    DatabaseReference ref;
    FirebaseDatabase db;
    TextView username,doctortext,food,sleep,fitness;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        maintoolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(maintoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    getSupportActionBar().setTitle(" ");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        db = FirebaseDatabase.getInstance();

        ref = db.getReference("status");
        f1 = findViewById(R.id.setup_progressbar2);
        username = findViewById(R.id.main_txt);
        doctortext = findViewById(R.id.main_doctor);
        food = findViewById(R.id.food_text);
        sleep = findViewById(R.id.sleep_text);
        fitness = findViewById(R.id.fit_text);


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if(user==null){
           sendtomain();
        }else{

            user_id = firebaseAuth.getCurrentUser().getUid();




            firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        f1.setVisibility(View.VISIBLE);
                        String name  = task.getResult().getString("name");
                        String doctor = task.getResult().getString("TEXT");


                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               String ttt2 = String.valueOf(dataSnapshot.child(user_id).child(name).getValue());
                                int hh;
                                hh = Integer.parseInt(ttt2);
                                if(hh==1){showCustomDialog(name);}
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        String sdate =  task.getResult().getString("start");
                        String edate =  task.getResult().getString("end");


                        String es1 = task.getResult().getString("ES1");
                        String es2  = task.getResult().getString("ES2");
                        String es3  = task.getResult().getString("ES3");
                        String es4  = task.getResult().getString("ES4");
                        String es5  = task.getResult().getString("ES5");

                        String ep1 = task.getResult().getString("EP1");
                        String ep2  = task.getResult().getString("EP2");
                        String ep3  = task.getResult().getString("EP3");
                        String ep4  = task.getResult().getString("EP4");
                        String ep5 = task.getResult().getString("EP5");
                        String ep6  = task.getResult().getString("EP6");
                        String ep7  = task.getResult().getString("EP7");
                        String ep8  = task.getResult().getString("EP8");

                        String ss1 = task.getResult().getString("SS1");
                        String ss2  = task.getResult().getString("SS2");
                        String ss3  = task.getResult().getString("SS3");
                        String ss4  = task.getResult().getString("SS4");

                        String sp1 = task.getResult().getString("SP1");
                        String sp2  = task.getResult().getString("SP2");
                        String sp3  = task.getResult().getString("SP3");
                        String sp4  = task.getResult().getString("SP4");

                        String m1 = task.getResult().getString("M1");
                        String m2  = task.getResult().getString("M2");

                        String as1 = task.getResult().getString("AS1");
                        String ap1  = task.getResult().getString("AP1");

                        String ns = task.getResult().getString("NS");
                        String np  = task.getResult().getString("NP");


                        String ls1 = task.getResult().getString("LS1");
                        String ls2  = task.getResult().getString("LS2");
                        String ls3  = task.getResult().getString("LS3");
                        String ls4  = task.getResult().getString("LS4");

                        String lp1 = task.getResult().getString("LP1");
                        String lp2  = task.getResult().getString("LP2");
                        String lp3  = task.getResult().getString("LP3");
                        String lp4  = task.getResult().getString("LP4");



                        username.setText(name);
                        doctortext.setText(doctor);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy");

                        cdate = simpleDateFormat.format(new Date());
                        Date date1;
                        Date date2;
                       calendar = Calendar.getInstance();

                        try{
                             date1 = simpleDateFormat.parse(sdate);
                             date2 = simpleDateFormat.parse(cdate);

                           long data =  compute(date1,date2);

                                 if(data==0){
                                     startalarm(ss1,1);
                                     startalarm(es1,2);
                                     sleep.setText("Sleep from : "+ ss1+ " to: "+ sp1 );
                                     food.setText("Take Melatonin at: " + "nil");
                                     fitness.setText("Bright light exposure: "+ es1 + " to: "+ep1+"\n\n");
                                 }
                            else if(data==1){

                                    startalarm(ss2,11);
                                     startalarm(ns,12);
                                     startalarm(es2,13);
                                     startalarm(es4,14);
                                sleep.setText("Sleep from : "+ ss2+ " to: "+ sp2+"\n\n"+"Take nap from: " +ns+" to: "+np );
                                     food.setText("Take Melatonin at: " + "nil");
                                     fitness.setText("Bright light exposure: "+ es2 + " to: "+ep2+"\n\n"+"Bright light exposure: " +es4+" to: "+ ep4+"\n\n ");
                                 }
                            else if(data>=2 && data<=8){
                                     startalarm(ss3,21);
                                     startalarm(ns,22);
                                     startalarm(es3,23);
                                     startalarm(es4,24);

                                sleep.setText("Sleep from : "+ ss3+ " to: "+ sp3+"\n\n"+"Take nap from: " +ns+" to: "+np  );
                                     food.setText("Take Melatonin at: " + m1);
                                     fitness.setText("Bright light exposure: "+ es3 + " to: "+ep3+"\n\n"+"Bright light exposure: " +es4+" to: "+ ep4+"\n\n ");}

                            else if(data==9){
                                     startalarm(ss4,31);
                                     startalarm(m2,32);
                                     startalarm(es5,33);
                                     startalarm(as1,34);
                                     startalarm(ls1,35);

                                sleep.setText("Sleep from : "+ ss4+ " to: "+ sp4 );
                                     food.setText("Take Melatonin at: " + m2);
                                     fitness.setText("Bright light exposure: "+ es5 + " to: "+ep5+"\n\n"+"Wear sunglass: " +as1+" to: "+ ap1+"\n\n "+"Dont wear sunglass from: " + ls1+ " to: "+lp1);
                                 }
                            else if(data==10){

                                     startalarm(ss4,41);
                                     startalarm(m2,42);
                                     startalarm(es5,43);
                                     startalarm(as1,44);
                                     startalarm(ls2,45);

                                sleep.setText("Sleep from : "+ ss4+ " to: "+ sp4 );
                                food.setText("Take Melatonin at: " + m2);
                                fitness.setText("Bright light exposure: "+ es5 + " to: "+ep6+"\n\n"+"Wear sunglass: " +as1+" to: "+ ap1+"\n\n "+"Dont wear sunglass from: " + ls2+ " to: "+lp2);
                                 }
                            else if(data==11){
                                     startalarm(ss4,51);

                                     startalarm(es5,53);
                                     startalarm(as1,54);
                                     startalarm(ls3,54);
                                     sleep.setText("Sleep from : "+ ss4+ " to: "+ sp4 );
                                     food.setText("Take Melatonin at: " + "nil");
                                     fitness.setText("Bright light exposure: "+ es5 + " to: "+ep7+"\n\n"+"Wear sunglass: " +as1+" to: "+ ap1+"\n\n "+"Dont wear sunglass from: " + ls3+ " to: "+lp3);
                                 }
                            else if(data==12){
                                     startalarm(ss4,51);

                                     startalarm(es5,53);

                                     startalarm(ls4,54);
                                     sleep.setText("Sleep from : "+ ss4+ " to: "+ sp4 );
                                     food.setText("Take Melatonin at: " + "nil");
                                     fitness.setText("Bright light exposure: "+ es5 + " to: "+ep8+"\n\n"+"Dont wear sunglass from: " + ls4+ " to: "+lp4);


                                 }


                        }catch (ParseException e){
                            Toast.makeText(MainActivity.this, "error date", Toast.LENGTH_SHORT).show();

                        }

f1.setVisibility(View.INVISIBLE);

                        }
                }else{
                    String error = task.getException().getMessage();
                    Toast.makeText(MainActivity.this, "error :" + error, Toast.LENGTH_SHORT).show();
                    f1.setVisibility(View.INVISIBLE);
                }

            }
        });


        }
    }

    private long compute(Date date1, Date date2) {
        long different = date2.getTime()-date1.getTime();
       long  differen  = different/(86400000);

        if(differen<13){differen=differen+0;}
        else{differen = differen%13;}
       return (differen);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

      switch (item.getItemId()){
        case R.id.main_logout:
            logout();
            return  true;
          case R.id.main_settings:
             Intent intent = new Intent(MainActivity.this,setupActivity.class);
             startActivity(intent);
             return true;
          case android.R.id.home:
              finish();
              return true;

          default:
              return false;

      }


    }

    private void logout() {
mAuth.signOut();
sendtomain();
    }

    private void sendtomain() {
        Intent intent  = new Intent(MainActivity.this,loginActivity.class);
        startActivity(intent);
        finish();
    }

    private void setAlarm(Notification notification , long time, int t) {

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent i = new Intent(this, Myalarm.class);
        i.putExtra(Myalarm. NOTIFICATION_ID , 1 ) ;
        i.putExtra(Myalarm. NOTIFICATION , notification) ;
        PendingIntent pi = PendingIntent.getBroadcast(this, t, i, 0);
am.setExact(AlarmManager.RTC_WAKEUP,time,pi);

    }

    private Notification getNotification (String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, default_notification_channel_id ) ;
        builder.setContentTitle( "Star Trek Notification" ) ;
        builder.setContentText(content) ;
        builder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
        return builder.build() ;
    }

    private void  startalarm(String ns,int t){

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        try{
            Date time1 = sdf.parse(ns);
            if (android.os.Build.VERSION.SDK_INT >= 23) {
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                        time1.getHours(), time1.getMinutes(), 0);
            } else {
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                        time1.getHours(), time1.getMinutes(), 0);
            }
            if(calendar.before(Calendar.getInstance())){}else {

                setAlarm(getNotification("Open App now to stop the alarm"), calendar.getTimeInMillis(),t);
            } }catch (ParseException e){
            Toast.makeText(MainActivity.this, "alarm error", Toast.LENGTH_SHORT).show();
        }

    }
    private void showCustomDialog(String u) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_warning2);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        EditText t1 = dialog.findViewById(R.id.weekly_w);
        EditText t2 = dialog.findViewById(R.id.weekly_m);

        ((Button) dialog.findViewById(R.id.bt_close22)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tt1 = t1.getText().toString();
                String tt2 = t2.getText().toString();
                if(!TextUtils.isEmpty(tt1)&&!TextUtils.isEmpty(tt2)){
                    Map<String,String> usermap = new HashMap<>();
                    usermap.put("WEIGHT_GAIN",tt1);
                    usermap.put("MEMORY_LOSS",tt2);
                    firebaseFirestore.collection("Users/"+user_id+"/health").add(usermap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(MainActivity.this, "status updated", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            ref.child(user_id).child(u).setValue(0);
                        }
                    });
                }else{
                    Toast.makeText(MainActivity.this, "all fields needed", Toast.LENGTH_SHORT).show();
                }

            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}