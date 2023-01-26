package be.ehb.wifree_2;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import be.ehb.wifree_2.data.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    TextView btn_login;
    EditText txt_email, txt_password, txt_username;
    Button btn_register;
    String email, password, username;

    private FirebaseAuth mAuth;
    FirebaseDatabase data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            finish();
            return;
        }

        // Search the activity for the button
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call the method to redirect
                registration();
            }
        });

        // Search the activity for the textview
        btn_login = findViewById(R.id.app_redirect_to_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call the method to redirect
                redirectToLogin();
            }
        });
    }

    private void registration(){
        // Search the activity for the three text-fields
        txt_email = findViewById(R.id.txt_email);
        txt_password = findViewById(R.id.txt_password);
        txt_username = findViewById(R.id.txt_username);

        // get the text of the text-fields
        email = txt_email.getText().toString();
        password = txt_password.getText().toString();
        username = txt_username.getText().toString();

        data = new FirebaseDatabase();
        // Check to see if any field is left open
        if(email.isEmpty() || password.isEmpty() || username.isEmpty()){
            Toast.makeText(this, "Please fill in all fields ", Toast.LENGTH_SHORT).show(); // if so show them an error message
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            data.getUserCount().observe(RegisterActivity.this, new Observer<Integer>() {
                                @Override
                                public void onChanged(Integer integer) {
                                    User newUser = new User(username,email,mAuth.getCurrentUser().getUid());
                                    data.add(newUser);
                                }
                            });
                            Log.d(TAG, "createUserWithEmail:success");
                            redirectToMain();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    // I put it in a separate method because of the error not getting the context
    // in a listener
    private void redirectToLogin(){
        // Redirect it to the Login activity
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    // TODO: LOOK FOR THIS ERROR
    private void redirectToMain() {
        // Redirect it to the Main activity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
