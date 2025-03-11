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

    private Aluno aluno = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nome = findViewById(R.id.editTextText);
        cpf = findViewById(R.id.editTextText2);
        telefone = findViewById(R.id.editTextText3);

        dao = new AlunoDAO(this);

        Intent it = getIntent(); //pega intenção
        if(it.hasExtra("aluno")){
            aluno = (Aluno) it.getSerializableExtra("aluno");
            nome.setText(aluno.getNome().toString());
            cpf.setText(aluno.getCPF());
            telefone.setText(aluno.getTelefone());
        }
    }

    public void salvar(View view){
        String nomeDigitado = nome.getText().toString().trim();
        String cpfDigitado = cpf.getText().toString().trim();
        String telefoneDigitado = telefone.getText().toString().trim();
        // Verifica se os campos estão vazios
        if (nomeDigitado.isEmpty() || cpfDigitado.isEmpty() || telefoneDigitado.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }
        // Validação do CPF (verifica se o formato e os dígitos são válidos)
        System.out.println("CPF antes da validação: " + cpfDigitado);
        if (!dao.isCPF(cpfDigitado)) {
            Toast.makeText(this, "CPF inválido. Digite novamente.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Se for cadastrar novo aluno ou Se for atualizar os dados ignora o CPF se for igual do próprio aluno
        //Se o aluno atualizar um cpf diferente dai sim será verificado
        if(aluno == null || !cpfDigitado.equals(aluno.getCPF())){
            // verifica se o CPF já existe no banco
            if (dao.cpfDuplicado(cpfDigitado)) {
                Toast.makeText(this, "CPF duplicado. Insira um CPF diferente.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (aluno == null) {
            // Criar objeto Aluno
            Aluno aluno = new Aluno();
            aluno.setNome(nomeDigitado);
            aluno.setCPF(cpfDigitado);
            aluno.setTelefone(telefoneDigitado);
            // Inserir aluno no banco de dados
            long id = dao.inserir(aluno);
            if (id != -1) {
                Toast.makeText(this, "Aluno inserido com id: " + id, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erro ao inserir aluno. Tente novamente.", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            // Atualização de um aluno existente
            aluno.setNome(nomeDigitado);
            aluno.setCPF(cpfDigitado);
            aluno.setTelefone(telefoneDigitado);
            dao.atualizar(aluno);
            Toast.makeText(this, "Aluno atualizado com sucesso!", Toast.LENGTH_SHORT).show();
        }
        // Fecha a tela de cadastro e volta para a listagem
        finish();
    }


    public void irParaListar(View view){
        Intent intent = new Intent(this, ListarAlunos.class);
        startActivity(intent);
    }
}