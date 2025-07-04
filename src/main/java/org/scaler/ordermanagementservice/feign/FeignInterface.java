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
    // Consider adding @CircuitBreaker here as well if this endpoint is critical
    ResponseEntity<Object> getAllBooks();

    // Fallback method for getBookById
    // It needs to have the same method signature as the original method, plus a Throwable parameter
    default ResponseEntity<Object> getBookByIdFallback(Long bookId, Throwable t) {
        // Log the error or the fact that fallback is being used
        // Using a simple logger here; ensure Slf4j is available or use System.out for basic logging
        // For proper logging, an instance logger would be better, but static context requires care.
        // Consider passing a logger or using a static logger if available.
        System.err.println("Fallback for getBookById: bookId " + bookId + ", error: " + t.getMessage());
        // Return a default response or an error response
        // This could be a custom object indicating fallback, or just an appropriate HTTP status
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Book service is currently unavailable. Fallback response for bookId: " + bookId);
    }
}
