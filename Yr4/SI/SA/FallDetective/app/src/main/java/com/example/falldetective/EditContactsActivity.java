package com.example.falldetective;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class EditContactsActivity extends AppCompatActivity implements ContactListViewInterface {

    List<ContactInfoModel> contacts = new ArrayList<>();
    ContactListViewAdapter adapter;
    private RecyclerView recyclerContacts;
    private ImageView editContactsBack;
    private Button submit, addContact;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contacts);

        editContactsBack = findViewById(R.id.editContactsBack);
        addContact = findViewById(R.id.addContact);
        recyclerContacts = findViewById(R.id.contactsRecyclerView);
        submit = findViewById(R.id.submit);

        adapter = new ContactListViewAdapter(this, this, contacts);
        recyclerContacts.setAdapter(adapter);
        recyclerContacts.setLayoutManager(new LinearLayoutManager(this));

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
                            if (user != null) {
                                adapter.setItems(user.getContacts());
                                adapter.notifyDataSetChanged();
                                contacts = user.getContacts();
                            }
                        } else {
                            Toast.makeText(EditContactsActivity.this, "No user data", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(EditContactsActivity.this, MainActivity.class));
                        }
                    } else {
                        Toast.makeText(EditContactsActivity.this, "Erro", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditContactsActivity.this, MainActivity.class));
                    }

                }
            });
        } else {
            startActivity(new Intent(EditContactsActivity.this, MainActivity.class));
        }

        addContact.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                View addContact = LayoutInflater.from(EditContactsActivity.this).inflate(R.layout.add_contact_dialog, null);
                TextInputEditText contactName = addContact.findViewById(R.id.contactName);
                TextInputEditText contactNumber = addContact.findViewById(R.id.contactNumber);

                AlertDialog.Builder builder = new AlertDialog.Builder(EditContactsActivity.this)
                        .setTitle("Adicionar Contacto")
                        .setView(addContact)
                        .setCancelable(false)
                        .setPositiveButton("Adicionar", (DialogInterface.OnClickListener) (dialog, which) -> {
                            String name = contactName.getText().toString().trim();
                            String number = contactNumber.getText().toString().trim();

                            if (name.isEmpty() || number.isEmpty() || !checkNumbersOnString(number) ){
                                Toast.makeText(EditContactsActivity.this, "Contacto Inválido", Toast.LENGTH_SHORT).show();
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

        editContactsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditContactsActivity.this, MainPageActivity.class));
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user.setContacts(contacts);
                reference.child(auth.getCurrentUser().getUid()).setValue(user);
                Toast.makeText(EditContactsActivity.this, "Os contactos de emergência foram atualizados", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(EditContactsActivity.this, MainPageActivity.class));
            }
        });
    }


    @Override
    public void onItemClick(int position) {

        View addContact = LayoutInflater.from(EditContactsActivity.this).inflate(R.layout.add_contact_dialog, null);
        TextInputEditText contactName = addContact.findViewById(R.id.contactName);
        TextInputEditText contactNumber = addContact.findViewById(R.id.contactNumber);

        contactName.setText(contacts.get(position).name);
        contactNumber.setText(contacts.get(position).number);

        AlertDialog.Builder builder = new AlertDialog.Builder(EditContactsActivity.this)
                .setTitle("Editar Contacto")
                .setView(addContact)
                .setPositiveButton("Atualizar", (DialogInterface.OnClickListener) (dialog, which) -> {
                    String name = contactName.getText().toString().trim();
                    String number = contactNumber.getText().toString().trim();

                    if (name.isEmpty() || number.isEmpty() || !checkNumbersOnString(number) ){
                        Toast.makeText(EditContactsActivity.this, "Contacto Inválido", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        contacts.set(position, new ContactInfoModel(number, name));
                        adapter.notifyItemChanged(position);
                    }

                })
                .setNegativeButton("Eliminar Contacto", (DialogInterface.OnClickListener) (dialog, which) -> {
                    if (contacts.size() == 1){
                        Toast.makeText(EditContactsActivity.this, "Número mínimo de contactos: 1", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        contacts.remove(position);
                        adapter.notifyItemRemoved(position);
                    }
                });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    static boolean checkNumbersOnString(String input) {
        return input.matches("\\d+");
    }
}