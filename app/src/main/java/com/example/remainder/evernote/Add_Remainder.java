package com.example.remainder.evernote;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class Add_Remainder extends ActionBarActivity implements View.OnClickListener{

    final static String DATE_FORMAT = "dd-MM-yy";
    final static String TIME_FORMAT = "HH:mm";

    DatePickerDialog date;
    TimePickerDialog time;
    EditText date1, time1, des1;
    DBAdapter db = new DBAdapter(this);
    int year2, month2, day2, hours2, minutes2;

    public static final String THEME_PREF = "Theme";
    public static final String THEME_PREF_NAME = "ThemeName";

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    HashMap<String,String> listItemSelected = null;
    Integer reminderId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__remainder);


        Calendar cal = Calendar.getInstance();

        date = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        EditText et = (EditText) findViewById(R.id.editText2);
                        et.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-"
                                + year);
                    }
                }, year2 = cal.get(Calendar.YEAR),
                month2 = cal.get(Calendar.MONTH),
                day2 = cal.get(Calendar.DAY_OF_MONTH));

        time = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        // TODO Auto-generated method stub
                        EditText et1 = (EditText) findViewById(R.id.editText3);
                        et1.setText(hourOfDay + ":" + minute);
                    }
                }, hours2 = cal.get(Calendar.HOUR_OF_DAY),
                minutes2 = cal.get(Calendar.MINUTE), false);

        date1 = (EditText) findViewById(R.id.editText2);
        date1.setOnClickListener(this);

        time1 = (EditText) findViewById(R.id.editText3);
        time1.setOnClickListener(this);

        des1 = (EditText) findViewById(R.id.editText1);

        Intent i = getIntent();
        if (i != null && i.hasExtra("message")) {
            if (i.hasExtra("id")) {
                reminderId = i.getIntExtra("id", 0);
            }
            des1.setText(i.getStringExtra("message"));
            date1.setText(i.getStringExtra("date"));
            time1.setText(i.getStringExtra("time"));

            int notificationId = i.getExtras().getInt("notificationID");
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (nm != null) {
                nm.cancel(notificationId);
            }
        }
        Button submit = (Button) findViewById(R.id.button1);
        submit.setOnClickListener(this);

        Button cancel = (Button) findViewById(R.id.button2);
        cancel.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add__remainder, menu);
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
        switch (view.getId()) {
            case R.id.editText2:
                date.show();
                break;

            case R.id.editText3:
                time.show();
                break;

            case R.id.button1:

                String s1 = des1.getText().toString();
                String s2 = date1.getText().toString();
                String s3 = time1.getText().toString();

                if (s1.equals("")) {
                    // Toast.makeText(this, "Please enter the details",
                    // Toast.LENGTH_SHORT).show();
                    des1.setError("Description cannot be empty");

                } else {

                    if (isDateValid(s2)) {

                        if (isTimeValid(s3)) {
                            // ---add a Record---
                            db.open();
                            long returnId;
                            if (reminderId != 0) {
                                returnId = db
                                        .updateReminder(reminderId, s1, s2, s3);
                                setAlarm(reminderId, s1, s2, s3);

                            } else {
                                returnId = db.insertReminder(s1, s2, s3);
                            //    Toast.makeText(this,"Date  : "+s2+"\nTime : "+s3, Toast.LENGTH_LONG).show();
                                setAlarm(returnId, s1, s2, s3);

                            }
                            if (returnId != 0) {
                                Toast.makeText(this, "Reminder saved succesfully",
                                        Toast.LENGTH_LONG).show();

                                Intent i = new Intent(this, RemainderView.class);
                                startActivity(i);

                            } else {
                                Toast.makeText(this, "Reminder could not be saved",
                                        Toast.LENGTH_LONG).show();
                            }
                            db.close();
                            // Toast.makeText(this, "Data Entered is "+s1+
                            // " , "+s2+" & "+s3,Toast.LENGTH_LONG).show();
                        } else {
                            time1.setError("Invalid time format (HH:mm)");
                        }
                    } else {
                        date1.setError("Invalid date format (dd-mm-yy)");
                    }
                }

                break;
            case R.id.button2:
                Intent i = new Intent(this, RemainderView.class);
                startActivity(i);
                break;
        }
    }

    public static boolean isDateValid(String date) {
        if (!date.equals("")) {
            try {
                DateFormat df = new SimpleDateFormat(DATE_FORMAT);
                df.setLenient(false);
                df.parse(date);
                return true;
            } catch (ParseException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean isTimeValid(String time) {
        if (!time.equals("")) {
            try {
                DateFormat df = new SimpleDateFormat(TIME_FORMAT);
                df.setLenient(false);
                df.parse(time);
                return true;
            } catch (ParseException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    private void setAlarm(long id, String message, String date, String time) {
        Calendar objCalendar = Calendar.getInstance();
        DateFormat dfd, dft;
        Date d = null, t = null;
        try {
            dfd = new SimpleDateFormat(DATE_FORMAT);
            dfd.setLenient(false);
            d = dfd.parse(date);
        } catch (ParseException e) {
        }
        try {
            dft = new SimpleDateFormat(TIME_FORMAT);
            dft.setLenient(false);
            t = dft.parse(time);
        } catch (ParseException e) {        }


        /* objCalendar.set(Calendar.YEAR, d.getYear()); //
		 * objCalendar.set(Calendar.YEAR, objCalendar.get(Calendar.YEAR));
		 * objCalendar.set(Calendar.MONTH, d.getMonth());
		 * objCalendar.set(Calendar.DAY_OF_MONTH, d.getDay());
		 * objCalendar.set(Calendar.HOUR_OF_DAY, t.getHours());
		 * objCalendar.set(Calendar.MINUTE, t.getMinutes());
		 * objCalendar.set(Calendar.SECOND, 0);
		 * objCalendar.set(Calendar.MILLISECOND, 0);
		 */
    //    objCalendar.set(Calendar.YEAR, date.getYear());
        // objCalendar.set(Calendar.YEAR, objCalendar.get(Calendar.YEAR));

        objCalendar.set(Calendar.YEAR, d.getYear());
        objCalendar.set(Calendar.MONTH, d.getMonth());
        objCalendar.set(Calendar.DAY_OF_MONTH, d.getDay());
        objCalendar.set(Calendar.HOUR_OF_DAY, t.getHours());
        objCalendar.set(Calendar.MINUTE, t.getMinutes());
        objCalendar.set(Calendar.SECOND, 0);
        objCalendar.set(Calendar.MILLISECOND, 0);



        //objCalendar.set(Calendar.AM_PM, Calendar.PM);

        // Log.d("myser", Date.toString(objCalendar.getTime()));

		/*
		 * Intent i = new Intent(this, ReminderDisplay.class);
		 * i.putExtra("message", message); PendingIntent pi =
		 * PendingIntent.getActivity(this, (int)id, i,
		 * PendingIntent.FLAG_UPDATE_CURRENT);
		 *
		 * AlarmManager objAlarmManager = (AlarmManager)
		 * getSystemService(ALARM_SERVICE);
		 * objAlarmManager.set(AlarmManager.RTC_WAKEUP,
		 * objCalendar.getTimeInMillis(), pi);
		 */

        Intent i3 = new Intent(this, ReminderDisplay.class);
        i3.putExtra("message", message);
        i3.putExtra("date",date);
        i3.putExtra("time",time);
        i3.putExtra("id",id);
        PendingIntent pi = PendingIntent.getActivity(this, (int)id, i3,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager objAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

       // objAlarmManager.set(AlarmManager.RTC_WAKEUP,objCalendar.getTimeInMillis(), pi);

        objAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,objCalendar.getTimeInMillis(), 100000, pi);

        finish();

    /*

    private void setAlarm() {
        cal = new GregorianCalendar();
        cal.setTimeInMillis(System.currentTimeMillis());
        day = cal.get(Calendar.DAY_OF_WEEK);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);

        Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
        i.putExtra(AlarmClock.EXTRA_HOUR, hour + Integer.parseInt(etHour.getText().toString()));
        i.putExtra(AlarmClock.EXTRA_MINUTES, minute + Integer.parseInt(etMinute.getText().toString()));
        i.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        startActivity(i);
*/

    }


}
