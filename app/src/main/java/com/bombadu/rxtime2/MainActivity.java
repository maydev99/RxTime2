package com.bombadu.rxtime2;

import android.os.Bundle;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/*
   Transformation Operators - Debounce
    The Debounce operator filters out items emitted by the source Observable that are rapidly
    followed by another emitted item. You can add a time delay to slow down the emittions.

    Good for execution a search after a time delay(instead of after every letter typed)
    Helps limit server requests

    Uses Whartons RxBinding library
 */

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    //ui
    private SearchView searchView;

    //vars
    private CompositeDisposable disposables = new CompositeDisposable();
    private long timeSinceLastRequest; //for printouts only. Not part of Logic.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = findViewById(R.id.search_view);
        timeSinceLastRequest = System.currentTimeMillis();

        //Create Observable
        Observable<String> observableQueryText = Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {
                        //listen for test input in searchview
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(final String newText) {
                                if (!emitter.isDisposed()) {
                                    emitter.onNext(newText); //pass the query to the emitter
                                }
                                return false;
                            }
                        });
                    }
                })
                .debounce(500, TimeUnit.MILLISECONDS) //Apply Debounce() operator to limit requests
                .subscribeOn(Schedulers.io());

        //Subscribe to and Observer
        observableQueryText.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposables.add(d);
            }

            @Override
            public void onNext(@NonNull String s) {
                Log.d(TAG, "onNext: time since last request: " + (System.currentTimeMillis() - timeSinceLastRequest));
                Log.d(TAG, "onNext: search query: " + s);
                timeSinceLastRequest = System.currentTimeMillis();

                //method sending request to server
                sendRequestToServer(s);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
    //Fake method for sending request to server
    private void sendRequestToServer(String query) {
        //do nothing
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }
}