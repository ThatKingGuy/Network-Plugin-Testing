package net.gabe.server;

public final class TestPlugin extends BasePlugin {


    @Override
    public void onEnable() {
        System.out.println("My test plugin is loaded!");
    }

    @Override
    public void onDisable() {
        System.out.println("unloading plugin!");
    }

    @Override
    public void onCommand(String command, String[] args) {
        System.out.println("There has been a command!");
    }




}