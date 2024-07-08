package com.msi.reactive.programming;

import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileService {
    private static final Path PATH = Paths.get("src/main/resources");

    public static Mono<String> read(String fileName){
        return Mono.fromSupplier(
                () -> readFile(fileName)
        );
    }

    public static Mono<Void> write(String fileName, String content){
        return Mono.fromRunnable(
                () -> writeFile(fileName, content)
        );
    }

    public static Mono<Void> delete(String fileName){
        return Mono.fromRunnable(
                () -> deleteFile(fileName)
        );
    }


    private static String readFile(String fileName){
        try {
            return Files.readString(PATH.resolve(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeFile(String fileName, String content){
        try {
            Files.writeString(PATH.resolve(fileName), content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void deleteFile(String fileName){
        try {
            Files.delete(PATH.resolve(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        read("file1.txt")
                .subscribe(Util.onNext(), Util.onError(), Util.onComplete());

        write("file3.txt", "create and write in file3")
                .subscribe(Util.onNext(), Util.onError(), Util.onComplete());

        delete("file3.txt")
                .subscribe(Util.onNext(), Util.onError(), Util.onComplete());

    }
}
