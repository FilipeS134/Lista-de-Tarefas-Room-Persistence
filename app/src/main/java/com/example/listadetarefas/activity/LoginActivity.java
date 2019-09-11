package com.example.listadetarefas.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.listadetarefas.R;
import com.example.listadetarefas.config.ConfiguracaoFirebase;
import com.example.listadetarefas.model.Usuario;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;


public class LoginActivity extends AppCompatActivity {


    private int RC_SIGN_IN = 0;
    private EditText email, senha;
    private CircularProgressButton logar;
    private TextView textCriarConta;
    private FirebaseAuth autenticacao;
    private Usuario usuario;
    private SignInButton sign;
    private GoogleSignInClient googleSignInClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.editText_emailLogar);
        senha = findViewById(R.id.editText_senhaLogar);
        logar = findViewById(R.id.button_logar);
        textCriarConta = findViewById(R.id.textView_criarConta);
        sign = findViewById(R.id.sign_in_button);

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            }
        });


        logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logar.startAnimation();
                login();

            }
        });


       textCriarConta.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
               startActivity(intent);

           }
       });

        GoogleSignInOptions glogin = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, glogin);


    }

    private void signIn() {
        Intent signIn = googleSignInClient.getSignInIntent();
        startActivityForResult(signIn, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            abrirTelaPrincipal();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Erro", "signInResult:failed code=" + e.getStatusCode());
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        verificarUserLogado();

    }

    public void login(){
            String textoEmail = email.getText().toString();
            String textoSenha = senha.getText().toString();

            if (!textoEmail.isEmpty()){
                if (!textoSenha.isEmpty()){

                    usuario = new Usuario();
                    usuario.setEmail(textoEmail);
                    usuario.setSenha(textoSenha);
                    validarLogin();

                }else {
                    Toast.makeText(getApplicationContext(), "Preencha sua Senha!", Toast.LENGTH_SHORT).show();
                    logar.stopAnimation();
                }

            }else {
                Toast.makeText(getApplicationContext(), "Preencha seu Email!", Toast.LENGTH_SHORT).show();
                logar.revertAnimation();
            }
        }

    public void validarLogin(){
        autenticacao = ConfiguracaoFirebase.getAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            logar.stopAnimation();
                            abrirTelaPrincipal();
                        }else {
                            String excecao = "";
                            try {
                                throw task.getException();
                            }catch (FirebaseAuthInvalidUserException e){
                                logar.revertAnimation();
                                excecao = "Usuário não está cadastrado.";
                            }catch (FirebaseAuthInvalidCredentialsException e){
                                logar.revertAnimation();
                                excecao = "E-mail ou senha não correspondem a um usuário cadastrado!";
                            }catch (Exception e){
                                logar.revertAnimation();
                                excecao = "Erro ao logar " + e.getMessage();
                                e.printStackTrace();
                            }

                            Toast.makeText(getApplicationContext(), excecao, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void verificarUserLogado(){
        autenticacao = ConfiguracaoFirebase.getAutenticacao();
        if (autenticacao.getCurrentUser() != null){
            abrirTelaPrincipal();
        }
    }

    public void abrirTelaPrincipal(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}
