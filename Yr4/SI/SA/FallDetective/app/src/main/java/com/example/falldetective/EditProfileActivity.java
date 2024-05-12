package com.example.falldetective;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditProfileActivity extends AppCompatActivity {

        private ImageView signUpBack;
        private TextInputEditText signUpEmail, signUpFullName;
        private Button signUp;
        private RadioGroup genderRadioGroup;
        private DatePicker birthDatePicker;
        private Calendar birthDate;
        private FirebaseAuth auth;
        private FirebaseDatabase database;
        private DatabaseReference reference;
        private UserModel user;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_edit_profile);

            signUpBack = findViewById(R.id.buttonSignUpBack);
            signUpEmail = findViewById(R.id.signUpEmail);
            signUpFullName = findViewById(R.id.signUpFullname);
            genderRadioGroup = findViewById(R.id.genderRadioGroup);
            birthDatePicker = findViewById(R.id.birthDate);
            birthDate = Calendar.getInstance();
            signUp = findViewById(R.id.buttonSignUp);
            auth = FirebaseAuth.getInstance();
            database = FirebaseDatabase.getInstance();
            reference = database.getReference("users");


            if (auth.getCurrentUser() != null) {
                String userId = auth.getCurrentUser().getUid();

                reference.child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            DataSnapshot dataSnapshot = task.getResult();
                            if (dataSnapshot.exists()) {
                                user = dataSnapshot.getValue(UserModel.class);
                                signUpFullName.setText(user.fullName);
                                signUpEmail.setText(user.email);

                                if (user.gender.equals("M")) {
                                    genderRadioGroup.check(R.id.gender_M);
                                } else if (user.gender.equals("F")) {
                                    genderRadioGroup.check(R.id.gender_F);
                                } else{
                                    genderRadioGroup.check(R.id.gender_Outro);
                                }

                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                Date date = null;
                                try {
                                    date = sdf.parse(user.birthDate);
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(date);

                                birthDate.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                                birthDatePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        birthDate.set(year, monthOfYear, dayOfMonth);
                                    }
                                });

                            } else {
                                Toast.makeText(EditProfileActivity.this, "No user data", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
                            }
                        } else {
                            Toast.makeText(EditProfileActivity.this, "Erro", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
                        }

                    }
                });
            } else {
                startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
            }

            signUpBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(EditProfileActivity.this, MainPageActivity.class));
                }
            });


            signUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String fullname = signUpFullName.getText().toString().trim();
                    String email = signUpEmail.getText().toString().trim();
                    Calendar holder = Calendar.getInstance();


                    if (fullname.isEmpty()) {
                        signUpFullName.setError("Campo obrigatório");
                    } else if (email.isEmpty()) {
                        signUpEmail.setError("Campo obrigatório");
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        signUpEmail.setError("Email Inválido");
                    } else if (genderRadioGroup.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(EditProfileActivity.this, "Selecionar Género", Toast.LENGTH_SHORT).show();
                    } else if ((holder.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR) <= 17)) {
                        Toast.makeText(EditProfileActivity.this, "Data de Nascimento Inválida", Toast.LENGTH_SHORT).show();
                    } else {
                        RadioButton genderOption = findViewById(genderRadioGroup.getCheckedRadioButtonId());
                        String gender = genderOption.getText().toString();
                        String date = birthDate.get(Calendar.DAY_OF_MONTH) + "/" + (birthDate.get(Calendar.MONTH) + 1) + "/" + birthDate.get(Calendar.YEAR);

                        UserModel user_updated = new UserModel(fullname, email, gender, date, user.getContacts());

                        reference.child(auth.getCurrentUser().getUid()).setValue(user_updated);

                        Toast.makeText(EditProfileActivity.this, "Perfil atualizado com sucesso", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(EditProfileActivity.this, MainPageActivity.class));
                    }
                }
            });

        }
}