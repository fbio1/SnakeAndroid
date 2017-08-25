package br.eaj.tads.acobrona;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class Activity2 extends AppCompatActivity {
    int n;
    GridLayout layout;
    ImageView tabuleiro[][];
    ArrayList<int[]> cobra = new ArrayList<>();
    int direcao[] = new int[2];
    int posicao[] = new int[2];
//  int posicaox;
//  int posicaoy;
//    int novaposicao[] = new int[2];
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

    public void movimento(final int[] direcao){
        final Handler handler = new Handler();
//        //runnable
        new  Thread(new Runnable(){
            public void run(){
//                //while
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //LIMPAR A TELA COM FOR DE COBRA INT[] COBRA:COBRA{BRANCO}

                        //inicializa o movimento da cobra
//                        posicao = (int[]) cobra.get(cobra.size()-1);
//                        novaposicao[0] = posicao[0];
//                        novaposicao[1] = posicao[1];

//                      //move o corpo
                        //if i !=0
//                            cobra.get(i)[0] = cobra.get(i-1)[0];
//                            cobra.get(i)[1] = cobra.get(i-1)[1];

                        for (int i = 0; i < cobra.size(); i++) {
                            int[] pos = cobra.get(i);
                            preto(tabuleiro[pos[0]][pos[1]]);
                        }
                        cabecinha(direcao);
                        movimento(direcao);//metodo de repetiçao do movimento

                    }
                },500);
            }
        }).start();
    }

    public void cabecinha(int[] direcao){//metodo de fazer a cobra mecher
        posicao = cobra.get(0);
        posicao[0] = posicao[0] + direcao[0];
        posicao[1] = posicao[1] + direcao[1];
        posicao = checkposicao(posicao);
        preto(tabuleiro[posicao[0]][posicao[1]]);
        cobra.set(0, posicao);
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
