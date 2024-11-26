package voll.med.gateway.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
public class RouterValidator {

    private static final Log log = LogFactory.getLog(RouterValidator.class);

    public static final List<String> openEndpoints = List.of(
            "/authentication/register-user-patient",
            "/authentication/register-user-doctor",
            "/authentication/login-user",
            "/patients/register-patient"
    );


    public Predicate<ServerHttpRequest> isSecured =
            request -> openEndpoints.stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
