package br.eaj.tads.acobrona;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Principal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void botao1(View v) {
        Intent intent = new Intent(this, Tabuleiro.class);
        startActivity(intent);
    }

    public void botao2(View v) {
        Intent intent = new Intent(this, Configurar.class);

        startActivity(intent);
    }

    public void botao3(View v) {
        finish();
    }
}
