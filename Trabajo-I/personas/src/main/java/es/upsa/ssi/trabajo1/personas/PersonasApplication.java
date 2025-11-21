package es.upsa.ssi.trabajo1.personas;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;


@OpenAPIDefinition(
        info = @Info(title = "OpenAPI para Personas",
                version = "1.0.0",
                contact = @Contact(url = "https://drupal.upsa.es/sites/default/files/guiaDocente2022_INFORMATICA_4_1_SistemasDeInformacion.pdf",
                        name = "Sistemas de información",
                        email = "apisonerolo.inf@upsa.es"
                ),
                description = "API de Personas",
                license = @License(name = "Apache 2.0",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"
                )
        ),
        tags = {@Tag(name = "personas",
                description = "Gestión de Personas"
        )
        }
)
@ApplicationPath("/")
public class PersonasApplication extends Application {
}
