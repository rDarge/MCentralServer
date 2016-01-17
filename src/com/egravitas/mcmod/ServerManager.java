package com.egravitas.mcmod;

/**
 * Created by mataz on 1/16/2016.
 */
public class ServerManager {

    private static ServerProcess serverProcess;

    //Body of a command should be command:[command string]
    public static String SendCommand(String command) {
        String input = command.replaceFirst("command:", "");
        String response = "";
        try {
            switch (input) {
                case "startserver":
                    ServerManager.init();
                    serverProcess.startServer();
                    serverProcess.startLogging();
                    response = "Server Started";
                    break;
                case "stopserver":
                    serverProcess.stopServer();
                    response = "Server Stopped";
                    break;
                default:
                    //TODO: Add the ability to send minecraft server commands here
                    //TODO: Need to expose the process
                    response = "Sent " + input + " to server";
                    break;
            }
        } catch (InterruptedException ie){
            ie.printStackTrace();
        }

        return response;
    }

    public static void init(){
        serverProcess = new ServerProcess();
    }
}
