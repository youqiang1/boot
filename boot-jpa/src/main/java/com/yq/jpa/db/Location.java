package com.yq.jpa.db;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigInteger;

@Setter
@Getter
@EqualsAndHashCode()
@ToString
@Entity
@Table(name = "LOCATION")
public class Location{

    @Id
    @GeneratedValue
    @Column(name = "ID", columnDefinition = "varchar(64) COMMENT '主键ID'")
    private String id;

    @Column(name = "LOCATION_AUDIT_TYPE", columnDefinition = "int(4)")
    private Integer locationAuditType;

    @Column(name = "LOCATION_CODE", columnDefinition = "varchar(32)")
    private String locationCode;

    @Column(name = "LOCATION_NAME", columnDefinition = "varchar(256)")
    private String locationName;

    @Column(name = "AREA_CODE", columnDefinition = "varchar(6)")
    private String areaCode;

    @Column(name = "LOCAITON_CITY", columnDefinition = "varchar(256)")
    private String locationCity;

    @Column(name = "LONGITUDE", columnDefinition = "varchar(20)")
    private String longitude;

    @Column(name = "LATITUDE", columnDefinition = "varchar(20)")
    private String latitude;

    @Column(name = "LOCATION_SERVER_TYPE", columnDefinition = "varchar(2)")
    private String locationServerType;

    @Column(name = "LOCATION_NATURE", columnDefinition = "varchar(6)")
    private String locationNature;

    @Column(name = "PRINCIPAL", columnDefinition = "varchar(128)")
    private String principal;

    @Column(name = "CERTIFICATE_TYPE", columnDefinition = "varchar(6)")
    private String certificateType;

    @Column(name = "CERTIFICATE_CODE", columnDefinition = "varchar(128)")
    private String certificateCode;

    @Column(name = "PHONE", columnDefinition = "varchar(128)")
    private String phone;

    @Column(name = "BUSINESS_START_TIME", columnDefinition = "varchar(8)")
    private String businessStartTime;

    @Column(name = "BUSINESS_END_TIME", columnDefinition = "varchar(8)")
    private String businessEndTime;

    @Column(name = "END_ONLINE_TIME", columnDefinition = "bigint(16)")
    private BigInteger endOnlineTime;

    @Column(name = "MANUFACTURER_CODE", columnDefinition = "varchar(9)")
    private String manufacturerCode;

    @Column(name = "SERVER_ONLINE_STATUS", columnDefinition = "int(2)")
    private Integer serverOnlineStatus;

    @Column(name = "DATA_ONLINE_STATUS", columnDefinition = "int(2)")
    private Integer dataOnlineStatus;

    @Column(name = "DEVICE_STATUS", columnDefinition = "int(2)")
    private Integer deviceStatus;

    @Column(name = "RESERVER1", columnDefinition = "varchar(50)")
    private String reserver1;

    @Column(name = "RESERVER2", columnDefinition = "varchar(50)")
    private String reserver2;

    @Column(name = "RESERVER3", columnDefinition = "varchar(50)")
    private String reserver3;

    @Column(name = "WAN_IP", columnDefinition = "varchar(32)")
    private String wanIp;

    @Column(name = "BELONG_POLICE", columnDefinition = "varchar(100)")
    private String belongPolice;

    @Column(name = "DATA_END_ONLINE", columnDefinition = "bigint(16)")
    private BigInteger dataEndOnline;

    @Column(name = "NAPMODE", columnDefinition = "varchar(155)")
    private String napmode;

    @Column(name = "NAPSERVICEPRO", columnDefinition = "varchar(155)")
    private String napservicepro;

    @Column(name = "NAPIP", columnDefinition = "varchar(155)")
    private String napIp;

    @Column(name = "BUSINESSSTATE", columnDefinition = "varchar(2)")
    private String businessState;

    @Column(name = "AUDIT_TYPE", columnDefinition = "int(2)")
    private Integer auditType;

    @Column(name = "RESERVER5", columnDefinition = "varchar(50)")
    private String reserver5;

    @Column(name = "RESERVER6", columnDefinition = "varchar(50)")
    private String reserver6;

    @Column(name = "RESERVER7", columnDefinition = "varchar(50)")
    private String reserver7;

    @Column(name = "CAMERA_RIFLE", columnDefinition = "varchar(256)")
    private String cameraRifle;

    @Column(name = "DATA_UP", columnDefinition = "varchar(2)")
    private String dataUp;

    @Column(name = "STATUS_UP", columnDefinition = "varchar(2)")
    private String statusUp;

    @Column(name = "ORIENTATION", columnDefinition = "int(3)")
    private Integer orientation;

    @Column(name = "COLLECT_SY", columnDefinition = "varchar(64)")
    private String collectSy;

    @Column(name = "LOCATION_PICTURE", columnDefinition = "varchar(256)")
    private String locationPicture;

    @Column(name = "NETWORK_TYPE", columnDefinition = "varchar(2)")
    private Integer networkType;

    @Column(name = "COLLECT_RADII", columnDefinition = "int(6)")
    private String collectRadii;

}
