package com.example.falldetective;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class AddContactDialog extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View addContact = inflater.inflate(R.layout.add_contact_dialog, null);



        alert.setTitle("Adicionar Contacto")
                .setView(addContact)
                .setCancelable(false)
                .setPositiveButton("Adicionar", (DialogInterface.OnClickListener) (dialog, which) -> {

                })
                .setNegativeButton("Cancelar", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.dismiss();
                });

        return super.onCreateDialog(savedInstanceState);
    }
}
