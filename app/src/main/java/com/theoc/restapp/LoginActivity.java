package com.theoc.restapp;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.theoc.restapp.dataorganization.GeneralSync;
import com.theoc.restapp.dataorganization.Giris;
import com.theoc.restapp.dataorganization.GirisType;
import com.theoc.restapp.dataorganization.KayitType;
import com.theoc.restapp.dataorganization.SifreUnuttum;
import com.theoc.restapp.dataorganization.SocketMessage;
import com.theoc.restapp.helper.VersionChecker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    String email, name, id, password;
    SharedPreferences prefs;
    Giris giris;
    LoginButton fbLoginButton;
    CallbackManager callbackManager;
    AccessTokenTracker fbTracker;
    SifreUnuttum sifreUnuttum;
    GoogleApiClient googleApiClient;
    SignInButton googleLoginButton;
    public static SocketMessage socket_message;
    String forgotTempEmail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        socket_message = new SocketMessage(this);
        giris = new Giris(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.googleclientid))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.v("GOOGLE API CLIENT: ", "FAILED");
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        setContentView(R.layout.activity_login);
        findViewById(R.id.imageButton).setOnClickListener(this);
        findViewById(R.id.fbButton).setOnClickListener(this);
        findViewById(R.id.googleButton).setOnClickListener(this);
        findViewById(R.id.registerButton).setOnClickListener(this);
        findViewById(R.id.misafirButton).setOnClickListener(this);
        findViewById(R.id.forgotButton).setOnClickListener(this);
        fbLoginButton = (LoginButton) findViewById(R.id.fbLoginButton);
        fbLoginButton.setOnClickListener(this);
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MapView mv = new MapView(getApplicationContext());
                    mv.onCreate(null);
                    mv.onPause();
                    mv.onDestroy();
                } catch (Exception ignored) {

                }
            }
        }).start();*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        VersionChecker versionChecker = new VersionChecker();
        try {
            String latestVersion = versionChecker.execute().get();
            if (latestVersion != null) {
                Log.w("LATEST VERSION=", latestVersion);
                Log.w("CURRENT VERSION=", BuildConfig.VERSION_NAME);
                if (compareVersionNames(BuildConfig.VERSION_NAME, latestVersion) == -1) {
                    prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    float lastTime = prefs.getFloat("lastUpdateCheck", 0);
                    if (lastTime != 0) {
                        if (System.currentTimeMillis() - lastTime >= 7200000) {
                            new AlertDialog.Builder(this)
                                    .setMessage("Henüz Beta sürecinde olduğumuz için tüm Restearn güncellemelerini zorunlu tutuyoruz. Lütfen uygulamanızı " + latestVersion + " sürümüne güncelleyin")
                                    .setPositiveButton("Güncelle", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Uri uri = Uri.parse("market://details?id=" + LoginActivity.this.getBaseContext().getPackageName());
                                            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                                            } else {
                                                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                                            }
                                            try {
                                                startActivity(goToMarket);
                                            } catch (ActivityNotFoundException e) {
                                                startActivity(new Intent(Intent.ACTION_VIEW,
                                                        Uri.parse("http://play.google.com/store/apps/details?id=" + LoginActivity.this.getBaseContext().getPackageName())));
                                            }
                                        }
                                    })
                                    .setCancelable(false)
                                    .show();
                        } else {
                            //Log.w("Last Update Check=", lastTime+"");
                            //Log.w("Time In Between", System.currentTimeMillis() - lastTime + "");
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putFloat("lastUpdateCheck", 0);
                            //Log.w("Last Update Check=", "ADDED");
                            editor.apply();

                            /*giris = new Giris(this);
                            giris.onceki_giris();*/
                        }

                    } else {
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putFloat("lastUpdateCheck", System.currentTimeMillis());
                        //Log.w("Last Update Check=", "ADDED");
                        editor.apply();
                    }

                } else {
                    /*giris = new Giris(this);
                            giris.onceki_giris();*/
                }
            } else {
                /*giris = new Giris(this);
                            giris.onceki_giris();*/
            }
        } catch (InterruptedException | ExecutionException e) {
            /*giris = new Giris(this);
                            giris.onceki_giris();*/
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton:
                if (!((EditText) findViewById(R.id.usernameEditText)).getText().toString().equals("") &&
                        !((EditText) findViewById(R.id.passwordEditText)).getText().toString().equals("")) {
                    giris.normal_giriş(((EditText) findViewById(R.id.usernameEditText)).getText().toString(),
                            ((EditText) findViewById(R.id.passwordEditText)).getText().toString(),
                            "0");
                } else {
                    Toast.makeText(this, "Lütfen tüm gerekli alanları doldurun", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.fbLoginButton:
                fbLogin();
                break;
            case R.id.googleButton:
                googleLogin();
                break;
            case R.id.fbButton:
                fbLoginButton.performClick();
                break;
            case R.id.registerButton:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.misafirButton:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Misafir Girişi")
                        .setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Gİrİş Yap", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                misafir_girisi();
                            }
                        })
                        .setMessage("Misafir girişi yapmak üzeresiniz. Bu süreçte birçok özellik devre dışı olacak fakat uygulama hakkında genel bir fikir elde edebileceksiniz")
                        .show();
                break;
            case R.id.forgotButton:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                final View layout=this.getLayoutInflater().inflate(R.layout.forgot_dialog, null);

                builder2.setTitle("Şifremi unuttum")
                        .setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Gönder", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                forgotTempEmail = ((EditText) layout.findViewById(R.id.forgotEditText)).getText().toString();
                                sifremi_unuttum(((EditText) layout.findViewById(R.id.forgotEditText)).getText().toString());
                            }
                        })
                        .setView(layout)
                        .setMessage("E-mail adresinize şifrenizi yenilemeniz için bir mail gönderilecek")
                        .show();
                        
                break;
            default:
                break;
        }
    }

    public void sifremi_unuttum(String username){
        sifreUnuttum = new SifreUnuttum(this);
        sifreUnuttum.sifremi_unuttum(username);
    }


    public void sifre_server_cevabı(KayitType kayitType, String key) {
        if (kayitType == KayitType.var) {
            final View layout=this.getLayoutInflater().inflate(R.layout.forgot_dialog, null);
            JSONObject jj_temp =new JSONObject();
            try {
                jj_temp.put("user_name", forgotTempEmail);
                forgotTempEmail = "";
                jj_temp.put("api_key", "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");
                jj_temp.put("key", key);
                jj_temp.put("type", "forgot");
                LoginActivity.socket_message.email_user(jj_temp);
                Toast.makeText(this, "E-mail adresinize şifre yenilemeniz için bir mail gönderildi", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (kayitType == KayitType.yok) {
            Toast.makeText(this, "Girdiğiniz e-mail adresi ile kayıtlı bir kullanıcı bulunamadı", Toast.LENGTH_SHORT).show();
        } else if (kayitType == KayitType.serversorunu) {
            Toast.makeText(this, "Bir sunucu hatası oluştu. Lütfen tekrar deneyin", Toast.LENGTH_SHORT).show();
        }
    }

    public void fbLogin() {
        callbackManager = CallbackManager.Factory.create();
        fbLoginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email"));
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                try {
                                    if (object.get("email") != null) {
                                        giris.face_giris(object.getString("email"),
                                                android.provider.Settings.System.getString(LoginActivity.super.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID),
                                                object.getString("first_name"),
                                                object.getString("last_name"));
                                    } else {
                                        giris.face_giris(object.getString("id") + "@facebook.com",
                                                android.provider.Settings.System.getString(LoginActivity.super.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID),
                                                object.getString("first_name"),
                                                object.getString("last_name"));
                                    }

                                } catch (JSONException e) {
                                    Toast.makeText(LoginActivity.this, "Lütfen Facebook girişinizi tekrarlayın", Toast.LENGTH_LONG).show();
                                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, email, first_name, last_name");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Facebook girişini iptal ettiniz", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Lütfen Facebook girişinizi tekrarlayın", Toast.LENGTH_LONG).show();
            }
        });

        fbTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken2) {
                if (accessToken2 == null) {
                    Log.v("FB", "User Logged Out.");
                }
            }


        };
    }

    public void googleLogin() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d("STATUS CODE", result.getStatus().toString());
            if (result.isSuccess()) {
                Log.d("RESULT = ", "SUCCESS");
                GoogleSignInAccount acct = result.getSignInAccount();
                if (acct != null) {
                    Log.d("ACCOUNT: ", acct.toString() + "");
                giris.mail_giris(acct.getEmail(),
                        android.provider.Settings.System.getString(LoginActivity.super.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID),
                        acct.getGivenName(),
                        acct.getFamilyName());
                }
            } else {
                Toast.makeText(LoginActivity.this, "Lütfen Google girişinizi tekrarlayın", Toast.LENGTH_LONG).show();
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void gir(GirisType type, String temp_key, int id) {

        if (type.equals(GirisType.girisbasarisiz)) {
            Toast.makeText(this.getBaseContext(), "Girdiginiz bilgilerde kullanici bulunamadi", Toast.LENGTH_LONG).show();

        } else if (type.equals(GirisType.girisbasarili)) {

            prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("id", id);
            editor.putString("temp_key", temp_key + "");
            GeneralSync.id = id;
            GeneralSync.temp_key = temp_key;
            editor.apply();
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        } else if (type.equals(GirisType.onaybekleniyor)) {
            if (temp_key.equals("")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("E-mail Aktivasyonu")
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Tekrar Gönder", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                giris.normal_giriş(((EditText) findViewById(R.id.usernameEditText)).getText().toString(),
                                        ((EditText) findViewById(R.id.passwordEditText)).getText().toString(),
                                        "1");
                            }
                        })
                        .setMessage("Giriş yapabilmek için e-mail adresinize gönderdiğimiz aktivasyon e-mailini onaylamanız gerekiyor. Aktivasyon e-mailini tekrar göndermek ister misiniz?")
                        .show();
            } else {
                JSONObject jj_temp =new JSONObject();
                try {
                    jj_temp.put("user_name", ((EditText) findViewById(R.id.usernameEditText)).getText().toString());
                    jj_temp.put("api_key", "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");
                    jj_temp.put("key", temp_key);
                    jj_temp.put("type", "register");
                    LoginActivity.socket_message.email_user(jj_temp);
                    Toast.makeText(LoginActivity.this, "Size yeni bir aktivasyon e-maili gönderdik. Lütfen e-mail aktivasyonunuzu yapın", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } else if (type.equals(GirisType.girisserversorunu)) {
            Toast.makeText(this.getBaseContext(), "Lütfen internet bağlantınızı kontrol edin", Toast.LENGTH_LONG).show();

        } else if (type.equals(GirisType.facegirisfail)) {


        } else if (type.equals(GirisType.facegirisvar)) {
            prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("id", id);
            editor.putString("temp_key", temp_key + "");
            GeneralSync.id = id;
            GeneralSync.temp_key = temp_key;
            editor.apply();
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        } else if (type.equals(GirisType.facegirisyok)) {
            prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("id", id);
            editor.putString("temp_key", temp_key + "");
            GeneralSync.id = id;
            GeneralSync.temp_key = temp_key;
            editor.apply();
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        } else if (type.equals(GirisType.mailgirisfail)) {


        } else if (type.equals(GirisType.mailgirisvar)) {
            prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("id", id);
            editor.putString("temp_key", temp_key + "");
            GeneralSync.id = id;
            GeneralSync.temp_key = temp_key;
            editor.apply();
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        } else if (type.equals(GirisType.mailgirisyok)) {
            prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("id", id);
            editor.putString("temp_key", temp_key + "");
            GeneralSync.id = id;
            GeneralSync.temp_key = temp_key;
            editor.apply();
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }


    }


    public void misafir_girisi() {
        GeneralSync.id = -1;
        GeneralSync.temp_key = "1234onurbarismertcan1234";
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public int compareVersionNames(String oldVersionName, String newVersionName) {
        int res = 0;

        String[] oldNumbers = oldVersionName.split("\\.");
        String[] newNumbers = newVersionName.split("\\.");

        // To avoid IndexOutOfBounds
        int maxIndex = Math.min(oldNumbers.length, newNumbers.length);

        for (int i = 0; i < maxIndex; i ++) {
            int oldVersionPart = Integer.valueOf(oldNumbers[i]);
            int newVersionPart = Integer.valueOf(newNumbers[i]);

            if (oldVersionPart < newVersionPart) {
                res = -1;
                break;
            } else if (oldVersionPart > newVersionPart) {
                res = 1;
                break;
            }
        }

        // If versions are the same so far, but they have different length...
        if (res == 0 && oldNumbers.length != newNumbers.length) {
            res = (oldNumbers.length > newNumbers.length)?1:-1;
        }

        return res;
    }

}

