package com.example.decathlon.api;

import com.example.decathlon.core.CompetitionService;
import com.example.decathlon.dto.ScoreReq;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiController {
    private final CompetitionService comp;

    public ApiController(CompetitionService comp) {
        this.comp = comp;
    }

    @PostMapping("/competitors")
    public ResponseEntity<?> add(@RequestBody Map<String, String> body) {
        String name = Optional.ofNullable(body.get("name")).orElse("").trim();
        if (name.isEmpty()) {
            return ResponseEntity.badRequest().body("Empty name");
        }
        try {
            String savedName = comp.addCompetitor(name);
            return ResponseEntity.status(201).body(Map.of("name", savedName));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(429).body(e.getMessage());
        }
    }

    @PostMapping("/score")
    public ResponseEntity<?> score(@RequestBody ScoreReq r) {
        if (r == null) {
            return ResponseEntity.badRequest().body("Invalid body");
        }

        String name = Optional.ofNullable(r.name()).orElse("").trim();
        String event = Optional.ofNullable(r.event()).orElse("").trim();
        double result = r.result();

        if (name.isEmpty()) {
            return ResponseEntity.badRequest().body("Empty name");
        }

        if (event.isEmpty()) {
            return ResponseEntity.badRequest().body("Empty event");
        }

        if (Double.isNaN(result) || Double.isInfinite(result)) {
            return ResponseEntity.badRequest().body("Invalid result");
        }

        try {
            String normalizedName = comp.normalizeName(name);
            int pts = comp.score(normalizedName, event, result);
            return ResponseEntity.ok(Map.of("name", normalizedName, "points", pts));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(429).body(e.getMessage());
        }
    }

    @GetMapping("/standings")
    public List<Map<String, Object>> standings() {
        return comp.standings();
    }

    @GetMapping(value = "/export.csv", produces = "text/csv")
    public ResponseEntity<String> export() {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=results.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(comp.exportCsv());
    }

    @PostMapping(value = "/import.csv", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> importCsv(@RequestBody String csv) {
        try {
            comp.importCsv(csv);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(429).body(e.getMessage());
        }
    }
}