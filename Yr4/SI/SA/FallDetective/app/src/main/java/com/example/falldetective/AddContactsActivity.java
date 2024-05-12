package com.example.falldetective;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class AddContactsActivity extends AppCompatActivity implements ContactListViewInterface{

    List<ContactInfoModel> contacts = new ArrayList<>();
    ContactListViewAdapter adapter;
    private String fullName, email, password, gender, birthDate;
    private RecyclerView recyclerContacts;
    private ImageView addContactsBack;
    private Button submit, addContact;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);

        addContactsBack = findViewById(R.id.addContactsBack);
        addContact = findViewById(R.id.addContact);
        recyclerContacts = findViewById(R.id.contactsRecyclerView);
        submit = findViewById(R.id.submit);

        fullName = getIntent().getStringExtra("FullName");
        email = getIntent().getStringExtra("Email");
        password = getIntent().getStringExtra("Password");
        gender = getIntent().getStringExtra("Gender");
        birthDate = getIntent().getStringExtra("BirthDate");

        adapter = new ContactListViewAdapter(this, this, contacts);
        recyclerContacts.setAdapter(adapter);
        recyclerContacts.setLayoutManager(new LinearLayoutManager(this));

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");

        addContactsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AddContactsActivity.this);
                builder.setTitle("Aviso!");
                builder.setMessage("Tem a certeza que pretende cancelar o registo de perfil?");
                builder.setCancelable(false);
                builder.setPositiveButton("Sim", (DialogInterface.OnClickListener) (dialog, which) -> {
                    startActivity(new Intent(AddContactsActivity.this, MainActivity.class));
                });

                builder.setNegativeButton("Não", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.dismiss();
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });


        addContact.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                View addContact = LayoutInflater.from(AddContactsActivity.this).inflate(R.layout.add_contact_dialog, null);
                TextInputEditText contactName = addContact.findViewById(R.id.contactName);
                TextInputEditText contactNumber = addContact.findViewById(R.id.contactNumber);

                AlertDialog.Builder builder = new AlertDialog.Builder(AddContactsActivity.this)
                        .setTitle("Adicionar Contacto")
                        .setView(addContact)
                        .setCancelable(false)
                        .setPositiveButton("Adicionar", (DialogInterface.OnClickListener) (dialog, which) -> {
                            String name = contactName.getText().toString().trim();
                            String number = contactNumber.getText().toString().trim();

                            if (name.isEmpty() || number.isEmpty() || !checkNumbersOnString(number) ){
                                Toast.makeText(AddContactsActivity.this, "Contacto Inválido", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                contacts.add(new ContactInfoModel(number, name));
                                adapter.notifyItemInserted(contacts.size()-1);
                            }

                        })
                        .setNegativeButton("Cancelar", (DialogInterface.OnClickListener) (dialog, which) -> {
                            dialog.dismiss();
                        });


                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (contacts.size() == 0){
                    Toast.makeText(AddContactsActivity.this, "Número mínimo de contactos: 1", Toast.LENGTH_SHORT).show();
                }
                else{
                    UserModel user = new UserModel(fullName, email, gender, birthDate, contacts);
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = task.getResult().getUser();
                            reference.child(firebaseUser.getUid()).setValue(user);

                            Toast.makeText(AddContactsActivity.this, "Registo completo", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddContactsActivity.this, MainPageActivity.class));
                        } else {
                            Toast.makeText(AddContactsActivity.this, "Não foi possível efetuar o registo" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    static boolean checkNumbersOnString(String input) {
        return input.matches("\\d+");
    }

    @Override
    public void onItemClick(int position) {

    }
}