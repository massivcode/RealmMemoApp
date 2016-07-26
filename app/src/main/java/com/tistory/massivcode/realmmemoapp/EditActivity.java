package com.tistory.massivcode.realmmemoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.util.Date;

@SuppressWarnings("ALL")
public class EditActivity extends AppCompatActivity {

    private EditText mTitleEditText, mContentsEditText;
    private int mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        receiveData();
        initViews();
    }

    private void receiveData() {
        Intent intent = getIntent();

        if (intent != null) {
            mId = intent.getIntExtra("id", -1);
            System.out.println(mId);
        }
    }

    private void initViews() {
        mTitleEditText = (EditText) findViewById(R.id.edit_title_et);
        mContentsEditText = (EditText) findViewById(R.id.edit_contents_et);

        findViewById(R.id.edit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mTitleEditText.getText().toString();
                String contents = mContentsEditText.getText().toString();

                Memo memo = new Memo(mId, title, contents, new Date(System.currentTimeMillis()));

                Intent intent = new Intent();
                intent.putExtra("data", memo);

                setResult(RESULT_OK, intent);
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
