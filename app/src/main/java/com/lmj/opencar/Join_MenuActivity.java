package com.lmj.opencar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class Join_MenuActivity extends AppCompatActivity {
    final static String APP_KEY ="8fa4719b2e5c14b2102859000fb2f9a3";
    String email,Nickname,inputId;
    Button btn_login_menu;
    ImageView kakao_login_button;
    RequestQueue queue;
    PortClass port;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_menu);

        //getKeyHash(); // 해시 키 구할 때만 사용.
        //Log.d("키",getKeyHash());


        kakao_login_button = findViewById(R.id.login);
        btn_login_menu = findViewById(R.id.btn_login_menu);

        queue = Volley.newRequestQueue(getApplicationContext());

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        inputId = auto.getString("inputId",null);

        if (inputId != null){
            Toast.makeText(Join_MenuActivity.this, inputId+"님 자동로그인", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Join_MenuActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        // 로그인 버튼
        btn_login_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it_login = new Intent(Join_MenuActivity.this,LoginActivity.class);
                startActivity(it_login);
            }
        });

        // 카카오 버튼
        kakao_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(Join_MenuActivity.this)){
                    login();
                }else {
                    accountLogin();
                }
            }
        });



    }//onCreate
    public void login() {
        String TAG = "login()";
        UserApiClient.getInstance().loginWithKakaoTalk(Join_MenuActivity.this, (oAuthToken, error) -> {
            if (error != null) {
                Log.e(TAG, "로그인 실패", error);
            } else if (oAuthToken != null) {
                Log.i(TAG, "로그인 성공(토큰) : " + oAuthToken.getAccessToken());
                getUserInfo();
            }
            return null;
        });
    }

    public void logout(){
        String TAG = " logout()";
        UserApiClient.getInstance().logout(error -> {
            if (error != null) {
                Log.e(TAG, "로그아웃 실패, SDK에서 토큰 삭제됨", error);
            }else{
                Log.e(TAG, "로그아웃 성공, SDK에서 토큰 삭제됨");
            }
            return null;
        });
    }
    public void accountLogin() {
        String TAG = "accountLogin()";
        UserApiClient.getInstance().loginWithKakaoAccount(Join_MenuActivity.this, (oAuthToken, error) -> {
            if (error != null) {
                Log.e(TAG, "로그인 실패", error);
            } else if (oAuthToken != null) {
                Log.i(TAG, "로그인 성공(토큰) : " + oAuthToken.getAccessToken());
                getUserInfo();
            }
            return null;
        });
    }
    public void getUserInfo() {
        String TAG = "getUserInfo()";
        UserApiClient.getInstance().me((user, meError) -> {
            if (meError != null) {
                Log.e(TAG, "사용자 정보 요청 실패", meError);
            } else {
                System.out.println("로그인 완료");
                Log.i(TAG, user.toString());
                {
                    Log.i(TAG, "사용자 정보 요청 성공" +
                            "\n회원번호: " + user.getId());
                }

                email = user.getKakaoAccount().getEmail();
                Nickname = user.getKakaoAccount().getProfile().getNickname();

                String url = port.port + "join/";
                StringRequest request_kjoin = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("응답", response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error in response", "Error :"+error.toString());
                    }
                }){
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<>();

                        param.put("User_id",email);
                        param.put("User_pw","kakao");
                        param.put("User_name",Nickname);
                        param.put("User_birthdate","");
                        return param;
                    }
                };
                queue.add(request_kjoin);
                Intent it_mail = new Intent(Join_MenuActivity.this,MainActivity.class);
                it_mail.putExtra("email",email);////
                it_mail.putExtra("id",Nickname);////
                startActivity(it_mail);

                SharedPreferences auto = getSharedPreferences("auto",Activity.MODE_PRIVATE);
                SharedPreferences.Editor autologin = auto.edit();
                autologin.putString("inputId",Nickname);
                autologin.putString("inputemail",email);
                autologin.commit();

                Account user1 = user.getKakaoAccount();
                System.out.println("사용자 계정" + user1);
            }
            return null;
        });
    }

    public String getKeyHash() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            if (packageInfo == null) return null;
            for (Signature signature : packageInfo.signatures) {
                try {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    return android.util.Base64.encodeToString(md.digest(), Base64.NO_WRAP);
                } catch (NoSuchAlgorithmException e) {
                    Log.w("getKeyHash", "Unable to get MessageDigest. signature=" + signature, e);
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.w("getPackageInfo", "Unable to getPackageInfo");
        }
        return null;
    }
}//class