package vn.yopi.tienpro.chat_v1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class registerActivity extends AppCompatActivity {

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.117.2:3000");
        } catch (URISyntaxException e) {}
    }

    private Button btn_register;
    private EditText edt_register;
    private String text_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btn_register = (Button) findViewById(R.id.btn_register);
        edt_register = (EditText)findViewById(R.id.edt_register);


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text_name = edt_register.getText().toString();
                mSocket.emit("register_user",text_name);
                edt_register.setText("");
                mSocket.on("register_res",register_res);

            }
        });

        mSocket.connect();
    }

    // receive result register
    private Emitter.Listener register_res = new Emitter.Listener() {
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
                            Toast.makeText(registerActivity.this, "resgister completed", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),loginActivity.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(registerActivity.this, "register fail", Toast.LENGTH_SHORT).show();
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
