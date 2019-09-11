package com.example.listadetarefas.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.listadetarefas.R;
import com.example.listadetarefas.model.Usuario;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class UsuarioActivity extends AppCompatActivity {

    private TextView id, nome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        id = findViewById(R.id.id_user);
        nome = findViewById(R.id.nome_user);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null){
            nome.setText( account.getDisplayName());
            id.setText(account.getId());
        }
    }
}
