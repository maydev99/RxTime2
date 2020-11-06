package com.bombadu.rxtime2;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.jakewharton.rxbinding3.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import kotlin.Unit;

/*
   Transformation Operators - Buffer2
    Add Composite Disposables so you can dispose of these observable when the activity is closed
    This example counts how many time a button is clicked in 4 seconds

    RxBinding dependency added for this example
 */

public class MainActivity extends AppCompatActivity {

    CompositeDisposable disposables = new CompositeDisposable();

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //detect clicks to a button
        RxView.clicks(findViewById(R.id.button))
                .map(new Function<Unit, Integer>() { //convert the detected clicks to an integer

                    @Override
                    public Integer apply(@NonNull Unit unit) throws Exception {
                        return 1;
                    }
                })
                .buffer(4, TimeUnit.SECONDS)//capture all the clicks during a 4 second interval
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Integer>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposables.add(d); // add to disposables so you can clear onDestroy
                    }

                    @Override
                    public void onNext(@NonNull List<Integer> integers) {
                        //produces a comma separated list. example{1, 1, 1, 1, 1, 1}
                        Log.d(TAG, "onNext: You clicked " + integers.size() + " times in 4 seconds!");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }
}