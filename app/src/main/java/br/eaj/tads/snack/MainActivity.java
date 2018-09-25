package br.eaj.tads.snack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAMGRID = "TAMGRID";
    private static final String DIFICULDADE = "DIFICULDADE";
    private static final String PONTUACAO = "PONTUACAO";
//    private static final String TAMANHOCOBRA = "TAMANHOCOBRA";
//    private static final String RECORD = "RECORD";
    private static final String CONTINUAR = "CONTINUAR";
    private static final String SALVA = "SALVA";
    private static final int JOGO = 11;
    private static final int CONF = 22;

    Button continuar;
    boolean cont;
    int n, dif, pontuacao, record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        continuar = (Button) findViewById(R.id.continuar);

        SharedPreferences salvo = getSharedPreferences(SALVA, MODE_PRIVATE);

        dif = salvo.getInt(DIFICULDADE, 250);
        n = salvo.getInt(TAMGRID, 28);
        pontuacao = salvo.getInt(PONTUACAO, 0);
//        record = salvo.getInt(RECORD,0);
        cont = salvo.getBoolean(CONTINUAR, false);

        if (cont){
            continuar.setVisibility(View.VISIBLE);
        }else{
            continuar.setVisibility(View.GONE);
        }

//        MediaPlayer mp = MediaPlayer.create(this, R.raw.musica);
//        mp.start();
//        mp.isLooping();
    }

    public void start(View v) {
        Intent intent = new Intent(this, Tabuleiro.class);
        Bundle params = new Bundle();
//        params.putInt("record", record);
        params.putInt("difficult", dif);
        params.putInt("tam", n);
        intent.putExtras(params);
        startActivityForResult(intent, JOGO);
    }

    public void continuar(View v){
        Intent intent = new Intent(this, Tabuleiro.class);
        Bundle params = new Bundle();
//        params.putInt("record", record);
        params.putInt("pontuacao", pontuacao);
        params.putInt("difficult", dif);
        params.putInt("tam", n);
        intent.putExtras(params);
        startActivityForResult(intent, JOGO);
    }

    public void config(View v) {
        Intent intent = new Intent(this, Configurar.class);
        Bundle params = new Bundle();
        params.putInt("difficult", dif);
        params.putInt("tam", n);
        intent.putExtras(params);
        startActivityForResult(intent, CONF);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CONF) {
            if (resultCode == RESULT_OK) {
                int dif1 = data.getIntExtra("difficult", 250);
                int tam1 = data.getIntExtra("tam", 28);
                if(dif1 != dif || tam1 != n){
                    cont = false;
                    continuar.setVisibility(View.GONE);
                }
                dif = dif1;
                n = tam1;
            }
        }else if(requestCode == JOGO){
            if (resultCode == RESULT_OK){
                Toast.makeText(this, "Jogo salvo com sucesso", Toast.LENGTH_SHORT).show();
                pontuacao = data.getIntExtra("pontuacao", 0);
                cont = true;
                continuar.setVisibility(View.VISIBLE);
            }
        }
    }

//    public void sair(View v) {
//        finish();
//    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences(SALVA, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
//        editor.putInt(RECORD, record);
        editor.putInt(DIFICULDADE, dif);
        editor.putInt(TAMGRID, n);
        editor.putInt(PONTUACAO, pontuacao);
        editor.putBoolean(CONTINUAR, cont);
        editor.commit();
    }
}
