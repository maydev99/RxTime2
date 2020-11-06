package com.bombadu.rxtime2;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jakewharton.rxbinding3.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import kotlin.Unit;

/*
   Transformation Operators - ThrottleFirst
    Reduces button spamming
    RxView make button click observable
    Example limit registering button click to every 4 seconds

    Uses Whartons RxBinding library
 */

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    //ui
    private Button button;

    //vars
    private CompositeDisposable disposables = new CompositeDisposable();
    private long timeSinceLastRequest; //for printouts only. Not part of Logic.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        timeSinceLastRequest = System.currentTimeMillis();

        //set a click listener to the button with RxBinding Library
        RxView.clicks(button)
                .throttleFirst(4000,TimeUnit.MILLISECONDS) //Throttles clicks so 4000 ms pass before registering another click
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Unit>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Unit unit) {
                        Log.d(TAG, "onNext: time since last clicked: " + (System.currentTimeMillis() - timeSinceLastRequest));
                        someMethod();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void someMethod() {
        timeSinceLastRequest = System.currentTimeMillis();
        //do something
        Toast.makeText(this, "you clicked the button!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear(); //Dispose Observable
    }
}