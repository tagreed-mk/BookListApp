package com.tagreed.abnd.booklistapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tagreed on 9/17/2017.
 */

public class Books implements Parcelable{
    private String mBookAuthor;
    private String mBookTitle;

    public Books(String BookAuthor, String BookTitle) {
        mBookAuthor = BookAuthor;
        mBookTitle = BookTitle;
    }

    private Books (Parcel in){
        mBookAuthor = in.readString();
        mBookTitle = in.readString();
    }

    public String getBookAuthor() {
        return mBookAuthor;
    }

    public String getBookTitle() {
        return mBookTitle;
    }

    @Override
    public int describeContents(){return 0;}

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(mBookAuthor);
        dest.writeString(mBookTitle);
    }

    public static final Parcelable.Creator<Books> CREATOR = new Parcelable.Creator<Books>(){
        public Books createFromParcel (Parcel in ) {return new Books(in);}
        public Books[] newArray(int size) {return new Books[size];}
    };
}
