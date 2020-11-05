package com.bombadu.rxtime2;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/*
    From Callable is useful when calling objects from room database or sqlite
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


// create Observable (method will not execute yet)
        Observable<List<Task>> callable = Observable
                .fromCallable(new Callable<List<Task>>() { //Callable
                    @Override
                    public List<Task> call() throws Exception {
                        return MyDatabase.getTask();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

// method will be executed since now something has subscribed
        callable.subscribe(new Observer<Task>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Task task) {
                Log.d(TAG, "onNext: : " + task.getDescription());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}