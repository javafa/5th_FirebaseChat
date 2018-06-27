package com.kodonho.android.firebasechat;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    LinearLayout layout;
    EditText editEmail, editPassword, editConfirm, editNickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        setView();
    }

    public void clickSignup(View view){
        if(layout.getVisibility() == View.INVISIBLE) {
            layout.setVisibility(View.VISIBLE);
        }else if(layout.getVisibility() == View.VISIBLE) {
            writeNewUser();
        }
    }

    private void writeNewUser(){
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        // 이메일이 검증되었는지 확인후 안되었으면 검증 이메일 발송 요청
                        if(!user.isEmailVerified()){
                            user.sendEmailVerification();
                        }
                    } else {
                        Toast.makeText(getBaseContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
    }

    public void clickSignin(View view){

    }

    private void setView(){
        layout = findViewById(R.id.layout);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editConfirm = findViewById(R.id.editConfirm);
        editNickname = findViewById(R.id.editNickname);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser){
        if(currentUser.getEmail() != null && currentUser.getEmail().length() > 1){
            // 로그인 다음 페이지인
            // 채팅방 페이지로 이동
        }
    }
}
