package com.sxy.snote.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;
    private String name;
    private String link;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "noteId")
    private Note note;

}
