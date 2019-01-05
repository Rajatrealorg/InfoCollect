package in.mywebdomain.infocollect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.mywebdomain.infocollect.activities.EntryActivity;
import in.mywebdomain.infocollect.activities.ShowActivity;


/*
 *
 * Developer : RKS
 * 1. This is the 1st Activity
 * 2. It contains the Menus [Entry] and [Show]
 *
 *
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_main);
        View view =getSupportActionBar().getCustomView();

        testJSON();
    }
    public native String stringFromJNI();

	private void testJSON() {
		JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", "fullname");
            jsonObject.put("pno", "phone");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonArray.put(jsonObject);
        jsonArray.put(jsonObject);
        Log.i("JSON", jsonArray.toString());
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        if(item.getItemId() == R.id.main_menu_entry){
            intent = new Intent(MainActivity.this, EntryActivity.class);
        }
        else if(item.getItemId() == R.id.main_menu_show){
            intent = new Intent(MainActivity.this, ShowActivity.class);
        }
        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }
}
