package com.jungdam.participant.domain.vo;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Nickname {

    @Column(name = "participant_nickname")
    private String nickname;

    protected Nickname() {
    }

    public Nickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public boolean isEquals(String nickname) {
        return this.nickname.equals(nickname);
    }
}