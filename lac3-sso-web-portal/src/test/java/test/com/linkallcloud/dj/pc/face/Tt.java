package test.com.linkallcloud.dj.pc.face;

import com.alibaba.fastjson.JSON;
import com.linkallcloud.sso.ticket.ProxyGrantingTicket;

public class Tt {

	public static void main(String[] args) {
		String pgtJson = "{\"expired\":false,\"stParent\":{\"fromNewLogin\":false,\"grantor\":{\"expired\":false,\"username\":\"admin\"},\"service\":{\"code\":\"lac_app_demo\",\"url\":\"http://localhost:8074/proxy/\"},\"siteMaping\":1,\"siteUser\":\"admin\",\"valid\":true},\"proxies\":[\"http://localhost:8074/proxy/nnl/proxyCallback\"],\"proxyId\":\"http://localhost:8074/proxy/nnl/proxyCallback\",\"username\":\"admin\"}";
		ProxyGrantingTicket pgt = JSON.parseObject(pgtJson, ProxyGrantingTicket.class);
		System.out.println(pgt.getUsername());
	}

}
