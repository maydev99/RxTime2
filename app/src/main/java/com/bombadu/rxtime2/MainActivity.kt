package com.bombadu.rxtime2

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //final Task task = new Task("Walk the dog", false, 3);
        val intervalObservable = Observable
                .interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .takeWhile { aLong: Long ->
                    Log.d(TAG, "Test: " + aLong + ", thread: " + Thread.currentThread().name)
                    aLong <= 5
                }
                .observeOn(AndroidSchedulers.mainThread())
        intervalObservable.subscribe(object : Observer<Long> {
            override fun onSubscribe(d: Disposable) {}
            override fun onNext(aLong: Long) {
                Log.d(TAG, "onNext: $aLong")
            }

            override fun onError(e: Throwable) {}
            override fun onComplete() {}
        })
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}