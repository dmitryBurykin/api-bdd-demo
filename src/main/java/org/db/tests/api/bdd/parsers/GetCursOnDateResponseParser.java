package org.db.tests.api.bdd.parsers;

import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.util.IteratorIterable;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

/**
 * Created by dmitry on 01.04.19.
 *
 */
public class GetCursOnDateResponseParser {

    public List<Element> getRatesInfo(String response) throws JDOMException, IOException {
        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = saxBuilder.build(new StringReader(response));

        Element rootElement = document.getRootElement();

        // Получить список элементов с курсами валют
        List<Element> ratesInfo = null;
        IteratorIterable<Content> responseIterator = rootElement.getDescendants();
        while (responseIterator.hasNext()){
            Content currentContent = responseIterator.next();

            // Искать только ноды
            if(!currentContent.getCType().name().toLowerCase().equals("text")){
                Element element = (Element) currentContent;
                if (element.getName().equals("ValuteData")){
                    ratesInfo = element.getChildren();
                    break;
                }
            }

        }
        return ratesInfo;
    }

    public String getCurrencyInfo(List<Element> ratesList, String currencyCode){
        String retval = null;
        for (Element currencyInfo : ratesList) {
            if (currencyInfo.getChild("VchCode").getText().equals(currencyCode)){
                retval = currencyInfo.getChild("Vcurs").getText();
                break;
            }
        }
        return retval;
    }
}
