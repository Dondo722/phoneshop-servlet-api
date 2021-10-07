package com.es.phoneshop.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultDosProtectionService implements DosProtectionService {
    private static volatile DefaultDosProtectionService instance;
    private static final Object instanceLock = new Object();

    private static final long THRESHOLD = 20;
    private Map<String,Long> countMap = new ConcurrentHashMap<>();

    public static DefaultDosProtectionService getInstance() {
        if (instance == null) {
            synchronized (instanceLock) {
                if (instance == null) {
                    instance = new DefaultDosProtectionService();
                }
            }
        }

        return instance;
    }

    @Override
    public boolean isAllowed(String id) {
        Long count = countMap.get(id);
        if(count == null) {
            count = 1L;
        }
        else {
            if (count > THRESHOLD) {
                return false;
            }
            count++;
        }
        countMap.put(id,count);
        return true;
    }

    @Override
    public void clearRequestCount() {
        countMap.clear();
    }
}
