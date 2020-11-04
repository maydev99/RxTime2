package com.bombadu.rxtime2;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView text;
     //vars
    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * 1. Create an Observable (Task)
         * 2. Apply an operator to the Observable
         * 3. Designate what thread to do the work on and what thread to emit the results to
         * 4. Subscribe an Observer to the Observable and view the results
         *
         * If using MVVM then use disposables in ViewModel
         */

        Observable<Task> taskObservable = Observable //Create Observable
                .fromIterable(DataSource.createTasksList()) //operator
                .subscribeOn(Schedulers.io()) //Which to do work on
                .filter(new Predicate<Task>() { //new predicate is a test
                    @Override
                    public boolean test(@NonNull Task task) throws Exception {
                        Log.d("TAG", "test: " + Thread.currentThread().getName());
                        try { //Freezing on Background Thread
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return task.isComplete();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()); //observe on this thread

        taskObservable.subscribe(new Observer<Task>() { //Observes results for action above
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d("TAG", "onSubscribe: called.");
                disposables.add(d);
            }

            @Override
            public void onNext(@NonNull Task task) {
                Log.d("TAG", "onNext: " + Thread.currentThread().getName());
                Log.d("TAG", "onNext: " + task.getDescription());

            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d("TAG", "onError: ",e);

            }

            @Override
            public void onComplete() {
                Log.d("TAG", "onComplete: called.");

            }
        });

        disposables.add(taskObservable.subscribe(new Consumer<Task>() {
            @Override
            public void accept(Task task) throws Exception {

            }
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
        //disposables.dispose(); hard clear, not used as much
    }
}