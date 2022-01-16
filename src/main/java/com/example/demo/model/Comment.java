package com.example.demo.model;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Order order;

    private String opinion;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment = (Comment) o;
        return Objects.equals(getOrder(), comment.getOrder()) &&
                Objects.equals(getOpinion(), comment.getOpinion());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrder(), getOpinion());
    }
}
