package com.tagreed.abnd.booklistapp;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Tagreed on 9/17/2017.
 */

public class LoadBook  extends AsyncTask<String, Void, ArrayList<Books>> {

    public MainActivity mMainActivity;
    private final String LOG_TAG = LoadBook.class.getSimpleName();
    private final Context mContext;

    public LoadBook(Context context, MainActivity mainActivity) {
        mContext = context;
        mMainActivity = mainActivity;
    }

    private ArrayList<Books> getBookFromJson (String booksJsonS)
        throws JSONException{

        ArrayList<Books> bookList = new ArrayList();

        final String BOOK_ITEMS = "items";
        final String BOOK_VOLUME_INFO = "volumeInfo";
        final String BOOK_TITLE = "title";
        final String BOOK_AUTHOR = "authors";

        try {
            JSONObject bookJson = new JSONObject(booksJsonS);
            JSONArray itemsArray = bookJson.getJSONArray(BOOK_ITEMS);

            for (int i=0; i<itemsArray.length(); i++){
                String title="";
                String author="";

                JSONObject bookInfo = itemsArray.getJSONObject(i);
                JSONObject volumeInfoJason = bookInfo.getJSONObject(BOOK_VOLUME_INFO);

                title = volumeInfoJason.getString(BOOK_TITLE);

                JSONArray authorsArray = volumeInfoJason.getJSONArray(BOOK_AUTHOR);
                for (int j=0; j<authorsArray.length(); j++){
                    if (j ==0){
                        author += authorsArray.getString(j);
                    }else {
                        author += ", " + authorsArray.getString(j);
                    }
                }
                Books book = new Books(author,title);
                boolean isBookInList = false;
                for(Books b: bookList){
                    if(b.getBookTitle().equals(title)){
                        isBookInList = true;
                    }
                }
                if(!isBookInList){
                    bookList.add(book);
                }
            }
            Log.d(LOG_TAG,"Book Load Completed");
        }catch (JSONException e){
            Log.e(LOG_TAG, e.getMessage(),e);
            e.printStackTrace();
        }
        return bookList;
    }

    @Override
    protected ArrayList<Books> doInBackground (String... params) {
        if (params.length == 0){
            return null;
        }
        String keywordQuery = params[0];

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String booksJason = null;
        int numMaxResult = 15;
        String order = "newest";

        try{
            final String GOOGLE_BOOKS_BASE_URL = "https://www.googleapis.com/books/v1/volumes?";
            final String QUERY_PARAM = "q";
            final String MAX_PARAM = "maxResults";
            final String ORDER_PARAM = "orderBy";

            Uri builtUri = Uri.parse(GOOGLE_BOOKS_BASE_URL).buildUpon().appendQueryParameter(QUERY_PARAM,params[0]).appendQueryParameter(MAX_PARAM,Integer.toString(numMaxResult)).appendQueryParameter(ORDER_PARAM,order).build();

            URL url = new URL(builtUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if(inputStream == null){
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null){
                buffer.append(line + "\n");
            }

            if (buffer.length()==0){
                return null;
            }
            booksJason = buffer.toString();
            return  getBookFromJson(booksJason);
        }catch (IOException e){
            Log.e(LOG_TAG,"Error",e);
        }catch (JSONException e){
            Log.e(LOG_TAG,e.getMessage(),e);
            e.printStackTrace();
        }finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if (reader != null){
                try {
                    reader.close();
                }catch (final IOException e){
                    Log.e (LOG_TAG,"Error Ending Stream",e);
                }
            }
        }
        return null;
    }
    @Override
    protected void onPostExecute(ArrayList<Books> result){
        if (result != null) {
            mMainActivity.refreshBookList(result);
        }
    }
}
