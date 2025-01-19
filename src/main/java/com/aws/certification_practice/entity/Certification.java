package com.aws.certification_practice.entity;

import com.aws.certification_practice.converter.BooleanToBitConverter;
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
@Table(name = "certifications")
public class Certification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String code;

    @Convert(converter = BooleanToBitConverter.class)
    private Boolean isEnabled;

    @ManyToOne
    @JoinColumn(name="added_by", referencedColumnName = "id", nullable = false)
    private User addedBy;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name="updated_by", referencedColumnName = "id", nullable = false)
    private User updatedBy;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
