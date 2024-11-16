package voll.med.gateway.client.securityclient;

import feign.HeaderMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "security-service")
public interface ISecurityClient {
    @GetMapping("/authentication/validate-token")
    Boolean validateToken(@RequestParam String token);

}
