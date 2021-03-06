package it.cnr.missioni.el.dao;

import it.cnr.missioni.el.model.search.builder.IPrenotazioneSearchBuilder;
import it.cnr.missioni.el.utility.PrenotazioneFunction;
import it.cnr.missioni.model.prenotazione.Prenotazione;
import org.geosdi.geoplatform.experimental.el.configurator.GPIndexConfigurator;
import org.geosdi.geoplatform.experimental.el.index.GPIndexCreator;
import org.geosdi.geoplatform.logger.support.annotation.GeoPlatformLog;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Salvia Vito
 */
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-DAO-Test.xml"})
public class PrenotazioneDAOTest {

    @GeoPlatformLog
    static Logger logger;
    @Resource(name = "missioniIndexConfigurator")
    private GPIndexConfigurator missioniDocIndexConfigurator;

    @Resource(name = "prenotazioneIndexCreator")
    private GPIndexCreator prenotazioneDocIndexCreator;

    @Resource(name = "prenotazioneDAO")
    private IPrenotazioneDAO prenotazioneDAO;

    private List<Prenotazione> listaPrenotazioni = new ArrayList<Prenotazione>();

    @Before
    public void setUp() {
        Assert.assertNotNull(missioniDocIndexConfigurator);
        Assert.assertNotNull(prenotazioneDocIndexCreator);
        Assert.assertNotNull(prenotazioneDAO);
    }

    @Test
    public void A_createPrenotazioneTest() throws Exception {
        listaPrenotazioni = PrenotazioneFunction.creaMassivePrenotazioni();
        prenotazioneDAO.persist(listaPrenotazioni);
        Thread.sleep(1000);
        logger.debug("############################NUMBER_OF_PRENOTAZONI: {}\n", prenotazioneDAO.count().intValue());
    }

    @Test
    public void B_addPrenotazioneTest() throws Exception {
        Prenotazione p = new Prenotazione();
        p.setId("03");
        DateTime now = new DateTime();
        p.setDataFrom(now);
        p.setDataTo(now.plusDays(1));
        prenotazioneDAO.persist(p);
        Thread.sleep(1000);

        IPrenotazioneSearchBuilder prenotazioneSearchBuilder = IPrenotazioneSearchBuilder.PrenotazioneSearchBuilder.getPrenotazioneSearchBuilder()
                .withRangeData(new DateTime(2016, 1, 1, 0, 0), new DateTime());
        List<Prenotazione> lista = prenotazioneDAO.findPrenotazioneByQuery(prenotazioneSearchBuilder).getResults();
        Assert.assertTrue("Totale prenotazioni", lista.size() == 3);
        logger.debug("############################NUMBER_OF_PRENOTAZONI: {}\n", prenotazioneDAO.count().intValue());

    }

    @Test
    public void C_updatePrenotazioneTest() throws Exception {
        Prenotazione p = new Prenotazione();
        p.setId("03");
        DateTime now = new DateTime();
        p.setDataFrom(now);
        p.setDataTo(now.plusDays(2));
        prenotazioneDAO.update(p);
        Thread.sleep(1000);
        IPrenotazioneSearchBuilder prenotazioneSearchBuilder = IPrenotazioneSearchBuilder.PrenotazioneSearchBuilder.getPrenotazioneSearchBuilder()
                .withRangeData(new DateTime(2016, 1, 1, 0, 0), new DateTime());
        List<Prenotazione> lista = prenotazioneDAO.findPrenotazioneByQuery(prenotazioneSearchBuilder).getResults();
        Assert.assertTrue("Totale prenotazioni", lista.size() == 3);
        logger.debug("############################NUMBER_OF_PRENOTAZONI: {}\n", prenotazioneDAO.count().intValue());

    }

    @Test
    public void D_deletePrenotazioneTest() throws Exception {
        prenotazioneDAO.delete("03");
        Thread.sleep(1000);
        IPrenotazioneSearchBuilder prenotazioneSearchBuilder = IPrenotazioneSearchBuilder.PrenotazioneSearchBuilder.getPrenotazioneSearchBuilder()
                .withRangeData(new DateTime(2016, 1, 1, 0, 0), new DateTime(2016, 1, 31, 0, 0));
        List<Prenotazione> lista = prenotazioneDAO.findPrenotazioneByQuery(prenotazioneSearchBuilder).getResults();
        Assert.assertTrue("Totale prenotazioni", lista.size() == 2);
        logger.debug("############################NUMBER_OF_PRENOTAZONI: {}\n", prenotazioneDAO.count().intValue());

    }

    @Test
    public void E_findPrenotazioneTest() throws Exception {

        IPrenotazioneSearchBuilder prenotazioneSearchBuilder = IPrenotazioneSearchBuilder.PrenotazioneSearchBuilder.getPrenotazioneSearchBuilder()
                .withRangeData(new DateTime(2016, 1, 1, 0, 0), new DateTime(2016, 1, 31, 0, 0));
        List<Prenotazione> lista = prenotazioneDAO.findPrenotazioneByQuery(prenotazioneSearchBuilder).getResults();
        Assert.assertTrue("Totale prenotazioni", lista.size() == 2);
        logger.debug("############################NUMBER_OF_PRENOTAZONI: {}\n", lista.size());

    }

//	@Test
//	public void tearDown() throws Exception {
//		this.prenotazioneDocIndexCreator.deleteIndex();
//	}

}
