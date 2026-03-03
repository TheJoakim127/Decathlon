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

    public ApiController(CompetitionService comp) { this.comp = comp; }

    @PostMapping("/competitors")
    public ResponseEntity<?> add(@RequestBody Map<String,String> body) {
        String name = Optional.ofNullable(body.get("name")).orElse("").trim();
        if (name.isEmpty()) return ResponseEntity.badRequest().body("Empty name");
        if (getCount() >= 40) return ResponseEntity.status(429).body("Too many competitors");

        comp.addCompetitor(name);
        return ResponseEntity.status(201).build();
    }

    private int getCount() {
        return comp.standings().size();
    }

    @PostMapping("/score")
    public ResponseEntity<?> score(@RequestBody ScoreReq r) {
        if (r == null) return ResponseEntity.badRequest().body("Invalid body");
        String name = Optional.ofNullable(r.name()).orElse("").trim();
        String event = Optional.ofNullable(r.event()).orElse("").trim();
        double raw = r.raw();
        if (name.isEmpty()) return ResponseEntity.badRequest().body("Empty name");
        if (event.isEmpty()) return ResponseEntity.badRequest().body("Empty event");
        if (Double.isNaN(raw) || Double.isInfinite(raw)) return ResponseEntity.badRequest().body("Invalid result");
        int pts = comp.score(r.name(), r.event(), r.raw());
        return ResponseEntity.ok(Map.of("points", pts));
    }

    @GetMapping("/standings")
    public List<Map<String,Object>> standings() { return comp.standings(); }

    @GetMapping(value="/export.csv", produces = "text/csv")
    public ResponseEntity<String> export() {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=results.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(comp.exportCsv());
    }
}