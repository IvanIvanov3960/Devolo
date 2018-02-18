package task.devolo.com.devolotask;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements DataFragment.OnDataPass{

    private String TAG = MainActivity.class.getSimpleName();

    GetJSONData getJSONData;

    ArrayList<HashMap<String, String>> usersList;

    FragmentManager manager;

    String jsonStr;

    Bundle bundletoDataList = new Bundle();

    Bundle bundleToImages = new Bundle();

    ArrayList<String> avatar_url = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usersList = new ArrayList<>();

        getJSONData = new GetJSONData();
        getJSONData.execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplication(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void imageFragment(View view) {
        if(avatar_url.size() < 1) {
            Toast.makeText(getApplicationContext(),
                    "Select at least one",
                    Toast.LENGTH_LONG).show();
        }else{
            bundleToImages.putStringArrayList("avatar_url", avatar_url);
            manager = getFragmentManager();

            ImageFragment imageFragment = new ImageFragment();
            imageFragment.setArguments(bundleToImages);
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.container, imageFragment, "imageFragment");
            transaction.commit();

        }
    }

    @Override
    public ArrayList<String> onDataPass(ArrayList<String> data) {
        Log.d("LOG","hello " + data);
        avatar_url = data;
        return  avatar_url;
    }

    private class GetJSONData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "https://api.github.com/repos/vmg/redcarpet/issues?state=closed";
            jsonStr = sh.makeServiceCall(url);
            bundletoDataList.putString("json", jsonStr);
            manager = getFragmentManager();

            DataFragment dataFragment = new DataFragment();
            dataFragment.setArguments(bundletoDataList);
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.container, dataFragment, "dataFragment");
            transaction.commit();

            Log.e(TAG, "Response from url: " + jsonStr);

//            HashMap<String, String> contact;
            if (jsonStr != null) {
                try {

                    // Getting JSON Array node
                    JSONArray pairsData = new JSONArray(jsonStr);

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }
}
