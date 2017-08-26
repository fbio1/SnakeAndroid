package br.eaj.tads.acobrona;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Config;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class Tabuleiro extends AppCompatActivity {
    private static final String PREF_SNAKE = "snake";
    private Boolean running = true;
    int n;
    int difficult;
    GridLayout layout;
    ImageView tabuleiro[][];
    ArrayList<int[]> cobra = new ArrayList<>();
    int direcao[] = new int[2];
    int posicao[] = new int[2];
    int fruit[] = new int[2];
    int pontuacao;
    Context c = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabuleiro);

        final ImageButton cima = (ImageButton) findViewById(R.id.imageButton4);
        final ImageButton baixo = (ImageButton) findViewById(R.id.imageButton3);
        final ImageButton direito = (ImageButton) findViewById(R.id.imageButton2);
        final ImageButton esquerda = (ImageButton) findViewById(R.id.imageButton);

        ImageButton play = (ImageButton) findViewById(R.id.imageButton6);
        play.setVisibility(View.INVISIBLE);

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
            n = 28;
            difficult = 200;
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
            difficult = recuperarDados.getInt("difficult");
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
        posicao[0] = n/2;
        posicao[1] = n/2;
        cobra.add(posicao);
        fruta();
        movimento(direcao);//inicia o moviemnto da cobra
    }

     public void play(View v){
         ImageButton play = (ImageButton) findViewById(R.id.imageButton6);
         ImageButton pause = (ImageButton) findViewById(R.id.imageButton5);
         running = true;
         movimento(direcao);
         play.setVisibility(View.INVISIBLE);
         pause.setVisibility(View.VISIBLE);
    }

    public void pause(View v){
        ImageButton play = (ImageButton) findViewById(R.id.imageButton6);
        play.setVisibility(View.VISIBLE);
        running = false;
        movimento(direcao);
        ImageButton pause = (ImageButton) findViewById(R.id.imageButton5);
        pause.setVisibility(View.INVISIBLE);
    }

    public void movimento(final int[] direcao){
        final Handler handler = new Handler();
        new  Thread(new Runnable(){
            public void run(){
                while(running){
                    try {
                        Thread.sleep(difficult);
                    } catch (InterruptedException e) {
                        e.printStackTrace();                    }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //LIMPAR A TELA COM FOR DE COBRA INT[] COBRA:COBRA{BRANCO}
                        Log.i("TESTES_", "tamanho"+cobra.size());
                        //limpar
                        for (int i = 0; i < cobra.size(); i++) {
                            int[] pos = cobra.get(i);
//                            Log.i("TESTES_", "pintarbranco:" +pos[0]+pos[1]);
                            branco(tabuleiro[pos[0]][pos[1]]);
                        }

                        //anda a cabeça
                        cabecinha();
                        //come
                        eat();
                        //anda o corpo

                        for (int i = cobra.size() - 1; i > 0; i--) {
                            if (i!=0) {
                                cobra.get(i)[0] = cobra.get(i - 1)[0];
                                cobra.get(i)[1] = cobra.get(i - 1)[1];
                            }else{
                                cobra.get(i)[0] += posicao[0];
                                cobra.get(i)[1] += posicao[1];
                            }
                        }
                        //Checar se a cabeça bateu no corpo
                        for (int i = 1; i < cobra.size(); i++) {
                            if (cobra.get(0)[0] == cobra.get(i)[0] && cobra.get(0)[1] == cobra.get(i)[1]) {
                                Log.i("cobra", ""+cobra.get(i)[0]);
                                Log.i("cobra", ""+cobra.get(i)[1]);
                                running = false;
                                Toast.makeText(c, "MORREU", Toast.LENGTH_SHORT).show();
                            }
                        }

                        //desenha
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


    public void cabecinha(){//metodo que faz cobra crescer
//        posicao = cobra.get(0);
        posicao[0] = posicao[0] + direcao[0];
        posicao[1] = posicao[1] + direcao[1];
        cobra.get(0)[0] = posicao[0];
        cobra.get(0)[1] = posicao[1];
        //chao para cima
        if (cobra.get(0)[0] >= n && cobra.get(0)[1] < n) {
            cobra.get(0)[0] = 0;
        }
        //direita para esquerda
        else if (cobra.get(0)[0] < n && cobra.get(0)[1] >= n) {
            cobra.get(0)[1] = 0;
        }
        if (cobra.get(0)[0] == -1) {
            cobra.get(0)[0] = n - 1;
        } else if (cobra.get(0)[1] == -1) {
            cobra.get(0)[1] = n - 1;
        }
    }

    public void eat(){
        if(cobra.get(0)[0] == fruit[0] && cobra.get(0)[1] == fruit[1]) {
            fruta();
            TextView tv = (TextView) findViewById(R.id.textView3);
            pontuacao += 50;
            tv.setText("" + pontuacao);
            difficult -= 2;
            cobra.add(new int[] {0,0});
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences settings = getSharedPreferences(PREF_SNAKE, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        //editor.putInt("", table[position[0]][position[1]]);
    }
}
