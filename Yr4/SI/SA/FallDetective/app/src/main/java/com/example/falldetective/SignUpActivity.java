package com.example.falldetective;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity {




    private ImageView signUpBack;
    private TextView LoginRedirect;
    private TextInputEditText signUpEmail, signUpFullName, signUpPassword;
    private Button signUp;
    private RadioGroup genderRadioGroup;
    private DatePicker birthDatePicker;
    private Calendar birthDate;
    private FirebaseAuth auth;
    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signUpBack = findViewById(R.id.buttonSignUpBack);
        LoginRedirect = findViewById(R.id.LoginRedirect);
        signUpEmail = findViewById(R.id.signUpEmail);
        signUpFullName = findViewById(R.id.signUpFullname);
        signUpPassword = findViewById(R.id.signUpPassword);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        birthDatePicker = findViewById(R.id.birthDate);
        birthDate = Calendar.getInstance();
        signUp = findViewById(R.id.buttonSignUp);
        auth = FirebaseAuth.getInstance();

        Calendar calendar = Calendar.getInstance();
        birthDatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            public void onDateChanged(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
                birthDate.set(year, monthOfYear, dayOfMonth);
            }
        });


        signUpBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fullname = signUpFullName.getText().toString().trim();
                String email = signUpEmail.getText().toString().trim();
                String password = signUpPassword.getText().toString().trim();
                Calendar holder = Calendar.getInstance();


                if (fullname.isEmpty()) {
                    signUpFullName.setError("Campo obrigatório");
                }
                else if (email.isEmpty()) {
                    signUpEmail.setError("Campo obrigatório");
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    signUpEmail.setError("Email Inválido");
                }
                else if (password.isEmpty()) {
                    signUpPassword.setError("Campo obrigatório");
                }
                else if (password.length() < 6) {
                    signUpPassword.setError("Password Inválida");
                }
                else if(genderRadioGroup.getCheckedRadioButtonId() == -1){
                    Toast.makeText(SignUpActivity.this, "Selecionar Género", Toast.LENGTH_SHORT).show();
                }
                else if((holder.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR) <= 17)){
                    Toast.makeText(SignUpActivity.this, "Data de Nascimento Inválida", Toast.LENGTH_SHORT).show();
                }
                else{
                    RadioButton genderOption = findViewById(genderRadioGroup.getCheckedRadioButtonId());
                    String gender = genderOption.getText().toString();
                    String date = birthDate.get(Calendar.DAY_OF_MONTH) + "/" + (birthDate.get(Calendar.MONTH) + 1) + "/" + birthDate.get(Calendar.YEAR);

                    Intent intent = new Intent(SignUpActivity.this, AddContactsActivity.class);
                    intent.putExtra("FullName", fullname);
                    intent.putExtra("Email", email);
                    intent.putExtra("Password", password);
                    intent.putExtra("Gender", gender);
                    intent.putExtra("BirthDate", date);

                    startActivity(intent);
                }
            }
        });


        LoginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });








    }
}
