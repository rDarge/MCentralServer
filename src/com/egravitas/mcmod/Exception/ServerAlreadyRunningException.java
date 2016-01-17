package com.egravitas.mcmod.Exception;

import com.egravitas.mcmod.MinecraftWrapper;

/**
 * Created by Ryan on 1/17/2016.
 */
public class ServerAlreadyRunningException extends Exception {
    public ServerAlreadyRunningException(MinecraftWrapper mw) {
        super("Server is already running on port " + mw.getMinecraftPort() + ".");
    }
}
