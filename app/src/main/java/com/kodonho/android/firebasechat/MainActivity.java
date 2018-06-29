package com.kodonho.android.firebasechat;

import android.content.Intent;
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
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;

import static com.jakewharton.rxbinding2.widget.RxTextView.textChangeEvents;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText editEmail, editPassword, editConfirm, editNickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        setView();

        Observable<TextViewTextChangeEvent> idObs = textChangeEvents(editEmail);
        Observable<TextViewTextChangeEvent> pwObs = textChangeEvents(editPassword);

        Observable.combineLatest(idObs,pwObs,
            (idChanges,pwChanges) -> {
                boolean idCheck = checkEmail(idChanges.text().toString());
                boolean pwCheck = checkPassword(pwChanges.text().toString());
                return idCheck && pwCheck;
            })
            .subscribe(
                checkFlag -> findViewById(R.id.btnSignin).setEnabled(checkFlag)
            );
    }

    private boolean verifyAccount(){
        boolean check = true;
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();
        // 이메일의 유효성 검증
        if(!checkEmail(email)){
            Toast.makeText(this, "이메일의 형식이 잘못되었습니다", Toast.LENGTH_SHORT).show();
            check = false;
        }
        // 비밀번호의 유효성 검증
        if(!checkPassword(password)){
            Toast.makeText(this, "비밀번호의 형식이 잘못되었습니다", Toast.LENGTH_SHORT).show();
            check = false;
        }
        return check;
    }

    public boolean checkEmail(String email){
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public boolean checkPassword(String password){
        String regex = "^[a-zA-Z0-9]{8,}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);
        return m.matches();
    }

    public void clickSignup(View view){
        if(editConfirm.getVisibility() == View.GONE) {
            editConfirm.setVisibility(View.VISIBLE);
            editNickname.setVisibility(View.VISIBLE);
        }else if(editConfirm.getVisibility() == View.VISIBLE) {
            writeNewUser();
        }
    }

    private void writeNewUser(){
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();
        String confirm = editConfirm.getText().toString();
        // 입력값 검증
        if(!verifyAccount()){
            return;
        }
        if(!password.equals(confirm)){
            Toast.makeText(this, "Password 와 Confirm Password가 다릅니다", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        // 이메일이 검증되었는지 확인후 안되었으면 검증 이메일 발송 요청
                        if(!user.isEmailVerified()){
                            Toast.makeText(getBaseContext(), "검증 이메일을 등록한 주소로 발송하였습니다.", Toast.LENGTH_LONG).show();
                            user.sendEmailVerification();
                        }
                    } else {
                        Toast.makeText(getBaseContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
    }

    public void clickSignin(View view){
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();
        if(!verifyAccount()){
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if(!user.isEmailVerified()){
                            Toast.makeText(getBaseContext(), "이메일 검증이 되지 않습니다. 검증 이메일을 등록한 주소로 재발송하였습니다.", Toast.LENGTH_LONG).show();
                            user.sendEmailVerification();
                        }else{
                            Intent intent = new Intent(getBaseContext(), ChatList.class);
                            intent.putExtra(ChatRoom.USER_ID, email);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(getBaseContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void setView(){
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
