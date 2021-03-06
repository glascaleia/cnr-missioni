package it.cnr.missioni.rest.api.resources.rimborsoKm;

import it.cnr.missioni.model.configuration.RimborsoKm;
import it.cnr.missioni.rest.api.path.rimborsoKm.RimborsoKmServiceRSPathConfig;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * 
 * @author Salvia Vito
 *
 */
@Path(value = RimborsoKmServiceRSPathConfig.API_BASE_PATH)
@Produces(value = MediaType.APPLICATION_JSON)
@Consumes(value = MediaType.APPLICATION_JSON)
public interface RimborsoKmRestService {

	/**
	 *
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path(value = RimborsoKmServiceRSPathConfig.GET_RIMBORSO_KM_BY_QUERY)
	Response getRimborsoKmByQuery() throws Exception;

	/**
	 * 
	 * @param rimborsoKm
	 * @return
	 * @throws Exception
	 */
	@POST
	@Path(value = RimborsoKmServiceRSPathConfig.ADD_RIMBORSO_KM_PATH)
	Response addRimborsoKm(RimborsoKm rimborsoKm) throws Exception;

	/**
	 * 
	 * @param rimborsoKm
	 * @return
	 * @throws Exception
	 */
	@PUT
	@Path(value = RimborsoKmServiceRSPathConfig.UPDATE_RIMBORSO_KM_PATH)
	Response updateRimborsoKm(RimborsoKm rimborsoKm) throws Exception;

	/**
	 * 
	 * @param rimborsoKmID
	 * @return
	 * @throws Exception
	 */
	@DELETE
	@Path(value = RimborsoKmServiceRSPathConfig.DELETE_RIMBORSO_KM_PATH)
	Response deleteRimborsoKm(@QueryParam(value = "rimborsoKmID") String rimborsoKmID) throws Exception;
}
