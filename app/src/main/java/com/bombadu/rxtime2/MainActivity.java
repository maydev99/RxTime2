package com.bombadu.rxtime2;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/*
   Transformation Operators - Buffer
   Useful for collecting object and emitting as a group

   This example emits the list in groups of 2

 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create an Observable
        Observable<Task> taskObservable = Observable
                .fromIterable(DataSource.createTasksList())
                .subscribeOn(Schedulers.io());

        taskObservable
                .buffer(2)// Apply Buffer() operator
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Task>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<Task> tasks) {
                        Log.d(TAG, "onNext: bundle results: ---------------");
                        for (Task task: tasks) {
                            Log.d(TAG, "onNext: " + task.getDescription());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


}