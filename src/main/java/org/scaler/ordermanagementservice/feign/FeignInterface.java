package org.scaler.ordermanagementservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
// Unused imports PostMapping and RequestBody, can be removed if getAllBooks is the only other method.
// For now, keeping them as they might be used by getAllBooks or future methods.
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "${feign.client.bookmanagementservice.name}")
public interface FeignInterface {

    String BOOK_SERVICE_CIRCUIT_BREAKER = "bookServiceCircuitBreaker";

    @GetMapping(path = "/{bookId}/")
    @CircuitBreaker(name = BOOK_SERVICE_CIRCUIT_BREAKER, fallbackMethod = "getBookByIdFallback")
    ResponseEntity<Object> getBookById(@PathVariable Long bookId);

    @GetMapping(path = "/")
    ResponseEntity<Object> getAllBooks();
    default ResponseEntity<Object> getBookByIdFallback(Long bookId, Throwable t) {
        System.err.println("Fallback for getBookById: bookId " + bookId + ", error: " + t.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Book service is currently unavailable. Fallback response for bookId: " + bookId);
    }
}
