package com.shoesshop.backend.controller;

import com.shoesshop.backend.entity.Promote;
import com.shoesshop.backend.service.PromoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/promote")
public class PromoteController {
    private final PromoteService promoteService;

    @PostMapping
    public ResponseEntity<Promote> addPromote(@RequestBody Promote promote) {
        return new ResponseEntity<>(promoteService.save(promote), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllPromote() {
        return ResponseEntity.ok(promoteService.getAllPromote());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Promote> getPromote(@PathVariable int id) {
        return ResponseEntity.ok(promoteService.getPromote(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Promote> updatePromote(@PathVariable int id, @RequestBody Promote promote) {
        return ResponseEntity.ok((promoteService.updatePromote(id, promote)));
    }

    public ResponseEntity<Void> deletePromote(@PathVariable int id) {
        promoteService.deletePromote(id);
        return ResponseEntity.noContent().build();
    }
}
