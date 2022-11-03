package com.stargazerstudios.existence.eligius.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor
public class FileResponseDTO {
    private String filename;
    private String extension;
    private long size;
    private boolean saved;
}
