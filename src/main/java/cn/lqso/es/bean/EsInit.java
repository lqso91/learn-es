package cn.lqso.es.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author luojie
 */
@Configuration
@PropertySource(value={"classpath:application.properties"})
public class EsInit {
    protected Logger logger = LoggerFactory.getLogger(EsInit.class);

    @Value("${es.host}")
    private String host;

    @Value("${es.port}")
    private Integer port;

    @Value("${es.index}")
    private String index;

    @Bean
    public EsConfig config(){
        EsConfig config = new EsConfig();
        config.setIndex(index);
        config.setHost(host);
        config.setPort(port);
        return config;
    }
}
