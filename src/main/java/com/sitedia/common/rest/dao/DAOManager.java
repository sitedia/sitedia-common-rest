package com.sitedia.common.rest.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
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

import com.sitedia.common.rest.exception.BusinessException;

/**
 * DAO manager
 * @author cedric
 *
 */
@Component
@Lazy
public class DAOManager {

    private static final Logger daoPersistLogger = Logger.getLogger("dao.persist");

    private static final int DEFAULT_MAX_ELEMENTS = 20;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MessageSource messageSource;

    /**
     * Create the entity in database
     * @param entityClass
     * @param objectEntity
     * @param primaryKey
     * @param alreadyExistsKey
     * @return
     * @throws BusinessException
     */
    public <T> T create(Class<? extends T> entityClass, T objectEntity, Object primaryKey, String alreadyExistsKey) throws BusinessException {

        // Check that the entity doesn't already exist
        if (primaryKey != null && entityManager.find(entityClass, primaryKey) != null) {
            String message = messageSource.getMessage(alreadyExistsKey, null, LocaleContextHolder.getLocale());
            throw new BusinessException(message);
        }

        entityManager.persist(objectEntity);
        daoPersistLogger.fine(String.format("%s: %s", entityClass.getSimpleName(), objectEntity.toString()));
        return objectEntity;
    }

    /**
     * Create or update the entity in database
     * @param entityClass
     * @param objectEntity
     * @param primaryKey
     * @param alreadyExistsKey
     * @return
     * @throws BusinessException
     */
    public <T> T createOrUpdate(T objectEntity) {
        entityManager.persist(objectEntity);
        daoPersistLogger.fine(objectEntity.getClass().getSimpleName() + ": " + objectEntity.toString());
        return objectEntity;
    }

    /**
     * Update the entity
     * @param entityClass
     * @param objectEntity
     * @param primaryKey
     * @return
     * @throws BusinessException
     */
    public <T> T update(Class<T> entityClass, T objectEntity, Object primaryKey) throws BusinessException {

        // Check that the entity already exist
        T entity = entityManager.find(entityClass, primaryKey);
        if (entity == null) {
            throw new BusinessException("the item has not been found");
        }

        entityManager.persist(objectEntity);
        daoPersistLogger.fine(String.format("%s: %s", entityClass.getSimpleName(), objectEntity.toString()));
        return objectEntity;
    }

    /**
     * Get an entity
     * @param entityClass
     * @param primaryKey
     * @return
     */
    public <T> T get(Class<T> entityClass, Object primaryKey) {
        T result = entityManager.find(entityClass, primaryKey);
        Logger.getLogger("dao.get").fine(entityClass.getSimpleName() + ": " + primaryKey);
        return result;
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

        CriteriaQuery<T> select = criteriaQuery.select(from);
        TypedQuery<T> typedQuery = entityManager.createQuery(select);
        typedQuery.setFirstResult(start);
        typedQuery.setMaxResults(size);
        List<T> result = typedQuery.getResultList();

        Logger.getLogger("dao.list").fine(entityClass.getSimpleName() + ": " + params);
        return result;
    }

    @SuppressWarnings({ "rawtypes" })
    private <T> List<Predicate> createPredicates(Map<String, Object> params, CriteriaBuilder criteriaBuilder, Root<T> from) {
        List<Predicate> predicates = new ArrayList<>();
        for (Entry<String, Object> entry : params.entrySet()) {
            if (!entry.getKey().startsWith("_")) {
                Object value = entry.getValue();
                Predicate predicate;
                if (entry.getKey().endsWith("_eq")) {
                    String key = entry.getKey().substring(0, entry.getKey().length() - 3);
                    predicate = criteriaBuilder.equal(from.get(key), value);
                } else if (entry.getKey().endsWith("_in")) {
                    String key = entry.getKey().split("_in")[0];
                    In<Object> inIds = criteriaBuilder.in(from.get(key));
                    for (Object id : ((ArrayList) value)) {
                        inIds.value(id);
                    }
                    predicate = inIds;
                } else if (value instanceof String) {
                    predicate = criteriaBuilder.like(criteriaBuilder.lower(from.get(entry.getKey())),
                            "%" + value.toString().toLowerCase(Locale.getDefault()) + "%");
                } else {
                    predicate = criteriaBuilder.equal(from.get(entry.getKey()), value);
                }
                predicates.add(predicate);
            }
        }
        return predicates;
    }

    /**
     * Count entities with filters
     * @param entityClass
     * @param params
     * @return
     */
    @SuppressWarnings("rawtypes")
    public <T> Long count(Class<T> entityClass, Map<String, Object> params) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<T> from = criteriaQuery.from(entityClass);

        // Count
        criteriaQuery.select(criteriaBuilder.countDistinct(from));

        // Filters
        List<Predicate> predicates = new ArrayList<>();
        for (Entry<String, Object> entry : params.entrySet()) {
            if (!entry.getKey().startsWith("_")) {
                Object value = entry.getValue();
                Predicate predicate;
                if (entry.getKey().endsWith("_eq")) {
                    String key = entry.getKey().substring(0, entry.getKey().length() - 3);
                    predicate = criteriaBuilder.equal(from.get(key), value);
                } else if (entry.getKey().endsWith("_in")) {
                    String key = entry.getKey().split("_in")[0];
                    In<Object> inIds = criteriaBuilder.in(from.get(key));
                    for (Object id : ((ArrayList) value)) {
                        inIds.value(id);
                    }
                    predicate = inIds;
                } else if (value instanceof String) {
                    predicate = criteriaBuilder.like(from.get(entry.getKey()), "%" + value + "%");
                } else {
                    predicate = criteriaBuilder.equal(from.get(entry.getKey()), value);
                }
                predicates.add(predicate);
            }
        }
        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));

        Long result = entityManager.createQuery(criteriaQuery).getSingleResult();

        Logger.getLogger("dao.count").fine(entityClass.getSimpleName() + ": " + params);
        return result;
    }

    /**
     * Delete the entity
     * @param entityClass
     * @param primaryKey
     * @throws BusinessException
     */
    public <T> void delete(Class<T> entityClass, Object primaryKey) throws BusinessException {

        // Check that the entity already exist
        T entity = entityManager.find(entityClass, primaryKey);
        if (entity == null) {
            throw new BusinessException("User not found");
        }
        entityManager.remove(entity);
        Logger.getLogger("dao.delete").fine(entityClass.getSimpleName() + ": " + primaryKey);
    }

    public <E> void refresh(E entity) {
        entityManager.refresh(entity);
        Logger.getLogger("dao.refresh").fine(entity.getClass().getSimpleName() + ": " + entity.toString());
    }

    public <T> List<T> getByParentId(Class<T> childrenClass, Object parentId, String parentField) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(childrenClass);
        Root<T> from = criteriaQuery.from(childrenClass);
        criteriaQuery.where(criteriaBuilder.equal(from.get(parentField), parentId));
        CriteriaQuery<T> select = criteriaQuery.select(from);
        TypedQuery<T> typedQuery = entityManager.createQuery(select);
        return typedQuery.getResultList();
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> find(String nativeQuery, Map<String, Object> params) {
        Query query = entityManager.createNativeQuery(nativeQuery);
        for (Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.getResultList();
    }

}
