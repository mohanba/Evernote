package com.example.remainder.evernote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class ReminderDisplay extends ActionBarActivity implements View.OnClickListener {

    DBAdapter db = new DBAdapter(this);

    int reminderId;
    String date1,time1;
    String s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_display);


        Button ok = (Button) findViewById(R.id.button1);
        ok.setOnClickListener(this);

        Button snooze = (Button) findViewById(R.id.button2);
        snooze.setOnClickListener(this);

        TextView display = (TextView) findViewById(R.id.textView4);


        Intent i = getIntent();
        s=i.getStringExtra("message");
    Toast.makeText(this,"the value of message is "+s,Toast.LENGTH_LONG).show();
        if (i != null && i.hasExtra("message")) {
            if (i.hasExtra("id")) {
            reminderId = i.getIntExtra("id", 0);
            }
            display.setText("Don't Forget: \n" + i.getStringExtra("message"));

        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                db.open();


			Boolean success= db.deleteReminder(s);
                Toast.makeText(this,"value of msg is"+s,Toast.LENGTH_LONG).show();

                if(success)
                {
                    Toast.makeText(this,"Successful removed from DB",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(this,"Failed to remove from DB",Toast.LENGTH_LONG).show();
                }
                db.close();


                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);


                finish();
                break;


            case R.id.button2:
                Intent j = new Intent(this, RemainderView.class);
                startActivity(j);
                finish();
                break;

        }
    }
}
