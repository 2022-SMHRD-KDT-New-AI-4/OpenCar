package com.lmj.opencar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class PopupActivity extends AppCompatActivity {

    TextView txtText;
    Button btn_ok;
    TextToSpeech tts;

    ToneGenerator tone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup);
        tone = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);

        txtText = findViewById(R.id.txtText);
        btn_ok = findViewById(R.id.btn_ok);


        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if( i == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.KOREAN);

                    if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA) {
                        Log.e("TTS","지원하지 않는 언어입니다");
                    } else {
                        tts.speak(txtText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, "uid");
                        tone.startTone(ToneGenerator.TONE_DTMF_S, 5000);
                        // btn1.setText("말하는중");
                    }
                } else {
                    Log.e("TTS","실패");
                }
            }
        });

        //데이터 가져오기
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        txtText.setText(data);



//        tts.speak(txtText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, "uid");
//        Log.d("onCreate",txtText.getText().toString());


//        CharSequence text = editText.getText();
//                tts.setPitch((float)1.0);
//                tts.setSpeechRate((float)1.0);

        //확인 버튼 클릭
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //데이터 전달하기
                Intent intent = new Intent();
                intent.putExtra("result", "Close Popup");
                setResult(RESULT_OK, intent);

                //액티비티(팝업) 닫기
                finish();

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();


        //데이터 가져오기
//        Intent intent = getIntent();
//        String data = intent.getStringExtra("data");
//        tts.speak(data,TextToSpeech.QUEUE_FLUSH,null,"uid");
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }


        Log.d("onstart",txtText.getText().toString());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }

        return true;
    }


    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(tts != null){
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }
}