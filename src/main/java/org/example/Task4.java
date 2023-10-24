package org.example;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

import java.util.concurrent.TimeUnit;

public class Task4 {
    public static void main(String[] args) {
        FileGenerator fileGenerator = new FileGenerator();
        QueueManager queueManager = new QueueManager();

        fileGenerator.generateFile()
                .subscribeOn(Schedulers.io())
                .subscribe(queueManager::enqueue);

        FileProcessor processor1 = new FileProcessor("XML");
        FileProcessor processor2 = new FileProcessor("JSON");
        FileProcessor processor3 = new FileProcessor("XLS");

        queueManager.getQueueObservable()
                .map(file -> {
                    switch (file.getType()){
                        case "XML":
                            return processor1.processFile(file);
                        case "JSON":
                            return processor2.processFile(file);
                        case "XLS":
                            return processor3.processFile(file);
                        default:
                            return null;
                    }
                })
                .subscribe(processedFile -> {
                    System.out.println("Файл обработан!");
                });

        try {
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class FileGenerator {
    public Observable<File> generateFile() {
        return Observable
                .interval((long) (Math.random() * 901) + 100, TimeUnit.MILLISECONDS)
                .map(tick -> new File(generateRandomFileType(), generateRandomFileSize()));
    }

    private String generateRandomFileType() {
        String[] fileTypes = {"XML", "JSON", "XLS"};
        return fileTypes[(int) (Math.random() * fileTypes.length)];
    }

    private int generateRandomFileSize() {
        return (int) (Math.random() * 91) + 10;
    }
}

class File {
    private String type;
    private int size;

    public File(String type, int size) {
        this.type = type;
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "File{" +
                "type='" + type + '\'' +
                ", size=" + size +
                '}';
    }
}

class QueueManager {
    private PublishSubject<File> queueSubject = PublishSubject.create();

    public void enqueue(File file) {
        System.out.println(file);
        queueSubject.onNext(file);
    }

    public Observable<File> getQueueObservable() {
        return queueSubject
                .observeOn(Schedulers.io())
                .delay(100, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.computation());
    }
}

class FileProcessor {
    private String supportedFileType;

    public FileProcessor(String supportedFileType) {
        this.supportedFileType = supportedFileType;
    }

    public Observable<Object> processFile(File file) {
        if (file.getType().equals(supportedFileType)) {
            int processingTime = file.getSize() * 7;
            return Observable
                    .just(file)
                    .delay(processingTime, TimeUnit.MILLISECONDS)
                    .map(f -> null)
                    .subscribeOn(Schedulers.io());
        } else {
            return Observable.empty();
        }
    }
}