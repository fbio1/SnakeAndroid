package br.eaj.tads.acobrona;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class Activity2 extends AppCompatActivity {
    private Boolean running = true;
    int n;
    GridLayout layout;
    ImageView tabuleiro[][];
    ArrayList<int[]> cobra = new ArrayList<>();
    int direcao[] = new int[2];
    int posicao[] = new int[2];
    int fruit[] = new int[2];
    int pontuacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);

        final ImageButton cima = (ImageButton) findViewById(R.id.imageButton4);
        final ImageButton baixo = (ImageButton) findViewById(R.id.imageButton3);
        final ImageButton direito = (ImageButton) findViewById(R.id.imageButton2);
        final ImageButton esquerda = (ImageButton) findViewById(R.id.imageButton);

        cima.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                direcao[0] = -1;
                direcao[1] = 0;
                baixo.setEnabled(false);
                direito.setEnabled(true);
                esquerda.setEnabled(true);
                cima.setEnabled(false);
            }
        });

        baixo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                direcao[0] = 1;
                direcao[1] = 0;
                cima.setEnabled(false);
                baixo.setEnabled(false);
                direito.setEnabled(true);
                esquerda.setEnabled(true);
            }
        });

        direito.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                direcao[0] = 0;
                direcao[1] = 1;
                esquerda.setEnabled(false);
                cima.setEnabled(true);
                direito.setEnabled(false);
                baixo.setEnabled(true);
            }
        });

        esquerda.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                direcao[0] = 0;
                direcao[1] = -1;
                direito.setEnabled(false);
                cima.setEnabled(true);
                esquerda.setEnabled(false);
                baixo.setEnabled(true);
            }
        });

        layout = (GridLayout) findViewById(R.id.gridlayout);

        Bundle recuperarDados = getIntent().getExtras();
        if (recuperarDados == null) {
            n = 30;
            layout.setColumnCount(n);
            layout.setRowCount(n);
            tabuleiro = new ImageView[n][n];
            for (int i = 0; i<n;i++){
                for (int j = 0; j<n;j++){
                    LayoutInflater inflater = LayoutInflater.from(this);
                    ImageView image = (ImageView) inflater.inflate(R.layout.inflate_image_view, layout, false);
                    tabuleiro[i][j] = image;
                    layout.addView(image);
                }
            }
        }else {
            n = recuperarDados.getInt("tam");
            layout.setColumnCount(n);
            layout.setRowCount(n);
            tabuleiro = new ImageView[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    LayoutInflater inflater = LayoutInflater.from(this);
                    ImageView image = (ImageView) inflater.inflate(R.layout.inflate_image_view, layout, false);
                    tabuleiro[i][j] = image;
                    layout.addView(image);
                }
            }
        }
       criandoDemonio();
    }

    public void criandoDemonio(){
        preto(tabuleiro[n/2][n/2]);
//      direcao que a cobra tem (pra cima)
        direcao[0] = -1;
        direcao[1] = 0;
        int posicao[] = new int[2];
        posicao[0] = n/2;
        posicao[1] = n/2;
        cobra.add(posicao);
        fruta();
        movimento(direcao);//inicia o moviemnto da cobra
    }

    public void play(View v){
        running = true;
        movimento(direcao);
    }

    public void pause(View v){
        running = false;
        movimento(direcao);
    }

    public void movimento(final int[] direcao){
        final Handler handler = new Handler();
        new  Thread(new Runnable(){
            public void run(){
                while(running){
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();                    }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //LIMPAR A TELA COM FOR DE COBRA INT[] COBRA:COBRA{BRANCO}
                        Log.i("TESTES_", "tamanho"+cobra.size());
                        for (int i = 0; i < cobra.size(); i++) {
                            int[] pos = cobra.get(i);
                            Log.i("TESTES_", "pintarbranco:" +pos[0]+pos[1]);
                            branco(tabuleiro[pos[0]][pos[1]]);
                        }
                        cabecinha();
                        //move o corpo
                        //if i !=0
//                            cobra.get(i)[0] = cobra.get(i-1)[0];
//                            cobra.get(i)[1] = cobra.get(i-1)[1];

                        for (int i = 0; i < cobra.size(); i++) {
                            int[] pos = cobra.get(i);
                            preto(tabuleiro[pos[0]][pos[1]]);
                        }
                    }
                });
                }
            }
        }).start();
    }


    public void cabecinha(){//metodo de fazer a cobra mecher
        posicao = cobra.get(0);
        posicao[0] = posicao[0] + direcao[0];
        posicao[1] = posicao[1] + direcao[1];
        posicao = checkposicao(posicao);
        cobra.get(0)[0] = posicao[0];
        cobra.get(0)[1] = posicao[1];
        comendo(posicao);
    }

    private int[] checkposicao(int[] posicao) {
        if(posicao[0] == n-1){
            posicao[0] = 0;
        }
        if(posicao[1] == n-1){
            posicao[1] = 0;
        }
        if(posicao[0] == -1){
            posicao[0] = n-1;
        }
        if(posicao[1] == -1){
            posicao[1] = n-1;
        }
        return posicao;
    }

    public void comendo(int[] pos){
        if(pos[0] == fruit[0] && pos[1] == fruit[1]){
            vermelho(tabuleiro[pos[0]][pos[1]]);
            fruta();
            TextView tv = (TextView) findViewById(R.id.textView3);
            pontuacao += 50;
            tv.setText(""+ pontuacao);
//            cobra.add(novaposicao);
        }
    }

    public void fruta(){
        fruit[0] = new Random().nextInt(n-1);
        fruit[1] = new Random().nextInt(n-1);
        vermelho(tabuleiro[fruit[0]][fruit[1]]);
    }

    public void vermelho(ImageView imageView){
        imageView.setImageResource(R.drawable.vermelho);
    }

    public void branco(ImageView imageView){
        imageView.setImageResource(R.drawable.branco);
    }

    public void preto(ImageView imageView){
        imageView.setImageResource(R.drawable.preto);
    }
}
