import org.springframework.boot.SpringApplication;
import simpleshopapi.Application;
import simpleshopapi.repository.TestcontainerConfiguration;

public class ApplicationWithContainer {

    public static void main(String[] args) {
        SpringApplication.from(Application::main)
            .with(TestcontainerConfiguration.class)
            .run(args);
    }
}
