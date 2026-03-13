package com.trade.member.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_key")
    private Long memberKey;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "password")
    private String password;

    @Column(name = "user_nm")
    private String userNm;

    @Column(name = "status_cd")
    private String statusCd;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    protected Member() {}

    public Long getMemberKey() { return memberKey; }
    public String getUserId() { return userId; }
    public String getUserNm() { return userNm; }
    public String getStatusCd() { return statusCd; }
}
