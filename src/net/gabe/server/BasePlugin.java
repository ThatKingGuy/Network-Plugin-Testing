package net.gabe.server;

public abstract class BasePlugin{


    public abstract void onEnable();
    public abstract void onDisable();
    public abstract void onCommand(String command, String[] args);
}
