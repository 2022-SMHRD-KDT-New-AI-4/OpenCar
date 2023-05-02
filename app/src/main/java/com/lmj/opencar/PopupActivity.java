package com.lmj.opencar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;



import java.util.Locale;

public class PopupActivity extends AppCompatActivity {

    TextView txtText;
    Button btn_ok;
    TextToSpeech tts;
    //ToneGenerator tone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup);

        txtText = findViewById(R.id.tv_alert);
        btn_ok = findViewById(R.id.btn_next);
        //tone = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);


        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if( i == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.KOREAN);

                    if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA) {
                        Log.e("TTS","지원하지 않는 언어입니다");
                    } else {
                        tts.speak(txtText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, "uid");
                        // btn1.setText("말하는중");
                        //tone.startTone(ToneGenerator.TONE_DTMF_S, 5000);
                        // Thread 생성
                        TimerThread thread = new TimerThread(btn_ok);
                        // Thread 실행
                        thread.start();

//                        try {
//                            thread.join();
//                        } catch (InterruptedException e) {
//                            throw new RuntimeException(e);
//                        }


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

    Handler timerHandler = new Handler(Looper.getMainLooper()){

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            int time = msg.arg1;

            Button tv = (Button)msg.obj;
            tv.setText("안내시작  "+time);
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
            for (int i = 10; i>=0; i--){

                Message message = new Message();

                message.arg1 = i;

                message.obj = tv;

                timerHandler.sendMessage(message);

                try {
                    Thread.sleep(1000);
                    if(i==0){
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://map.kakao.com/link/to/2009536799")));
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
            // 그 숫자를 tv_timer에 반영!


        }







    }
}