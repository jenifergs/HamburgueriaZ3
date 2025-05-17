package com.example.hamburgueriaz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

public class MainActivity extends AppCompatActivity {

    private EditText edtNomeCliente;
    private CheckBox checkBacon, checkQueijo, checkOvo;
    private Button btnAdicionar, btnSubtrair, btnEnviarPedido;
    private TextView txtQuantidade, txtPrecoTotal;
    private MaterialToolbar topAppBar;

    private int quantidade = 1;
    private final double precoBase = 20.0;
    private final double precoAdicional = 2.5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topAppBar = findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);

        edtNomeCliente = findViewById(R.id.edtNomeCliente);
        checkBacon = findViewById(R.id.checkBacon);
        checkQueijo = findViewById(R.id.checkQueijo);
        checkOvo = findViewById(R.id.checkOvo);
        btnAdicionar = findViewById(R.id.btnAdicionar);
        btnSubtrair = findViewById(R.id.btnSubtrair);
        txtQuantidade = findViewById(R.id.txtQuantidade);
        txtPrecoTotal = findViewById(R.id.txtPrecoTotal);
        btnEnviarPedido = findViewById(R.id.btnEnviarPedido);

        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantidade++;
                atualizarQuantidade();
                atualizarPrecoTotal();
            }
        });

        btnSubtrair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantidade > 1) {
                    quantidade--;
                    atualizarQuantidade();
                    atualizarPrecoTotal();
                }
            }
        });

        View.OnClickListener checkboxListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                atualizarPrecoTotal();
            }
        };

        checkBacon.setOnClickListener(checkboxListener);
        checkQueijo.setOnClickListener(checkboxListener);
        checkOvo.setOnClickListener(checkboxListener);

        btnEnviarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarPedido();
            }
        });

        atualizarQuantidade();
        atualizarPrecoTotal();
    }

    private void atualizarQuantidade() {
        txtQuantidade.setText(String.valueOf(quantidade));
    }

    private void atualizarPrecoTotal() {
        double adicionaisValor = 0.0;
        if (checkBacon.isChecked()) adicionaisValor += 2.0;     // Bacon R$ 2,00
        if (checkQueijo.isChecked()) adicionaisValor += 2.0;    // Queijo R$ 2,00
        if (checkOvo.isChecked()) adicionaisValor += 3.0;       // Onion rings R$ 3,00

        double total = quantidade * (precoBase + adicionaisValor);
        txtPrecoTotal.setText(String.format("R$ %.2f", total));
    }

    private double calcularPrecoTotal(int quantidade, boolean temBacon, boolean temQueijo, boolean temOnionRings) {
        double precoBase = 20.0;
        double precoBacon = 2.0;
        double precoQueijo = 2.0;
        double precoOnionRings = 3.0;

        double adicionaisValor = 0.0;
        if (temBacon) adicionaisValor += precoBacon;
        if (temQueijo) adicionaisValor += precoQueijo;
        if (temOnionRings) adicionaisValor += precoOnionRings;

        return quantidade * (precoBase + adicionaisValor);
    }


    private void enviarPedido() {
        String nome = edtNomeCliente.getText().toString().trim();
        if (nome.isEmpty()) {
            Toast.makeText(this, "Por favor, insira o nome do cliente.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean temBacon = checkBacon.isChecked();
        boolean temQueijo = checkQueijo.isChecked();
        boolean temOnionRings = checkOvo.isChecked();

        double precoFinal = calcularPrecoTotal(quantidade, temBacon, temQueijo, temOnionRings);

        String mensagem = "Nome do cliente: " + nome + "\n" +
                "Tem Bacon? " + (temBacon ? "Sim" : "Não") + "\n" +
                "Tem Queijo? " + (temQueijo ? "Sim" : "Não") + "\n" +
                "Tem Onion Rings? " + (temOnionRings ? "Sim" : "Não") + "\n" +
                "Quantidade: " + quantidade + "\n" +
                String.format("Preço final: R$ %.2f", precoFinal);

        // Cria o Intent para enviar e-mail
        Intent intentEmail = new Intent(Intent.ACTION_SENDTO);
        intentEmail.setData(android.net.Uri.parse("mailto:"));
        intentEmail.putExtra(Intent.EXTRA_SUBJECT, "Pedido de " + nome);
        intentEmail.putExtra(Intent.EXTRA_TEXT, mensagem);

        // Verifica se existe algum app para tratar o Intent e inicia
        if (intentEmail.resolveActivity(getPackageManager()) != null) {
            startActivity(intentEmail);
        } else {
            Toast.makeText(this, "Nenhum aplicativo de e-mail encontrado.", Toast.LENGTH_SHORT).show();
        }
    }


}