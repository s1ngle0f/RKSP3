package org.example;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class Task1 {
    public static void main(String[] args) {
        TemperatureSensor temperatureSensor = new TemperatureSensor();
        CO2Sensor co2Sensor = new CO2Sensor();
        AlarmSystem alarmSystem = new AlarmSystem();

        temperatureSensor.subscribe(alarmSystem);
        co2Sensor.subscribe(alarmSystem);

        while (true) {
            int randomTemperature = (int) (Math.random() * 16) + 15;
            int randomCO2Level = (int) (Math.random() * 71) + 30;

            temperatureSensor.setTemperature(randomTemperature);
            co2Sensor.setCO2Level(randomCO2Level);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

enum SensorType{
    TEMPERATURE,
    CO2
}

class Info{
    private SensorType sensorType;
    private int value;

    public Info(SensorType sensorType, int value) {
        this.sensorType = sensorType;
        this.value = value;
    }

    public SensorType getSensorType() {
        return sensorType;
    }

    public void setSensorType(SensorType sensorType) {
        this.sensorType = sensorType;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

class TemperatureSensor extends Observable<Info> {
    private PublishSubject<Info> temperatureSubject = PublishSubject.create();

    @Override
    protected void subscribeActual(Observer<? super Info> observer) {
        temperatureSubject.subscribe(observer);
    }

    public void setTemperature(int temperature) {
        temperatureSubject.onNext(new Info(SensorType.TEMPERATURE, temperature));
    }
}

class CO2Sensor extends Observable<Info> {
    private PublishSubject<Info> co2Subject = PublishSubject.create();

    @Override
    protected void subscribeActual(Observer<? super Info> observer) {
        co2Subject.subscribe(observer);
    }

    public void setCO2Level(int co2Level) {
        co2Subject.onNext(new Info(SensorType.CO2, co2Level));
    }
}

class AlarmSystem implements Observer<Info> {
    private static final int TEMPERATURE_THRESHOLD = 25;
    private static final int CO2_THRESHOLD = 70;

    private boolean temperatureExceeded = false;
    private boolean co2Exceeded = false;

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(Info info) {
        if (info.getSensorType() == SensorType.TEMPERATURE) {
            if(info.getValue() > TEMPERATURE_THRESHOLD){
                System.out.println("High Temperature Warning: " + info.getValue());
                temperatureExceeded = true;
            }else
                temperatureExceeded = false;
        }
        if (info.getSensorType() == SensorType.CO2) {
            if(info.getValue() > CO2_THRESHOLD){
                System.out.println("High CO2 Level Warning: " + info.getValue());
                co2Exceeded = true;
            }else
                co2Exceeded = false;
        }

        if (temperatureExceeded && co2Exceeded) {
            System.out.println("ALARM!!!");
        }
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}