package sample.controller;

import sample.model.User;

public class UserController {

    private FileController fileController;

    public UserController() {
        fileController = new FileController();
    }

    public boolean register(String name, String password) {
        return fileController.register(name, password, 1);
    }

    public User login(String name, String password) {
        return fileController.login(name, password);
    }
}
