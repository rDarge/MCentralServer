package com.egravitas.mcmod.Exception;

/**
 * Created by Ryan on 1/17/2016.
 */
public class ServerNotRunningException extends Exception {

    public ServerNotRunningException(){
        super("The server is not running");
    }
}
