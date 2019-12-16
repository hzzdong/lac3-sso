package com.linkallcloud.sso.redis.ticket;

import org.springframework.stereotype.Component;

import com.linkallcloud.sso.ticket.ProxyTicket;

@Component
public class RedisProxyTicketCache extends RedisTicketCache<ProxyTicket> {

}
