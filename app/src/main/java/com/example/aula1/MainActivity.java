package com.example.aula1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private EditText nome;
    private EditText cpf;
    private EditText telefone;

    private AlunoDAO dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nome = findViewById(R.id.editTextText);
        cpf = findViewById(R.id.editTextText2);
        telefone = findViewById(R.id.editTextText3);

        dao =new AlunoDAO(this);
    }

    public void salvar(View view){
        Aluno a = new Aluno(
                nome.getText().toString(),
                cpf.getText().toString(),
                telefone.getText().toString()
            );

        if (a.getNome().isEmpty() || a.getCPF().isEmpty() || a.getTelefone().isEmpty()){
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!dao.isCPF(a.getCPF())){
            Toast.makeText(this, "CPF Invalido", Toast.LENGTH_SHORT).show();
            return;
        }
        if (dao.cpfDuplicado(a.getCPF())){
            Toast.makeText(this, "CPF ja cadastrado", Toast.LENGTH_SHORT).show();
            return;
        }
        long id = dao.inserir(a);
        Toast.makeText(this, "Aluno inserido com id: "+ id, Toast.LENGTH_SHORT).show();
    }

    public void irParaListar(View view){
        Intent intent = new Intent(this, ListarAlunos.class);
        startActivity(intent);
    }
}