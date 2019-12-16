package com.linkallcloud.sso.redis.ticket;

import org.springframework.stereotype.Component;

import com.linkallcloud.sso.ticket.ProxyGrantingTicket;

@Component
public class RedisProxyGrantingTicketCache extends RedisTicketCache<ProxyGrantingTicket> {

}
