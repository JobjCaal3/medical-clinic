package voll.med.gateway.configuration;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
public class RouterValidator {
    public static final List<String> openEndPints = List.of(
            "/authentication/register-user-doctor",
            "/authentication/login-user");

    public Predicate<ServerHttpRequest> isSecured = request -> openEndPints.stream()
            .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
