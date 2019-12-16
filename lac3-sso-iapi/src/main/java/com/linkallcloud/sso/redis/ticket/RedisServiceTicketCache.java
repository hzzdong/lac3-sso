package com.linkallcloud.sso.redis.ticket;

import org.springframework.stereotype.Component;

import com.linkallcloud.sso.ticket.ServiceTicket;

@Component
public class RedisServiceTicketCache extends RedisTicketCache<ServiceTicket> {

}
