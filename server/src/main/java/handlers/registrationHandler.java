package handlers;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dataAccess.DataAccessException;
import model.UserData;
import dataAccess.UserDAO;
import services.registrationService;

import java.io.*;
import java.net.HttpURLConnection;


public class registrationHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        try{
            if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                InputStream requestBody = exchange.getRequestBody();
                String rBody = readString(requestBody);

                Gson gson = new Gson();
                UserData user = gson.fromJson(rBody, UserData.class);

                registrationService regService = new registrationService();
                regService.createUser(user);

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream responseBody = exchange.getResponseBody();
                writeString(gson.toJson(user), responseBody);
                responseBody.close();
                success = true;

            }

            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                exchange.getResponseBody().close();
            }
        }
        catch (IOException exception){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR,0);
            exchange.getResponseBody().close();
            exception.printStackTrace();
        }

    }

    //Write a string to the outputStream
    private void writeString(String string, OutputStream outStream) throws IOException {
        OutputStreamWriter streamWrite = new OutputStreamWriter(outStream);
        streamWrite.write(string);
        streamWrite.flush();
    }

    //Read string from inputString
    private String readString(InputStream inStream) throws IOException {
        StringBuilder strBuild = new StringBuilder();
        InputStreamReader streamRead = new InputStreamReader(inStream);
        char[] buf = new char[1024];
        int length;
        while ((length = streamRead.read(buf)) > 0) {
            strBuild.append(buf, 0, length);
        }
        return strBuild.toString();
    }
}

