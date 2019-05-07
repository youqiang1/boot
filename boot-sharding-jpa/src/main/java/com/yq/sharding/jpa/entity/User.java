package com.yq.sharding.jpa.entity;

import com.yq.kernel.enu.SexEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * <p> user实体</p>
 * @author youq  2019/4/29 9:16
 */
@Data
@Entity
@Table(name = "t_user")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private Long id;

    private LocalDateTime createTime;

    private LocalDateTime lastModified;

    private String name;

    private String phone;

    private String email;

    private String password;

    private Integer cityId;

    private SexEnum sex;

}
