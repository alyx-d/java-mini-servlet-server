package com.qt.server.mapping;

import java.util.regex.Pattern;

public abstract class AbstractMapping {

    protected final String url;
    protected final Pattern pattern;
    public AbstractMapping(final String url) {
        this.url = url;
        this.pattern = buildPattern(url);
    }

    Pattern buildPattern(String urlPattern) {
        StringBuilder sb = new StringBuilder(urlPattern.length() + 16);
        sb.append('^');
        for (int i = 0; i < urlPattern.length(); i++) {
            char ch = urlPattern.charAt(i);
            if (ch == '*') {
                sb.append(".*");
            } else if (ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' || ch >= '0' && ch <= '9') {
                sb.append(ch);
            } else {
                sb.append('\\').append(ch);
            }
        }
        sb.append('$');
        return Pattern.compile(sb.toString());
    }
}
