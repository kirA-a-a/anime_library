package io.student.rococo.model;

import java.util.Date;

public record SessionJson(String username, Date issuedAt, Date expiresAt) {


}
