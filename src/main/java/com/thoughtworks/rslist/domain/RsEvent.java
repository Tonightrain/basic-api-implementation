package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RsEvent {
    @NotNull
    private String eventName;
    @NotNull
    private String keyWord;
    @Valid
    private String userId;

    //@JsonIgnore
    public String getUserId() {
        return userId;
    }

    @JsonProperty
    public void setUserId(String userId) {
        this.userId = userId;
    }

    private int voteNum;
}
