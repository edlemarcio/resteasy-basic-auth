package br.com.resteasy.auth.service;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJacksonProvider;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.ext.Provider;
import java.text.SimpleDateFormat;

/**
 * Created by enrique1 on 12/27/14.
 */
@Provider
@Consumes({"application/json", "text/json"})
@Produces({"application/json", "text/json"})
public class JacksonConfigurator extends ResteasyJacksonProvider {
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZZZ";

    public JacksonConfigurator() {
	super();
	ObjectMapper mapper = _mapperConfig.getConfiguredMapper();
	configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
	configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
	SerializationConfig serConfig = mapper.getSerializationConfig();
	serConfig.withDateFormat(new SimpleDateFormat(DATE_FORMAT));

	serConfig.withSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
	DeserializationConfig deserializationConfig = mapper.getDeserializationConfig();
	deserializationConfig.setDateFormat(new SimpleDateFormat(DATE_FORMAT));

    }

}