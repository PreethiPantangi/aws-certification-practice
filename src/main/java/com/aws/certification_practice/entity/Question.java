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
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "certification_id", referencedColumnName = "id", nullable = false)
    private Certification certification;

    @Column(name = "question", length = 500, nullable = false)
    private String question;

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

    @Convert(converter = BooleanToBitConverter.class)
    private Boolean isEnabled;

}
