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
import voll.med.gateway.Service.JwtUtils;
import voll.med.gateway.exceptions.ValidationIntegration;

import java.util.List;

@Component
@RefreshScope
public class AuthenticationFilter implements GatewayFilter {
    private static final Log log = LogFactory.getLog(AuthenticationFilter.class);
    private RouterValidator routerValidator;
    private JwtUtils jwtUtils;
    @Autowired
    public AuthenticationFilter(RouterValidator routerValidator, JwtUtils jwtUtils) {
        this.routerValidator = routerValidator;
        this.jwtUtils = jwtUtils;
    }

    /**
     *in charge of prior security and token validation
     * @param exchange the current server exchange
     * @param chain provides a way to delegate to the next filter
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if(routerValidator.isSecured.test(request)) {
            if (authenticationMissing(request))
                throw new ValidationIntegration(HttpStatus.UNAUTHORIZED, "without authority");

            String bearerToken = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

            if (!StringUtils.hasText(bearerToken) && !bearerToken.startsWith("Bearer "))
                throw new ValidationIntegration(HttpStatus.UNAUTHORIZED, "Token invalid");

            jwtUtils.validateToken(bearerToken);
        }
        return chain.filter(exchange);
    }

    /**
     *responsible for checking if the header contains authorization
     * @param request
     * @return
     */
    private Boolean authenticationMissing(ServerHttpRequest request){
        return !request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION);
    }
}
