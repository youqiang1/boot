package com.yq.kafka.config.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * <p> result</p>
 *
 * @author youq  2020/5/19 下午9:58
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JsonResultRequest implements Serializable {

    private String createTime;

    private String type;

    private String deviceId;

    private String wifiId;

    private String mac;

    private String brand;

    private String signalIntensity;

    private String company;

}