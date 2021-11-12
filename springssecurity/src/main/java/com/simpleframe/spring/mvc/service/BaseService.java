package com.simpleframe.spring.mvc.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.simpleframe.boot.common.exception.ServiceException;
import com.simpleframe.spring.jpa.helper.SpecificationHelper;
import com.simpleframe.spring.mvc.page.PageInfo;
import com.simpleframe.spring.mvc.repository.BaseRepository;

// ~ File Information
// ====================================================================================================================

public class BaseService<R extends BaseRepository<T, ID>, T, ID> {

	// ~ Static Fields
	// ==================================================================================================================

	// ~ Fields
	// ==================================================================================================================

	@Autowired
	private R baseRepository;

	@PersistenceContext
	private EntityManager entityManager;

	// ~ Constructors
	// ==================================================================================================================

	// ~ Methods
	// ==================================================================================================================
	
	/**
	 * 清空缓存信息.
	 * 
	 */
	@Async
	public void clearCache() {}

	/**
	 * 根据ID查询.
	 * 
	 * @param id
	 * @return
	 */
	public T get(ID id) {

		if (id == null) {
			return null;
		}

		Optional<T> opt = baseRepository.findById(id);

		if (opt.isPresent()) {
			return opt.get();
		}

		return null;
	}

	/**
	 * 查询所有.
	 * 
	 * @return
	 */
	public List<T> findAll() {

		return baseRepository.findAll();
	}

	/**
	 * 查询所有,加入排序条件.
	 * 
	 * @param sort
	 * @return
	 */
	public List<T> findAll(Sort sort) {

		return baseRepository.findAll(sort);
	}

	/**
	 * 根据范例查询.
	 * 
	 * @param example
	 * @return
	 */
	public List<T> findAll(Example<T> example) {

		return this.baseRepository.findAll(example);
	}

	/**
	 * 根据范例查询, 并加入排序.
	 * 
	 * @param example
	 * @param sort
	 * @return
	 */
	public List<T> findAll(Example<T> example, Sort sort) {

		return this.baseRepository.findAll(example, sort);
	}
	
	/**
	 * 数据总量.
	 * 
	 * @return
	 */
	public long count() {
		return this.baseRepository.count();
	}

	/**
	 * 保存.
	 * 
	 * @param entity
	 * @return
	 */
	@Transactional(readOnly = false)
	public T save(T entity) {
		this.clearCache();
		return this.baseRepository.save(entity);
	}

	/**
	 * 保存所有.
	 * 
	 * @param entities
	 * @return
	 */
	@Transactional(readOnly = false)
	public <S extends T> Collection<S> saveAll(Collection<S> entities) {
		this.clearCache();
		return this.baseRepository.saveAll(entities);
	}

	/**
	 * 删除实体.
	 * 
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public void delete(T entity) {
		this.clearCache();
		this.baseRepository.delete(entity);
	}

	/**
	 * 根据ID进行删除.
	 * 
	 * @param id
	 */
	@Transactional(readOnly = false)
	public void deleteById(ID id) {

		Assert.notNull(id, "The given id must not be null!");

		T entity = this.get(id);
		if (entity != null) {
			this.delete(entity);
		}
	}
	
	/**
	 * 根据IDS进行删除.
	 * 
	 * @param id
	 */
	@Transactional(readOnly = false)
	public void deleteByIds(ID[] ids) {

		Assert.notNull(ids, "The given id must not be null!");
		
		if (ids != null && ids.length > 0) {
			for (ID id: ids) {
				T entity = this.get(id);
				if (entity != null) {
					this.delete(entity);
				}
			}
		}
		this.clearCache();
	}

	/**
	 * 分页查询.
	 * 
	 * @param spec
	 * @param page
	 * @return
	 */
	public PageInfo<T> findPage(@Nullable Specification<T> spec, PageInfo<T> page) {

		Page<T> tPage = baseRepository.findAll(spec, page.getPageable());
		page.setPage(tPage);
		return page;
	}

	/**
	 * 分页查询.
	 * 
	 * @param spec
	 * @param page
	 * @return
	 */
	public PageInfo<T> findPage(@Nullable T param, PageInfo<T> page) {

		Specification<T> specification = SpecificationHelper.getSpecification(param);
		Page<T> tPage = baseRepository.findAll(specification, page.getPageable());
		page.setPage(tPage);

		return page;
	}

