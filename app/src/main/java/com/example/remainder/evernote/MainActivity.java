package com.example.remainder.evernote;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    ImageButton mRemainder, mNote, mImage, mList, mExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRemainder = (ImageButton) findViewById(R.id.ImgRem);
        mNote = (ImageButton) findViewById(R.id.ImgNote);
        mImage = (ImageButton) findViewById(R.id.ImgImg);
        mList = (ImageButton) findViewById(R.id.ImgList);
        mExit = (ImageButton) findViewById(R.id.ImgExit);


        mRemainder.setOnClickListener(this);
        mNote.setOnClickListener(this);
        mImage.setOnClickListener(this);
        mList.setOnClickListener(this);
        mExit.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ImgRem:
                Intent i=new Intent(this,Add_Remainder.class);
                startActivity(i);
                finish();
                break;
            case R.id.ImgNote:
                Intent k=new Intent(this,Add_Note.class);
                startActivity(k);
                finish();

                break;
            case R.id.ImgImg:
                finish();
                break;

            case R.id.ImgList:
                Intent j=new Intent(this,RemainderView.class);
                startActivity(j);
                finish();
                break;


            case R.id.ImgExit:
                finish();
                break;

        }
    }
}