package it.cnr.missioni.notification.dispatcher;

import it.cnr.missioni.model.missione.*;
import it.cnr.missioni.model.rimborso.Fattura;
import it.cnr.missioni.model.rimborso.Rimborso;
import it.cnr.missioni.model.user.*;
import it.cnr.missioni.notification.message.factory.NotificationMessageFactory;
import it.cnr.missioni.notification.support.itext.missione.MissionePDFBuilder;
import org.geosdi.geoplatform.logger.support.annotation.GeoPlatformLog;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static it.cnr.missioni.notification.mail.CNRMissioniEmailTest.GP_MAIL_KEY;

/**
 * @author Giuseppe La Scaleia - CNR IMAA geoSDI Group
 * @email giuseppe.lascaleia@geosdi.org
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"classpath:applicationContext-Dispatcher-Prod-Test.xml"})
@ActiveProfiles(value = {"prod", "GPMailVelocitySupport"})
public class MissioniNotificationDispatcherProdTest {

    @GeoPlatformLog
    private static Logger logger;
    //
    private static final String GP_ASYNC_KEY = "GP_ASYNC_FILE_PROP";
    //
    @Resource(name = "missioniMailDispatcher")
    private MissioniMailDispatcher missioniMailDispatcher;
    @Resource(name = "notificationMessageProdFactory")
    private NotificationMessageFactory notificationMessageProdFactory;

    @BeforeClass
    public static void beforeClass() {
        System.setProperty(GP_MAIL_KEY, "gp-mail-test.prop");
        System.setProperty(GP_ASYNC_KEY, "gp-async-test.prop");
    }

    @AfterClass
    public static void afterClass() {
        System.clearProperty(GP_MAIL_KEY);
        System.clearProperty(GP_ASYNC_KEY);
    }

    @Before
    public void setUp() throws Exception {
        Assert.assertNotNull(this.missioniMailDispatcher);
        Assert.assertNotNull(this.notificationMessageProdFactory);
    }

    @Test
    public void dispatchAddMissioneMailProdTest() throws Exception {
        this.missioniMailDispatcher.dispatchMessage(this.notificationMessageProdFactory
                .buildAddMissioneMessage("Giuseppe", "La Scaleia", "glascaleia@gmail.com",
                        "francesco.izzi@gmail.com", MissionePDFBuilder
                                .newPDFBuilder()
                                .withUser(buildUserTest())
                                .withMissione(buildMissioneTest())));
        Thread.sleep(6000);
    }

    @Test
    public void dispatchUpdateMissioneMailProdTest() throws Exception {
        this.missioniMailDispatcher.dispatchMessage(this.notificationMessageProdFactory
                .buildUpdateMissioneMessage("Giuseppe", "La Scaleia", "glascaleia@gmail.com",
                        "vito.salvia@gmail.com", UUID.randomUUID().toString(),
                        MissionePDFBuilder
                                .newPDFBuilder()
                                .withUser(buildUserTest())
                                .withMissione(buildMissioneTest())));
        Thread.sleep(4000);
    }

    @Test
    public void createFileTest() throws Exception {
        MissionePDFBuilder
                .newPDFBuilder()
                .withUser(buildUserTest())
                .withMissione(buildMissioneTest())
                .withFile(new File("./target/Missione.pdf")).build();
    }

    User buildUserTest() {
        User user = new User();
        Anagrafica anagrafica = null;
        Credenziali credenziali = null;
        user.setId("01");
        anagrafica = new Anagrafica();
        anagrafica.setCognome("Salvia");
        anagrafica.setNome("Vito");
        anagrafica.setDataNascita(new DateTime(1982, 7, 30, 0, 0));
        anagrafica.setCodiceFiscale("slvvttttttttttt");
        anagrafica.setLuogoNascita("Potenza");
        credenziali = new Credenziali();
        credenziali.setUsername("vito.salvia");
        credenziali.setRuoloUtente(RuoloUtenteEnum.UTENTE_SEMPLICE);
        credenziali.setPassword(credenziali.md5hash("vitosalvia"));
        user.setCredenziali(credenziali);
        user.setAnagrafica(anagrafica);
        Veicolo veicolo = new Veicolo();
        veicolo.setTipo("Ford Fiesta");
        veicolo.setTarga("AA111BB");
        veicolo.setCartaCircolazione("12234");
        veicolo.setPolizzaAssicurativa("A1B2");
        veicolo.setVeicoloPrincipale(true);
        Map<String, Veicolo> mappaVeicoli = new HashMap<String, Veicolo>();
        mappaVeicoli.put(veicolo.getTarga(), veicolo);
        user.setMappaVeicolo(mappaVeicoli);
        DatiCNR datiCNR = new DatiCNR();
        datiCNR.setDatoreLavoro("Izzi");
        datiCNR.setIban("IT0000000000000000");
        datiCNR.setLivello("5");
        datiCNR.setMail("vito.salvia@gmail.com");
        datiCNR.setMatricola("1111111");
        datiCNR.setQualifica("");
        datiCNR.setCodiceTerzo("123");
        user.setDatiCNR(datiCNR);
        Patente p = new Patente();
        p.setDataRilascio(new DateTime(2001, 12, 15, 0, 0));
        p.setNumeroPatente("12334");
        p.setRilasciataDa("MCTC");
        p.setValidaFinoAl(new DateTime(2021, 12, 15, 0, 0));
        user.setPatente(p);
        Residenza r = new Residenza();
        r.setIndirizzo("Via Verdi");
        r.setComune("Tito");
        r.setDomicilioFiscale("Via Convento");
        user.setResidenza(r);
        user.setDataRegistrazione(new DateTime(2015, 1, 4, 0, 0));
        user.setDateLastModified(new DateTime(2015, 1, 4, 0, 0));
        user.setRegistrazioneCompletata(true);
        return user;
    }

    Missione buildMissioneTest() {
        Missione missione = new Missione();
        missione.setId("M_01");
        missione.setOggetto("Conferenza");
        missione.setLocalita("Roma");
        missione.setIdUser("01");
        missione.setMissioneEstera(false);
        missione.setStato(StatoEnum.PRESA_IN_CARICO);
        missione.setFondo("fondo");
        missione.setGAE("GAE");
        missione.setDataInserimento(new DateTime(2015, 11, 13, 0, 0, DateTimeZone.UTC));
        missione.setMezzoProprio(true);
        missione.setDistanza(100.00);
        DatiAnticipoPagamenti dati = new DatiAnticipoPagamenti();
        dati.setAnticipazioniMonetarie(true);
        dati.setMandatoCNR("AA11");
        dati.setRimborsoDaTerzi(false);
        dati.setSpeseMissioniAnticipate(102.00);
        missione.setDatiAnticipoPagamenti(dati);
        DatiPeriodoMissione datiPeriodoMissione = new DatiPeriodoMissione();
        datiPeriodoMissione.setInizioMissione(new DateTime(2015, 11, 11, 0, 0, DateTimeZone.UTC));
        datiPeriodoMissione.setFineMissione(new DateTime(2015, 11, 15, 0, 0, DateTimeZone.UTC));
        missione.setDatiPeriodoMissione(datiPeriodoMissione);

        DatiMissioneEstera datiMissioneEstera = new DatiMissioneEstera();
        datiMissioneEstera.setAttraversamentoFrontieraAndata(new DateTime(2015, 11, 11, 0, 0, DateTimeZone.UTC));
        datiMissioneEstera.setAttraversamentoFrontieraRitorno(new DateTime(2015, 11, 15, 0, 0, DateTimeZone.UTC));
        missione.setDatiMissioneEstera(datiMissioneEstera);

        Fattura fattura = new Fattura();
        fattura.setNumeroFattura(new Long(134));
        fattura.setData(new DateTime(2015, 11, 12, 13, 0, DateTimeZone.UTC));
        fattura.setImporto(89.8);
        fattura.setTipologiaSpesa("Pernottamento");
        fattura.setValuta("Euro");
        fattura.setId("1111111111111");

        Fattura fattura_2 = new Fattura();
        fattura_2.setNumeroFattura(new Long(135));
        fattura_2.setData(new DateTime(2015, 11, 13, 13, 0, DateTimeZone.UTC));
        fattura_2.setImporto(89.8);
        fattura_2.setTipologiaSpesa("Pernottamento");
        fattura_2.setValuta("Euro");
        fattura_2.setId("2222222222222");

        Rimborso rimborso = new Rimborso();
        rimborso.setNumeroOrdine(56l);
        rimborso.setAvvisoPagamento("Via Verdi");
        rimborso.setAnticipazionePagamento(0.0);
        rimborso.setDataRimborso(new DateTime(2015, 12, 12, 13, 14, DateTimeZone.UTC));
        rimborso.setTotale(179.6);

        rimborso.getMappaFattura().put("1111111111111", fattura);
        rimborso.getMappaFattura().put("2222222222222", fattura_2);
        missione.setRimborso(rimborso);
        return missione;
    }
}
