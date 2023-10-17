package org.example;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import java.util.Random;

public class Task2 {
    public static void main(String[] args) {
        Random random = new Random();
        Observable<Integer> observable = Observable
                .range(1, 1000)
                .map(i -> random.nextInt(1001))
                .subscribeOn(Schedulers.io());

        observable
                .filter(num -> num > 500)
                .observeOn(Schedulers.single())
                .subscribe(
                        num -> System.out.print(num + " "),
                        Throwable::printStackTrace,
                        () -> System.out.println("\nОбработка завершена")
                );

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
