package br.eaj.tads.acobrona;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

public class Configurar extends AppCompatActivity {
//    Button continuar;
    RadioGroup radioGroup;
    RadioGroup radioDifficult;
    int difficult;
    Context c = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuracao);
        radioGroup = (RadioGroup) findViewById(R.id.idgrup);
        radioDifficult = (RadioGroup) findViewById(R.id.difficult);
    }

    public void confirmar(View v){
        Intent i = new Intent(c, Principal.class);
        Bundle params = new Bundle();
        if(R.id.radio1 == radioGroup.getCheckedRadioButtonId()){
            verificarDifficult(v);
            params.putInt("tam", 28);
            params.putInt("difficult", difficult);
            i.putExtras(params);
            startActivity(i);
        }else if(R.id.radio2 == radioGroup.getCheckedRadioButtonId()){
            verificarDifficult(v);
            params.putInt("tam", 34);
            params.putInt("difficult", difficult);
            i.putExtras(params);
            startActivity(i);
        }
    }

    public void verificarDifficult(View v){
        if(R.id.easy == radioDifficult.getCheckedRadioButtonId()){
            difficult = 300;
            Log.i("TENTOU", "verificou a dificultade");
        }else if(R.id.medium == radioDifficult.getCheckedRadioButtonId()){
            difficult = 250;
            Log.i("TENTOU", "verificou a dificultade");
        }else if(R.id.hard == radioDifficult.getCheckedRadioButtonId()){
            difficult = 150;
            Log.i("TENTOU", "verificou a dificultade");
        }
    }

    public void sair(View v){
        finish();
    }
}
