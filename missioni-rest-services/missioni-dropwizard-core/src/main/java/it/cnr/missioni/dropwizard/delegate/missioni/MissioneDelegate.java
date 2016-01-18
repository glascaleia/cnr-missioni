package it.cnr.missioni.dropwizard.delegate.missioni;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import it.cnr.missioni.el.dao.IMissioneDAO;
import it.cnr.missioni.el.dao.IUserDAO;
import it.cnr.missioni.el.model.search.builder.MissioneSearchBuilder;
import it.cnr.missioni.model.missione.Missione;
import it.cnr.missioni.model.user.User;
import it.cnr.missioni.model.validator.ICNRMissionValidator;
import it.cnr.missioni.notification.dispatcher.MissioniMailDispatcher;
import it.cnr.missioni.notification.message.factory.NotificationMessageFactory;
import it.cnr.missioni.notification.spring.configuration.CNRMissioniEmail;
import it.cnr.missioni.notification.support.itext.missione.MissionePDFBuilder;
import it.cnr.missioni.rest.api.request.NotificationMissionRequest;
import it.cnr.missioni.rest.api.response.missione.MissioniStore;
import org.geosdi.geoplatform.exception.IllegalParameterFault;
import org.geosdi.geoplatform.exception.ResourceNotFoundFault;
import org.geosdi.geoplatform.experimental.el.dao.GPElasticSearchDAO.Page;
import org.geosdi.geoplatform.logger.support.annotation.GeoPlatformLog;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Giuseppe La Scaleia - CNR IMAA geoSDI Group
 * @email giuseppe.lascaleia@geosdi.org
 */
class MissioneDelegate implements IMissioneDelegate {

    static {
        gen = Generators.timeBasedGenerator(EthernetAddress.fromInterface());
    }

    private static final TimeBasedGenerator gen;
    //
    @GeoPlatformLog
    private static Logger logger;
    //
    @Resource(name = "missioneDAO")
    private IMissioneDAO missioneDAO;
    @Resource(name = "userDAO")
    private IUserDAO userDAO;
    @Resource(name = "missioniMailDispatcher")
    private MissioniMailDispatcher missioniMailDispatcher;
    @Autowired
    private NotificationMessageFactory notificationMessageFactory;
    @Resource(name = "cnrMissioniItaliaEmail")
    private CNRMissioniEmail cnrMissioniItaliaEmail;
    @Resource(name = "cnrMissioniEsteroEmail")
    private CNRMissioniEmail cnrMissioniEsteroEmail;
    @Resource(name = "notificationMissionRequestValidator")
    private ICNRMissionValidator<NotificationMissionRequest, String> notificationMissionRequestValidator;

    @Override
    public MissioniStore getMissioneByQuery(String idMissione, String idUser, String stato, String numeroOrdineRimborso)
            throws Exception {
        // if ((missioneID == null) || (missioneID.isEmpty())) {
        // throw new IllegalParameterFault("The Parameter missioneID must not "
        // + "be null or an Empty String");
        // }

        MissioneSearchBuilder missioneSearchBuilder = MissioneSearchBuilder.getMissioneSearchBuilder()
                .withIdUser(idUser).withIdMissione(idMissione).withStato(stato)
                .withNumeroOrdineMissione(numeroOrdineRimborso);

        List<Missione> listaMissioni = this.missioneDAO.findMissioneByQuery(new Page(0, 10), missioneSearchBuilder);
        if (!listaMissioni.isEmpty()) {
            MissioniStore missioniStore = new MissioniStore();
            missioniStore.setMissioni(listaMissioni);
            return missioniStore;
        } else
            return null;

    }

    @Override
    public MissioniStore getLastUserMissions(String userID) throws Exception {
        return null;
    }

    @Override
    public String addMissione(Missione missione) throws Exception {
        if ((missione == null)) {
            throw new IllegalParameterFault("The Parameter missione must not be null ");
        }
        this.missioneDAO.persist(missione);
        return null;
    }

    @Override
    public Boolean updateMissione(Missione missione) throws Exception {
        if ((missione == null)) {
            throw new IllegalParameterFault("The Parameter missione must not be null ");
        }
        this.missioneDAO.update(missione);
        return Boolean.TRUE;
    }

    @Override
    public Boolean deleteMissione(String missioneID) throws Exception {
        if ((missioneID == null) || (missioneID.isEmpty())) {
            throw new IllegalParameterFault("The Parameter missioneID must not be null " + "or an Empty String.");
        }
        this.missioneDAO.delete(missioneID);
        return Boolean.TRUE;
    }

    /**
     * @param request
     * @return {@link Boolean}
     * @throws Exception
     */
    @Override
    public Boolean notifyMissionAdministration(NotificationMissionRequest request) throws Exception {
        if (request == null)
            throw new IllegalParameterFault("The Parameter Request must not be null.");

        String message = this.notificationMissionRequestValidator.validate(request);
        if (message != null) {
            throw new IllegalParameterFault(message);
        }

        Missione missione = this.missioneDAO.find(request.getMissionID());
        if (missione == null)
            throw new ResourceNotFoundFault("La Missione con ID : " + request.getMissionID() + " non esiste");

        User user = this.userDAO.find(missione.getIdUser());
        if (user == null)
            throw new ResourceNotFoundFault("L'Utente con ID : " + missione.getIdUser() + " non esiste");

        this.missioniMailDispatcher.dispatchMessage(this.notificationMessageFactory
                .buildAddMissioneMessage(user.getAnagrafica().getNome(), user.getAnagrafica().getCognome(),
                        user.getDatiCNR().getMail(),
                        (missione.isMissioneEstera() ? this.cnrMissioniEsteroEmail.getEmail()
                                : this.cnrMissioniItaliaEmail.getEmail()),
                        MissionePDFBuilder
                                .newPDFBuilder()
                                .withUser(user)
                                .withMissione(missione)));

        return Boolean.TRUE;
    }
}
