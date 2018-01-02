package vn.yopi.tienpro.chat_v1;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class home_chatActivity extends AppCompatActivity {

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.117.2:3000");
        } catch (URISyntaxException e) {}
    }
    private ImageButton img_btn_friend,img_btn_msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_chat);
        img_btn_friend = (ImageButton)findViewById(R.id.btn_listFriend);
        img_btn_msg = (ImageButton)findViewById(R.id.btn_msg);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fl = fm.beginTransaction();
        fl.add(R.id.frame_layout, new fragment_list_friend());
        fl.commit();

        mSocket.connect();
    }
}
