package it.cnr.missioni.el.model.search;

import org.elasticsearch.index.query.QueryBuilder;
import org.geosdi.geoplatform.experimental.el.dao.GPPageableElasticSearchDAO.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Salvia Vito
 */
public interface IBooleanSearch {

    EnumBooleanType getType();

    QueryBuilder getBooleanQuery() throws Exception;

    ;

    abstract class AbstractBooleanSearch<S> implements IBooleanSearch {


        protected static final Logger logger = LoggerFactory.getLogger(Page.class);
        protected String field;
        protected S value;
        //DEFAULT
        protected EnumBooleanType type = EnumBooleanType.MUST;


        public AbstractBooleanSearch() {

        }

        public AbstractBooleanSearch(String field, S value) {
            this.field = field;
            this.value = value;
        }

        public AbstractBooleanSearch(String field, S value, EnumBooleanType type) {
            this.field = field;
            this.value = value;
            this.type = type;
        }

        public AbstractBooleanSearch(String field, EnumBooleanType type) {
            this.field = field;
            this.type = type;
        }

        public AbstractBooleanSearch(String field) {
            this.field = field;
        }

        public abstract QueryBuilder getBooleanQuery() throws Exception;

        /**
         * @return the field
         */
        public String getField() {
            return field;
        }

        /**
         * @param field
         */
        public void setField(String field) {
            this.field = field;
        }

        /**
         * @return the value
         */
        public S getValue() {
            return value;
        }

        /**
         * @param value
         */
        public void setValue(S value) {
            this.value = value;
        }

        /**
         * @return the type
         */
        public EnumBooleanType getType() {
            return type;
        }

        /**
         * @param type
         */
        public void setType(EnumBooleanType type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + " {"
                    + " field = " + field
                    + ", value = " + value + '}';
        }


    }

}
