import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

public class HandleReedemProduct {

    private static final URI ENDPOINT = URI.create("http://localhost:8080/api/reedem");

    record ReedemRequest(String email, String product){}

    public static HttpRequest reedem(ReedemRequest request){
        final var json = """
            {
                "customer_email": "%s",
                "product_code": "%s"
            }
        """.formatted(request.email(), request.product());

        return HttpRequest
                .newBuilder()
                .uri(ENDPOINT)
                .headers("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(json))
                .build();
    }

    public static void main(String[] args) {
        try (final var executors = Executors.newVirtualThreadPerTaskExecutor(); final var client = HttpClient.newHttpClient()){
            final var customers = List.of(
                    new ReedemRequest("customer_133@email.com", "free-ebook"),
                    new ReedemRequest("customer_815@email.com", "free-ebook"),
                    new ReedemRequest("customer_545@email.com", "free-ebook"),
                    new ReedemRequest("customer_689@email.com", "free-ebook"),
                    new ReedemRequest("customer_481@email.com", "free-ebook")
            );

            final var tasks = customers
                    .stream()
                    .<Callable<HttpResponse<String>>>map(customer -> {
                        final var request = reedem(customer);
                        return () -> client.send(request, BodyHandlers.ofString());
                    })
                    .toList();

            final var futures = executors.invokeAll(tasks);

            for (var future : futures){
                final var response = future.get();
                final var status = response.statusCode();
                final var body = response.body();

                System.out.println("<---------------->");
                System.out.println("Status: " + status);
                System.out.println(body);
                System.out.println("<---------------->");
            }
        } catch (Exception e){
            System.out.println("Failed to Reedem");
            System.out.println(e.getMessage());
        }
    }
}
