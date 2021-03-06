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
public class AddRimborsoMessage extends CNRMissioniMessage {

    private final String cnrMissioniEmail;
    private final PDFBuilder rimborsoPDFBuilder;
    private final String missioneID;

    public AddRimborsoMessage(String userName, String userSurname, String userEmail,
            String theCnrMissioniEmail, String theMissioneID, PDFBuilder theRimborsoPDFBuilder) {
        super(userName, userSurname, userEmail);
        this.cnrMissioniEmail = theCnrMissioniEmail;
        this.missioneID = theMissioneID;
        this.rimborsoPDFBuilder = theRimborsoPDFBuilder;
    }

    /**
     * @return {@link MissioniMailImplementor.NotificationMessageType}
     */
    @Override
    public MissioniMailImplementor.NotificationMessageType getNotificationMessageType() {
        return MissioniMailImplementor.NotificationMessageType.RICHIESTA_RIMBORSO_MISSIONE_MAIL_PROD;
    }

    /**
     * @param map
     */
    @Override
    protected void injectParameters(Map<String, Object> map) {
        map.put("cnrMissioniEmail", this.cnrMissioniEmail);
        map.put("missioneID", this.missioneID);
        map.put("rimborsoPDFBuilder", this.rimborsoPDFBuilder);
    }
}
