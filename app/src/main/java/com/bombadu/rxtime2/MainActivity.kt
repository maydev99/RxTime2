package com.bombadu.rxtime2

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bombadu.rxtime2.DataSource.createTasksList
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
         * 1. Create an Observable (Task)
         * 2. Apply an operator to the Observable
         * 3. Designate what thread to do the work on and what thread to emit the results to
         * 4. Subscribe an Observer to the Observable and view the results
         */
        val taskObservable = Observable //Create Observable
                .fromIterable(createTasksList()) //operator
                .subscribeOn(Schedulers.io()) //Which to do work on
                .filter { task ->

                    //new predicate is a test
                    Log.d("TAG", "test: " + Thread.currentThread().name)
                    try { //Freezing on Background Thread
                        Thread.sleep(1000)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    task.isComplete
                }
                .observeOn(AndroidSchedulers.mainThread()) //observe on this thread
        taskObservable.subscribe(object : Observer<Task> {
            //Observes results for action above
            override fun onSubscribe(d: Disposable) {
                Log.d("TAG", "onSubscribe: called.")
            }

            override fun onNext(task: Task) {
                Log.d("TAG", "onNext: " + Thread.currentThread().name)
                Log.d("TAG", "onNext: " + task.description)
            }

            override fun onError(e: Throwable) {
                Log.d("TAG", "onError: ", e)
            }

            override fun onComplete() {
                Log.d("TAG", "onComplete: called.")
            }
        })
    }
}