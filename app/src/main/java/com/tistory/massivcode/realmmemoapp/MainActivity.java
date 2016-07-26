package com.tistory.massivcode.realmmemoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_ADD_MEMO = 1000;
    private final int REQUEST_EDIT_MEMO = 2000;

    private ListView mListView;
    private MemoAdapter mAdapter;
    private ArrayList<Memo> mData;
    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRealm();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initRealm() {
        RealmConfiguration configuration = new RealmConfiguration.Builder(this)
                .name("RealmMemoApp")
                .build();

        mRealm = Realm.getInstance(configuration);

        mData = CrudWrapper.Instance.selectAllMemos(mRealm);
        mAdapter = new MemoAdapter(this, mData);
    }

    private void initViews() {
        mListView = (ListView) findViewById(R.id.main_lv);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Memo memo = mData.get(position);
                System.out.println(memo);
                startActivityForResult(new Intent(MainActivity.this, EditActivity.class)
                                .putExtra("id", memo.getId())
                        , REQUEST_EDIT_MEMO);
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Memo memo = mData.get(position);
                CrudWrapper.Instance.deleteMemo(mRealm, memo);
                mAdapter.deleteMemo(position);
                return true;
            }
        });

        findViewById(R.id.main_add_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, AddMemoActivity.class), REQUEST_ADD_MEMO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ADD_MEMO) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(MainActivity.this, "메모를 추가하였습니다!", Toast.LENGTH_SHORT).show();
                Memo memo = data.getParcelableExtra("data");
                CrudWrapper.Instance.insertMemo(mRealm, memo);
                mData.add(memo);
                mAdapter.notifyDataSetChanged();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, "메모 작성을 취소하였습니다!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_EDIT_MEMO) {
            if (resultCode == RESULT_OK) {
                Memo memo = data.getParcelableExtra("data");
                CrudWrapper.Instance.updateMemo(mRealm, memo);
                mAdapter.swapMemo(memo);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, "메모 수정을 취소하였습니다!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
