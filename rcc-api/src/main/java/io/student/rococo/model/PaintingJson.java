package io.student.rococo.model;

import java.util.UUID;

public record PaintingJson(
    UUID id, String title, String description, String content, ArtistJson artist, MuseumJson museum
) {

}
