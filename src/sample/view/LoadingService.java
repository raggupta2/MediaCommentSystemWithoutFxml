package sample.view;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class LoadingService {

    public interface Listener {
        void run();
    }

    public Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }


    public void service() {
        Service<Void> service = new Service<>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() throws Exception {
                        listener.run();

                        //Keep with the background work
                        return null;
                    }
                };
            }
        };
        service.start();
    }

}
