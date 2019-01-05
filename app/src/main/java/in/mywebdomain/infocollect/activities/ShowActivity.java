package in.mywebdomain.infocollect.activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import in.mywebdomain.infocollect.R;
import in.mywebdomain.infocollect.adapters.CardRecyclerAdapter;
import in.mywebdomain.infocollect.adapters.RecycleAdapter;
import in.mywebdomain.infocollect.entities.TheClient;
import in.mywebdomain.infocollect.others.Constants;
import in.mywebdomain.infocollect.others.RKSHttp;

public class ShowActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecycleAdapter recycleAdapter;
    CardRecyclerAdapter cardRecyclerAdapter;
    List<TheClient> list;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        recyclerView = findViewById(R.id.show_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setVisibility(View.INVISIBLE);
        progressBar = findViewById(R.id.show_progressbar);
        progressBar.setVisibility(View.VISIBLE);

        list = new ArrayList<>();
        String pno = "";
        try {
            // TODO : Show ProgressBar, Hide List and Load Data
            new LoadShowData(this).execute(pno);
        } catch (Exception e) {
            Toast.makeText(ShowActivity.this, "Please make an Entry first", Toast.LENGTH_SHORT).show();
        }
//        loadTestList();
    }

    private static class LoadShowData extends AsyncTask<String, Integer, String> {
        private WeakReference<ShowActivity> weakReference;

        LoadShowData(ShowActivity showActivity) {
            weakReference = new WeakReference<>(showActivity);
        }

        @Override
        protected String doInBackground(String... strings) {
            String res = null;
            try {
                res = RKSHttp.postOkHttp(Constants.URL_DATA, "");
            } catch (Exception e) {
                e.printStackTrace();
                res = "4";
            }
            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s!=null && s.equals("[]")){
                Toast.makeText(getContext(), "No Entries", Toast.LENGTH_LONG).show();
                getContext().finish();
            }else if(s.length()>2) {
                List<TheClient> list1 = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        list1.add(getJSONObjectAsTheClient(jsonObject));
                    }

                    getContext().cardRecyclerAdapter = new CardRecyclerAdapter(list1, getContext());
                    getContext().recyclerView.setAdapter(getContext().cardRecyclerAdapter);
                    getContext().recyclerView.setVisibility(View.VISIBLE);
                    getContext().progressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("JSON RES", s);
                }
            }
            super.onPostExecute(s);
        }
        private ShowActivity getContext() {
            return weakReference.get();
        }

        private TheClient getJSONObjectAsTheClient(JSONObject jsonObject) {
            TheClient theClient = null;
            try {
                theClient = new TheClient(
                        jsonObject.getString("name"),
                        jsonObject.getString("pno"),
                        jsonObject.getString("dob"),
                        jsonObject.getString("lat"),
                        jsonObject.getString("lng"),
                        jsonObject.getString("address")
                        );
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return theClient;
        }
    }

    private void loadTestList() {
        list.add(new TheClient("name1", "192809821", "10/02/1999", "23.23231", "99.992", "B-1 adkjs sdjehf, ajkdhje, bbb"));
        list.add(new TheClient("name2", "292809821", "11/02/1999", "23.23231", "99.992", "B-1 adkjs sdjehf, ajkdhje, bbb"));
        list.add(new TheClient("name3", "392809821", "12/02/1999", "23.23231", "99.992", "B-1 adkjs sdjehf, ajkdhje, bbb"));
        list.add(new TheClient("name4", "492809821", "13/02/1999", "23.23231", "99.992", "B-1 adkjs sdjehf, ajkdhje, bbb"));
        list.add(new TheClient("name5", "592809821", "14/02/1999", "23.23231", "99.992", "B-1 adkjs sdjehf, ajkdhje, bbb"));
        list.add(new TheClient("name6", "692809821", "15/02/1999", "23.23231", "99.992", "B-1 adkjs sdjehf, ajkdhje, bbb"));
        list.add(new TheClient("name7", "792809821", "16/02/1999", "23.23231", "99.992", "B-1 adkjs sdjehf, ajkdhje, bbb"));
        list.add(new TheClient("name8", "892809821", "17/02/1999", "23.23231", "99.992", "B-1 adkjs sdjehf, ajkdhje, bbb"));
        list.add(new TheClient("name9", "992809821", "18/02/1999", "23.23231", "99.992", "B-1 adkjs sdjehf, ajkdhje, bbb"));
        list.add(new TheClient("name10", "102809821", "19/02/1999", "23.23231", "99.992", "B-1 adkjs sdjehf, ajkdhje, bbb"));
        list.add(new TheClient("name11", "112809821", "20/02/1999", "23.23231", "99.992", "B-1 adkjs sdjehf, ajkdhje, bbb"));
        list.add(new TheClient("name12", "122809821", "21/02/1999", "23.23231", "99.992", "B-1 adkjs sdjehf, ajkdhje, bbb"));

        recycleAdapter = new RecycleAdapter(list, this);
        cardRecyclerAdapter = new CardRecyclerAdapter(list, this);

        recyclerView.setAdapter(cardRecyclerAdapter);
    }
}
