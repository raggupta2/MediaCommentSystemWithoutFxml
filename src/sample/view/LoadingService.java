package sample.view;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.concurrent.CountDownLatch;

public class LoadingService {

    public interface Listener {
        void before();

        void body();

        void after();
    }

    public Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }


    public void service() {
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        //Background work
                        try {
                            listener.before();
                        } catch (Exception e) {
                            System.out.println("before:" + e.getMessage());
                        }
                        final CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    //FX Stuff done here
                                    listener.body();
                                } catch (Exception e) {
                                    System.out.println("body:" + e.getMessage());
                                } finally {
                                    latch.countDown();
                                }
                            }
                        });
                        latch.await();
                        //Keep with the background work
                        try {
                            listener.after();
                        } catch (Exception e) {
                            System.out.println("after:" + e.getMessage());
                        }
                        return null;
                    }
                };
            }
        };
        service.start();
    }

}
