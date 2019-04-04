package org.db.tests.api.bdd.dataproviders;

import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringSubstitutor;
import org.db.tests.api.bdd.exceptions.FrameworkException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dmitry on 01.04.19.
 *
 */
public class SoapMessageBroker {

    //private String endpointURL;
    private String requestMessage;
    private String responseMessage;
    private int responseStatus;

    private HttpURLConnection soapConnection;

    public SoapMessageBroker(String endpointURL) {
        setSoapConnection(endpointURL);
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    public void setRequestMessageFromFile(String soapRequestFileName){
        this.requestMessage = getSoapRequestFromFile(soapRequestFileName);
    }

    public void setRequestMessageFromFile(String soapRequestFileName, Map soapRequestValues){
        String requestTemplateMessage = getSoapRequestFromFile(soapRequestFileName);
        StringSubstitutor placeholderManager = new StringSubstitutor(soapRequestValues);
        this.requestMessage = placeholderManager.replace(requestTemplateMessage);
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public int getResponseStatus() {
        return responseStatus;
    }

    public void sendSoapRequest(){
        if (this.requestMessage!=null){
            try {
                // Отправить данные на сервер
                DataOutputStream wr = new DataOutputStream(soapConnection.getOutputStream());
                wr.writeBytes(requestMessage);
                wr.flush();
                wr.close();
                System.out.println(String.format("Request: %s", requestMessage));

                // Получить код ответа
                responseStatus = soapConnection.getResponseCode();

                // Прочитать текст ответа
                String inputLine;
                BufferedReader in = new BufferedReader(new InputStreamReader(soapConnection.getInputStream()));
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                responseMessage = response.toString();
                System.out.println(String.format("Response: [%d]: %s", responseStatus, responseMessage));

            } catch (IOException e) {
                throw new FrameworkException(
                        String.format("Не удалось отправить soap-запрос на сервер. " +
                                "Параметры: {URL: \"%s\", Request: \"%s\"}", this.soapConnection.getURL(), responseMessage), e);
            }
        }
        else {
            throw new FrameworkException("Не указан текст soap-запроса!");
        }
    }

    private void setSoapConnection(String endpointURL) {
        try {
            URL url = new URL(endpointURL);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty("Content-Type","application/soap+xml; charset=utf-8");
            httpConnection.setDoOutput(true);
            this.soapConnection = httpConnection;
        }
        catch (MalformedURLException e)
        {
            throw new FrameworkException("Указан некорректный адрес (wsdl) веб-сервиса: " + endpointURL, e);
        }
        catch (IOException ioe)
        {
            throw new FrameworkException("Не удалось установить соединение с веб-сервисом: " + endpointURL, ioe);
        }
    }

    private String getSoapRequestFromFile(String soapRequestFileName){
        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(soapRequestFileName);
            if (is == null) {
                // Если же ресурс не загрузили, то пытаемся из файловой системы
                is = new FileInputStream(soapRequestFileName);
            }
            return IOUtils.toString(is, StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            e.printStackTrace();
            throw new FrameworkException("Файл c шаблоном soap-запроса не найден: " + soapRequestFileName);
        }
    }
}
