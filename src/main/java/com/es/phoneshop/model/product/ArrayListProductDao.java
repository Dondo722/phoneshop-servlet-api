package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.exception.ProductNotFoundException;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {
    private static final String SPACE = " ";
    private static final Object instanceLock = new Object();

    private static volatile ProductDao instance;
    private final List<Product> products;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();
    private long currentId;

    public static ProductDao getInstance() {
        if (instance == null) {
            synchronized (instanceLock) {
                if (instance == null) {
                    instance = new ArrayListProductDao();
                }
            }
        }
        return instance;
    }

    private ArrayListProductDao() {
        products = new ArrayList<>();
    }

    @Override
    public Product getProduct(Long id) {
        readLock.lock();
        try {
            return products.stream()
                    .filter(p -> p.getId().equals(id))
                    .findAny()
                    .orElseThrow(ProductNotFoundException::new);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        readLock.lock();
        try {
            Comparator<Product> comparator = (p1, p2) -> {
                if (SortField.DESCRIPTION == sortField) {
                    return p1.getDescription().compareTo(p2.getDescription());
                } else if (SortField.PRICE == sortField) {
                    return p1.getPrice().getCurrentPrice().compareTo(p2.getPrice().getCurrentPrice());
                } else {
                    return 0;
                }
            };
            if (SortOrder.DESC == sortOrder) {
                comparator = comparator.reversed();
            }
            return products.stream()
                    .filter(product -> isProductSuitableForQuery(product,query))
                    .sorted((p1, p2) -> coincidences(p2,query) - coincidences(p1,query))
                    .sorted(comparator)
                    .collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void save(Product product) {
        writeLock.lock();
        try {
            product.setId(++currentId);
            products.add(new Product(product));
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void delete(Long id) {
        writeLock.lock();
        try {
            products.remove(getProduct(id));
            currentId--;
            products.stream()
                    .filter(p -> p.getId() > id)
                    .forEach(p -> p.setId(p.getId() - 1));
        } finally {
            writeLock.unlock();
        }
    }

    private boolean isProductSuitableForQuery(Product product, String query) {
        if (query == null || query.isEmpty()) {
            return true;
        }
        return numberOfMatchesWithQuery(product, query) != 0;
    }

    private int coincidences(Product product, String query) {
        if (query == null || query.isEmpty()) {
            return 1;//default val
        }
        int productDescriptionWordsNumber = product.getDescription().split(SPACE).length;
        return  numberOfMatchesWithQuery(product,query) * 100 / productDescriptionWordsNumber;
    }

    private int numberOfMatchesWithQuery(Product product, String query) {
        List<String> productDescriptions = Arrays.asList(product.getDescription().toLowerCase().split(SPACE));
        List<String> queries = Arrays.asList(query.toLowerCase().split(SPACE));
        List<Boolean> matches = queries.stream()
                .map(productDescriptions::contains)
                .filter(aBoolean -> aBoolean)
                .collect(Collectors.toList());
        return matches.size();
    }
}
