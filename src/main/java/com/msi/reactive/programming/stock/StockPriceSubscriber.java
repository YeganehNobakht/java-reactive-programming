package com.msi.reactive.programming.stock;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.concurrent.CountDownLatch;

public class StockPriceSubscriber {

    public static void main(String[] args) throws InterruptedException {


        CountDownLatch latch = new CountDownLatch(1);

        StockPricePublisher.getPrice()
                .subscribeWith(
//                        This is getting scheduled in a separate thread
                        new Subscriber<Integer>() {
                            private Subscription subscription;
                            @Override
                            public void onSubscribe(Subscription subscription) {
                                subscription.request(Long.MAX_VALUE);
                                this.subscription = subscription;
                            }

                            @Override
                            public void onNext(Integer price) {
                                System.out.println(LocalDateTime.now() + ": Price: " + price);
                                if (price > 110 || price<90){
                                    this.subscription.cancel();
                                    latch.countDown();
                                }
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                latch.countDown();
                            }

                            @Override
                            public void onComplete() {

                            }

                        }
                );
//Causes the current thread to wait until the latch has counted down to zero, unless the thread is interrupted.
        latch.await();


    }
}
