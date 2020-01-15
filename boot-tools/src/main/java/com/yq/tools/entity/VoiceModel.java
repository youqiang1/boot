package com.yq.tools.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoiceModel {

    /**
     * varchar(24),   主叫
     */
    private String caller;
    /**
     * varchar(24),   被叫
     */
    private String called;
    /**
     * varchar(24),   原始主叫
     */
    private String originalCaller;
    /**
     * varchar(24),   原始被叫
     */
    private String originalCalled;
    /**
     * DATETIME,      呼入时间
     */
    private String intoLineTime;
    /**
     * DATETIME,      呼出时间
     */
    private String makeCallTime;
    /**
     * DATETIME,      振铃时间
     */
    private String ringTime;
    /**
     * DATETIME,      应答时间
     */
    private String connectTime;
    /**
     * DATETIME,      拆线时间
     */
    private String releaseTime;
    /**
     * Integer,           通话时长
     */
    private Integer talkDuration;
    /**
     * Integer,           占线时长
     */
    private Integer connectDuration;
    /**
     * varchar(64),   设备名
     */
    private String deviceName;
    /**
     * Integer,           路由号
     */
    private Integer routeNo;
    /**
     * Integer,           呼入中继组号
     */
    private Integer groupNoIn;
    /**
     * varchar(64),   呼入中继组名
     */
    private String groupNameIn;
    /**
     * Integer,           呼出中继组号
     */
    private Integer groupNoOut;
    /**
     * varchar(64),   呼出中继组名
     */
    private String groupNameOut;
    /**
     * varchar(32),   呼入通道
     */
    private String channelIn;
    /**
     * varchar(16),   呼入编码
     */
    private String encodingNameIn;
    /**
     * varchar(32),   呼出通道
     */
    private String channelOut;
    /**
     * varchar(16),   呼出编码
     */
    private String encodingNameOut;
    /**
     * varchar(64)	  呼入媒体信息
     */
    private String mediaInfoIn;
    /**
     * varchar(64)	  呼出媒体信息
     */
    private String mediaInfoOut;
    /**
     * varchar(32),   主叫IP
     */
    private String callerIP;
    /**
     * varchar(32),   被叫IP
     */
    private String calledIP;
    /**
     * varchar(256),  呼叫唯一标识
     */

    /**
     * varchar(24)    先挂机方
     */
    private String onhookDirection;
    /**
     * varchar(64)     被叫地区
     */
    private String calledArea;


    /**
     * 版本号
     */
    private String version;

    /**
     * 节点名称
     */
    private String node;

    /**
     * 原始主叫 IP
     */
    private String via;

    /**
     * 发送的序列号码
     */
    private String serialNumber;

    /**
     * 发送的序列号码
     */
    private Long serialNumberLong;

    /**
     * 号码是真是假
     */
    private Integer phoneCheck;

    /**
     * 计费类型
     * 0.默认计费规则
     * 1.能力开发平台计费
     */
    private String billType;

    /**
     * 计费类型id
     */
    private String bill_id;

}
