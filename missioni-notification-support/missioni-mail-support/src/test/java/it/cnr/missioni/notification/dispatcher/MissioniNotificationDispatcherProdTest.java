package it.cnr.missioni.notification.dispatcher;

import it.cnr.missioni.model.configuration.Direttore;
import it.cnr.missioni.model.configuration.UrlImage;
import it.cnr.missioni.model.missione.*;
import it.cnr.missioni.model.rimborso.Fattura;
import it.cnr.missioni.model.rimborso.Rimborso;
import it.cnr.missioni.model.user.*;
import it.cnr.missioni.model.user.DatiCNR.LivelloUserEnum;
import it.cnr.missioni.notification.message.factory.NotificationMessageFactory;
import it.cnr.missioni.notification.support.itext.PDFBuilder;
import it.cnr.missioni.notification.support.itext.anticipopagamento.AnticipoPagamentoPDFBuilder;
import it.cnr.missioni.notification.support.itext.missione.MissionePDFBuilder;
import it.cnr.missioni.notification.support.itext.rimborso.RimborsoPDFBuilder;
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
                .buildAddMissioneMessage("Giuseppe", "La Scaleia", "vito.salvia@gmail.com","vito.salvia@gmail.com",
                        "vito.salvia@gmail.com", MissionePDFBuilder
                                .newPDFBuilder()
                                .withUser(buildUserTest())
                                .withMissione(buildMissioneTest()).withUrlImage(buildUrlImageTest()).withDirettore(buildDirettoreTest()),"1-2016"));
        Thread.sleep(6000);
    }

    @Test
    public void dispatchUpdateMissioneMailProdTest() throws Exception {
        this.missioniMailDispatcher.dispatchMessage(this.notificationMessageProdFactory
                .buildUpdateMissioneMessage("Giuseppe", "La Scaleia","Inserita",
                        "vito.salvia@gmail.com","vito.salvia@gmail.com", UUID.randomUUID().toString(),
                        MissionePDFBuilder
                                .newPDFBuilder()
                                .withUser(buildUserTest())
                                .withMissione(buildMissioneTest()).withUrlImage(buildUrlImageTest()).withDirettore(buildDirettoreTest())));
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
    
    @Test
    public void dispatchAddVeicoloMailProdTest() throws Exception {
    	
    	PDFBuilder pdfBuilder = MissionePDFBuilder
                .newPDFBuilder();
    	pdfBuilder.setMezzoProprio(true);
    	pdfBuilder.withUser(buildUserTest())
                .withMissione(buildMissioneTest()).withVeicolo(buildVeicoloTest()).withUrlImage(buildUrlImageTest()).withDirettore(buildDirettoreTest());
        this.missioniMailDispatcher.dispatchMessage(this.notificationMessageProdFactory
                .buildAddMissioneMessage("Vito", "Salvia", "vito.salvia@gmail.com","vito.salvia@gmail.com",
                        "vito.salvia@gmail.com", pdfBuilder,"1-2016"));
        Thread.sleep(6000);
    }

    @Test
    public void dispatchAddRimborsoMissioneMailProdTest() throws Exception {
    	PDFBuilder pdfBuilder = RimborsoPDFBuilder
                .newPDFBuilder();
    	    	pdfBuilder.withUser(buildUserTest())
                .withMissione(buildMissioneTest()).withUrlImage(buildUrlImageTest()).withDirettore(buildDirettoreTest());
        this.missioniMailDispatcher.dispatchMessage(this.notificationMessageProdFactory
                .buildAddRimborsoMessage("Vito", "Salvia", "vito.salvia@gmail.com","vito.salvia@gmail.com",
                		UUID.randomUUID().toString(),pdfBuilder));
        Thread.sleep(6000);
    }
    
    @Test
    public void dispatchAddAnticipoPagamentoMailProdTest() throws Exception {
    	
    	PDFBuilder pdfBuilder = AnticipoPagamentoPDFBuilder
                .newPDFBuilder();
    	    	pdfBuilder.withUser(buildUserTest())
    	    	.withFile(new File("./target/AnticipoPagamento.pdf"))
                .withMissione(buildMissioneTest()).withUrlImage(buildUrlImageTest()).withDirettore(buildDirettoreTest());
        this.missioniMailDispatcher.dispatchMessage(this.notificationMessageProdFactory
                .buildAddAnticipoPagamentoMessage("Vito", "Salvia", "vito.salvia@gmail.com","vito.salvia@gmail.com",
                		UUID.randomUUID().toString(),pdfBuilder));
        Thread.sleep(6000);
    }
    
    @Test
    public void dispatchRecuperaPasswordMailProdTest() throws Exception {
        this.missioniMailDispatcher.dispatchMessage(this.notificationMessageProdFactory
                .buildRecuperaPasswordMessage("Vito", "Salvia", "vito.salvia@gmail.com","new_password"));
        Thread.sleep(6000);
    }
    
    Veicolo buildVeicoloTest(){
    	Veicolo v = new Veicolo();
    	v.setTipo("01");
    	v.setCartaCircolazione("Carta:123");
    	v.setPolizzaAssicurativa("Polizza:AAAA");
    	v.setTipo("FORD");
    	return v;
    }
    
    Direttore buildDirettoreTest(){
    	Direttore d = new Direttore();
    	d.setValue("Dott. Vincenzo Lapenna");
    	return d;
    }
    
    UrlImage buildUrlImageTest(){
    	UrlImage urlImage = new UrlImage();
		urlImage.setLogoMinistero("https://missioni.imaa.cnr.it/logoMinistero.jpg");
		urlImage.setLogoCnr("https://missioni.imaa.cnr.it/logoCnr.jpg");
		urlImage.setLogoImaa("https://missioni.imaa.cnr.it/logoImaa.jpg");
    	return urlImage;
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
        credenziali.setRuoloUtente(RuoloUserEnum.UTENTE_SEMPLICE);
        credenziali.setPassword(("vitosalvia"));
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
        datiCNR.setLivello(LivelloUserEnum.V);
        datiCNR.setMail("vito.salvia@gmail.com");
        datiCNR.setMatricola("1111111");
        datiCNR.setDescrizioneQualifica("Assegnista");
        datiCNR.setIdQualifica("01");
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
        missione.setAltreLocalita("Napoli");
        missione.setIdUser("01");
        missione.setMissioneEstera(false);
        missione.setStato(StatoEnum.PRESA_IN_CARICO);
        missione.setFondo("fondo");
        missione.setGAE("GAE");
        missione.setDataInserimento(new DateTime(2015, 11, 13, 0, 0, DateTimeZone.UTC));
        missione.setMezzoProprio(true);
        missione.setDistanza("100.00 Km");
        missione.setMotivazioni("prova");
        missione.setAltreDisposizioni("Altre disposizioni");
        missione.setShortUserSeguito("Salvia Vito");
        missione.setMotivazioneSeguito("A seguito");
        
        DatiPeriodoMissione datiPeriodoMissione = new DatiPeriodoMissione();
        datiPeriodoMissione.setInizioMissione(new DateTime(2015, 11, 11, 8, 0, DateTimeZone.UTC));
        datiPeriodoMissione.setFineMissione(new DateTime(2015, 11, 12, 7, 59, DateTimeZone.UTC));
        missione.setDatiPeriodoMissione(datiPeriodoMissione);
        
        DatiMissioneEstera datiMissioneEstera = new DatiMissioneEstera();
//        datiMissioneEstera.setAttraversamentoFrontieraAndata(new DateTime(2015, 11, 11, 0, 0, DateTimeZone.UTC));
//        datiMissioneEstera.setAttraversamentoFrontieraRitorno(new DateTime(2015, 11, 15, 0, 0, DateTimeZone.UTC));
//        datiMissioneEstera.setTrattamentoMissioneEsteraEnum(TrattamentoMissioneEsteraEnum.RIMBORSO_DOCUMENTATO);
        missione.setDatiMissioneEstera(datiMissioneEstera);

        Fattura fattura = new Fattura();
        fattura.setNumeroFattura("134");
        fattura.setData(new DateTime(2015, 11, 12, 13, 0, DateTimeZone.UTC));
        fattura.setImporto(89.8);
        fattura.setIdTipologiaSpesa("01");
        fattura.setShortDescriptionTipologiaSpesa("Pernottamento");
        fattura.setValuta("Euro");
        fattura.setId("1111111111111");

        Fattura fattura_2 = new Fattura();
        fattura_2.setNumeroFattura("135");
        fattura_2.setData(new DateTime(2015, 11, 13, 13, 0, DateTimeZone.UTC));
        fattura_2.setImporto(89.8);
        fattura.setIdTipologiaSpesa("01");
        fattura_2.setShortDescriptionTipologiaSpesa("Pernottamento");
        fattura_2.setValuta("Euro");
        fattura_2.setId("2222222222222");

        Rimborso rimborso = new Rimborso();
        rimborso.setNumeroOrdine("M_01");
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
