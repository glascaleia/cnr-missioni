package it.cnr.missioni.rest.api.resources.base;

import it.cnr.missioni.model.missione.Missione;
import it.cnr.missioni.rest.api.path.base.BaseServiceRSPathConfig;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Giuseppe La Scaleia - CNR IMAA geoSDI Group
 * @email giuseppe.lascaleia@geosdi.org
 */
@Path(value = BaseServiceRSPathConfig.API_BASE_PATH)
@Produces(value = MediaType.APPLICATION_JSON)
@Consumes(value = MediaType.APPLICATION_JSON)
public interface BaseRestService {

    /**
     * @param missioneID
     * @return {@link Response}
     * @throws Exception
     */
    @GET
    @Path(value = BaseServiceRSPathConfig.GET_MISSIONE_BY_ID_PATH)
    Response getMissioneByID(@QueryParam(value = "missioneID") String missioneID) throws Exception;

    /**
     * @param userID
     * @return {@link Response}
     * @throws Exception
     */
    @GET
    @Path(value = BaseServiceRSPathConfig.GET_LAST_USER_MISSIONS_PATH)
    Response getLastUserMissions(@QueryParam(value = "userID") String userID) throws Exception;

    /**
     * @param missione
     * @return {@link Response}
     * @throws Exception
     */
    @POST
    @Path(value = BaseServiceRSPathConfig.ADD_MISSIONE_PATH)
    Response addMissione(Missione missione) throws Exception;

    /**
     * @param missione
     * @return {@link Response}
     * @throws Exception
     */
    @PUT
    @Path(value = BaseServiceRSPathConfig.UPDATE_MISSIONE_PATH)
    Response updateMissione(Missione missione) throws Exception;

    /**
     * @param missionID
     * @return {@link Response}
     * @throws Exception
     */
    @DELETE
    @Path(value = BaseServiceRSPathConfig.DELETE_MISSIONE_PATH)
    Response deleteMissione(@QueryParam(value = "missioneID") String missioneID) throws Exception;
}