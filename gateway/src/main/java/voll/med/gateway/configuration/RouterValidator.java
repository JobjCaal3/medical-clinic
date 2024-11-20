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
            "/authentication/login-user",
            "/patients/register-patient"
    );

    public Predicate<ServerHttpRequest> isSecured = request -> {
        String requestPath = normalizePath(request.getURI().getPath());
        log.info("Evaluating path: " + requestPath);

        boolean isOpen = openEndpoints.stream()
                .map(this::normalizePath) // Normaliza las rutas registradas.
                .anyMatch(openEndpoint -> openEndpoint.equals(requestPath));

        log.info("Is secured? " + !isOpen + " for path: " + requestPath);
        return !isOpen; // Devuelve `true` si la ruta NO está en la lista abierta.
    };

    /**
     * Normaliza la ruta para evitar problemas con barras finales o caracteres no deseados.
     */
    private String normalizePath(String path) {
        if (path == null) return "";
        return path.trim().replaceAll("/$", ""); // Elimina barra al final si existe.
    }
}
