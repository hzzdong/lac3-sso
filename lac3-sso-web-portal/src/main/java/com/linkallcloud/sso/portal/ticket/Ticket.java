package com.linkallcloud.sso.portal.ticket;

/**
 * Represents a generic CAS ticket.
 */
public abstract class Ticket {

  /** Retrieves the ticket's username. */
  public abstract String getUsername();

}
