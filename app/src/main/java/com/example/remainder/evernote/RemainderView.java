package com.example.remainder.evernote;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class RemainderView extends ActionBarActivity implements View.OnClickListener {

    List<HashMap<String,String>> reminders;
    HashMap<Integer, Integer> reminderIds;
    DBAdapter db = new DBAdapter(this);
    ListView lv;
    ListAdapter la;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    HashMap<String,String> listItemSelected = null;
    Integer reminderId;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remainder_view);
        lv = (ListView) findViewById(R.id.listView1);

        registerForContextMenu(lv);
        fillListView();
        b1= (Button) findViewById(R.id.home);
        b1.setOnClickListener(this);

    }
    private void fillListView() {
        db.open();
        Cursor c = db.getAllReminders();
        int i=0;
        reminderIds = new HashMap<Integer, Integer>();
        reminders = new ArrayList<HashMap<String,String>>();

        if (c.moveToFirst()) {
            do {
                HashMap<String,String> hm = new HashMap<String,String>();
                hm.put("line1", c.getString(1));
                hm.put("line2", c.getString(2) + " " + c.getString(3));
                reminders.add(hm);
                reminderIds.put(i, c.getInt(0));
                i++;
            } while (c.moveToNext());
        }
        la = new SimpleAdapter(getApplicationContext(), reminders,
                android.R.layout.two_line_list_item, new String[] {"line1", "line2"},
                new int[] {android.R.id.text1, android.R.id.text2});
        lv.setAdapter(la);

        db.close();

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_remainder_view, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);
        finish();

    }
}
