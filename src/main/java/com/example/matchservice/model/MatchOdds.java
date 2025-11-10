package com.example.matchservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"match_id", "specifier"})
        }
)
@Data
public class MatchOdds {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String specifier;

    @NotNull
    @Positive
    private Double odd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    @NotNull
    @JsonBackReference // prevent serializing parent inside child
    private Match match;

    // Use this getter to return match_id
    @JsonProperty("match_id")
    public Long getMatchId() {
        return match != null ? match.getId() : null;
    }
}
