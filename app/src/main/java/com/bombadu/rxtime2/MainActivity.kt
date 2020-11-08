package com.bombadu.rxtime2

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bombadu.rxtime2.DataSource.createTasksList
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //final Task task = new Task("Walk the dog", false, 3);
        val tasks = createTasksList()
        val taskObservable = Observable
                .create { emitter: ObservableEmitter<Task?> ->
                    for (task in createTasksList()) {
                        if (!emitter.isDisposed) {
                            emitter.onNext(task)
                        }
                    }
                    if (!emitter.isDisposed) {
                        emitter.onComplete()
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        taskObservable.subscribe(object : Observer<Task> {
            override fun onSubscribe(d: Disposable) {}
            override fun onNext(task: Task) {
                Log.d(TAG, "onNext: " + task.description)
            }

            override fun onError(e: Throwable) {}
            override fun onComplete() {}
        })
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}

