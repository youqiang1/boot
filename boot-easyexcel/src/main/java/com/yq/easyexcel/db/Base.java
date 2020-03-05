package com.yq.easyexcel.db;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p> 数据库实体公用类</p>
 * @author yq  2019/4/2 14:29
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Base implements Serializable{

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "int(11) COMMENT '主键ID'")
    private Integer id;

    /**
     * 创建时间
     */
    @CreatedDate
    private LocalDateTime createTime;

    /**
     * 最后修改时间
     */
    @LastModifiedDate
    private LocalDateTime lastModified;

    /**
     * 是否删除，用于逻辑删除
     */
    private Boolean remove = false;

}