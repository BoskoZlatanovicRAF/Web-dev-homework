package rs.raf.demo;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import rs.raf.demo.filters.AuthFilter;
import rs.raf.demo.repositories.post.PostRepository;
import rs.raf.demo.repositories.post.Repository;
import rs.raf.demo.repositories.user.UserInterface;
import rs.raf.demo.repositories.user.UserRepository;
import rs.raf.demo.services.PostService;
import rs.raf.demo.services.UserService;

import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api")
public class HelloApplication extends ResourceConfig {

    public HelloApplication() {
        // Ukljucujemo validaciju, citaju se anotacije
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);

        // Definisemo implementacije u dependency container-u
        AbstractBinder binder =  new AbstractBinder() {
            @Override
            protected void configure() {
                this.bind(PostRepository.class).to(Repository.class).in(Singleton.class);
                this.bind(UserRepository.class).to(UserInterface.class).in(Singleton.class);

                this.bindAsContract(PostService.class);
                this.bindAsContract(UserService.class);
            }
        };
        register(binder);

        // Ucitavamo resurse
        packages("rs.raf.demo.resources");

        register(AuthFilter.class);
    }
}
