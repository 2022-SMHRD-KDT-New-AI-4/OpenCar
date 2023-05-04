package com.lmj.opencar;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

/* 새로운 Service 클래스 작성 방법
 1. app오른쪽클릭>New>Service>Service 선택
   - 생성하게되면, AndroidManifest.xml에 서비스관련 태그가 자동으로 등록
 2.  onCreate(), onDestroy(), onStartCommand() 를 선택하여 세 가지 메소드를 오버라이딩

 서비스 사용시 주의사항
서비스를 사용할 때는 반드시 새로운 스레드를 생성 필요
서비스는 기본적으로 어플리케이션의 main thread를 이용하므로,
별도 스레드 생성없이 작업할 코드를 수행하면 메인 스레드에 과부하가 걸린다.*/

public class BackGroundService extends Service {

    // 필드 부분 private 접근자 사용

    // volley 관련 필드
    private RequestQueue queue;
    private StringRequest request_background;
    private PortClass port;
    private String url;

    // Thread 관련 필드
    private backgroundThread backgroundThread;
    private ToneGenerator tone;
    TextToSpeech tts;
    private String [] wakeUp_alarms;
    private Random random;

    // Service 관련 필드
    private boolean check; // 운전 시작 버튼 클릭시 true / 운전 완료 버튼 클릭시 false
    private boolean first;
    private String result; // 졸음운전 감지 ( 감지 완료 - ( 졸음 o - true , 졸음 x - false) 감지 실패 - 감지x ) 3가지 분류
    private String loginId,dr_seq;



    public BackGroundService() {  // 생성자
    }  // 생성자

    @Override
    public void onCreate() {
        super.onCreate();
        /* onCreate 메소드
        서비스가 생성될 때 한번만 호출 / 순서는 onStartCommand()와 onBind()보다 앞선다.
        */

        // 초기화
        random = new Random();
        check = true;
        first = false;
        result = "false";
        backgroundThread = new backgroundThread();
        tone = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);
        wakeUp_alarms = new String[]
                { "또 졸고 있네요","계속 졸지 마세요","왜 또 졸아요?","그만 자세요","정신차려"};

        // volley, thread 기능 시작
        backgroundThread.start();
        requestBackground();


    }  // onCreate


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /* onStartCommand 메소드
        : startService() 함수가 호출되면 호출 / 독립적인 서비스가 필요할 때 구현
        - startService()로 서비스를 호출되면 stopSelf(),  stopService() 호출될 때 까지 계속 살아있다.
        */

        return START_REDELIVER_INTENT;
        /* 리턴값 메소드
         * 1. START_STICKY : Service 강제 종료시 Service를 재시작 / intent 값을 null로 초기화
         * 2. START_NOT_STICKY : 강제로 종료 된 Service가 재시작 x
         * 3. START_REDELIVER_INTENT : Service를 재시작 시켜 주지만 intent 값을 그대로 유지  */

    }  // onStartCommand

    @Override
    public void onDestroy() {
        super.onDestroy();

        /* onDestroy 메소드
        : 서비스가 파괴될 때 호출
        - 리스너 및 리시버 제거, 리소스 정리 필요
         */

        check = false; // Thread 완료

    }  // onDestroy


    @Override
    public IBinder onBind(Intent intent) { // 기본으로 만들어진 메소드
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    } // onBind

    // requestBackground 생성
    public void requestBackground() {
        queue = Volley.newRequestQueue(getApplicationContext());

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        loginId = auto.getString("inputId",null);
        dr_seq = auto.getString("dr_seq",null);




        url = port.port + "sleep_sensors/android/"+loginId+"/"+dr_seq;

        request_background = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            // 서버로부터 문자열 데이터를 얻을 목적

            @Override
            public void onResponse(String response) {
                //결과 처리

                if (response.equals("false")) {  // 안전운전중
                    result = "false";
                } else if (response.equals("true")) {  // 졸음 감지
                    result = "true";

                    if (!first) {
                        // 졸음 감지 처음이면 알림창 이동

                        Intent intent = new Intent();
                        intent.setClassName("com.lmj.opencar",
                                "com.lmj.opencar.PopupActivity"); // 패키지 이름 ksm --> lmj로 일단 바꿔둠
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        first = true;
                    } else {
                        // 두번째 부터 알림음만
                        tts = new TextToSpeech(BackGroundService.this, new TextToSpeech.OnInitListener() {
                            @Override
                            public void onInit(int i) {
                                if (i == TextToSpeech.SUCCESS) {
                                    int result = tts.setLanguage(Locale.KOREAN);
                                    AudioManager audioManager = (AudioManager) getSystemService(BackGroundService.this.AUDIO_SERVICE);
                                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 9, 0);

                                    if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA) {
                                        Log.e("TTS", "지원하지 않는 언어입니다");
                                    } else {
                                        wakeUp_alarms = new String[]
                                                { "또 졸고 있네요","계속 졸지 마세요","왜 또 졸아요?","그만 자세요","정신차려"};
                                        tts.speak(wakeUp_alarms[random.nextInt(wakeUp_alarms.length)],
                                                TextToSpeech.QUEUE_FLUSH, null, "uid");

                                    }
                                } else {
                                    Log.e("TTS", "실패");
                                }
                            }
                        });

                    }


                } else if (response.equals("감지x")) {
                    result = "감지x";
                    // 아직 몰라 대기
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            // 서버에 데이터 전송시

            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                return params;
            }
        };

    } // requestBackground

    // Handler 생성
    Handler backgroundHandler = new Handler(Looper.getMainLooper()) {
        //  Thread 간 상호 작용을 위한 클래스
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            // 1. Message 받아오기

            // 2. Thread 작업
            queue.add(request_background);

        }
    }; // backgroundHandler

    // Thread 생성
    class backgroundThread extends Thread {

        @Override
        public void run() {
            super.run();
            while (check) {

                try {

                    Message message = new Message();
                    Thread.sleep(500);

                    if (result.equals("true")) { // 졸음이 감지되면
                        tone.startTone(ToneGenerator.TONE_DTMF_S, 10000);
                        Log.d("hereherehere", "15초 휴식...");

                        Thread.sleep(15000); // 15초간 스레드 멈춤
                        Log.d("hereherehere", "15초 끝...");
                    }


                    backgroundHandler.sendMessage(message);

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    } // backgroundThread


}  // BaackGroundService