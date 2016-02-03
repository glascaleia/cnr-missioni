/*
 *  geo-platform
 *  Rich webgis framework
 *  http://geo-platform.org
 * ====================================================================
 *
 * Copyright (C) 2008-2014 geoSDI Group (CNR IMAA - Potenza - ITALY).
 *
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. This program is distributed in the 
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without 
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR 
 * A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details. You should have received a copy of the GNU General 
 * Public License along with this program. If not, see http://www.gnu.org/licenses/ 
 *
 * ====================================================================
 *
 * Linking this library statically or dynamically with other modules is 
 * making a combined work based on this library. Thus, the terms and 
 * conditions of the GNU General Public License cover the whole combination. 
 * 
 * As a special exception, the copyright holders of this library give you permission 
 * to link this library with independent modules to produce an executable, regardless 
 * of the license terms of these independent modules, and to copy and distribute 
 * the resulting executable under terms of your choice, provided that you also meet, 
 * for each linked independent module, the terms and conditions of the license of 
 * that module. An independent module is a module which is not derived from or 
 * based on this library. If you modify this library, you may extend this exception 
 * to your version of the library, but you are not obligated to do so. If you do not 
 * wish to do so, delete this exception statement from your version. 
 *
 */
package it.cnr.missioni.connector.core;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import it.cnr.missioni.connector.core.spring.connector.MissioniCoreClientConnector;
import it.cnr.missioni.el.model.search.builder.PrenotazioneSearchBuilder;
import it.cnr.missioni.model.prenotazione.Prenotazione;
import it.cnr.missioni.rest.api.response.prenotazione.PrenotazioniStore;

/**
 * 
 * @author Salvia Vito
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContext.xml" })
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class PrenotazioneRestServiceTest {

	static final String CORE_CONNECTOR_KEY = "MISSIONI_CORE_FILE_PROP";
	//
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource(name = "missioniCoreClientConnector")
	protected MissioniCoreClientConnector missioniCoreClientConnector;

	@BeforeClass
	public static void beforeClass() {
		System.setProperty(CORE_CONNECTOR_KEY, "missioni-core-test.prop");
	}

	@Before
	public void setUp() throws Exception {
		logger.debug("\n\n\t@@@@@@@ {}.setUp @@@@@@\n\n", this.getClass().getSimpleName());

	}

	// @After
	// public void tearDown() {
	// logger.debug("\n\t@@@ {}.tearDown @@@", this.getClass().getSimpleName());
	// // Delete Organization
	// this.deleteOrganization(organizationTest.getId());
	// }

	@AfterClass
	public static void afterClass() {
		System.clearProperty(CORE_CONNECTOR_KEY);
	}

	@Test
	public void A_testFindPrenotazione() throws Exception {
		
		PrenotazioneSearchBuilder prenotazioneSearchBuilder = PrenotazioneSearchBuilder.getPrenotazioneSearchBuilder().withRangeData(new DateTime(2016,1,1,0,0), new DateTime(2016,1,31,0,0));
		PrenotazioniStore prenotazioniStore= missioniCoreClientConnector.getPrenotazioneByQuery(prenotazioneSearchBuilder);
		Assert.assertNotNull(prenotazioniStore);
	}
	
	@Test
	public void B_addPrenotazionetest() throws Exception {
		Prenotazione p = new Prenotazione();
		p.setId("03");
		p.setDataFrom(new DateTime(2016,1,27,0,0));
		p.setDataTo(new DateTime(2016,1,28,0,0));
		missioniCoreClientConnector.addPrenotazione(p);
		Thread.sleep(1000);
		PrenotazioneSearchBuilder prenotazioneSearchBuilder = PrenotazioneSearchBuilder.getPrenotazioneSearchBuilder().withRangeData(new DateTime(2016,1,1,0,0), new DateTime(2016,1,31,0,0));
		PrenotazioniStore prenotazioniStore= missioniCoreClientConnector.getPrenotazioneByQuery(prenotazioneSearchBuilder);
		Assert.assertTrue("Totale prenotazioni", prenotazioniStore.getPrenotazioni().size() == 3);
	}
	
	@Test
	public void C_updatePrenotazionetest() throws Exception {
		Prenotazione p = new Prenotazione();
		p.setId("03");
		DateTime now = new DateTime();
		p.setDataFrom(now);
		p.setDataTo(now.plusDays(2));
		missioniCoreClientConnector.updatePrenotazione(p);
		Thread.sleep(1000);
		PrenotazioneSearchBuilder prenotazioneSearchBuilder = PrenotazioneSearchBuilder.getPrenotazioneSearchBuilder().withRangeData(new DateTime(2016,1,1,0,0), new DateTime(2016,1,31,0,0));
		PrenotazioniStore prenotazioniStore= missioniCoreClientConnector.getPrenotazioneByQuery(prenotazioneSearchBuilder);
		Assert.assertTrue("Totale prenotazioni", prenotazioniStore.getPrenotazioni().size() == 3);
	}
	
	@Test
	public void D_deletePrenotazionetest() throws Exception {
		missioniCoreClientConnector.deletePrenotazione("03");
		Thread.sleep(1000);
		PrenotazioneSearchBuilder prenotazioneSearchBuilder = PrenotazioneSearchBuilder.getPrenotazioneSearchBuilder().withRangeData(new DateTime(2016,1,1,0,0), new DateTime(2016,1,31,0,0));
		PrenotazioniStore prenotazioniStore= missioniCoreClientConnector.getPrenotazioneByQuery(prenotazioneSearchBuilder);
		Assert.assertTrue("Totale prenotazioni", prenotazioniStore.getPrenotazioni().size() == 2);
	}

	
}
