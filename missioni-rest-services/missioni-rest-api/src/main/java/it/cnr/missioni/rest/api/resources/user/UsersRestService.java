package it.cnr.missioni.rest.api.resources.user;

import it.cnr.missioni.model.user.User;
import it.cnr.missioni.rest.api.path.missione.MissioneServiceRSPathConfig;
import it.cnr.missioni.rest.api.path.user.UsersServiceRSPathConfig;
import it.cnr.missioni.rest.api.request.NotificationMissionRequest;
import it.cnr.missioni.rest.api.request.RecuperaPasswordRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * @author Giuseppe La Scaleia - CNR IMAA geoSDI Group
 * @email giuseppe.lascaleia@geosdi.org
 */
@Path(value = UsersServiceRSPathConfig.API_BASE_PATH)
@Produces(value = MediaType.APPLICATION_JSON)
@Consumes(value = MediaType.APPLICATION_JSON)
public interface UsersRestService {

	/**
	 * @param userName
	 * @param password
	 * @return {@link Response}
	 * @throws Exception
	 */
	@GET
	@Path(value = UsersServiceRSPathConfig.AUTHORIZE_USER_PATH)
	Response authorize(@QueryParam(value = "username") String userName, @QueryParam(value = "password") String password)
			throws Exception;

	/**
	 * @return {@link Response}
	 * @throws Exception
	 */
	// @GET
	// @Path(value = UsersServiceRSPathConfig.GET_ALL_USERS_PATH)
	// Response getLastUsers() throws Exception;

/**
 * 
 * @param nome
 * @param cognome
 * @param codiceFiscale
 * @param matricola
 * @param username
 * @param targa
 * @param numeroPatente
 * @param cartaCircolazione
 * @param polizzaAssicurativa
 * @param iban
 * @param mail
 * @param id
 * @param notId
 * @param responsabileGruppo
 * @param all
 * @param from
 * @param size
 * @return
 * @throws Exception
 */
	@GET
	@Path(value = UsersServiceRSPathConfig.GET_USER_BY_QUERY)
	Response getUserByQuery(@QueryParam(value = "nome") String nome, @QueryParam(value = "cognome") String cognome,
			@QueryParam(value = "codiceFiscale") String codiceFiscale,
			@QueryParam(value = "matricola") String matricola, @QueryParam(value = "username") String username,
			@QueryParam(value = "targa") String targa, @QueryParam(value = "numeroPatente") String numeroPatente,
			@QueryParam(value = "cartaCircolazione") String cartaCircolazione,
			@QueryParam(value = "polizzaAssicurativa") String polizzaAssicurativa,
			@QueryParam(value = "iban") String iban, @QueryParam(value = "mail") String mail,
			@QueryParam(value = "notId") String notId,
			@QueryParam(value = "id") String id,
			@QueryParam(value = "responsabileGruppo") Boolean responsabileGruppo,
			@QueryParam(value = "multiMatch") String  multiMatch,
			@DefaultValue(value="prefix") @QueryParam(value = "searchType") String  searchType,
			@QueryParam(value = "all") boolean all, @QueryParam(value = "from") int from,
			@QueryParam(value = "size") int size) throws Exception;

	@GET
	@Path(value = UsersServiceRSPathConfig.GET_USER_BY_USERNAME)
	Response getUserByQuery(@QueryParam(value = "username") String username) throws Exception;
	
	/**
	 * @param user
	 * @return {@link Response}
	 * @throws Exception
	 */
	@POST
	@Path(value = UsersServiceRSPathConfig.ADD_USER_PATH)
	Response addUser(User user) throws Exception;

	/**
	 * @param user
	 * @return {@link Response}
	 * @throws Exception
	 */
	@PUT
	@Path(value = UsersServiceRSPathConfig.UPDATE_USER_PATH)
	Response updateUser(User user) throws Exception;

	/**
	 * @param userID
	 * @return {@link Response}
	 * @throws Exception
	 */
	@DELETE
	@Path(value = UsersServiceRSPathConfig.DELETE_USER_PATH)
	Response deleteUser(@QueryParam(value = "userID") String userID) throws Exception;
	
	/**
	 * @param request
	 * @return {@link Response}
	 * @throws Exception
	 */
	@POST
	@Path(value = UsersServiceRSPathConfig.RECUPERA_PASSWORD_PATH)
	Response recuperaPassword(RecuperaPasswordRequest request) throws Exception;
}
