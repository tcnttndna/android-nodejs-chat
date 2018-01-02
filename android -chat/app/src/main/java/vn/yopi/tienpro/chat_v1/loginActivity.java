package vn.yopi.tienpro.chat_v1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class loginActivity extends AppCompatActivity {
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.117.2:3000");
        } catch (URISyntaxException e) {}
    }
    private Button btn_login;
    private EditText edt_nickName;
    private TextView tv_register;
    private String nickName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_login = (Button)findViewById(R.id.btn_login);
        edt_nickName = (EditText)findViewById(R.id.edt_nickname_login);
        tv_register = (TextView)findViewById(R.id.tv_register);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nickName = edt_nickName.getText().toString();
                mSocket.emit("login",nickName);
                mSocket.on("login_res",getResultLogin);
            }
        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent = new Intent(loginActivity.this,registerActivity.class);
              startActivity(intent);
            }
        });

        mSocket.connect();
    }

    private Emitter.Listener getResultLogin = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject)args[0];
                    int result;

                    try {
                        result = data.getInt("result");
                        if (result == 1){
                            Toast.makeText(loginActivity.this, "login completed", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),home_chatActivity.class);
                            intent.putExtra("nickName",nickName);
                            startActivity(intent);
                        }else {
                            Toast.makeText(loginActivity.this, "login fail", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        mSocket.disconnect();
    }
}
