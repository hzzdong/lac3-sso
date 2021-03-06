package com.linkallcloud.sso.portal.auth.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.www.utils.WebUtils;
import com.linkallcloud.sso.domain.Account;
import com.linkallcloud.sso.exception.AuthException;
import com.linkallcloud.sso.portal.auth.PasswordHandler;

/**
 * A PasswordHandler base class that implements logic to block IP addresses that engage in too many unsuccessful login
 * attempts. The goal is to limit the damage that a dictionary-based password attack can achieve. We implement this with
 * a token-based strategy; failures are regularly forgotten, and only build up when they occur faster than expiry.
 */
public abstract class WatchfulPasswordHandler implements PasswordHandler {

    // *********************************************************************
    // Constants

    /**
     * The number of failed login attempts to allow before locking out the source IP address. (Note that failed login
     * attempts "expire" regularly.)
     */
    private static final int FAILURE_THRESHOLD = 100;

    /**
     * The interval to wait before expiring recorded failure attempts.
     */
    private static final int FAILURE_TIMEOUT = 60;

    // *********************************************************************
    // Private state

    /** Map of offenders to the number of their offenses. */
    private static Map<String, Integer> offenders = new HashMap<>();

    /** Thread to manage offenders. */
    private static Thread offenderThread = new Thread() {
        public void run() {
            try {
                while (true) {
                    Thread.sleep(FAILURE_TIMEOUT * 1000);
                    expireFailures();
                }
            } catch (InterruptedException ex) {
                // ignore
            }
        }
    };

    static {
        offenderThread.setDaemon(true);
        offenderThread.start();
    }

    // *********************************************************************
    // Gating logic

    /**
     * Returns true if the given request comes from an IP address whose allotment of failed login attemps is within
     * reasonable bounds; false otherwise. Note: We don't actually validate the user and password; this functionality
     * must be implemented by subclasses.
     */
    @Override
    public synchronized Account authenticate(Trace t, javax.servlet.ServletRequest request, String netid,
            String password, int appClazz) {
        if (getFailures(request.getRemoteAddr()) < FAILURE_THRESHOLD) {
            return doAuthenticate(t, request, netid, password, appClazz);
        }
        throw new AuthException(AuthException.ARG_CODE_AUTH, "您的IP错误次数太多，请稍后再试！");
    }

    protected abstract Account doAuthenticate(Trace t, ServletRequest request, String netid, String password,
            int appClazz);

    /** Registers a login failure initiated by the given address. */
    protected synchronized void registerFailure(javax.servlet.ServletRequest r) {
        String address = WebUtils.getIpAddress((HttpServletRequest) r);// r.getRemoteAddr();
        offenders.put(address, Integer.valueOf(getFailures(address) + 1));
    }

    /** Returns the number of "active" failures for the given address. */
    private synchronized static int getFailures(String address) {
        Integer o = offenders.get(address);
        if (o == null)
            return 0;
        else
            return o.intValue();
    }

    /**
     * Removes one failure record from each offender; if any offender's resulting total is zero, remove it from the
     * list.
     */
    private synchronized static void expireFailures() {
        // scoop up addresses from Map so as to avoid modifying the Map in-place
        Set<String> keys = offenders.keySet();
        Iterator<String> ki = keys.iterator();
        List<String> l = new ArrayList<>();
        while (ki.hasNext())
            l.add(ki.next());

        // now, decrement and prune as appropriate
        for (int i = 0; i < l.size(); i++) {
            String address = (String) l.get(i);
            int failures = getFailures(address) - 1;
            if (failures > 0)
                offenders.put(address, Integer.valueOf(failures));
            else
                offenders.remove(address);
        }
    }

}
