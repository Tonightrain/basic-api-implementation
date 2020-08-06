package com.thoughtworks.rslist.domain;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class RsEvent {
    @NotNull
    private String eventName;
    @NotNull
    private String keyWord;
    @Valid
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RsEvent(String eventName, String keyWord, User user){
        this.eventName = eventName;
        this.keyWord = keyWord;
        this.user = user;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}
