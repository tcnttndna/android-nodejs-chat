package vn.yopi.tienpro.chat_v1;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by Admin-PC on 12/26/2017.
 */

public class fragment_list_friend extends Fragment {

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.117.2:3000");
        } catch (URISyntaxException e) {}
    }
    private ArrayList<String> allUser = new ArrayList<>();
    private ListView lv_allUser;
    private ArrayAdapter<String> adapter;
    private String nickName_user = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_friend,container,false);
        lv_allUser = (ListView)view.findViewById(R.id.lv_allUser);

        // get extra bundle nickname user
        if (getActivity().getIntent().hasExtra("nickName")){
            nickName_user = getActivity().getIntent().getStringExtra("nickName");
        }

        //socket
        mSocket.on("send_all_user",getAllUser);
        mSocket.on("user_new",getUserNew);
        mSocket.connect();

        //adapter
        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,allUser);
        return view;
    }

    // receive all user
    private Emitter.Listener getAllUser = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject)args[0];
                    JSONArray array;
                    try {
                        array = data.getJSONArray("users");
                        for (int i = 0;i<array.length();i++){
                            allUser.add(array.getString(i));
                        }
                        adapter.notifyDataSetChanged();


                    }catch (JSONException e){
                        return;
                    }

                }
            });
        }
    };

    // receive user new
    private Emitter.Listener getUserNew = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject)args[0];
                    String userNew;

                    try {
                        userNew = data.getString("user_new");
                        if (userNew != nickName_user ){
                            allUser.add(userNew);
                            adapter.notifyDataSetChanged();
                        }


                    } catch (JSONException e) {
                        return;
                    }

                }
            });
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lv_allUser.setAdapter(adapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSocket.disconnect();
    }
}
