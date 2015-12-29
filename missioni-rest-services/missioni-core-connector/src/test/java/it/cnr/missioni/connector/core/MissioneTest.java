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
import it.cnr.missioni.model.missione.Missione;
import it.cnr.missioni.rest.api.response.missione.MissioniStore;

/**
 *
 * @author Giuseppe La Scaleia - CNR IMAA geoSDI Group
 * @email giuseppe.lascaleia@geosdi.org
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContext.xml" })
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class MissioneTest {

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
	public void A_testFindMissione() throws Exception {
		Missione missione = missioniCoreClientConnector.getMissioneById("M_01");
		Assert.assertNotNull(missione);
	}

	@Test
	public void B_testFindMissioneByUser() throws Exception {
		MissioniStore missioniStore = missioniCoreClientConnector.getLastUserMissions("01");
		Assert.assertEquals("Find Missione by user", 2, missioniStore.getMissioni().size());
	}

	@Test
	public void C_testInsertMissione() throws Exception {

		Missione missione = new Missione();
		missione.setId("M_04");
		missione.setIdUser("01");
		missione.setLocalita("Roma");
		missioniCoreClientConnector.addMissione(missione);
		Thread.sleep(1000);
		missione = missioniCoreClientConnector.getMissioneById("M_04");
		logger.debug("############################INSERT MISSIONE\n");
	}
	
	@Test
	public void D_testUpdateMissione() throws Exception {

		Missione missione_update = new Missione();
		missione_update.setId("M_04");
		missione_update.setLocalita("Napoli");
		missioniCoreClientConnector.updateMissione(missione_update);
		Thread.sleep(1000);
		Missione missione = missioniCoreClientConnector.getMissioneById("M_04");
		
		Assert.assertTrue("Update Missione", missione.getLocalita().equals(missione_update.getLocalita()));
	}
	
	@Test
	public void E_testDeleteMissione() throws Exception {
		missioniCoreClientConnector.deleteMissione("M_04");
		logger.debug("############################DELETE MISSIONE\n");
	}

}
