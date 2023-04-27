package com.lmj.opencar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    final static String APP_KEY ="8fa4719b2e5c14b2102859000fb2f9a3";
    EditText edt_id,edt_pw;
    Button btn_login;
    CheckBox ch_auto;
    String loginId,loginPw,email,Nickname,inputId;
    TextView tv_join;
    ImageView btn_kakao;
    RequestQueue queue;
    PortClass port;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //getKeyHash(); // 해시 키 구할 때만 사용.
        //Log.d("키",getKeyHash());

        edt_id =  findViewById(R.id.edt_id);
        edt_pw =  findViewById(R.id.edt_pw);
        btn_login = findViewById(R.id.btn_join);
        ch_auto =  findViewById(R.id.ch_auto);
        tv_join = findViewById(R.id.tv_join);
        btn_kakao = findViewById(R.id.img_kakao);

        tv_join.setPaintFlags(tv_join.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        // 통로는 한개만 있어두 됨
        queue = Volley.newRequestQueue(getApplicationContext());

        // 회원가입 페이지로 이동
        tv_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it_join = new Intent(LoginActivity.this,JoinActivity.class);
                startActivity(it_join);
            }
        });

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        loginId = auto.getString("inputId",null);

        if (loginId != null){
            Toast.makeText(LoginActivity.this, loginId+"님 자동 로그인", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        else if (loginId == null) {
            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String url = port.port + "login/";/// 3
                    StringRequest request_login = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {  /// 4
                        @Override
                        public void onResponse(String response) {
                            Log.d("응답", response);

                            if (response != null && ch_auto.isChecked()==true){
                                SharedPreferences auto = getSharedPreferences("auto",Activity.MODE_PRIVATE);
                                SharedPreferences.Editor autologin = auto.edit();
                                loginId = auto.getString("inputId",edt_id.getText().toString());
                                autologin.putString("inputId",edt_id.getText().toString());
                                autologin.putString("inputPw",edt_pw.getText().toString());
                                autologin.commit();
                                Toast.makeText(LoginActivity.this, loginId+"님 환영합니다", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                            }else if (response != null && ch_auto.isChecked()==false){
                                loginId = auto.getString("inputId",edt_id.getText().toString());
                                Toast.makeText(LoginActivity.this, loginId+"님 환영합니다", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("id",edt_id.getText().toString());
                                startActivity(intent);
                                finish();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("error in response","Error :"+error.toString());
                        }
                    }){
                        protected Map<String, String> getParams() throws AuthFailureError{  ///6
                            Map<String, String> param = new HashMap<>();

                            param.put("User_id",edt_id.getText().toString());
                            param.put("User_pw",edt_pw.getText().toString());
                            return param;
                        }
                    };
                    request_login.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                            20000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    queue.add(request_login);

                }
            });
        }

        btn_kakao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(LoginActivity.this)){
                    login();
                }else {
                    accountLogin();
                }

            }
        });

    }// onCreate
    public void login() {
        String TAG = "login()";
        UserApiClient.getInstance().loginWithKakaoTalk(LoginActivity.this, (oAuthToken, error) -> {
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
        UserApiClient.getInstance().loginWithKakaoAccount(LoginActivity.this, (oAuthToken, error) -> {
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
                Intent it_mail = new Intent(LoginActivity.this,MainActivity.class);
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

}// class