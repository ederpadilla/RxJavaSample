package com.eder.padilla.rxjavasample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DrawableUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.internal.Utils;
import io.reactivex.Observable;


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
    public void log(String message){
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

}
