package com.simpleframe.spring.jpa.mapper;

import com.simpleframe.boot.common.annotation.search.CombColumn;
import com.simpleframe.boot.common.annotation.search.QueryType;
import com.simpleframe.boot.common.annotation.search.RelationType;

// ~ File Information
// ====================================================================================================================

public class MapperColumn {

	// ~ Static Fields
	// ==================================================================================================================

	// ~ Fields
	// ==================================================================================================================

	private String name;

	private QueryType queryType;

	private boolean tenant;

	private CombColumn[] combColumns;
	
	private RelationType combRelationType;

	private RelationType relationType;

	// ~ Constructors
	// ==================================================================================================================

	public MapperColumn(String name, QueryType queryType) {

		this.name = name;
		this.queryType = queryType;
	}

	public MapperColumn(String name, QueryType queryType, boolean tenant) {

		this.name = name;
		this.queryType = queryType;
		this.tenant = tenant;
	}
	
	public MapperColumn(String name, QueryType queryType, boolean tenant, RelationType relationType, RelationType combRelationType, CombColumn[] combColumns) {

		this.name = name;
		this.queryType = queryType;
		this.tenant = tenant;
		this.relationType = relationType;
		this.combRelationType = combRelationType;
		this.combColumns = combColumns;
	} 

	// ~ Methods
	// ==================================================================================================================

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public QueryType getQueryType() {

		return queryType;
	}

	public void setQueryType(QueryType queryType) {

		this.queryType = queryType;
	}

	public boolean isTenant() {

		return tenant;
	}

	public void setTenant(boolean tenant) {

		this.tenant = tenant;
	}
	
	public CombColumn[] getCombColumns() {
	
		return combColumns;
	}

	public void setCombColumns(CombColumn[] combColumns) {
	
		this.combColumns = combColumns;
	}

	public RelationType getRelationType() {

		return relationType;
	}

	public void setRelationType(RelationType relationType) {

		this.relationType = relationType;
	}

	public RelationType getCombRelationType() {

		return combRelationType;
	}

	public void setCombRelationType(RelationType combRelationType) {

		this.combRelationType = combRelationType;
	}
}
