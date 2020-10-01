package com.hari.asus.sleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginActivity extends AppCompatActivity {
    private EditText login_email;
    private EditText login_password;
    private Button login_login;
    private TextView login_new;
    private FrameLayout progressBar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        login_email = findViewById(R.id.editlogin_login);
        login_password = findViewById(R.id.editpass_login);
        login_login = findViewById(R.id.loginbtn_login);
        login_new = findViewById(R.id.login_new_button);
        progressBar = findViewById(R.id.progressBarHolder);
progressBar.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();
        login_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(loginActivity.this,createAccount.class);
                startActivity(in);
            }
        });
        login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String loginemail = login_email.getText().toString();
                String loginPass = login_password.getText().toString();

                if(!TextUtils.isEmpty(loginemail)&&!TextUtils.isEmpty(loginPass)){
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(loginemail,loginPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                sendtomain();
                            }else{
                                String errormessage = task.getException().getMessage();
                                Toast.makeText(loginActivity.this, "error : "+errormessage, Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }else{
                    Toast.makeText(loginActivity.this, "All fields are necessary", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentuser = mAuth.getCurrentUser();

        if(currentuser!=null){sendtomain();}
    }

    private void sendtomain() {


            Intent mainintent  = new Intent(loginActivity.this,MainActivity.class);
            startActivity(mainintent);
            finish();

    }
}