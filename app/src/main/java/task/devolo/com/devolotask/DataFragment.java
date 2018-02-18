package task.devolo.com.devolotask;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ivanv on 2/5/2018.
 */

public class DataFragment extends Fragment {

    private String TAG = MainActivity.class.getSimpleName();

    ArrayList<HashMap<String, String>> dataList;

    ArrayList<String> selectList;

    OnDataPass dataPasser;

    ListView listView;

    String str;

    Bundle bundle;

    Button button;

    String avatar_url;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataList = new ArrayList<>();

        selectList = new ArrayList<>();

        bundle = this.getArguments();
        str = bundle.getString("json");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.data_fragment, container, false);

        button = view.findViewById(R.id.image_fragment);
        listView = view.findViewById(R.id.list);

        final String jsonStr = bundle.getString("json");

        HashMap<String, String> dataItem;

        if (jsonStr != null){
            try{

                JSONArray jsonArray = new JSONArray(jsonStr);

                for(int i = 0; i < jsonArray.length(); i++){
                    dataItem = new HashMap<>();

                    JSONObject c = jsonArray.getJSONObject(i);

                    String title = c.getString("title");
                    JSONObject user = c.getJSONObject("user");
                    String login = user.getString("login");

                    // adding each child node to HashMap key => value
                    dataItem.put("title", title);
                    dataItem.put("login", login);
                    dataList.add(dataItem);
                }
            }
            catch (final JSONException e){
                Log.e(TAG, "Json parsing error: " + e.getMessage());

                Toast.makeText(getActivity(),
                        "Json parsing errorrrrrrrrrrrrrr: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }

        final ListAdapter adapter = new SimpleAdapter(view.getContext(), dataList,
                R.layout.list_item, new String[]{ "title", "login"},
                new int[]{R.id.title, R.id.login});
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                try {
                    JSONArray res = new JSONArray(jsonStr);
                    JSONObject c = res.getJSONObject(i);
                    JSONObject user = c.getJSONObject("user");
                    avatar_url = user.getString("avatar_url");
                    selectList.add(avatar_url);
                    dataPasser.onDataPass(selectList);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),
                            "Json parsing error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                Toast.makeText(getActivity(),
                        "You selected " + selectList.size() + " items",
                        Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public interface OnDataPass {
        ArrayList<String> onDataPass(ArrayList<String> data);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }

    public void passData(ArrayList<String> data) {
        dataPasser.onDataPass(data);
    }
}
