package com.tagreed.abnd.booklistapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Tagreed on 9/17/2017.
 */

public class BooksAdapter extends ArrayAdapter<Books> {
    public BooksAdapter (Activity context, ArrayList<Books> books ){super(context,0,books);}

    @Override
    public View getView (int position , View convertView, ViewGroup parent){
        Books book = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.book_list_item,parent,false);
        }

        TextView bookTitleTextView = (TextView) convertView.findViewById(R.id.book_title);
        bookTitleTextView.setText(book.getBookTitle());

        TextView bookAuthorTextView = (TextView) convertView.findViewById(R.id.book_author);
        bookAuthorTextView.setText(book.getBookAuthor());

        return convertView;
    }
}
