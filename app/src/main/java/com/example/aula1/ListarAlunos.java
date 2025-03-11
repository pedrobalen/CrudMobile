package com.example.aula1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class ListarAlunos extends AppCompatActivity {
    private ListView listView;
    private AlunoDAO alunoDAO;
    private List<Aluno> alunos;
    private List<Aluno> alunosFiltrados = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_alunos);

        listView = findViewById(R.id.lista_alunos);
        alunoDAO = new AlunoDAO(this);
        alunos = alunoDAO.obterTodos();
        alunosFiltrados.addAll(alunos);

        ArrayAdapter<Aluno> adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alunos);
        listView.setAdapter(adaptador);
        registerForContextMenu(listView);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        // Chama o método da superclasse (neste caso, o método onCreateContextMenu da classe pai).
        // Isso é importante para garantir que qualquer comportamento padrão do método na superclasse
        // (por exemplo, qualquer configuração padrão de menu que a superclasse realiza) seja executado antes
        // de você adicionar suas próprias ações ao menu.
        super.onCreateContextMenu(menu, v, menuInfo);

        // Cria um objeto MenuInflater, que é responsável por inflar (converter um arquivo XML de menu em um objeto Menu)
        // o menu de contexto a partir de um arquivo XML de menu que você criou anteriormente.
        MenuInflater i = getMenuInflater();

        // O método inflate do MenuInflater é usado para inflar o menu de contexto.
        // Aqui, você está especificando o recurso XML (R.menu.menu_contexto) que define as opções de menu
        // que aparecerão quando um item da lista for pressionado.
        i.inflate(R.menu.menu_contexto, menu); //Aqui coloca o nome do menu que havia sido configurado
    }

    public void voltar(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void excluir(MenuItem item){
        //pegar qual a posicao do item da lista que eu selecionei para excluir
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Aluno alunoExcluir = alunosFiltrados.get(menuInfo.position);
        //mensagem perguntando se quer realmente excluir
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Atenção")
                .setMessage("Realmente deseja excluir o aluno?")
                .setNegativeButton("NÃO",null)
                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alunosFiltrados.remove(alunoExcluir);
                        alunos.remove(alunoExcluir);
                        alunoDAO.excluir(alunoExcluir);
                        listView.invalidateViews();
                    }
                } ).create(); //criar a janela
        dialog.show(); //manda mostrar a janela
    }

    public void atualizar(MenuItem item){
        //mesma lógica do excluir porque o botão de menu é o mesmo
        //pegar qual a posicao do item da lista que eu selecionei para atualizar
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Aluno alunoAtualizar = alunosFiltrados.get(menuInfo.position);
        //Ao selecionar atualizar, abrir a janela de cadastro e enviar esse aluno para lá
        Intent it = new Intent(this, MainActivity.class); //Nosso cadastrar se chama 'MainActivity’
        //será preenchido com os dados do aluno que quer atualizar, podemos alterar e salvar
        it.putExtra("aluno",alunoAtualizar);
        startActivity(it);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recarrega todos os alunos do banco de dados
        alunos = alunoDAO.obterTodos();
        // Limpa a lista filtrada e adiciona os novos alunos
        alunosFiltrados.clear();
        alunosFiltrados.addAll(alunos);
        // Atualiza o adapter da ListView para refletir os novos dados
        ArrayAdapter<Aluno> adaptador = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, alunosFiltrados);
        listView.setAdapter(adaptador);
    }
}