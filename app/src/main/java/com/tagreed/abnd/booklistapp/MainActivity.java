package com.tagreed.abnd.booklistapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static final String BOOK_LIST_VALUE = "bookListValue";
    ArrayList<Books> books = new ArrayList<>();
    private BooksAdapter mBookAdapter;
    private ListView mListView;
    private String mKeyword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            books = savedInstanceState.getParcelableArrayList(BOOK_LIST_VALUE);
        }

        mBookAdapter = new BooksAdapter(this, books);

        mListView = (ListView) findViewById(R.id.book_list);
        View emptyView = findViewById(R.id.empty_book_list);
        mListView.setEmptyView(emptyView);
        mListView.setAdapter(mBookAdapter);

        final EditText searchingKeyWord = (EditText) findViewById(R.id.search_text);
        Button searchButton = (Button) findViewById(R.id.serch_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mKeyword = searchingKeyWord.getText().toString();
                searchBooks();
            }
        });
    }

    private void searchBooks() {
        LoadBook bookList = new LoadBook(this, this);
        bookList.execute(mKeyword);
    }

    public void refreshBookList(ArrayList<Books> result) {
        mBookAdapter.clear();
        for (Books books : result) {
            mBookAdapter.add(books);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList(BOOK_LIST_VALUE, books);
        super.onSaveInstanceState(savedInstanceState);
    }
}
