package com.linkallcloud.sso.redis.ticket;

import org.springframework.stereotype.Component;

import com.linkallcloud.sso.ticket.TicketGrantingTicket;

@Component
public class RedisTicketGrantingTicketCache extends RedisTicketCache<TicketGrantingTicket> {

}
