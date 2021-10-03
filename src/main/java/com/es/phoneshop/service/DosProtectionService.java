package com.es.phoneshop.service;

public interface DosProtectionService {
    boolean isAllowed(String id);

    void clearRequestCount();
}
