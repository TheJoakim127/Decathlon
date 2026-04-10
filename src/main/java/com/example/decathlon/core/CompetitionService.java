package com.example.decathlon.core;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class CompetitionService {
    private final ScoringService scoring;

    public CompetitionService(ScoringService scoring) {
        this.scoring = scoring;
    }

    public static class Competitor {
        public final String name;
        public final Map<String, Integer> points = new ConcurrentHashMap<>();

        public Competitor(String name) {
            this.name = name;
        }

        public int total() {
            return points.values().stream().mapToInt(i -> i).sum();
        }
    }

    private static final List<String> ALL_EVENT_IDS = List.of(
            "100m",
            "longJump",
            "shotPut",
            "highJump",
            "400m",
            "110mHurdles",
            "discusThrow",
            "poleVault",
            "javelinThrow",
            "1500m",
            "hep100mHurdles",
            "hepHighJump",
            "hepShotPut",
            "hep200m",
            "hepLongJump",
            "hepJavelinThrow",
            "hep800m"
    );

    private final Map<String, Competitor> competitors = new LinkedHashMap<>();

    public synchronized String normalizeName(String name) {
        String trimmed = name == null ? "" : name.trim().replaceAll("\\s+", " ");
        String lower = trimmed.toLowerCase(Locale.ROOT);
        StringBuilder sb = new StringBuilder(lower.length());
        boolean upperNext = true;

        for (int i = 0; i < lower.length(); i++) {
            char ch = lower.charAt(i);
            if (upperNext && Character.isLetter(ch)) {
                sb.append(Character.toTitleCase(ch));
                upperNext = false;
            } else {
                sb.append(ch);
                upperNext = ch == ' ' || ch == '-' || ch == '\'';
            }
        }

        return sb.toString();
    }

    public synchronized String addCompetitor(String name) {
        String normalized = normalizeName(name);

        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("Empty name");
        }

        if (competitors.containsKey(normalized)) {
            return normalized;
        }

        if (competitors.size() >= 40) {
            throw new IllegalStateException("Too many competitors");
        }

        competitors.put(normalized, new Competitor(normalized));
        return normalized;
    }

    public synchronized int score(String name, String eventId, double result) {
        String normalized = normalizeName(name);
        Competitor c = competitors.get(normalized);

        if (c == null) {
            if (competitors.size() >= 40) {
                throw new IllegalStateException("Too many competitors");
            }
            c = new Competitor(normalized);
            competitors.put(normalized, c);
        }

        int pts = scoring.score(eventId, result);
        c.points.put(eventId, pts);
        return pts;
    }

    public synchronized List<Map<String, Object>> standings() {
        List<Competitor> sorted = competitors.values().stream()
                .sorted(Comparator.comparingInt(Competitor::total).reversed().thenComparing(c -> c.name))
                .collect(Collectors.toList());

        List<Map<String, Object>> list = new ArrayList<>();
        int rank = 0;
        int prevTotal = Integer.MIN_VALUE;

        for (int i = 0; i < sorted.size(); i++) {
            Competitor c = sorted.get(i);

            if (c.total() != prevTotal) {
                rank = i + 1;
                prevTotal = c.total();
            }

            Map<String, Object> m = new LinkedHashMap<>();
            m.put("rank", rank);
            m.put("name", c.name);
            m.put("scores", new LinkedHashMap<>(c.points));
            m.put("total", c.total());
            list.add(m);
        }

        return list;
    }

    public synchronized String exportCsv() {
        List<String> header = new ArrayList<>();
        header.add("Name");
        header.addAll(ALL_EVENT_IDS);
        header.add("Total");

        StringBuilder sb = new StringBuilder();
        sb.append(String.join(",", header)).append("\n");

        for (Competitor c : competitors.values().stream()
                .sorted(Comparator.comparingInt(Competitor::total).reversed().thenComparing(x -> x.name))
                .toList()) {
            List<String> row = new ArrayList<>();
            row.add(csv(c.name));
            for (String ev : ALL_EVENT_IDS) {
                Integer p = c.points.get(ev);
                row.add(p == null ? "" : String.valueOf(p));
            }
            row.add(String.valueOf(c.total()));
            sb.append(String.join(",", row)).append("\n");
        }

        return sb.toString();
    }

    public synchronized void importCsv(String csv) {
        if (csv == null || csv.isBlank()) {
            throw new IllegalArgumentException("Empty CSV");
        }

        String[] lines = csv.replace("\r", "").split("\n");
        if (lines.length == 0) {
            throw new IllegalArgumentException("Empty CSV");
        }

        List<String> header = parseCsvLine(lines[0]);
        int nameIndex = header.indexOf("Name");

        if (nameIndex < 0) {
            throw new IllegalArgumentException("Missing Name column");
        }

        Map<String, Competitor> imported = new LinkedHashMap<>();

        for (int i = 1; i < lines.length; i++) {
            if (lines[i].isBlank()) {
                continue;
            }

            List<String> cells = parseCsvLine(lines[i]);
            String rawName = getCell(cells, nameIndex);
            String normalizedName = normalizeName(rawName);

            if (normalizedName.isEmpty()) {
                continue;
            }

            Competitor competitor = imported.computeIfAbsent(normalizedName, Competitor::new);

            for (String eventId : ALL_EVENT_IDS) {
                int idx = header.indexOf(eventId);
                if (idx < 0) {
                    continue;
                }

                String value = getCell(cells, idx).trim();
                if (value.isEmpty()) {
                    continue;
                }

                competitor.points.put(eventId, Integer.parseInt(value));
            }
        }

        if (imported.size() > 40) {
            throw new IllegalStateException("Too many competitors");
        }

        competitors.clear();
        competitors.putAll(imported);
    }

    private String getCell(List<String> cells, int index) {
        return index >= 0 && index < cells.size() ? cells.get(index) : "";
    }

    private String csv(String value) {
        String escaped = value.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }

    private List<String> parseCsvLine(String line) {
        List<String> out = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (ch == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (ch == ',' && !inQuotes) {
                out.add(current.toString());
                current.setLength(0);
            } else {
                current.append(ch);
            }
        }

        out.add(current.toString());
        return out;
    }
}