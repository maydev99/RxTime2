package com.bombadu.rxtime2;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/*
   Transformation Operators - Map
   Convert Task to String List - outputs only the descriptions

   This can be done with boolean or longs as well
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Observable
                .fromIterable(DataSource.createTasksList())
                .map(new Function<Task, String>() { //you can also create the function below and pass it to Map)
                    @Override
                    public String apply(@NonNull Task task) throws Exception {
                        return task.getDescription();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String string) {
                        Log.d(TAG, "onNext: " + string);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    /*Function<Task, String> extractDescriptionFunction = new Function<Task, String>() {
        @Override
        public String apply(@NonNull Task task) throws Exception {
            Log.d(TAG, "apply: doing work on a thread: " + Thread.currentThread().getName());
            return task.getDescription();

        }
    };*/


}