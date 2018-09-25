package br.eaj.tads.snack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Configurar extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioGroup radioDifficult;
    int difficult;
    int tamanho;
    RadioButton radio1, radio2, radio3, radio4, radio5;
    int easy = 300;
    int medium = 250;
    int hard = 150;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuracao);

        Bundle params = getIntent().getExtras();
        difficult = params.getInt("difficult");
        tamanho = params.getInt("tam");

        radioGroup = (RadioGroup) findViewById(R.id.idgrup);
        radioDifficult = (RadioGroup) findViewById(R.id.difficult);
        radio1 = (RadioButton) findViewById(R.id.easy);
        radio2 = (RadioButton) findViewById(R.id.medium);
        radio3 = (RadioButton) findViewById(R.id.hard);

        radio4 = (RadioButton) findViewById(R.id.radio1);//pequeno
        radio5 = (RadioButton) findViewById(R.id.radio2);//grande

        //verficar a dificuldade do jogo
        if (difficult == easy) {
            radio1.setChecked(true);
        } else if (difficult == medium) {
            radio2.setChecked(true);
        } else if (difficult == hard) {
            radio3.setChecked(true);
        }

        //verificar o tamanho do grid
        if (tamanho == 28) {
            radio4.setChecked(true);
        } else if (tamanho == 34) {
            radio5.setChecked(true);
        }
    }

    public void confirmar(View v){
        Intent i = new Intent();
        Bundle params = new Bundle();

        if(R.id.easy == radioDifficult.getCheckedRadioButtonId()){
            difficult = easy;
        }else if(R.id.medium == radioDifficult.getCheckedRadioButtonId()){
            difficult = medium;
        }else if(R.id.hard == radioDifficult.getCheckedRadioButtonId()){
            difficult = hard;
        }

        if(R.id.radio1 == radioGroup.getCheckedRadioButtonId()){
            tamanho = 28;
        }else if(R.id.radio2 == radioGroup.getCheckedRadioButtonId()){
            tamanho = 34;
        }

        params.putInt("tam", tamanho);
        params.putInt("difficult", difficult);
        i.putExtras(params);
        setResult(RESULT_OK, i);
        finish();
    }

    public void sair(View v){
        setResult(RESULT_CANCELED);
        finish();
    }
}
