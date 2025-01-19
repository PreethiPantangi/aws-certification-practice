package com.aws.certification_practice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "options")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String optionValue;

    @CreationTimestamp
    private LocalDateTime addedAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "added_by", referencedColumnName = "id", nullable = false)
    private User addedBy;

    @ManyToOne
    @JoinColumn(name = "updated_by", referencedColumnName = "id", nullable = false)
    private User updatedBy;

}
