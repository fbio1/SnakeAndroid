package br.eaj.tads.snack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GameOver extends AppCompatActivity {
    String pontos;
    TextView pontuacao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);
        pontos = getIntent().getStringExtra("pontos");
        pontuacao = (TextView) findViewById(R.id.pontos);
        pontuacao.setText(pontos);
    }

    public void finish(View v){
        finish();
    }
}
