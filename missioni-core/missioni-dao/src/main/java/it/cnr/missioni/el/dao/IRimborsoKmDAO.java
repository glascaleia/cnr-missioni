package it.cnr.missioni.el.dao;

import org.geosdi.geoplatform.experimental.el.dao.GPElasticSearchDAO;
import org.geosdi.geoplatform.experimental.el.dao.PageResult;

import it.cnr.missioni.el.model.search.builder.RimborsoKmSearchBuilder;
import it.cnr.missioni.model.configuration.RimborsoKm;

/**
 * @author Salvia Vito
 */
public interface IRimborsoKmDAO extends GPElasticSearchDAO.GPElasticSearchBaseDAO<RimborsoKm> {

	PageResult<RimborsoKm> findRimborsoKmByQuery(RimborsoKmSearchBuilder rimborsoKmSearchBuilder) throws Exception;


}
