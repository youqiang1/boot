package com.yq.jpa.db;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * <p> 会员表</p>
 * @author yq  2018/5/19 14:16
 */
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "fn_member")
@Where(clause = "remove=false")
@SQLDelete(sql = "update fn_member set remove=true,last_modified=now() where id=?")
public class FnMember extends Base{
    private String email;
    /**
     * 会员编号，这名字，真醉了
     */
    private String username;
    /**
     * 会议单位名称
     */
    private String name;
    private String phone;
}
