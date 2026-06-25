package io.github.pauliustechin.egzaminas.feature.ratings.model;


import io.github.pauliustechin.egzaminas.feature.recipe.model.Recipe;
import io.github.pauliustechin.egzaminas.feature.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(
        name = "ratings",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "recipe_id"})}
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;


    @Column(nullable = false)
    private Integer rating;


    @Column(length = 500)
    private String comment;


    private Instant createdAt;

    private Instant updatedAt;


    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
    }


    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }
}
