package com.hari.asus.sleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class createAccount extends AppCompatActivity {

    private EditText reg_email;
    private EditText reg_pass;
    private EditText reg_confirmtxt;
    private Button reg_create;
    private FrameLayout reg_bar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mAuth = FirebaseAuth.getInstance();
        reg_email = findViewById(R.id.editTextTextEmailAddressreg);
        reg_pass = findViewById(R.id.editTextTextPasswordreg);
        reg_confirmtxt = findViewById(R.id.reg_confirm);
        reg_create = findViewById(R.id.buttonreg);
        reg_bar = findViewById(R.id.progressBarreg);

        reg_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String regemail = reg_email.getText().toString();
                String regPass = reg_pass.getText().toString();
                String regconfirm = reg_confirmtxt.getText().toString();

                if(!TextUtils.isEmpty(regemail)&&!TextUtils.isEmpty(regPass)&&!TextUtils.isEmpty(regconfirm)){
                   if(regPass.equals(regconfirm)){
                        reg_bar.setVisibility(View.VISIBLE);

                        mAuth.createUserWithEmailAndPassword(regemail,regPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                  Intent intent = new Intent(createAccount.this,setupActivity.class);
                                  startActivity(intent);
                                  finish();
                                }else{
                                    String errormessage = task.getException().getMessage();
                                    Toast.makeText(createAccount.this, "Error : " + errormessage, Toast.LENGTH_SHORT).show();
                                }

                                reg_bar.setVisibility(View.INVISIBLE);
                            }
                        });


                   }else{
                       Toast.makeText(createAccount.this, "Passwords dont match", Toast.LENGTH_SHORT).show();
                   }
                }else{
                    Toast.makeText(createAccount.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null){
            sendtomain();
        }
    }
    private void sendtomain() {
        Intent intent  = new Intent(createAccount.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}