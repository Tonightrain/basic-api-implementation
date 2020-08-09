package com.thoughtworks.rslist.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Builder
@Entity
@Data
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    private String gender;
    private Integer age;

    private String email;
    private String phone;
    @Builder.Default
    private int voteNum=10;
    @OneToMany(cascade = CascadeType.REMOVE,mappedBy = "userId")
    private List<RsEventEntity> rsEventEntityList;
    @OneToMany(cascade = CascadeType.REMOVE,mappedBy = "userEntity")
    private List<VoteEntity> voteEntities;
}
