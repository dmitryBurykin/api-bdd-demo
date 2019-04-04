package org.db.tests.api.bdd.services;

import org.db.tests.api.bdd.dataproviders.ProjectPropertiesFileReader;
import org.db.tests.api.bdd.dataproviders.SoapMessageBroker;
import org.db.tests.api.bdd.parsers.GetCursOnDateResponseParser;

import org.jdom2.Element;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by dmitry on 11.03.19.
 *
 */
public class SoapClientGetCursOnDateXML {

    private final ProjectPropertiesFileReader configReader = new ProjectPropertiesFileReader();
    private final String soapRqTemplateFilename = configReader.getProperty("ws.template.rq");
    private final String serviceEndpointURL = configReader.getProperty("ws.endpoint");
    private final SoapMessageBroker soapMessageBroker = new SoapMessageBroker(serviceEndpointURL);
    private final GetCursOnDateResponseParser responseParser = new GetCursOnDateResponseParser();


    public SoapMessageBroker getSoapMessageBroker() {
        return soapMessageBroker;
    }

    /**
     * Метод запрашивает курсы валют ЦБ на указанную дату
     * @param dateStr - дата в формате YYYY-MM-DD
     */
    public void getRatesOnDate(String dateStr){
        Map requestValues = getRequestValues(dateStr);
        soapMessageBroker.setRequestMessageFromFile(soapRqTemplateFilename, requestValues);
        soapMessageBroker.sendSoapRequest();
    }

    /**
     * Метод возвращает курс указанной валюты
     * @param currencyCode - код валюты (USD/ EUR и пр.)
     * @return курс валюты
     * @throws JDOMException
     * @throws IOException
     */
    public String getRate(String currencyCode) throws JDOMException, IOException {
        String response = soapMessageBroker.getResponseMessage();
        List<Element> ratesInfo = responseParser.getRatesInfo(response);
        return responseParser.getCurrencyInfo(ratesInfo, currencyCode);
    }

    private Map getRequestValues(String dateStr){
        Map<String, String> requestValues = new HashMap<>();
        Calendar calendar = new GregorianCalendar();
        if (dateStr.trim().toLowerCase().equals("текущая")){
            requestValues.put("cursOnDate", new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
        }
        else {
            requestValues.put("cursOnDate", dateStr.trim());
        }
        return requestValues;
    }


}
