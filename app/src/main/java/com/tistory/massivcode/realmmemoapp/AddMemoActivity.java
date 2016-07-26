package com.tistory.massivcode.realmmemoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.Date;

public class AddMemoActivity extends AppCompatActivity {

    private EditText mTitleEditText, mContentsEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memo);

        initViews();
    }

    private void initViews() {
        mTitleEditText = (EditText) findViewById(R.id.add_title_et);
        mContentsEditText = (EditText) findViewById(R.id.add_contents_et);

        findViewById(R.id.add_save_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mTitleEditText.getText().toString();
                String contents = mContentsEditText.getText().toString();

                Intent callback = new Intent();
                callback.putExtra("data", new Memo(title, contents, new Date(System.currentTimeMillis())));
                setResult(RESULT_OK, callback);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}
