package com.example.webtoon.webtoon.domain.model;

import com.example.webtoon.global.domain.BaseEntity;
import com.example.webtoon.user.domain.model.User;
import com.example.webtoon.webtoon.domain.tyoe.Day;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Webtoon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User author;
    private String webtoonName;
    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "WEBTOON_ID")
    private Set<WebtoonHashtag> hashtags = new HashSet<>();
    @Enumerated(EnumType.STRING)
    private Day uploadDay;
    private Long viewCount;
    private Double starScore;

    public List<String> getHashtagsAsStringList() {
        return hashtags.stream().map(WebtoonHashtag::getName).collect(Collectors.toList());
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Webtoon webtoon = (Webtoon) o;
        return id.equals(webtoon.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
