package be.ehb.wifree_2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    TextView btn_register;
    EditText txt_email, txt_password;
    Button btn_login;
    String email, password;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            finish();
            return;
        }

        // Search the activity for the button
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call the method to login
                login();
            }
        });

        // Search the activity for the textview
        btn_register = findViewById(R.id.app_redirect_to_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call the method to redirect
                redirectToRegister();
            }
        });
    }

    private void login(){
        // Search the activity for the two textfields
        txt_email = findViewById(R.id.txt_email);
        txt_password = findViewById(R.id.txt_password);

        email = txt_email.getText().toString();
        password = txt_password.getText().toString();

        // Check to see if any field is left open
        // if so show them an error message
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please fill in all fields ", Toast.LENGTH_SHORT).show();
        }

        // Sign in using Firebase Email/Password
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    redirectToMain();
                } else {
                    Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // I put it in a separate method because of the error not getting the context
    // in a listener
    private void redirectToRegister(){
        // Redirect it to the Register activity
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    private void redirectToMain() {
        // Redirect it to the Main activity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
