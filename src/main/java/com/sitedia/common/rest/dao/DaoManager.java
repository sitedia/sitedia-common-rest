package com.sitedia.common.rest.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import com.sitedia.common.rest.exceptions.BusinessException;

/**
 * DAO tool for developers. It allows performing CRUD operations in database,
 * and provides a full filter/sorting/pagination list function.
 * @author sitedia
 *
 */
@Lazy
@Component
public class DaoManager {

    private static final int DEFAULT_MAX_ELEMENTS = 20;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MessageSource messageSource;

    /**
     * Creates the entity in database
     * @param entityClass
     * @param entity
     * @param primaryKey
     * @return
     * @throws BusinessException
     */
    public <T> T create(Class<? extends T> entityClass, T entity) throws BusinessException {
        entityManager.persist(entity);
        return entity;
    }

    /**
     * Updates the entity in database
     * @param entityClass
     * @param entity
     * @param primaryKey
     * @return
     * @throws BusinessException
     */
    public <T> T update(Class<T> entityClass, T entity, Object primaryKey) throws BusinessException {

        // Check that the entity already exist
        T source = entityManager.find(entityClass, primaryKey);
        if (source == null) {
            String message = messageSource.getMessage("sitedia.commonRest.dao.entity.notFound", null, LocaleContextHolder.getLocale());
            throw new BusinessException(message);
        }

        entityManager.persist(entity);
        return entity;
    }

    /**
     * Gets an entity from database
     * @param entityClass
     * @param primaryKey
     * @return
     */
    public <T> T get(Class<T> entityClass, Object primaryKey) {
        return entityManager.find(entityClass, primaryKey);
    }

    /**
     * Deletes the entity in database
     * @param entityClass
     * @param primaryKey
     * @throws BusinessException
     */
    public <T> void delete(Class<T> entityClass, Object primaryKey) throws BusinessException {

        // Check that the entity already exist
        T source = entityManager.find(entityClass, primaryKey);
        if (source == null) {
            String message = messageSource.getMessage("sitedia.commonRest.dao.entity.notFound", null, LocaleContextHolder.getLocale());
            throw new BusinessException(message);
        }

        entityManager.remove(source);
    }

    /**
     * List entities with, pagination, sort and filters
     * @param entityClass
     * @param params
     * @return
     */
    public <T> List<T> list(Class<T> entityClass, Map<String, Object> params) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<T> from = criteriaQuery.from(entityClass);

        // Filters
        List<Predicate> predicates = createPredicates(params, criteriaBuilder, from);
        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));

        // Sorting
        String sortColumn = (String) params.get("_sortField");
        String sortOrder = (String) params.get("_sortDir");
        if (sortColumn != null) {
            if (sortOrder == null || "asc".equalsIgnoreCase(sortOrder)) {
                criteriaQuery.orderBy(criteriaBuilder.asc(from.get(sortColumn)));
            } else {
                criteriaQuery.orderBy(criteriaBuilder.desc(from.get(sortColumn)));
            }
        }

        // Pagination
        Integer size = params.get("_perPage") != null ? new Integer(params.get("_perPage").toString()) : DEFAULT_MAX_ELEMENTS;
        Integer start = size * (params.get("_page") == null ? 0 : (new Integer(params.get("_page").toString()) - 1));

        // Run query
        CriteriaQuery<T> select = criteriaQuery.select(from);
        TypedQuery<T> typedQuery = entityManager.createQuery(select);
        typedQuery.setFirstResult(start);
        typedQuery.setMaxResults(size);
        return typedQuery.getResultList();
    }

    /**
     * Count entities with filters
     * @param entityClass
     * @param params
     * @return
     */
    public <T> Long count(Class<T> entityClass, Map<String, Object> params) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<T> from = criteriaQuery.from(entityClass);

        // Filters
        List<Predicate> predicates = createPredicates(params, criteriaBuilder, from);
        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));

        // Run query
        CriteriaQuery<Long> select = criteriaQuery.select(criteriaBuilder.countDistinct(from));
        TypedQuery<Long> typedQuery = entityManager.createQuery(select);
        return typedQuery.getSingleResult();
    }

    /**
     * Generates predicates according to given params
     * @param params
     * @param criteriaBuilder
     * @param from
     * @return
     */
    @SuppressWarnings({ "rawtypes" })
    private <T> List<Predicate> createPredicates(Map<String, Object> params, CriteriaBuilder criteriaBuilder, Root<T> from) {
        List<Predicate> predicates = new ArrayList<>();
        for (Entry<String, Object> entry : params.entrySet()) {

            // Process params no startin by _
            if (!entry.getKey().startsWith("_")) {
                Object value = entry.getValue();
                Predicate predicate;

                // Equal condition
                if (entry.getKey().endsWith("_eq")) {
                    String key = entry.getKey().substring(0, entry.getKey().length() - 3);
                    predicate = criteriaBuilder.equal(from.get(key), value);
                }

                // In condition
                else if (entry.getKey().endsWith("_in")) {
                    String key = entry.getKey().split("_in")[0];
                    In<Object> inIds = criteriaBuilder.in(from.get(key));
                    for (Object id : ((ArrayList) value)) {
                        inIds.value(id);
                    }
                    predicate = inIds;

                }

                // String condition
                else if (value instanceof String) {
                    final String criteria = "%" + value.toString().toLowerCase(Locale.getDefault()) + "%";
                    predicate = criteriaBuilder.like(criteriaBuilder.lower(from.get(entry.getKey())), criteria);
                }

                // Other conditions
                else {
                    predicate = criteriaBuilder.equal(from.get(entry.getKey()), value);
                }

                predicates.add(predicate);
            }
        }

        return predicates;
    }

}
