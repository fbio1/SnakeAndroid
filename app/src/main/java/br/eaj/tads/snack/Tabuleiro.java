package br.eaj.tads.snack;

import android.content.Context;
import android.content.Intent;
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

public class Tabuleiro extends AppCompatActivity {
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

        layout = (GridLayout) findViewById(R.id.gridlayout);
        TextView pontos = (TextView) findViewById(R.id.points);
        final ImageButton cima = (ImageButton) findViewById(R.id.imageButton4);
        final ImageButton baixo = (ImageButton) findViewById(R.id.imageButton3);
        final ImageButton direito = (ImageButton) findViewById(R.id.imageButton2);
        final ImageButton esquerda = (ImageButton) findViewById(R.id.imageButton);

//        ImageButton play = (ImageButton) findViewById(R.id.imageButton6);
//        play.setVisibility(View.INVISIBLE);

        ImageButton play_tab = (ImageButton) findViewById(R.id.play_tab);
        play_tab.setVisibility(View.INVISIBLE);

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

        Bundle recuperarDados = getIntent().getExtras();
        n = recuperarDados.getInt("tam");
        difficult = recuperarDados.getInt("difficult");
        pontuacao = recuperarDados.getInt("pontuacao");
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

        posicao[0] = n/2;
        posicao[1] = n/2;
        cobra.add(0, posicao);

        fruta();
        direcao[0] = -1;
        direcao[1] = 0;
//        gerar();

        //Log.i("TESTE", ""+direcao);

        pontos.setText(""+ pontuacao);
        movimento();
    }

    public void play_tab(View v){
        ImageButton play_tab = (ImageButton) findViewById(R.id.play_tab);
        ImageButton pause_tab = (ImageButton) findViewById(R.id.pause_tab);
        running = true;
        movimento();
        play_tab.setVisibility(View.INVISIBLE);
        pause_tab.setVisibility(View.VISIBLE);
    }

    public void pause_tab(View v){
        ImageButton play_tab = (ImageButton) findViewById(R.id.play_tab);
        play_tab.setVisibility(View.VISIBLE);
        running = false;
        movimento();
        ImageButton pause_tab = (ImageButton) findViewById(R.id.pause_tab);
        pause_tab.setVisibility(View.INVISIBLE);
    }

    public void movimento(){
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
                        //verifica se a cobra comeu a fruta
                        if(cobra.get(0)[0] == fruit[0] && cobra.get(0)[1] == fruit[1]) {
                            TextView tv = (TextView) findViewById(R.id.points);
                            pontuacao += 50;
                            tv.setText("" + pontuacao);
                            difficult -= 2;
                            int lastPosicao = cobra.size();
                            int pos[] = new int[2];
                            pos[0] = cobra.get(lastPosicao - 1)[0];
                            pos[1] = cobra.get(lastPosicao - 1)[1];
                            cobra.add(lastPosicao, pos);
                            fruta();
                        }
                        //Log.i("TESTES_", "tamanho"+cobra.size());

                        //limpa a tela
                        for (int i = 0; i < cobra.size(); i++) {
                            int[] pos = cobra.get(i);
                            //Log.i("TESTES_", "pintarbranco:" +pos[0]+pos[1]);
                            branco(tabuleiro[pos[0]][pos[1]]);
                        }

                        //movimento do corpo
                        for (int i = cobra.size()-1; i > 0; i--) {//pega a ultima posiçao da cobra e adiciona a posiçao posterior
                            cobra.get(i)[0] = cobra.get(i - 1)[0];
                            cobra.get(i)[1] = cobra.get(i - 1)[1];
                        }

                        //movimento da cabeça da cobra
                            //se colocar antes, a cobra so cresce se depois de duas frutas
                        head();

                        //desenha a cobra
                        for (int i = 0; i < cobra.size(); i++) {
                            int[] pos = cobra.get(i);
                            //Log.i("TESTE", ""+cobra.size());
                            preto(tabuleiro[pos[0]][pos[1]]);
                        }

                        //Checar se a cabeça bateu no corpo
                        for (int i = 1; i < cobra.size(); i++) {
                            if (cobra.get(0)[0] == cobra.get(i)[0] && cobra.get(0)[1] == cobra.get(i)[1]) {
                                Log.i("cobra", ""+cobra.get(i)[0]);
                                Log.i("cobra", ""+cobra.get(i)[1]);
                                running = false;
                                Intent config = new Intent(c, GameOver.class);
                                config.putExtra("pontos", ""+pontuacao);
                                startActivity(config);
                                finish();
                            }
                        }
                    }
                });
                }
            }
        }).start();
    }

    public void head(){//metodo que faz cobra crescer

        //Log.i("TESTES_", "Movimenta cabeca: " + cobra.get(0)[0] + "," + cobra.get(0)[1] + " + direcao: " + direcao[0] + "," + direcao[1] );

        cobra.get(0)[0] += direcao[0];
        cobra.get(0)[1] += direcao[1];

        //baixo para cima
        if (cobra.get(0)[0] == n) {
            //Log.i("TESTE_", "Case X == "+ n);
            cobra.get(0)[0] = 0;
        }
        //cima para baixo
        if (cobra.get(0)[1] == n) {
            //Log.i("TESTE_", "Case Y == "+ n);
            cobra.get(0)[1] = 0;
        }
        //esquerda para direita
        if (cobra.get(0)[0] == -1) {
            //Log.i("TESTE_", "Case X == -1");
            cobra.get(0)[0] = n - 1;
        }
        //direita para esqueda
        if (cobra.get(0)[1] == -1) {
            //Log.i("TESTE_", "Case Y == -1");
            cobra.get(0)[1] = n - 1;
        }

        //Log.i("TESTES_", "Cabeca: " + cobra.get(0)[0] + "," + cobra.get(0)[1] );
    }

    //Cria randomicamente a fruta no gridlayout
    public void fruta(){
        fruit[0] = new Random().nextInt(n-1);
        fruit[1] = new Random().nextInt(n-1);
        vermelho(tabuleiro[fruit[0]][fruit[1]]);
    }

    public void salvar(View v){
        Intent intent = new Intent(c , MainActivity.class);
        Bundle parans = new Bundle();
        parans.putInt("pontuacao", pontuacao);
        intent.putExtras(parans);
        setResult(RESULT_OK, intent);
        finish();
    }

//    public void gerar(){
//
////        String dados = String.valueOf(posicao);
//        String cobra1 = "10,1-10,2-10,3";
////        Log.i("TESTE", ""+cobra1.toString());
//        String[] posicoes = cobra1.split("-");
////        Log.i("TESTE", "ENTROU NO METODO");
//        for (int i = 0; i < posicoes.length; i++) {
//            String[] pos_string = posicoes[i].split(",");
//            cobra.add(new int[] {Integer.parseInt(pos_string[0]), Integer.parseInt(pos_string[1])});
////            pontuacao = pos_string.length*50;
////            Log.i("TESTE", "" + cobra.size());
//        }
//    }

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
