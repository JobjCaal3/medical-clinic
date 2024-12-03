package voll.med.gateway.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.factory.TokenRelayGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableHystrix
public class GatewayConfig {
    @Autowired
    private AuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("doctors",r -> r
                        .path("/doctors/**")
                        .filters(f->f.filter(filter))
                        .uri("lb://doctors"))
                .route("patients", r -> r
                        .path("/patients/**")
                        .filters(f->f.filter(filter))
                        .uri("lb://patients")
                        )
                .route("security-service", r -> r
                        .path("/authentication/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://security-service"))
                .build();
    }
}
