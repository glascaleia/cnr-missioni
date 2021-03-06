package it.cnr.missioni.notification.message;

import it.cnr.missioni.notification.bridge.implementor.MissioniMailImplementor;
import it.cnr.missioni.notification.support.itext.PDFBuilder;
import net.jcip.annotations.Immutable;

import java.util.Map;

/**
 * @author Giuseppe La Scaleia - CNR IMAA geoSDI Group
 * @email giuseppe.lascaleia@geosdi.org
 */
@Immutable
public class AddMissioneMessage extends CNRMissioniMessage {

    private final String cnrMissioniEmail;
    private final PDFBuilder missionePDFBuilder;
    private final String responsabileEMail;
    private final String missioneId;

    public AddMissioneMessage(String userName, String userSurname, String userEmail,String responsabileEMail,
            String theCnrMissioniEmail, PDFBuilder theMissionePDFBuilder,String missioneId) {
        super(userName, userSurname, userEmail);
        this.cnrMissioniEmail = theCnrMissioniEmail;
        this.responsabileEMail = responsabileEMail;
        this.missionePDFBuilder = theMissionePDFBuilder;
        this.missioneId = missioneId;
    }

    /**
     * @return {@link MissioniMailImplementor.NotificationMessageType}
     */
    @Override
    public MissioniMailImplementor.NotificationMessageType getNotificationMessageType() {
        return MissioniMailImplementor.NotificationMessageType.AGGIUNGI_MISSIONE_MAIL_PROD;
    }

    /**
     * @param map
     */
    @Override
    protected void injectParameters(Map<String, Object> map) {
        map.put("cnrMissioniEmail", this.cnrMissioniEmail);
        map.put("responsabileEmail", this.responsabileEMail);
        map.put("missionePDFBuilder", this.missionePDFBuilder);
        map.put("missioneId", this.missioneId);

    }
}
