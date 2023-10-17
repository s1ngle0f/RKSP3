package org.example;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import java.util.Random;

public class Task2 {
    public static void main(String[] args) {
//        task1();
//        task2();
//        task3();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void task3() {
        Observable<Integer> randomNumbersStream = generateRandomStream(10);

        Observable<Integer> filteredStream = randomNumbersStream
                .take(5)
                .observeOn(Schedulers.single());

        filteredStream.subscribe(
                num -> System.out.print(num + " "),
                Throwable::printStackTrace,
                () -> System.out.println("Обработка завершена")
        );
    }

    private static void task2() {
        Observable<Integer> stream1 = generateRandomStream(1000);
        Observable<Integer> stream2 = generateRandomStream(1000);

        Observable<Integer> mergedStream = Observable.concat(stream1, stream2)
                .observeOn(Schedulers.single());

        mergedStream.subscribe(
                num -> System.out.print(num + " "),
                Throwable::printStackTrace,
                () -> System.out.println("\nОбработка завершена")
        );
    }

    private static Observable<Integer> generateRandomStream(int count) {
        return Observable.range(1, count)
                .map(i -> (int) (Math.random() * 1000));
    }

    private static void task1(){
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
    }
}
