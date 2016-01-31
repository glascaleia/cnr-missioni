package it.cnr.missioni.rest.api.path.missione;

import it.cnr.missioni.rest.api.path.ApplicationServiceRSPathConfig;

/**
 * @author Giuseppe La Scaleia - CNR IMAA geoSDI Group
 * @email giuseppe.lascaleia@geosdi.org
 */
public final class MissioneServiceRSPathConfig extends ApplicationServiceRSPathConfig {

    /**
     * Missioni Service Path
     */
    public static final String MISSIONI_PATH = "/missioni";
    public static final String GET_MISSIONE_BY_QUERY = MISSIONI_PATH + "/getMissioneByQuery";
    public static final String GET_LAST_USER_MISSIONS_PATH = MISSIONI_PATH + "/getLastUserMissions";
    public static final String ADD_MISSIONE_PATH = MISSIONI_PATH + "/addMissione";
    public static final String UPDATE_MISSIONE_PATH = MISSIONI_PATH + "/updateMissione";
    public static final String DELETE_MISSIONE_PATH = MISSIONI_PATH + "/deleteMissione";
    public static final String NOTIFY_MISSIONE_ADMINISTRATION_PATH = MISSIONI_PATH + "/notifyMissionAdministration";
    public static final String NOTIFY_RIMBORSO_MISSIONE_ADMINISTRATION_PATH = MISSIONI_PATH + "/notifyRimborsoMissionAdministration";
    public static final String DOWNLOAD_MISSIONE_AS_PDF_PATH = MISSIONI_PATH + "/downloadMissioneAsPdf";
    public static final String DOWNLOAD_RIMBORSO_MISSIONE_AS_PDF_PATH = MISSIONI_PATH + "/downloadRimborsoMissioneAsPdf";
    public static final String GET_GEOCODER_STORE_FOR_MISSIONE_LOCATION_PATH = MISSIONI_PATH + "/getGeocoderStoreForMissioneLocation";
    public static final String GET_DISTANCE_FOR_MISSIONE_PATH = MISSIONI_PATH + "/getDistanceForMissione";

    private MissioneServiceRSPathConfig() {
    }
}