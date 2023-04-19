package com.lmj.opencar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class BellActivity extends AppCompatActivity {
    Button btn_ok1;
    Spinner sp_list;
    String[] items = {"알람","1","2","3"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bell);

        btn_ok1 = findViewById(R.id.btn_ok1);
        sp_list = findViewById(R.id.sp_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        sp_list.setAdapter(adapter);


        btn_ok1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it_ok = new Intent(BellActivity.this,MainActivity.class);
                // it_ok.putExtra(); 스피너 값을 가지고 올때 사용
                startActivity(it_ok);
            }
        });


    }
}