	/**
	 * 返回repository.
	 * 
	 * @return
	 */
	public R getBaseRepository() {

		return this.baseRepository;
	}

	/**
	 * 根据条件获取列表.
	 * 
	 * @param param
	 * @return
	 */
	public List<T> findList(T param) {

		List<T> list = new ArrayList<>();
		Specification<T> specification = SpecificationHelper.getSpecification(param);
		list = baseRepository.findAll(specification);

		return list;
	}

	/**
	 * 根据条件获取列表.
	 * 
	 * @param param
	 * @param sort
	 * @return
	 */
	public List<T> findList(T param, Sort sort) {

		List<T> list = new ArrayList<>();
		Specification<T> specification = SpecificationHelper.getSpecification(param);
		list = baseRepository.findAll(specification, sort);

		return list;
	}
	
	/**
	 * 根据属性查询.
	 * 
	 * @param params
	 * @return
	 */
	public List<T> findByProp(Map<String, Object> params) {

		Specification<T> spec = new Specification<T>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
				List<Predicate> preds = new ArrayList<Predicate>();
				
				if (params != null && !params.isEmpty()) {
					for (String key : params.keySet()) {
						preds.add(criteriaBuilder.equal(root.get(key), params.get(key)));
					}
				}

				return criteriaBuilder.and(preds.toArray(new Predicate[preds.size()]) );
			}
		};

		return this.getBaseRepository().findAll(spec);
	}

	/**
	 * 根据属性查询.
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public List<T> findByProp(String key, Object value) {

		if (StringUtils.isBlank(key) || value == null) {
			return new ArrayList<T>();
		}

		Specification<T> spec = new Specification<T>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

				return criteriaBuilder.equal(root.get(key), value);
			}
		};

		return this.getBaseRepository().findAll(spec);
	}
	
	/**
	 * 根据属性查询.
	 * 
	 * @param key
	 * @param value
	 * @param sort
	 * @return
	 */
	public List<T> findByProp(String key, Object value, Sort sort) {
		
		if (sort == null) {
			return this.findByProp(key, value);
		}

		if (StringUtils.isBlank(key) || value == null) {
			return new ArrayList<T>();
		}

		Specification<T> spec = new Specification<T>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

				return criteriaBuilder.equal(root.get(key), value);
			}
		};

		return this.getBaseRepository().findAll(spec, sort);
	}
	
	/**
	 * 根据属性查询.
	 * 
	 * @param keys
	 * @param values
	 * @return
	 */
	public List<T> findByProps(String[] keys, Object[] values) {

		if (keys == null || keys.length == 0 || values == null || values.length == 0) {
			return new ArrayList<T>();
		} else if (keys.length != values.length) {
			throw new ServiceException("参数和值下标不对应");
		}

		Specification<T> spec = new Specification<T>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
				List<Predicate> predicates = new ArrayList<Predicate>();
				
				for (int i = 0; i < keys.length; i++) {
					predicates.add(criteriaBuilder.equal(root.get(keys[i]), values[i]));
				}

				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};

		return this.getBaseRepository().findAll(spec);
	}
	
	/**
	 * 根据属性查询.
	 * 
	 * @param keys
	 * @param values
	 * @param sort
	 * @return
	 */
	public List<T> findByProps(String[] keys, Object[] values, Sort sort) {
		
		if (keys == null || keys.length == 0 || values == null || values.length == 0) {
			return new ArrayList<T>();
		} else if (keys.length != values.length) {
			throw new ServiceException("参数和值下标不对应");
		}

		Specification<T> spec = new Specification<T>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
				List<Predicate> predicates = new ArrayList<Predicate>();
				
				for (int i = 0; i < keys.length; i++) {
					predicates.add(criteriaBuilder.equal(root.get(keys[i]), values[i]));
				}

				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};

		return this.getBaseRepository().findAll(spec, sort);
	}

	/**
	 * 验证属性是否唯一.
	 * 
	 * @param propName
	 * @param propValue
	 * @param idValue
	 * @return
	 */
	public boolean validateOnlyOne(String propName, String propValue, String idValue) {

		return this.validateOnlyOne(null, propName, propValue, "id", idValue);
	}

	/**
	 * 验证属性是否唯一.
	 * 
	 * @param tenantId
	 * @param propName
	 * @param propValue
	 * @param idValue
	 * @return
	 */
	public boolean validateOnlyOne(String tenantId, String propName, String propValue, String idValue) {

		return this.validateOnlyOne(tenantId, propName, propValue, "id", idValue);
	}

	/**
	 * 验证属性是否唯一.
	 * 
	 * @param tenantId
	 * @param propName
	 * @param propValue
	 * @param propIdName
	 * @param idValue
	 * @return
	 */
	public boolean validateOnlyOne(String tenantId,
	    String propName,
	    String propValue,
	    String propIdName,
	    String idValue) {

		if (StringUtils.isAnyBlank(propName, propValue)) {
			return true;
		}

		Specification<T> params = new Specification<T>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

				List<Predicate> predicateList = new ArrayList<Predicate>();

				// 加入租户条件
				if (StringUtils.isNotBlank(tenantId)) {
					predicateList.add(criteriaBuilder.equal(root.get("tenantId"), tenantId));
				}

				// id是否为空, 如果为空则是新增, 如果不为空则是修改
				if (StringUtils.isNotBlank(idValue)) {
					predicateList.add(criteriaBuilder.notEqual(root.get(propIdName).as(String.class), idValue));
				}

				predicateList.add(criteriaBuilder.equal(root.get(propName).as(String.class), propValue));

				Predicate[] restrictions = new Predicate[predicateList.size()];
				return criteriaBuilder.and(predicateList.toArray(restrictions));
			}
		};

		if (this.getBaseRepository().count(params) == 0) {
			return true;
		}

		return false;
	}

	/**
	 * 执行hql.
	 * 
	 * @param hql
	 * @param params
	 * @return
	 */
	public List<?> findListByParams(String hql, Map<String, Object> params) {

		if (StringUtils.isBlank(hql)) {
			return new ArrayList<>();
		}

		Query query = this.entityManager.createQuery(hql);

		if (params != null) {
			for (String key : params.keySet()) {
				query.setParameter(key, params.get(key));
			}
		}
		return query.getResultList();
	}
	
	/**
	 * 查询总数.
	 * 
	 * @param hql
	 * @param params
	 * @return
	 */
	private long getTotal(String hql, Map<String, Object> params) {
		
		long total = 0;
		System.out.println(hql);
		if (StringUtils.isBlank(hql)) {
			return total;
		}
		
		Query query = this.entityManager.createQuery(hql);
		
		if (params != null) {
			for (String key : params.keySet()) {
				query.setParameter(key, params.get(key));
			}
		}
		
		System.out.println(hql);
		
		total = (long) query.getSingleResult();
		
		return total;
	}
	
	/**
	 * 执行hql.
	 * 
	 * @param hql
	 * @param params
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageInfo<T> findListByParams(String hql, Map<String, Object> params, PageInfo<T> page) {
		
		if (StringUtils.isBlank(hql)) {
			return page;
		}
		
		Query query = null;
		
		// 判断字符串是否包含select
		if (StringUtils.indexOf(hql, "from") > -1) {
			String tempHql = hql.substring(StringUtils.indexOf(hql, "from"));
			
			long total = this.getTotal("select count(*) " + tempHql, params);
			
			page.setTotal(total);
		} else {
			return page;
		}

		query = this.entityManager.createQuery(hql + " " + page.getOrderby());

		if (params != null) {
			for (String key : params.keySet()) {
				query.setParameter(key, params.get(key));
			}
		}
		
		query.setFirstResult((page.getPageNumber() - 1) * page.getPageSize());
		query.setMaxResults(page.getPageSize());
		page.setContent(query.getResultList());
		
		return page;
	}

	/**
	 * 执行hql.
	 * 
	 * @param hql
	 * @param params
	 * @return
	 */
	public List<?> findListByParams(String hql, Object[]... props) {

		if (StringUtils.isBlank(hql)) {
			return new ArrayList<>();
		}

		Query query = this.entityManager.createQuery(hql);

		if (props != null && props.length > 0) {
			for (int i = 0; i < props.length; i++) {
				query.setParameter(i, props[i]);
			}
		}
		return query.getResultList();
	}

	public EntityManager getEntityManager() {

		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {

		this.entityManager = entityManager;
	}
}