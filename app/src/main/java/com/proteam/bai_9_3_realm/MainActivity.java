package com.proteam.bai_9_3_realm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.proteam.bai_9_3_realm.model.MyBook;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEditTitle;
    private EditText mEditUpdateTitle;
    private Button mBtnAdd;
    private Button mBtnDelete;
    private Button mBtnUpdate;
    private RecyclerView mRecyclerView;
    private Realm mRealm;
    private MyListAdapter myListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRealm = Realm.getInstance(MainActivity.this.getApplicationContext());

        mEditTitle = (EditText) findViewById(R.id.edit_title);
        mEditUpdateTitle = (EditText) findViewById(R.id.edit_title_update);
        mBtnAdd = (Button) findViewById(R.id.button_add);
        mBtnDelete = (Button) findViewById(R.id.button_remove);
        mBtnUpdate = (Button) findViewById(R.id.button_update);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        myListAdapter = new MyListAdapter(mRealm.allObjects(MyBook.class));
        mRecyclerView.setAdapter(myListAdapter);

        mBtnAdd.setOnClickListener(this);
        mBtnDelete.setOnClickListener(this);
        mBtnUpdate.setOnClickListener(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add:
                addRecordDatabase();
                break;
            case R.id.button_remove:
                removeRecordDatabase();
                break;
            case R.id.button_update:
                updateRecordDatabase();
                break;
        }
    }

    private void addRecordDatabase() {
        mRealm.beginTransaction();
        MyBook book = mRealm.createObject(MyBook.class);
        book.setTitle(getTrimmedTitle());
        mRealm.commitTransaction();


    }

    private void removeRecordDatabase() {
        mRealm.beginTransaction();
        RealmResults<MyBook> books = mRealm.where(MyBook.class).equalTo("title", getTrimmedTitle()).findAll();
        if (!books.isEmpty()) {
            for (int i = books.size() - 1; i >= 0; i--) {
                books.get(i).removeFromRealm();
            }
        }
        mRealm.commitTransaction();

    }

    private void updateRecordDatabase() {
        mRealm.beginTransaction();
        RealmResults<MyBook> books = mRealm.where(MyBook.class).equalTo("title", getTrimmedTitle()).findAll();
        if (!books.isEmpty()) {
            for (int i = books.size() - 1; i >= 0; i--) {
                books.get(i).setTitle(getTrimmedTitleUpdate());
            }
        };
        mRealm.copyToRealmOrUpdate(books);
        mRealm.commitTransaction();
    }

    private String getTrimmedTitle() {
        return mEditTitle.getText().toString().trim();
    }

    private String getTrimmedTitleUpdate() {
        return mEditUpdateTitle.getText().toString().trim();
    }

}
