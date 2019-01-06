package com.tealsquares.oscarsdeathrace.entities;

import java.util.HashMap;
import java.util.Map;

public class WatchedMovie {

    private long watchedTimestamp;

    public long getWatchedTimestamp() {
        return watchedTimestamp;
    }

    public void setWatchedTimestamp(long watchedTimestamp) {
        this.watchedTimestamp = watchedTimestamp;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> watchedMovieMap = new HashMap<>();
        watchedMovieMap.put("watchedTimestamp", watchedTimestamp);
        return watchedMovieMap;
    }

}
