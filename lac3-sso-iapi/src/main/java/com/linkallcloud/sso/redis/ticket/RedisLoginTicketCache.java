package com.linkallcloud.sso.redis.ticket;

import org.springframework.stereotype.Component;

import com.linkallcloud.sso.ticket.LoginTicket;

@Component
public class RedisLoginTicketCache extends RedisTicketCache<LoginTicket> {

}
