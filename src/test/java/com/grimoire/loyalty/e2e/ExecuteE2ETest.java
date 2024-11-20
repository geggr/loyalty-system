package com.grimoire.loyalty.e2e;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Gatherers;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ExecuteE2ETest {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private static HttpRequest createProduct(String code, String name, Integer price, boolean limited){
        final var json = """
            {
                "code": "%s",
                "name": "%s",
                "price": %s,
                "limited": %s
            }
        """.formatted(code, name, price, limited);

        return HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/api/products"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
    }

    private static HttpRequest createStockForProduct(String code, int total){
        final var json = """
            {
                "total": %s,
                "initial_available": true
            }
        """.formatted(total);

        return HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/api/products/" + code + "/stock"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
    }

    private static HttpRequest createCustomerWithPoints(String customerName, Long amount){
        final var json = """
            {
                "customer": "%s",
                "amount": %s,
                "date": "%s"
            }
        """.formatted(customerName, amount, LocalDateTime.now().toString());

        return HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/api/purchase/add"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
    }

    private static HttpRequest claimProductForCustomer(String email, String productCode){
        final var json = """
            {
                "customer_email": "%s",
                "product_code": "%s"
            }
        """.formatted(email, productCode);

        return HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/api/reedem"))
                .headers("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(json))
                .build();
    }

    private static <T, R> R send(HttpClient client, HttpRequest request, Class<T> target, Function<T, R> mapper){
        try {
            final var response = client.send(request, BodyHandlers.ofString());
            final var parsed = MAPPER.readValue(response.body(), target);
            return mapper.apply(parsed);
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    public static class ReedemResponse {
        private String sku;
        
        public ReedemResponse(){}

        public String getSku() {
            return sku;
        }

        public boolean hasSku(){
            return sku != null;
        }

        @Override
        public String toString() {
            return "Reedem Response <%s>".formatted(sku);
        }
    }

    public static class E2ETestResult {
        private static String MESSAGE_TEMPLATE = """
            Teste E2E Finalizado!.
            Timestamp: %s
            ---
            Sucesso: %s,
            Erros: %s,
            ---
            Finalizado com sucesso ? %s
        """;

        private final int expectedSuccess;
        private int success = 0;
        private int errors  = 0;

        public E2ETestResult(int expectedSuccess){
            this.expectedSuccess = expectedSuccess;
        }

        public void addSuccess(){
            this.success += 1;
        }

        public void addError(){
            this.errors += 1;
        }

        public void log(){
            System.out.println(
                MESSAGE_TEMPLATE.formatted(
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                    success,
                    errors,
                    success == expectedSuccess
                )
            );
        }
    }

    @SuppressWarnings("preview")
    public static void main(String[] args) {
        try (
            final var executors = Executors.newVirtualThreadPerTaskExecutor();
            final var client = HttpClient.newHttpClient()){

            final var random = new Random();

            final var voucher = send(
                client,
                createProduct("voucher", "Voucher de 50 reais", 50, false),
                HashMap.class,
                (it) -> (String) it.get("code")
            );

            final var mouse = send(
                client,
                createProduct("mousepad", "Mousepad Grimoire", 80, true),
                HashMap.class,
                (it) -> (String) it.get("code")
            );

            send(
                client,
                createStockForProduct(mouse, 50),
                HashMap.class,
                Function.identity()
            );

            final var customers = IntStream
                .range(0, 100)
                .<String>mapToObj(_ -> "customer_%s@email.com".formatted(random.nextInt(1000)))
                .toList();

            final var customerTasks = customers
                .stream()
                .<Callable<HashMap<?, ?>>>map((it) -> {
                    final var request = createCustomerWithPoints(it, 500_000L);
                    return () -> send(client, request, HashMap.class, Function.identity());
                })
                .toList();

            executors.invokeAll(customerTasks);
            
            Thread.sleep(Duration.ofSeconds(10L));

            final var reedemVoucherTask = customers.stream().<Callable<ReedemResponse>>map(
                (it) -> {
                    final var request = claimProductForCustomer(it, voucher);
                    return () -> send(client, request, ReedemResponse.class, Function.identity());
                }
            )
            .toList();

            final var reedemMouseTask = customers.stream().<Callable<ReedemResponse>>map(
                (it) -> {
                    final var request = claimProductForCustomer(it, mouse);
                    return () -> send(client, request, ReedemResponse.class, Function.identity());
                }
            )
            .toList();

            final var reedemVoucher = executors.invokeAll(reedemVoucherTask);
            final var reedemMouse = executors.invokeAll(reedemMouseTask);

			final var completed = Stream.concat(
                reedemVoucher.stream(),
                reedemMouse.stream()    
            )
            .gather(
                Gatherers.fold(
                    () -> new E2ETestResult(50),
                    (result, response) -> {
                        try {
                            final var reedem = response.get();
                            if (reedem.hasSku()){
                                result.addSuccess();
                            }
                            else {
                                result.addError();
                            }
                        }
                        catch (Exception e){
                            // ignore
                        }

                        return result;
                    }
                )
            )
            .findFirst()
            .get();

            completed.log();
        }
        catch (Exception e){
            System.out.println("Failed to executed E2E, reason was: ");
            System.out.println(e.getMessage());
        }
    }
}
