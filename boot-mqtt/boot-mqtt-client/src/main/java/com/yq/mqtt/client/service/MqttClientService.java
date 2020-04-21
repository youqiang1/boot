package com.yq.mqtt.client.service;

import com.yq.mqtt.client.util.HexUtils;
import com.yq.mqtt.client.util.SslUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Paths;

/**
 * <p> mqtt客户端配置</p>
 * @author youq  2020/4/21 10:18
 */
@Slf4j
@Component
public class MqttClientService {

    @Value("${mqtt.server.url}")
    private String hostAddress;

    @Value("${mqtt.server.key}")
    private String keyPassword;

    @Value("${mqtt.client.username}")
    private String username;

    @Value("${mqtt.client.password}")
    private String password;

    @Value("${mqtt.client.id}")
    private String clientId;

    @Value("${mqtt.client.maxInflight}")
    private Integer maxInflight;

    @Value("${mqtt.client.clearSession}")
    private Boolean clearSession;

    @Value("${mqtt.client.caCertFilePath}")
    private String caCertFilePath;

    @Value("${mqtt.client.certFilePath}")
    private String certFilePath;

    @Value("${mqtt.client.KeyFilePath}")
    private String KeyFilePath;

    private MqttClient client;

    @Value("${mqtt.client.defaultTopic}")
    private String destTopic;

    @PostConstruct
    public void init() {
        connect();
    }

    /**
     * <p> 发送消息</p>
     * @param topic   topic name
     * @param payload 消息体
     * @author youq  2020/4/21 11:36
     */
    public void sendMsg(String topic, byte[] payload) throws Exception {
        if (StringUtils.isBlank(topic)) {
            topic = destTopic;
        }
        //mqttTopic
        MqttTopic mqttTopic = client.getTopic(topic);
        //mqttMessage
        final int qos = 1;
        if (payload == null) {
            payload = createPayload();
        }
        MqttMessage message = new MqttMessage();
        message.setQos(qos);
        message.setPayload(payload);
        //publish
        MqttDeliveryToken publish = mqttTopic.publish(message);
        log.info("send a message: {}", HexUtils.bytesToHex(payload));
        log.info("publish: {}", publish);
    }

    /**
     * <p> 连接</p>
     * @author youq  2020/4/21 11:34
     */
    private void connect() {
        //获取ssl相关文件
        String caCertFile = getResourcePath(caCertFilePath);
        String certFile = getResourcePath(certFilePath);
        String privateKeyFile = getResourcePath(KeyFilePath);
        try {
            client = new MqttClient(hostAddress, clientId, new MemoryPersistence());
            // 连接服务器
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(clearSession);
            options.setUserName(username);
            options.setPassword(password.toCharArray());
            options.setAutomaticReconnect(true);
            options.setMaxInflight(maxInflight);
            try {
                options.setSocketFactory(SslUtil.createSocketFactory(caCertFile, certFile, privateKeyFile, keyPassword));
            } catch (Exception e) {
                log.error("init method createSocketFactory exception: ", e);
            }
            //连接
            boolean connected = false;
            while (!connected) {
                try {
                    client.setCallback(new PushCallback());
                    client.connect(options);
                    connected = true;
                    log.info("clientId: {}, 连接成功", client.getClientId());
                } catch (Exception e) {
                    log.error("mqtt client连接异常：", e);
                }
            }
            // 等待连接成功
            while (!client.isConnected()) {
                log.info("等待连接：{}", client.getClientId());
                try {
                    Thread.sleep(3 * 1000);
                } catch (InterruptedException ignored) {
                }
            }
        } catch (Exception e) {
            log.error("init mqtt client exception: ", e);
        }
    }

    /**
     * <p> 断开client连接</p>
     * @author youq  2020/4/21 11:12
     */
    public void disconnect() {
        try {
            client.disconnect();
            client.close(true);
            log.info("断开client【{}】连接", client.getClientId());
        } catch (MqttException e) {
            log.error("断开client【{}】连接异常：", client.getClientId(), e);
        }
    }

    /**
     * <p> 获取资源路径</p>
     * @param resourceName 资源名称
     * @author youq  2020/4/21 11:03
     */
    private String getResourcePath(String resourceName) {
        URL resource = getClass().getResource(resourceName);
        try {
            return Paths.get(resource.toURI()).toAbsolutePath().toString();
        } catch (URISyntaxException e) {
            log.error("获取资源路径异常：", e);
            return "";
        }
    }

    private byte[] createPayload() throws UnsupportedEncodingException {
        byte[] messageBody = new byte[71];
        ByteBuffer buffer = ByteBuffer.wrap(messageBody);

        // 写入消息类型
        byte type = 0x1;
        buffer.put(type); // 1 byte

        // 写入时间
        buffer.putInt((int) System.currentTimeMillis()); // 5 bytes

        byte dbm = 0x23;
        buffer.put(dbm); // 6 bytes

        String sa = RandomStringUtils.randomAscii(6);
        buffer.put(sa.getBytes("utf-8")); // 12 bytes

        String netCode = RandomStringUtils.randomAlphabetic(15);
        buffer.put(netCode.getBytes("utf-8")); // 27 bytes

        String deviceId = RandomStringUtils.randomAlphabetic(22);
        buffer.put(deviceId.getBytes("utf-8")); // 49 bytes

        String lng = "116.307629";
        String lat = "40.058359";

        byte[] byteLng = lng.getBytes("utf-8");
        buffer.position(buffer.position() + 11 - byteLng.length);
        buffer.put(byteLng); // + 11 bytes = 60 bytes

        byte[] byteLat = lat.getBytes("utf-8");
        buffer.position(buffer.position() + 11 - byteLat.length);
        buffer.put(byteLat); // + 11 bytes = 71 bytes

        return messageBody;
    }

}
