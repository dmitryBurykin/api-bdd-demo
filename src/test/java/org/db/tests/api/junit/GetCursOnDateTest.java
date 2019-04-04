package org.db.tests.api.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.db.tests.api.bdd.services.SoapClientGetCursOnDateXML;
import org.jdom2.JDOMException;
import org.junit.Test;

public class GetCursOnDateTest {
	
	@Test
	public void test_getRatesOnCurrentDate() throws JDOMException, IOException {
		SoapClientGetCursOnDateXML webService = new SoapClientGetCursOnDateXML();
		webService.getRatesOnDate("текущая");
		
		assertEquals(200, webService.getSoapMessageBroker().getResponseStatus());
		
		String rate = webService.getRate("EUR");
		assertNotNull(rate);
		
		webService.getRatesOnDate("2019-01-01");
		
		
	}
}
