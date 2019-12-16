package com.linkallcloud.sso.ticket;

import com.linkallcloud.sso.redis.ticket.RedisTicketCache;

/**
 * Represents a generic CAS ticket.
 */
public abstract class Ticket {

	private String id;

	/** Retrieves the ticket's username. */
	public abstract String getUsername();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public abstract void loadReference(RedisTicketCache<? extends Ticket> cache);

}
