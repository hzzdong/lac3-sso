package com.linkallcloud.sso.pc.kiss;

import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.TypeReference;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.face.message.request.FaceRequest;
import com.linkallcloud.core.face.message.request.ObjectFaceRequest;
import com.linkallcloud.core.face.message.response.ObjectFaceResponse;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.sh.sm.ISignatureMessage;
import com.linkallcloud.sh.sm.SignatureMessage;
import com.linkallcloud.um.domain.sys.Application;

public abstract class BaseKiss {

	protected static Log log = Logs.get();

	@Value("${oapi.url.um}")
	protected String umBaseUrl;

	@Value("${oapi.appcode}")
	protected String myAppCode;// 签名者ID(appKey)//用code替代
	@Value("${oapi.messageEncAlg}")
	protected String messageEncAlg;// 消息加解密算法，暂时支持AES。
	@Value("${oapi.messageEncKey}")
	protected String messageEncKey;// 加密的秘钥
	@Value("${oapi.signatureAlg}")
	protected String signatureAlg;// 签名算法(SHA1/MD5)
	@Value("${oapi.signatureKey}")
	protected String signatureKey;// 签名密钥

	public FaceRequest convertToFaceRequest(Trace t, Object message) {
		FaceRequest request = null;
		if (message == null) {
			request = new ObjectFaceRequest<Object>();
		} else if (message.getClass().getName().equals("java.lang.String")) {
			String temp = (String) message;
			if (Strings.isBlank(temp)) {
				request = new ObjectFaceRequest<String>();
			} else {
				request = new ObjectFaceRequest<String>(temp);
			}
		} else if (!(message instanceof FaceRequest)) {
			request = new ObjectFaceRequest<Object>(message);
		} else {
			request = (FaceRequest) message;
		}
		request.setT(t);
		return request;
	}

	public String packMessage(Trace t, Object message) {
		try {
			FaceRequest request = convertToFaceRequest(t, message);
			ISignatureMessage sendSM = new SignatureMessage(request, myAppCode, signatureAlg);
			sendSM.sign(signatureKey);// 签名
			String sendMsgPkg = sendSM.packMessage();// 打包后的安全消息
			log.debug("*********** 打包后准备发送数据包：" + sendMsgPkg);
			return sendMsgPkg;
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public <T> T unpackMessage(String responseJson, TypeReference<ObjectFaceResponse<T>> tr) {
		log.debug("*********** 接收到数据包：" + responseJson);
		try {
			ISignatureMessage receivedSM = SignatureMessage.from(responseJson);
			receivedSM.verifyStrict(myAppCode, signatureKey, signatureAlg);
			ObjectFaceResponse<T> res = receivedSM.unpackMessage(tr.getType());
			if (res.getCode().equals("0")) {
				return res.getData();
			}
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public String packMessage4App(Trace t, Object message, Application app) {
		try {
			FaceRequest request = convertToFaceRequest(t,message);
			ISignatureMessage sendSM = new SignatureMessage(request, app.getCode(), app.getSignatureAlg());
			sendSM.sign(app.getSignatureKey());// 签名
			String sendMsgPkg = sendSM.packMessage();// 打包后的安全消息
			log.debug("*********** 打包后准备发送数据包：" + sendMsgPkg);
			return sendMsgPkg;
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public <T> T unpackMessage4App(String responseJson, TypeReference<ObjectFaceResponse<T>> tr, Application app) {
		log.debug("*********** 接收到数据包：" + responseJson);
		try {
			ISignatureMessage receivedSM = SignatureMessage.from(responseJson);
			receivedSM.verifyStrict(app.getCode(), app.getSignatureKey(), app.getSignatureAlg());
			ObjectFaceResponse<T> res = receivedSM.unpackMessage(tr.getType());
			if (res.getCode().equals("0")) {
				return res.getData();
			}
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public String getUmOapiUrl() {
		return umBaseUrl;
	}

	public String getMyAppCode() {
		return myAppCode;
	}

	public String getMessageEncAlg() {
		return messageEncAlg;
	}

	public String getMessageEncKey() {
		return messageEncKey;
	}

	public String getSignatureAlg() {
		return signatureAlg;
	}

	public String getSignatureKey() {
		return signatureKey;
	}

}
