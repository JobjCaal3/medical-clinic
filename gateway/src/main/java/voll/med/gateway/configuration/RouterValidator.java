package voll.med.gateway.configuration;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
public class RouterValidator {
    /**
     * is responsible for storing open urls
     */
    public static final List<String> openEndpoints = List.of(
            "/authentication/register-user-patient",
            "/authentication/register-user-doctor",
            "/authentication/login-user",
            "/patients/register-patient"
    );

    /**
     * It is responsible for verifying if the urls to which a request
     * is being made is blocked or open
     */
    public Predicate<ServerHttpRequest> isSecured =
            request -> openEndpoints.stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
