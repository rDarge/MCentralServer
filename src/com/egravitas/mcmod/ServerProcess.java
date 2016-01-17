package com.egravitas.mcmod;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mataz on 1/15/2016.
 */
public class ServerProcess {
    private volatile Process serverProcess;

    private ExecutorService threadPool;

    public volatile InputStream serverOutput;
    public volatile InputStream serverOutputErrors;
    public volatile OutputStream serverInput;

    private volatile boolean readyForProcessing = false;

    public ServerProcess() {
        threadPool = Executors.newCachedThreadPool();
    }

    public void startServer() {
        //temp directory, will eventually be limited to current working directory
        Runnable serverProcessRunnable = new RunnableProcess("java -server -Xmx14G -Xms14G -jar \"D:\\ftb server\\FTBServer.jar\" nogui");
        threadPool.execute(serverProcessRunnable);
    }

    public void stopServer() throws InterruptedException {
        serverProcess.destroy();
        threadPool.shutdown();
        threadPool.shutdownNow();
    }

    public void startLogging() {
        Runnable loggingTask = () -> {
            while (!readyForProcessing) {/* Waiting doot doot doot */}

            try {
                ConsoleUtils.printConsoleOutput(serverOutput);
                ConsoleUtils.printConsoleOutput(serverOutputErrors);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        threadPool.execute(loggingTask);
    }

    class RunnableProcess implements Runnable {

        String path;

        RunnableProcess(String path) {
            this.path = path;
        }

        @Override
        public void run() {
            try {
                serverProcess = Runtime.getRuntime().exec(path);

                serverOutput = serverProcess.getInputStream();
                serverOutputErrors = serverProcess.getErrorStream();
                serverInput = serverProcess.getOutputStream();

                readyForProcessing = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
