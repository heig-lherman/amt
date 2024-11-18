package ch.heigvd.amt.jpa.resource;

import ch.heigvd.amt.jpa.entity.Customer;
import ch.heigvd.amt.jpa.entity.Inventory;
import ch.heigvd.amt.jpa.service.RentalService;
import ch.heigvd.amt.jpa.service.RentalService.CustomerDTO;
import ch.heigvd.amt.jpa.service.RentalService.FilmInventoryDTO;
import ch.heigvd.amt.jpa.service.RentalService.RentalDTO;
import ch.heigvd.amt.jpa.service.StaffService;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;
import io.smallrye.common.annotation.Blocking;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import java.util.Collections;
import java.util.List;
import org.jboss.resteasy.reactive.RestForm;

// The existing annotations on this class must not be changed (i.e. new ones are allowed)
@Path("rental")
@Authenticated
public class RentalResource {

    private final RentalService rentalService;
    private final StaffService staffService;

    public RentalResource(
        RentalService rentalService,
        StaffService staffService
    ) {
        this.rentalService = rentalService;
        this.staffService = staffService;
    }

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance rental(String username);

        public static native TemplateInstance rental$success(RentalDTO rental);

        public static native TemplateInstance rental$failure(String message);

        public static native TemplateInstance searchFilmsResults(List<FilmInventoryDTO> films);

        public static native TemplateInstance searchFilmsSelect(FilmInventoryDTO film);

        public static native TemplateInstance searchCustomersResults(List<CustomerDTO> customers);

        public static native TemplateInstance searchCustomersSelect(CustomerDTO customer);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance rental(@Context SecurityContext securityContext) {
        return Templates.rental(securityContext.getUserPrincipal().getName());
    }

    @POST
    @Produces(MediaType.TEXT_HTML)
    @Blocking
    public TemplateInstance registerRental(@Context SecurityContext securityContext,
                                           @RestForm Integer inventory, @RestForm Integer customer) {
        if (inventory == null || customer == null) {
            return Templates.rental$failure("The submission is not valid, missing inventory or customer");
        }

        return rentalService
            .rentFilm(
                new Inventory().setId(inventory),
                new Customer().setId(customer),
                staffService.findStaffByUsername(securityContext.getUserPrincipal().getName())
            )
            .map(Templates::rental$success)
            .orElseGet(() -> Templates.rental$failure("The selected item is not available."));
    }

    @GET
    @Path("/film/{inventory}")
    @Produces(MediaType.TEXT_HTML)
    @Blocking
    public TemplateInstance selectFilmsGet(Integer inventory) {
        return Templates.searchFilmsSelect(
            rentalService.searchFilmInventory(inventory)
        );
    }

    @POST
    @Path("/film/search")
    @Produces(MediaType.TEXT_HTML)
    @Blocking
    public TemplateInstance searchFilmsPost(@Context SecurityContext securityContext, @RestForm String query) {
        if (query == null || query.trim().isEmpty()) {
            return Templates.searchFilmsResults(Collections.emptyList());
        }

        return Templates.searchFilmsResults(
            rentalService.searchFilmInventory(
                query,
                staffService.findStaffByUsername(
                    securityContext.getUserPrincipal().getName()
                ).getStore()
            )
        );
    }

    @POST
    @Path("/customer/search")
    @Produces(MediaType.TEXT_HTML)
    @Blocking
    public TemplateInstance searchCustomersPost(@Context SecurityContext securityContext, @RestForm String query) {
        if (query == null || query.trim().isEmpty()) {
            return Templates.searchCustomersResults(Collections.emptyList());
        }

        return Templates.searchCustomersResults(
            rentalService.searchCustomer(
                query,
                staffService.findStaffByUsername(
                    securityContext.getUserPrincipal().getName()
                ).getStore()
            )
        );
    }

    @GET
    @Path("/customer/{customer}")
    @Produces(MediaType.TEXT_HTML)
    @Blocking
    public TemplateInstance selectCustomerGet(Integer customer) {
        return Templates.searchCustomersSelect(
            rentalService.searchCustomer(customer)
        );
    }
}
