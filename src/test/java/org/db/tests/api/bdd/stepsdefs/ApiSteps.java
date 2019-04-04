package org.db.tests.api.bdd.stepsdefs;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.db.tests.api.bdd.services.SoapClientGetCursOnDateXML;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by dmitry on 02.02.19.
 *
 */
public class ApiSteps {

    private final SoapClientGetCursOnDateXML webService = new SoapClientGetCursOnDateXML();

    @When("^Система запрашивает котировки валют на дату: \"([^\"]+)\"$")
    public void getRatesOnDate(String dateStr) {
        webService.getRatesOnDate(dateStr);
    }

    @Then("^Веб-сервис возвращает ответ$")
    public void validateWsResponse() {
        assertNotNull(webService.getSoapMessageBroker().getResponseStatus());
        assertNotNull(webService.getSoapMessageBroker().getResponseMessage());
        assertEquals(200, webService.getSoapMessageBroker().getResponseStatus());
    }

    @Then("^В ответе есть курс для валюты с кодом \"([^\"]+)\"$")
    public void hasWsResponseRateWithAlfaCode(String currencyCode) throws JDOMException, IOException {
        String rate = webService.getRate(currencyCode);
        assertNotNull(rate);
        System.out.println(String.format("[%s]:%s", currencyCode, rate));
    }

}
