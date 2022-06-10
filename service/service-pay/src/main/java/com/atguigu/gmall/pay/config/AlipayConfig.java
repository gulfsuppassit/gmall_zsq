package com.atguigu.gmall.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 */
@Data
@Component
@ConfigurationProperties(prefix = "app.pay.alipay")
public class AlipayConfig {

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	private String appId;
	
	// 商户私钥，您的PKCS8格式RSA2私钥
	private String merchantPrivateKey;

	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
	private String alipayPublicKey;

	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	private String notifyUrl;

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	private String returnUrl;

	// 签名方式
	private String signType;
	
	// 字符编码格式
	private String charset;
	
	// 支付宝网关
	private String gatewayUrl;



}

