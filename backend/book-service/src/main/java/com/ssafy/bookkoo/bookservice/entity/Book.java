package com.ssafy.bookkoo.bookservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    @Setter
    private Long id;

    @Column(name = "cover_img_url")
    private String coverImgUrl;

    @Column(name = "author")
    private String author;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "summary")
    private String summary;

    @Column(name = "title", nullable = false)
    private String title;

    @Builder
    public Book(String coverImgUrl, String author, String publisher, String summary, String title) {
        this.coverImgUrl = coverImgUrl;
        this.author = author;
        this.publisher = publisher;
        this.summary = summary;
        this.title = title;
    }
}
