package io.student.rococo.model;

import java.util.UUID;

public record MuseumJson(UUID id, String title, String description, String photo, GeoJson geo) {
}
