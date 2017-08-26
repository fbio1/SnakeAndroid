package br.eaj.tads.acobrona;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Principal extends AppCompatActivity {
    Button continuar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        continuar = (Button) findViewById(R.id.continuar);
        continuar.setVisibility(View.GONE);
    }

    public void botao1(View v) {
        Intent intent = new Intent(this, Tabuleiro.class);
        Bundle params = getIntent().getExtras();
        if (params == null){
            startActivity(intent);
        }else{
            int n = params.getInt("tam");
            int dif = params.getInt("difficult");
            intent.putExtra("tam", n);
            intent.putExtra("difficult", dif);
            startActivity(intent);
        }
    }

    public void botao2(View v) {
        Intent intent = new Intent(this, Configurar.class);
        startActivity(intent);
    }

    public void botao3(View v) {
        finish();
    }

    public void botao4(View v){

    }

    @Override
    protected void onStop() {
        super.onStop();
        continuar.setVisibility(View.VISIBLE);
    }
}
