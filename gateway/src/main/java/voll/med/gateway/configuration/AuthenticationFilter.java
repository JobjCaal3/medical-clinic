package voll.med.gateway.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import voll.med.gateway.client.securityclient.ISecurityClient;

@Component
@RefreshScope
public class AuthenticationFilter implements GatewayFilter {
    private static final Log log = LogFactory.getLog(AuthenticationFilter.class);
    private RouterValidator routerValidator;
    private ISecurityClient securityClient;
    @Autowired
    public AuthenticationFilter(RouterValidator routerValidator, ISecurityClient securityClient) {
        this.routerValidator = routerValidator;
        this.securityClient = securityClient;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("llega aqui 1");
        ServerHttpRequest request = exchange.getRequest();
        log.info("llega aqui 2" + request.getURI().getPath());
        if(routerValidator.isSecured.test(request)) {
            log.info("llega aqui 3");
            String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authenticationMissing(request)) {
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }
            if (!StringUtils.hasText(bearerToken) && !bearerToken.startsWith("Bearer ")) {
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }

            final Boolean validateToken = securityClient.validateToken(bearerToken);
            if (!validateToken){
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }
        }else {
            log.info("Open endpoint: " + request.getURI().getPath());
        }
            log.info("llega aqui 4");
        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus){
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private Boolean authenticationMissing(ServerHttpRequest request){
        return !request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION);
    }
}
