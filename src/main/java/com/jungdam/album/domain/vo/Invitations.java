package com.jungdam.album.domain.vo;

import com.jungdam.invitation.domain.Invitation;
import com.jungdam.member.domain.Member;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Invitations {

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Invitation> invitations = new ArrayList<>();

    public void add(Invitation invitation) {
        invitations.add(invitation);
    }

    public boolean find(Member member) {
        return invitations.stream()
            .anyMatch(i -> i.isEquals(member));
    }
}