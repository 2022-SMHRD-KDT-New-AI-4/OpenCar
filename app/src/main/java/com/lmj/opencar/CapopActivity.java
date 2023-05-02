package com.lmj.opencar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class CapopActivity extends AppCompatActivity {

    Button btn_ck;
    TextView tv_caution;
    TextToSpeech tts;
    ToneGenerator tone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capop);
        btn_ck = findViewById(R.id.btn_ck);
        tv_caution = findViewById(R.id.tv_caution);
        tone = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if( i == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.KOREAN);

                    if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA) {
                        Log.e("TTS","지원하지 않는 언어입니다");
                    } else {
                        tts.speak(tv_caution.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, "uid");
                        // btn1.setText("말하는중");
                        tone.startTone(ToneGenerator.TONE_DTMF_S, 5000);
                        // Thread 생성
                        TimerThread thread = new TimerThread(btn_ck);
                        // Thread 실행
                        thread.start();
                    }
                } else {
                    Log.e("TTS","실패");
                }
            }
        });

        //확인 버튼 클릭
        btn_ck.setOnClickListener(new View.OnClickListener() {
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

    Handler timerHandler = new Handler(Looper.getMainLooper()){

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            int time = msg.arg1;

            Button tv = (Button)msg.obj;
            tv.setText("확인  "+time);
        }
    };

    class TimerThread extends Thread{

        Button tv;

        public TimerThread(Button tv){
            this.tv = tv;
        }

        @Override
        public void run() {

            // 10에서 0까지 감소하는 숫자 설정!
            for (int i = 5; i>=0; i--){

                Message message = new Message();

                message.arg1 = i;

                message.obj = tv;

                timerHandler.sendMessage(message);

                try {
                    Thread.sleep(1000);
                    if(i==0){
                        finish();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
            // 그 숫자를 tv_timer에 반영!


        }







    }
}