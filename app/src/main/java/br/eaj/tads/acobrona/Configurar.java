package br.eaj.tads.acobrona;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;

public class Configurar extends AppCompatActivity {
    RadioGroup radioGroup;
    Configurar c = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configurar);
        radioGroup = (RadioGroup) findViewById(R.id.idgrup);
    }

    public void confirmar(View v){
        if(R.id.radio1 == radioGroup.getCheckedRadioButtonId()){
            Intent i = new Intent(c, Activity2.class);
            Bundle params = new Bundle();
            params.putInt("tam", 30);
            i.putExtras(params);
            startActivity(i);
//            finish();
        }else if(R.id.radio2 == radioGroup.getCheckedRadioButtonId()){
            Intent i = new Intent(c, Activity2.class);
            Bundle params = new Bundle();
            params.putInt("tam", 40);
            i.putExtras(params);
            startActivity(i);
//            finish();
        }
    }

    public void sair(View v){
        finish();
    }
}
