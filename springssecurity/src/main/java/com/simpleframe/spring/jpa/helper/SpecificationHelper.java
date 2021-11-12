package com.simpleframe.spring.jpa.helper;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.Specification;

import com.simpleframe.boot.common.annotation.search.CombColumn;
import com.simpleframe.boot.common.annotation.search.QueryType;
import com.simpleframe.boot.common.annotation.search.RelationType;
import com.simpleframe.spring.jpa.annotation.AnnotationHelper;
import com.simpleframe.spring.jpa.mapper.MapperColumn;


// ~ File Information
// ====================================================================================================================

/**
 * 条件查询工具类.
 * 
 * <pre>
 * 条件查询工具类
 * </pre>
 * 
 * @author liutao
 * @since $Rev$
 *
 */
public class SpecificationHelper {

	// ~ Static Fields
	// ==================================================================================================================

	private static final Logger logger = LoggerFactory.getLogger(SpecificationHelper.class);

	// 需要排除的属性栏位
	private static final String[] EXCLUDE_PORPS = new String[] {
	  "class"
	};

	// ~ Fields
	// ==================================================================================================================

	// ~ Constructors
	// ==================================================================================================================

	// ~ Methods
	// ==================================================================================================================

	/**
	 * 根据传递的数据拼装查询条件.
	 * 
	 * @param obj
	 * @return
	 */
	public static <T> Specification<T> getSpecification(T obj) {

		Specification<T> spec = new Specification<T>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

				List<Predicate> predicateList = new ArrayList<Predicate>();

				if (obj != null) {
					
					Map<String, MapperColumn> columnMap = AnnotationHelper.loadColumnMap(obj.getClass());

					// 获取该类的所有栏位
					PropertyDescriptor[] props = BeanUtils.getPropertyDescriptors(obj.getClass());

					if (props != null && props.length > 0) {
						for (PropertyDescriptor prop : props) {
							SpecificationHelper.addPredicate(obj, prop, root, criteriaBuilder, columnMap.get(prop.getName()), predicateList);
						}
					}
				}

				Predicate[] restrictions = new Predicate[predicateList.size()];
				return criteriaBuilder.and(predicateList.toArray(restrictions));
			}
		};

		return spec;
	}

	/**
	 * 创建查询条件.
	 * 
	 * @param root
	 * @param criteriaBuilder
	 * @param propName
	 * @param propVal
	 * @param queryType
	 * @return
	 */
	public static <T> Predicate createPredicate(Root<T> root, CriteriaBuilder criteriaBuilder, String propName, String propVal, QueryType queryType) {
		
		Predicate result = null;
		
		if (queryType == null) {
			result = criteriaBuilder.equal(root.get(propName), propVal);
		} else {
			switch (queryType) {
				case EQ : {
					result = criteriaBuilder.equal(root.get(propName), propVal);
					break;
				}
				case LT : {
					result = criteriaBuilder.lessThan(root.get(propName), propVal);
					break;
				}
				case LE : {
					result = criteriaBuilder.lessThanOrEqualTo(root.get(propName), propVal);
					break;
				}
				case GT : {
					result = criteriaBuilder.greaterThan(root.get(propName), propVal);
					break;
				}
				case GE : {
					result = criteriaBuilder.greaterThanOrEqualTo(root.get(propName), propVal);
					break;
				}
				case LIKE : {
					result = criteriaBuilder.like(root.get(propName), "%" + propVal + "%");
					break;
				}
				case LIKEBEFORE : {
					result = criteriaBuilder.like(root.get(propName), propVal + "%");
					break;
				}
				case LIKEAFTER : {
					result = criteriaBuilder.like(root.get(propName), "%" + propVal);
					break;
				}
				case IN : {
					//TODO
					result = criteriaBuilder.equal(root.get(propName), propVal);
					break;
				}
				default: {
					result = criteriaBuilder.equal(root.get(propName), propVal);
					break;
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 创建查询条件.
	 * 
	 * @param root
	 * @param criteriaBuilder
	 * @param propName
	 * @param propVal
	 * @param queryType
	 * @return
	 */
	public static <T> Predicate createPredicate(Root<T> root, CriteriaBuilder criteriaBuilder, String propName, Integer propVal, QueryType queryType) {
		
		Predicate result = null;
		
		if (queryType == null) {
			result = criteriaBuilder.equal(root.get(propName), propVal);
		} else {
			switch (queryType) {
				case EQ : {
					result = criteriaBuilder.equal(root.get(propName), propVal);
					break;
				}
				case LT : {
					result = criteriaBuilder.lessThan(root.get(propName), propVal);
					break;
				}
				case LE : {
					result = criteriaBuilder.lessThanOrEqualTo(root.get(propName), propVal);
					break;
				}
				case GT : {
					result = criteriaBuilder.greaterThan(root.get(propName), propVal);
					break;
				}
				case GE : {
					result = criteriaBuilder.greaterThanOrEqualTo(root.get(propName), propVal);
					break;
				}
				case IN : {
					//TODO
					result = criteriaBuilder.equal(root.get(propName), propVal);
					break;
				}
				default: {
					result = criteriaBuilder.equal(root.get(propName), propVal);
					break;
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 创建查询条件.
	 * 
	 * @param root
	 * @param criteriaBuilder
	 * @param propName
	 * @param propVal
	 * @param queryType
	 * @return
	 */
	public static <T> Predicate createPredicate(Root<T> root, CriteriaBuilder criteriaBuilder, String propName, Date propVal, QueryType queryType) {
		
		Predicate result = null;
		
		if (queryType == null) {
			result = criteriaBuilder.equal(root.get(propName), propVal);
		} else {
			switch (queryType) {
				case EQ : {
					result = criteriaBuilder.equal(root.get(propName), propVal);
					break;
				}
				case LT : {
					result = criteriaBuilder.lessThan(root.get(propName), propVal);
					break;
				}
				case LE : {
					result = criteriaBuilder.lessThanOrEqualTo(root.get(propName), propVal);
					break;
				}
				case GT : {
					result = criteriaBuilder.greaterThan(root.get(propName), propVal);
					break;
				}
				case GE : {
					result = criteriaBuilder.greaterThanOrEqualTo(root.get(propName), propVal);
					break;
				}
				case IN : {
					//TODO
					result = criteriaBuilder.equal(root.get(propName), propVal);
					break;
				}
				default: {
					result = criteriaBuilder.equal(root.get(propName), propVal);
					break;
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 创建查询条件.
	 * 
	 * @param root
	 * @param criteriaBuilder
	 * @param propName
	 * @param propVal
	 * @param queryType
	 * @return
	 */
	public static <T> Predicate createPredicate(Root<T> root, CriteriaBuilder criteriaBuilder, String propName, Boolean propVal, QueryType queryType) {
		return criteriaBuilder.equal(root.get(propName), propVal);
	}

	/**
	 * 拼装查询条件.
	 * 
	 * @param obj
	 * @param prop
	 * @param root
	 * @param criteriaBuilder
	 * @param mapper
	 * @param predicateList
	 */
	public static <T> void addPredicate(T obj,
	    PropertyDescriptor prop,
	    Root<T> root,
	    CriteriaBuilder criteriaBuilder,
	    MapperColumn col,
	    List<Predicate> predicateList) {

		// 排除属性
		if (ArrayUtils.contains(EXCLUDE_PORPS, prop.getName())) {
			return;
		}

		// 获取对象值
		try {

			Object value = prop.getReadMethod().invoke(obj);

			if (value == null) {
				return;
			}

			// 属性名称
			String propName = prop.getName();

			// 属性类型
			Class<?> classz = prop.getPropertyType();

			// 判断属性类型
			switch (classz.getSimpleName()) {
				case "String": {

					String propVal = value != null ? value.toString() : null;

					// 值不是空的时候增加查询条件
					if (StringUtils.isNotBlank(propVal)) {
						
						// 是否包含组合列, 包含组合列需要加入组合条件
						if (col != null && col.getCombColumns().length > 0) {
							List<Predicate> results = new ArrayList<>();
							results.add(createPredicate(root, criteriaBuilder, propName, propVal, col.getQueryType()));
							for (CombColumn subCol : col.getCombColumns()) {
								results.add(createPredicate(root, criteriaBuilder, subCol.column(), propVal, subCol.queryType()));
							}
							// 如果是或者关系
							if (col.getCombRelationType() == RelationType.OR) {
								Predicate[] predicates = new Predicate[results.size()];
								results.toArray(predicates);
								predicateList.add(criteriaBuilder.or(predicates));
							}
							else {
								predicateList.addAll(results);
							}
						}
						else {
							predicateList.add(createPredicate(root, criteriaBuilder, propName, propVal, col == null ? null : col.getQueryType()));
						}
					}

					break;
				}
				case "Integer": {

					Integer propVal = value != null ? Integer.valueOf(value.toString()) : null;

					// 值不是空的时候增加查询条件
					if (propVal != null) {
						predicateList.add(createPredicate(root, criteriaBuilder, propName, propVal, col == null ? null : col.getQueryType()));
					}

					break;
				}
				case "Date": {

					Date propVal = value != null ? (Date) value : null;

					// 值不是空的时候增加查询条件
					if (propVal != null) {
						predicateList.add(createPredicate(root, criteriaBuilder, propName, propVal, col == null ? null : col.getQueryType()));
					}

					break;
				}
				case "Boolean": {

					Boolean propVal = value != null ? Boolean.valueOf(value.toString()) : null;

					// 值不是空的时候增加查询条件
					if (propVal != null) {
						predicateList.add(createPredicate(root, criteriaBuilder, propName, propVal, col == null ? null : col.getQueryType()));
					}

					break;
				}
				default: {

					// 值不是空的时候增加查询条件
					if (value != null) {
						predicateList.add(criteriaBuilder.equal(root.get(propName), value));
					}

					break;
				}
			}

		} catch (Exception e) {
			logger.info(String.format("获取属性值错误, 属性:%s, 错误信息:%s", prop.getName(), e.getMessage()));
		}
	}
}
