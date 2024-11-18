import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class HandleAddPurchase {
    private static final URI ENDPOINT = URI.create("http://localhost:8080/api/purchase/add");

    private static HttpRequest createCustomerWithPoints(String customerName, Long amount){
        final var json = """
            {
                "customer": "%s@email.com",
                "amount": %s,
                "date": "%s"
            }        
        """.formatted(customerName, amount, LocalDateTime.now().toString());

        return HttpRequest
            .newBuilder()
            .uri(ENDPOINT)
            .header("Content-Type", "application/json")
            .POST(BodyPublishers.ofString(json))
            .build();
    }

    public static void main(String[] args) {
        try (final var executors = Executors.newVirtualThreadPerTaskExecutor(); final var client = HttpClient.newHttpClient()){
            final var random = new Random();

            final var tasks = IntStream
                .rangeClosed(1, 5)
                .<Callable<HttpResponse<String>>>mapToObj(_ -> {
                    final var customer = "customer_" + random.nextInt(1000);
                    final var request = createCustomerWithPoints(customer, 100_000L);

                    return () -> client.send(request, BodyHandlers.ofString());
                })
                .toList();

            final var futures = executors.invokeAll(tasks);

            for (var future : futures){
                final var response = future.get();
                final var status = response.statusCode();
                final var body = response.body();

                System.out.println("Status: " + status);
                System.out.println(body);

            }
        } catch (Exception e){
            System.out.println("Failed to Reedem");
            System.out.println(e.getMessage());
        }
    }
}
