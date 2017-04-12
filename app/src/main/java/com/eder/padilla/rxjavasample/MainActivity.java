package com.eder.padilla.rxjavasample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    Button button;

    List<String> stringList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        button =(Button)findViewById(R.id.button);
        //button.setOnClickListener(e-> toastandAll());
        button.setOnClickListener(v->toastandAll());
    }
    public void toastandAll(){
        Toast.makeText(getApplicationContext(),"Hola retrolambda",Toast.LENGTH_SHORT).show();
        getUserName().subscribe(this::onComple,this::onError);
        createList().subscribe(this::onCompleList,this::onError);
        /**
        getUserName().subscribe(
                // On Next
                new Action1<String>() {
                    @Override
                    public void call(String userName) {
                        mUserNameTextView.setText(userName);
                    }
                },

                // On Error
                new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(TAG, throwable.getMessage(), throwable);
                    }
                },

                // On Complete
                new Action0() {
                    @Override
                    public void call() {
                        mLoadingProgressBar.setVisibility(View.GONE);
                    }
                }
        );
        // */

    }

    private void onCompleList(List<String> strings) {
        log("La lista es "+strings.toString());
    }

    private void onError(Throwable throwable) {
        log("Throwable "+throwable.toString());
    }

    private void onComple(String s) {
        log("Si complete "+s);
    }

    public Observable<String> getUserName(){
        return Observable.create(suscriber -> {
            try{
                suscriber.onNext("Hola rxJava");
                suscriber.onComplete();
            }catch (Exception e){
                suscriber.onError(e);
            }
        });
    }
    public static void log(String message){
        Log.e("RXDebuggable ",message);
    }

    public Observable<List<String>> createList(){
        return Observable.create(subscriber -> {
            try{
                List<String> stringList = new ArrayList<>();
                stringList.add("Hola");
                stringList.add("que haces");
                stringList.add("mundo");
                subscriber.onNext(stringList);
                subscriber.onComplete();
            }catch (Exception e){
                subscriber.onError(e);
            }
        });
    }

    @OnClick(R.id.button_just)
    public void just(){
        log("Si entra al click");
        Observable.just("Hello ","just sample").subscribe(this::OnCompleteJust,this::onError);
    }

    private void OnCompleteJust(String s) {
        log("Se obtiene en el just "+s);
    }

    @OnClick(R.id.button_filter)
    public void filter(){
        Observable.just(1,2,3,4,5)
                .filter(a-> a<=3)
                .subscribe(this::onFilterSuccess,this::onError);

    }

    private void onFilterSuccess(Integer integer) {
        log("En el int se tiene "+integer);
    }
    /**Map se encarga de convertir un objeto a otro en este caso es de String a int*/
    @OnClick(R.id.button_map)
    public void map(){
        Integer OUTPUT_LIMIT=20;
        Observable.just("First Message", "Second Message",
                "This is a large message", "Short message")
                .map(String::length)
                .filter(length -> length < OUTPUT_LIMIT)
                .subscribe(this::onCompleteMap,this::onError);
    }

    private void onCompleteMap(Integer integer) {
        log("Map "+integer);
    }

    @OnClick(R.id.button_from)
    public void from(){
        Integer [] numbers ={1,2,3,4,5,6,7,8,9,10,11,12,13};
        Observable.fromArray(numbers)
                .subscribe(this::onCompleteFrom,this::onError);
    }

    private void onCompleteFrom(Integer integer) {
    log("Se imprime en el from "+integer);
    }

    @OnClick(R.id.button_all)
    public void all(){
        Observable.just("hola", "content","j")
                .all(field -> field != null)
                .filter(areFieldNotNull -> areFieldNotNull)
                .subscribe(result -> log("All fields are not null"));

    }

    private void oncompleteAll(Boolean aBoolean) {
        log("Regresa el all "+aBoolean);
    }

    @OnClick(R.id.button_group_by)
    public void groupBy(){
        Observable.just(1,2,3,4,5)
                .groupBy(number -> number % 2 == 0 ? "Par" : "Impar")
                .subscribe(group -> {
                    group.count().subscribe(count ->
                            log(
                                    String.format("There are %d %s numbers", count, group.getKey()))
                    );
                });
    }

    @OnClick(R.id.button_contains)
    public void contains(){
        Integer[] numbers = {1,2,3,4,5,6};
        Observable.fromArray(numbers)
                .contains(5)
                .subscribe(result -> log("Is 5 in numbers? " + result));
    }

    @OnClick(R.id.button_exists)
    public void exists(){
        Observable.just("Fabio", "Marcos", "Pedro", "Elena")
                .contains("Fabiodfsdf")
                .subscribe(result -> log("Exists name with more than four chars? " + result));
    }

    @OnClick(R.id.button_limit)
    public void limit(){
        Integer[] numbers = {1,2,3,4,5};
        Observable.fromArray(numbers)
                .take(3)
                .subscribe(number -> log("Number " + number));
    }

    @OnClick(R.id.button_is_empty)
    public void isEmpty(){
        Integer[] numbers = {};
        Observable.fromArray(numbers)
                .isEmpty()
                .subscribe(result -> log("Is array empty? " + result));
    }

    @OnClick(R.id.button_concat)
    public void concat(){
        Observable<Integer> firstObservable  = Observable.just(-1, 0, 1);
        Observable<Integer> secondObservable = Observable.just(3, 6, 2, 8, 5);
        Observable.concat(firstObservable, secondObservable)
                .subscribe(number -> log("Number " + number));
    }

    @OnClick(R.id.button_sequence_equal)
    public void sequenceEqual(){
        Integer[] numbers = {1,2,3,6,2,8,5};
        Observable<Integer> firstObservable  = Observable.fromArray(numbers);
        Observable<Integer> secondObservable = Observable.just(3, 6, 2, 8, 5);

        Observable.sequenceEqual(firstObservable, secondObservable)
                .subscribe(result -> log("Is sequence equal? " + result));
    }

    @OnClick(R.id.button_combine_latest)
    public void combineLates(){
        Observable<Integer> firstObservable  = Observable.just(-1, 0, 3);//con el 3 el ultimo
        Observable<Integer> secondObservable = Observable.just(4, 6, 2, 8, 5);///suma todos estos
        Observable.combineLatest(firstObservable, secondObservable,
                (latestNumberInFirst, numberInSecond) -> latestNumberInFirst + numberInSecond)
                .subscribe(result -> log("Sum " + result));
    }

    @OnClick(R.id.button_zip)
    public void zip(){
        Observable<Integer> firstObservable  = Observable.just(0, 1, 2);
        Observable<String>  secondObservable = Observable.just("First Number = ", "Second Number = ", "Third Number = ");
        Observable.zip(firstObservable, secondObservable,
                (number, message) -> message + number)
                .subscribe(System.out::println);
    }

    @OnClick(R.id.button_async)
    public void async(){
        Thread.currentThread().setName("Main Thread");
        getNumbers()
                .flatMap(numbers -> {
                    log("FlatMap Thread = " + Thread.currentThread().getName());
                    return Observable.fromArray(numbers);
                })
                .map(number -> {
                    log("Map Thread = " + Thread.currentThread().getName());
                    return number % 2;
                })
                .forEach(number -> {
                    log("ForEach Thread = " + Thread.currentThread().getName());
                    log("Is Odd " + (number == 0));
                });
    }

    private static Observable<Integer[]> getNumbers(){
        log("Function getNumbers Thread = " + Thread.currentThread().getName());
        return Observable.create(
                subscriber -> {
                    log("Observable.create Thread = " + Thread.currentThread().getName());
                    Integer[] numbers = {3,6,2};
                    subscriber.onNext(numbers);
                    subscriber.onComplete();
                }
        );
    }

    @OnClick(R.id.button_observe_on)
    public void observeOn(){
        Thread.currentThread().setName("Main Thread");
        getNumbers()
                .observeOn(Schedulers.newThread())
                .flatMap(numbers -> {  //flatMap que es capaz a partir de una colección emitir items en streaming, items que pueden ser de diferente tipo de los que le llegan.
                    log("FlatMap Thread = " + Thread.currentThread().getName());
                    return Observable.fromArray(numbers);
                })
                .map(number -> {
                    log("Map Thread = " + Thread.currentThread().getName());
                    return number % 2;
                })

                .forEach(number -> {
                    log("ForEach Thread = " + Thread.currentThread().getName());
                    log("Is Odd " + (number != 0));
                });
    }

    @OnClick(R.id.button_subscribe_on)
    public void subscribeOn(){
        Thread.currentThread().setName("Main Thread");
        getNumbers()
                .subscribeOn(Schedulers.newThread())
                .flatMap(numbers -> {
                    log("FlatMap Thread = " + Thread.currentThread().getName());
                    return Observable.fromArray(numbers);
                })
                .map(number -> {
                    log("Map Thread = " + Thread.currentThread().getName());
                    return number % 2;
                })
                .forEach(number -> {
                    log("ForEach Thread = " + Thread.currentThread().getName());
                    log("Is Odd " + (number != 0));
                });
    }

    @OnClick(R.id.button_amb)
    public void amb(){
        Random r = new Random();
        Observable<Integer> firstObservable = Observable.just(-1, -2, -3)
                .subscribeOn(Schedulers.newThread())
                .delay(r.nextInt(2), TimeUnit.SECONDS);

        Observable<Integer> secondObservable = Observable.just(1, 2, 3)
                .subscribeOn(Schedulers.newThread())
                .delay(r.nextInt(2), TimeUnit.SECONDS);


        Observable.ambArray(firstObservable, secondObservable)
                .forEach(number -> {
                    log("ForEach Thread = " + Thread.currentThread().getName());
                    log("Number " + number);
                });
    }

    @OnClick(R.id.button_merge)
    public void merge(){
        Random r = new Random();
        Observable<Integer> firstObservable = Observable.just(-1, -2, -3)
                .subscribeOn(Schedulers.newThread())
                .delay(r.nextInt(2), TimeUnit.SECONDS);
        Observable<Integer> secondObservable = Observable.just(1, 2, 3)
                .subscribeOn(Schedulers.newThread())
                .delay(r.nextInt(2), TimeUnit.SECONDS);


        Observable.merge(firstObservable, secondObservable)
                .forEach(number -> {
                    log("ForEach Thread = " + Thread.currentThread().getName());
                    log("Number " + number);
                });

    }



}
