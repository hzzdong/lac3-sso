package com.linkallcloud.sso.client.proxy.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link ProxyGrantingTicketStorage} that is backed by a
 * HashMap that keeps a ProxyGrantingTicket for a specified amount of time.
 * <p/>
 * A cleanup thread is run periodically to clean out the HashMap.
 * 
 */
public final class ProxyGrantingTicketMapStorage implements ProxyGrantingTicketStorage {

	/**
	 * Default timeout in milliseconds.
	 */
	private static final long DEFAULT_TIMEOUT = 60000;

	private final Map<String, ProxyGrantingTicketHolder> cache = new HashMap<String, ProxyGrantingTicketHolder>();

	/**
	 * Constructor set the timeout to the default value.
	 */
	public ProxyGrantingTicketMapStorage() {
		this(DEFAULT_TIMEOUT);
	}

	/**
	 * Sets the amount of time to hold on to a ProxyGrantingTicket if its never been
	 * retrieved.
	 * 
	 * @param timeout the time to hold on to the ProxyGrantingTicket
	 */
	public ProxyGrantingTicketMapStorage(final long timeout) {
		final Thread thread = new ProxyGrantingTicketCleanupThread(timeout, this.cache);
		thread.setDaemon(true);
		thread.start();
	}

	/*
	 * NOTE: you can only retrieve a ProxyGrantingTicket once with this method. Its
	 * removed after retrieval.
	 * 
	 */
	@Override
	public String retrieve(final String proxyGrantingTicketIou) {
		final ProxyGrantingTicketHolder holder = (ProxyGrantingTicketHolder) this.cache.get(proxyGrantingTicketIou);

		if (holder == null) {
			return null;
		}

		this.cache.remove(proxyGrantingTicketIou);

		return holder.getProxyGrantingTicket();
	}

	@Override
	public void save(final String proxyGrantingTicketIou, final String proxyGrantingTicket) {
		final ProxyGrantingTicketHolder holder = new ProxyGrantingTicketHolder(proxyGrantingTicket);

		this.cache.put(proxyGrantingTicketIou, holder);
	}

	private final class ProxyGrantingTicketHolder {
		private final String proxyGrantingTicket;
		private final long timeInserted;

		protected ProxyGrantingTicketHolder(final String proxyGrantingTicket) {
			this.proxyGrantingTicket = proxyGrantingTicket;
			this.timeInserted = System.currentTimeMillis();
		}

		public String getProxyGrantingTicket() {
			return this.proxyGrantingTicket;
		}

		final boolean isExpired(final long timeout) {
			return System.currentTimeMillis() - this.timeInserted > timeout;
		}
	}

	private final class ProxyGrantingTicketCleanupThread extends Thread {
		private final long timeout;
		private final Map<String, ProxyGrantingTicketHolder> cache;

		public ProxyGrantingTicketCleanupThread(final long timeout,
				final Map<String, ProxyGrantingTicketHolder> cache) {
			this.timeout = timeout;
			this.cache = cache;
		}

		public void run() {

			while (true) {
				try {
					Thread.sleep(this.timeout);
				} catch (final InterruptedException e) {
					// nothing to do
				}

				final List<String> itemsToRemove = new ArrayList<String>();

				synchronized (this.cache) {
					for (final Iterator<String> iter = this.cache.keySet().iterator(); iter.hasNext();) {
						final String key = iter.next();
						final ProxyGrantingTicketHolder holder = (ProxyGrantingTicketHolder) this.cache.get(key);

						if (holder.isExpired(this.timeout)) {
							itemsToRemove.add(key);
						}
					}

					for (final Iterator<String> iter = itemsToRemove.iterator(); iter.hasNext();) {
						this.cache.remove(iter.next());
					}
				}
			}
		}
	}
}
