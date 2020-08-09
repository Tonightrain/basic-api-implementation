package com.thoughtworks.rslist.entity;

import com.thoughtworks.rslist.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Builder
@Entity
@Data
@Table(name = "event")
@NoArgsConstructor
@AllArgsConstructor
public class RsEventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String eventName;
    private String keyWord;
    @Builder.Default
    private int voteNum = 0;
    private String userId;
    @ManyToOne
    //@JoinColumn(name = "userEntity")
    private UserEntity userEntity;

//    @OneToMany(mappedBy = "rsEventEntity")
//    private List<VoteEntity> voteEntities;
}
