package com.example.ecommerce.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;

public final class CsvExporter {
    private CsvExporter() {}

    public static <T> void export(Path file, List<String> headers, List<T> rows, Function<T, List<String>> mapper) throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            w.write(toCsvLine(headers));
            w.newLine();
            for (T row : rows) {
                w.write(toCsvLine(mapper.apply(row)));
                w.newLine();
            }
        }
    }

    private static String toCsvLine(List<String> cols) {
        return cols.stream().map(CsvExporter::escape).reduce((a, b) -> a + "," + b).orElse("");
    }

    private static String escape(String raw) {
        if (raw == null) return "";
        boolean mustQuote = raw.contains(",") || raw.contains("\"") || raw.contains("\n") || raw.contains("\r");
        String s = raw.replace("\"", "\"\"");
        return mustQuote ? "\"" + s + "\"" : s;
    }
}

