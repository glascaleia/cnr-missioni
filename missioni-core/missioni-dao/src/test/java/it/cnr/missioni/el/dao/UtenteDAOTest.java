package it.cnr.missioni.el.dao;

import it.cnr.missioni.el.model.search.UtenteModelSearch;
import it.cnr.missioni.model.utente.Anagrafica;
import it.cnr.missioni.model.utente.Credenziali;
import it.cnr.missioni.model.utente.DatiCNR;
import it.cnr.missioni.model.utente.Patente;
import it.cnr.missioni.model.utente.Residenza;
import it.cnr.missioni.model.utente.RuoloUtenteEnum;
import it.cnr.missioni.model.utente.Utente;
import it.cnr.missioni.model.utente.Veicolo;

import org.geosdi.geoplatform.experimental.el.configurator.GPIndexConfigurator;
import org.geosdi.geoplatform.experimental.el.dao.GPElasticSearchDAO.Page;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Salvia Vito
 */
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-DAO-Test.xml"})
public class UtenteDAOTest {

    @GeoPlatformLog
    static Logger logger;
    @Resource(name = "missioniIndexConfigurator")
    private GPIndexConfigurator missioniDocIndexConfigurator;

    @Resource(name = "utenteIndexCreator")
    private GPIndexCreator utenteDocIndexCreator;

    @Resource(name = "utenteDAO")
    private IUtenteDAO utenteDAO;

    private List<Utente> listaUtenti = new ArrayList<Utente>();

    @Before
    public void setUp() {
        Assert.assertNotNull(utenteDocIndexCreator);
        Assert.assertNotNull(missioniDocIndexConfigurator);
        Assert.assertNotNull(utenteDAO);
    }

    @Test
    public void A_createUtenteCNRTest() throws Exception {
        creaUtenti();
        utenteDAO.persist(listaUtenti);
        Thread.sleep(1000);
        logger.debug("############################NUMBER_OF_UTENTI : {}\n", this.utenteDAO.count().intValue());
    }

    @Test
    public void B_findUtenteByUsernameValidaTest() throws Exception {
    	UtenteModelSearch utenteModelSearch = new UtenteModelSearch(null, null, null, "vito.salvia");
        List<Utente> lista = utenteDAO.findUtenteByQuery(new Page(0,10),utenteModelSearch);
        logger.debug("############################UTENTE_WITH_USERNAME : {}\n",
                lista.get(0).getAnagrafica().getCognome() + " " + lista.get(0).getAnagrafica().getNome());
        Assert.assertTrue("FIND UTENTE BY USERNAME VALIDA", lista.get(0) != null);
    }

    @Test
    public void C_findUtenteByUsernameErrataTest() throws Exception {
    	UtenteModelSearch utenteModelSearch = new UtenteModelSearch(null, null, null, "vito.salvi");
        List<Utente> lista = utenteDAO.findUtenteByQuery(new Page(0,10),utenteModelSearch);
        Assert.assertTrue("FIND UTENTE BY USERNAME ERRATA", lista.isEmpty());
    }

    @Test
    public void D_updatePasswordUtente() throws Exception {
    	UtenteModelSearch utenteModelSearch = new UtenteModelSearch(null, null, null, "vito.salvia");
        List<Utente> lista = utenteDAO.findUtenteByQuery(new Page(0,10),utenteModelSearch);
        Utente  utente = lista.get(0);
        String oldPassword = utente.getCredenziali().getPassword();
        utente.getCredenziali().setPassword(utente.getCredenziali().md5hash("salvia.vito"));
        utenteDAO.persist(utente);
        Thread.sleep(1000);
        utenteModelSearch = new UtenteModelSearch(null, null, null, "vito.salvia");
        lista = utenteDAO.findUtenteByQuery(new Page(0,10),utenteModelSearch);
        utente = lista.get(0);
        String newPassword = utente.getCredenziali().getPassword();
        Assert.assertTrue("Update Password Utente", !oldPassword.equals(newPassword));

    }

    @Test
    public void tearDown() throws Exception {
        this.utenteDocIndexCreator.deleteIndex();
    }

    private void creaUtenti() {
        Utente utente = null;
        Anagrafica anagrafica = null;
        Credenziali credenziali = null;
        utente = new Utente();
        utente.setId("01");
        anagrafica = new Anagrafica();
        anagrafica.setCognome("Salvia");
        anagrafica.setNome("Vito");
        anagrafica.setDataNascita(new DateTime(1982,7,30,0,0));
        anagrafica.setCodiceFiscale("slvvttttttttttt");
        anagrafica.setLuogoNascita("Potenza");
        credenziali = new Credenziali();
        credenziali.setUsername("vito.salvia");
        credenziali.setRuoloUtente(RuoloUtenteEnum.UTENTE_SEMPLICE);
        credenziali.setPassword(credenziali.md5hash("vitosalvia"));
        utente.setCredenziali(credenziali);
        utente.setAnagrafica(anagrafica);
        Veicolo veicolo = new Veicolo();
        veicolo.setTipo("Ford Fiesta");
        veicolo.setTarga("AA111BB");
        veicolo.setCartaCircolazione("12234");
        veicolo.setPolizzaAssicurativa("A1B2");
        Map<String,Veicolo> mappaVeicoli = new HashMap<String,Veicolo>();
        mappaVeicoli.put(veicolo.getTarga(), veicolo);
        utente.setMappaVeicolo(mappaVeicoli);
        DatiCNR datiCNR = new DatiCNR();
        datiCNR.setDatoreLavoro("Izzi");
        datiCNR.setIban("IT0000000000000000");
        datiCNR.setLivello(5);
        datiCNR.setMail("vito.salvia@gmail.com");
        datiCNR.setMatricola("1111111");
        datiCNR.setQualifica("");
        utente.setDatiCNR(datiCNR);
        Patente p = new Patente();
        p.setDataRilascio(new DateTime(2001,12,15,0,0));
        p.setNumeroPatente("12334");
        p.setRilasciataDa("MCTC");
        p.setValidaFinoAl(new DateTime(2021,12,15,0,0));
        utente.setPatente(p);
        Residenza r = new Residenza();
        r.setIndirizzo("Via Verdi");
        r.setComune("Tito");
        r.setDomicilioFiscale("Via Convento");
        utente.setResidenza(r);
        utente.setDataRegistrazione(new DateTime(2015,1,4,0,0));
        listaUtenti.add(utente);

        utente = new Utente();
        utente.setId("02");
        anagrafica = new Anagrafica();
        anagrafica.setCognome("Rossi");
        anagrafica.setNome("Paolo");
        credenziali = new Credenziali();
        credenziali.setUsername("paolo.rossi");
        credenziali.setPassword(credenziali.md5hash("paolorossi"));
        utente.setCredenziali(credenziali);
        utente.setAnagrafica(anagrafica);
        listaUtenti.add(utente);

        utente = new Utente();
        utente.setId("03");
        anagrafica = new Anagrafica();
        anagrafica.setCognome("Mario");
        anagrafica.setNome("Bianchi");
        credenziali = new Credenziali();
        credenziali.setUsername("mario.bianchi");
        credenziali.setPassword(credenziali.md5hash("mariobianchi"));
        utente.setCredenziali(credenziali);
        utente.setAnagrafica(anagrafica);
        listaUtenti.add(utente);

    }

}
