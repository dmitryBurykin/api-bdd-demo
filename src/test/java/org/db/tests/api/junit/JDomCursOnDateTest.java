package org.db.tests.api.junit;

import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.util.IteratorIterable;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by dmitry on 25.03.19.
 *
 */
public class JDomCursOnDateTest {

    private final String responseFileName = "src/test/resources/xml/Rs.xml";

    @Test
    public void parseGetCursOnDateResponse() throws JDOMException, IOException {
        File inputFile = new File(responseFileName);
        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = saxBuilder.build(inputFile);

        Element rootElement = document.getRootElement();
        System.out.println("Root element :" + rootElement.getName());

        // Получить список элементов с курсами валют
        List<Element> cursList = null;
        IteratorIterable<Content> responseIterator = rootElement.getDescendants();
        while (responseIterator.hasNext()){
            Content currentContent = responseIterator.next();

            // Искать только ноды
            if(!currentContent.getCType().name().toLowerCase().equals("text")){
                Element element = (Element) currentContent;
                if (element.getName().equals("ValuteData")){
                    cursList = element.getChildren();
                    break;
                }
            }

        }

        Assert.assertNotNull(cursList);
        Assert.assertTrue(cursList.size()>0);

        // Вывести курс каждой найденной валюты
        for (Element currencyInfo : cursList) {
            String currencyCode = currencyInfo.getChild("VchCode").getText();
            String currencyRate = currencyInfo.getChild("Vcurs").getText();

            Assert.assertNotNull(currencyCode);
            Assert.assertNotNull(currencyRate);

            System.out.println(String.format("[%s]:%s", currencyCode, currencyRate));

        }

    }
}
