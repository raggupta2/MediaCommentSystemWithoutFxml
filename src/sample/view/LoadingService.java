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
                        listener.before();
                        final CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    //FX Stuff done here
                                    listener.body();
                                } finally {
                                    latch.countDown();
                                }
                            }
                        });
                        latch.await();
                        //Keep with the background work
                        listener.after();
                        return null;
                    }
                };
            }
        };
        service.start();
    }

}
