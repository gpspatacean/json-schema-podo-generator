package net.gspatace.json.schema.podo.generator.webapp;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * This class is a configuration class for Spring Web MVC framework.
 * It implements the WebMvcConfigurer interface to provide custom configuration options.
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    final WebAppConfigurationFilter webAppConfigurationFilter;

    public WebMvcConfiguration(final WebAppConfigurationFilter webAppConfigurationFilter) {
        this.webAppConfigurationFilter = webAppConfigurationFilter;
    }

    @Bean
    FilterRegistrationBean<WebAppConfigurationFilter> filterRegistrationBean() {
        final FilterRegistrationBean<WebAppConfigurationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(webAppConfigurationFilter);
        registrationBean.addUrlPatterns("/assets/config.json");
        registrationBean.setOrder(100);
        return registrationBean;
    }
}
