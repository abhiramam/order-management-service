package org.scaler.ordermanagementservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("BOOKMANAGEMENTSERVICE")
public interface FeignInterface {

    @GetMapping(path = "/{bookId}/")
    public ResponseEntity<Object> getBookById(@PathVariable Long bookId);
    @GetMapping(path = "/")
    public ResponseEntity<Object> getAllBooks();
}
