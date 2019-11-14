/*
 * cn.zj.pubinfo.sso.client.util.UrlPattern.java
 * Aug 30, 2011 
 */
package com.linkallcloud.sso.client.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import com.linkallcloud.core.lang.Strings;

/**
 * Aug 30, 2011
 * 
 * @author <a href="mailto:touchnan@gmail.com">chengqiang.han</a>
 * 
 */
public class UrlPattern {
    private List<Pattern> patterns;

    public UrlPattern() {
        super();
        this.patterns = Collections.emptyList();
    }
    
    public UrlPattern(String commaDelim) {
        super();
        if (!Strings.isBlank(commaDelim)) {
            String[] split = commaDelim.split(",");
            this.patterns = new ArrayList<Pattern>(split.length);
            for (int i = 0; i < split.length; i++) {
                String pattern = split[i].trim();
                if (pattern.length() > 0) {
                    this.patterns.add(Pattern.compile(pattern));
                }
            }
        } else {
            this.patterns = Collections.emptyList();
        }        
    }
    
    public boolean isMatcher(String expr) {
        for (Pattern pattern : this.patterns) {
            if (pattern.matcher(expr).matches())
                return true;
        }
        return false;
    }
}
