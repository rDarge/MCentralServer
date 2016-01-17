package com.egravitas.mcmod;

import com.egravitas.mcmod.Exception.*;

/**
 * Created by mataz on 1/16/2016.
 */
public class ServerManager {

    private static MinecraftWrapper minecraftWrapper;

    //Body of a command should be command:[command string]
    public static String SendCommand(String command) {
        String input = command.replaceFirst("command:", "");
        String response = "";
        try {
            switch (input) {
                case "startserver":
                    response = startServer();
                    break;
                case "stopserver":
                    response = stopServer();
                    break;
                default:
                    //TODO: Add the ability to send minecraft server commands here
                    //TODO: Need to expose the process
                    response = sendCommand(input);
                    break;
            }
        } catch (ServerNotRunningException | ServerAlreadyRunningException e){
            response = e.getMessage();
        }

        return response;
    }

    public static String startServer() throws ServerAlreadyRunningException {
        if(minecraftWrapper != null && minecraftWrapper.isAlive()) {
            throw new ServerAlreadyRunningException(minecraftWrapper);
        }
        ServerManager.init();
        minecraftWrapper.startServer();
        minecraftWrapper.startLogging();
        return "Server started successfully.";
    }

    public static String stopServer() throws ServerNotRunningException {
        if(minecraftWrapper == null) {
            throw new ServerNotRunningException();
        } else try {
            minecraftWrapper.stopServer();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        return "Server stopped successfully.";
    }

    /**
     * sendCommand delivers commands to the minecraft server.
     * Acceptable forms of commands:
     *      kick garland
     *      /kick garland
     *      command:kick garland
     *      command:/kick garland
     * @param command
     * @return
     * @throws ServerNotRunningException
     */
    public static String sendCommand(String command) throws ServerNotRunningException {
        if(minecraftWrapper == null) {
            throw new ServerNotRunningException();
        }
        return minecraftWrapper.command(command);
    }

    public static void init(){
        minecraftWrapper = new MinecraftWrapper();
    }
}
