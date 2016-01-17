package com.egravitas.mcmod;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mataz on 1/15/2016.
 */
public class MinecraftWrapper {
    private volatile Process minecraftServer;

    private ExecutorService threadPool;

    protected volatile List<String> serverMessages;
    protected volatile List<String> serverErrors;
    protected volatile InputStream serverOutput;
    protected volatile InputStream serverOutputErrors;
    protected volatile PrintWriter serverWriter;

    private volatile boolean readyForProcessing = false;

    public MinecraftWrapper() {
        threadPool = Executors.newCachedThreadPool();
    }

    public void startServer() {
        //temp directory, will eventually be limited to current working directory
        Runnable serverProcessRunnable = new RunnableProcess("C:\\Minecraft\\Minecraft", "minecraft_server.1.8.9.jar");
        threadPool.execute(serverProcessRunnable);
    }

    public void stopServer() throws InterruptedException {
        command("stop");
        //Halt processing till server stops todo make this asynch
        while(minecraftServer.isAlive()) {/*DOOT DOOT STOPPING THIS TRAIN*/ }
        minecraftServer.destroy();
        threadPool.shutdown();
        threadPool.shutdownNow();
    }

    public void startLogging() {
        serverMessages = new ArrayList<>();
        serverErrors = new ArrayList<>();
        Runnable loggingTask = () -> {
            while (!readyForProcessing) {/* Waiting doot doot doot */}

            try {
                ConsoleUtils.printConsoleOutput(serverOutput, serverMessages);
                ConsoleUtils.printConsoleOutput(serverOutputErrors, serverErrors);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        threadPool.execute(loggingTask);
    }


    public String command(String command) {
        if(minecraftServer == null || !minecraftServer.isAlive()) {
            return "Server is offline.";
        }
        String oldMessages = "";
        String newMessages = "";
        if(!command.endsWith("\r\n")) {
            command += "\r\n";
        }
        serverWriter.write(command);
        serverWriter.flush();

        return "\""+command+"\" sent to server successfully.";
    }

    public boolean isAlive() {
        return (minecraftServer != null && minecraftServer.isAlive());
    }

    public int getMinecraftPort() {
        //todo make this configurable
        return 25565;
    }

    class RunnableProcess implements Runnable {

        String minecraftJar;
        String workingPath;

        RunnableProcess(String workingPath, String minecraftJar) {
            this.minecraftJar = minecraftJar;
            this.workingPath = workingPath;
        }

        @Override
        public void run() {
            try {
                minecraftServer = new ProcessBuilder(
                        "java",
                        "-server",
                        "-Xmx14G",
                        "-Xms14G",
                        "-jar",
                        minecraftJar,
                        "nogui"
                ).directory(new File(workingPath)).start(); //Runtime.getRuntime().exec(command);

                serverOutput = minecraftServer.getInputStream();
                serverOutputErrors = minecraftServer.getErrorStream();
                serverWriter = new PrintWriter(minecraftServer.getOutputStream());

                readyForProcessing = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
