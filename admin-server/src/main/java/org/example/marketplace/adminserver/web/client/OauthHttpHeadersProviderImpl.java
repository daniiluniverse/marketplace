package org.example.marketplace.adminserver.web.client;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

public class OauthHttpHeadersProviderImpl extends OauthHttpHeadersProvider {

    private final OAuth2AuthorizedClientManager authorizedClientManager;

    public OauthHttpHeadersProviderImpl(OAuth2AuthorizedClientManager authorizedClientManager) {
        this.authorizedClientManager = authorizedClientManager;
    }

    @Override
    public HttpHeaders getHeaders(Instance instance) {
        OAuth2AuthorizedClient auth2AuthorizedClient = this.authorizedClientManager.authorize(
                OAuth2AuthorizeRequest.withClientRegistrationId("keycloak")
                        .principal("admin-service")
                        .build());
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(auth2AuthorizedClient.getAccessToken().getTokenValue());
        return headers;
    }
}
