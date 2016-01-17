package com.egravitas.mcmod;

import spark.Spark;

import static spark.Spark.*;

public class Main {

    private static volatile String test = "";

    public static void main(String[] args) {
        Spark.port(25564);

        get("/test",
                (req, res) -> "Response: " + getTest());
        post("/test",
                (req, res) -> {
                    test = req.body();
                    return "Set";
                });

        post("/monitor",
                (request, response) -> "Response: " + ServerManager.SendCommand(request.body()));
    }

    private static String getTest(){
        return test;
    }

    private static void setTest(String input){
        test = input;
    }
}
