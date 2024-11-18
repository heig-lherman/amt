package ch.heigvd.amt.jpa.resource;

import io.quarkus.security.Authenticated;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.time.Instant;
import java.util.Date;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("logout")
public class LogoutResource {

    private final String cookieName;
    private final URI loginUri;

    public LogoutResource(
        @ConfigProperty(name = "quarkus.http.auth.form.cookie-name") String cookieName,
        @ConfigProperty(name = "quarkus.http.auth.form.login-page") URI loginUri
    ) {
        this.cookieName = cookieName;
        this.loginUri = loginUri;
    }

    @GET
    @Authenticated
    public Response logout() {
        var expiredCookie = new NewCookie.Builder(cookieName)
            .value("")
            .path("/")
            .maxAge(0)
            .expiry(Date.from(Instant.EPOCH))
            .build();

        return Response.seeOther(loginUri).cookie(expiredCookie).build();
    }
}
