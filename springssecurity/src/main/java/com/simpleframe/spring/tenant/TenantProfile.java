package com.simpleframe.spring.tenant;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.simpleframe.boot.common.exception.ServiceException;
import com.simpleframe.boot.common.param.StatusConst;

// ~ File Information
// ====================================================================================================================

public class TenantProfile implements Serializable {

	// ~ Static Fields
	// ==================================================================================================================

	private static final long serialVersionUID = -3283104780143603725L;

	// ~ Fields
	// ==================================================================================================================

	// 机构主键
	private String tenantId;

	// 机构编号
	private String tenantNo;

	// 机构名称
	private String tenantName;

	// 机构简称
	private String tenantNameAbbr;

	// logo地址
	private String logo;

	// 行业分类
	private String industry;

	// 行业分类中文
	private String industryDesc;

	// 认证类型
	private String certType;

	// 认证状态
	private String certStatus;

	// 权重
	private Integer weights;

	// 姓名
	private String name;

	// 电话
	private String telephone;

	// 身份证号
	private String idCard;

	// 社会统一信用代码
	private String uscc;

	// 区域
	private String areaCodes;

	private String areaNames;

	// 详细地址
	private String address;

	// 身份证 正面
	private String cardPositive;

	// 反面
	private String cardNegative;

	// 营业执照
	private String businessLicense;

	// 租户过期
	private boolean tenantNonExpired;

	// 租户认证过期
	private boolean credentialsNonExpired;

	// 启用
	private boolean enabled;

	// ~ Constructors
	// ==================================================================================================================

	public TenantProfile(Map<String, Object> tenant) {

		if (tenant == null) {
			throw new ServiceException("机构信息为空");
		}

		this.tenantId = MapUtils.getString(tenant, "id");
		this.tenantName = MapUtils.getString(tenant, "tenantName");
		this.tenantNo = MapUtils.getString(tenant, "tenantNo");
		this.tenantNameAbbr = MapUtils.getString(tenant, "tenantNameAbbr");
		this.logo = MapUtils.getString(tenant, "logo");
		this.industry = MapUtils.getString(tenant, "industry");
		this.industryDesc = MapUtils.getString(tenant, "industryDesc");
		this.enabled = StringUtils.equals(StatusConst.ENABLE, MapUtils.getString(tenant, "status"));

		// 其他
		this.weights = MapUtils.getInteger(tenant, "weights", 0);
		this.areaCodes = MapUtils.getString(tenant, "areaCodes");
		this.areaNames = MapUtils.getString(tenant, "areaNames");
		this.certStatus = MapUtils.getString(tenant, "certStatus");
		this.address = MapUtils.getString(tenant, "address");
		this.name = MapUtils.getString(tenant, "name");
		this.telephone = MapUtils.getString(tenant, "telephone");
		this.uscc = MapUtils.getString(tenant, "uscc");
		this.idCard = MapUtils.getString(tenant, "idCard");
		this.cardPositive = MapUtils.getString(tenant, "cardPositive");
		this.cardNegative = MapUtils.getString(tenant, "cardNegative");
		this.businessLicense = MapUtils.getString(tenant, "businessLicense");

		// 过期
		if (MapUtils.getObject(tenant, "expired") == null) {
			this.tenantNonExpired = true;
		} else {
			Date expiredDate = (Date) MapUtils.getObject(tenant, "expired");
			this.tenantNonExpired = (expiredDate.getTime() > new Date().getTime());
		}

		this.credentialsNonExpired = true;
	}

	// ~ Methods
	// ==================================================================================================================

	/**
	 * @return 返回 tenantId。
	 */
	public String getTenantId() {

		return tenantId;
	}

	/**
	 * @return 返回 tenantNo。
	 */
	public String getTenantNo() {

		return tenantNo;
	}

	/**
	 * @return 返回 tenantName。
	 */
	public String getTenantName() {

		return tenantName;
	}

	/**
	 * @return 返回 tenantNameAbbr。
	 */
	public String getTenantNameAbbr() {

		return tenantNameAbbr;
	}

	/**
	 * @return 返回 logo。
	 */
	public String getLogo() {

		return logo;
	}

	/**
	 * @return 返回 industry。
	 */
	public String getIndustry() {

		return industry;
	}

	/**
	 * @return 返回 certType。
	 */
	public String getCertType() {

		return certType;
	}

	/**
	 * @return 返回 certStatus。
	 */
	public String getCertStatus() {

		return certStatus;
	}

	/**
	 * @return 返回 weights。
	 */
	public Integer getWeights() {

		return weights;
	}

	/**
	 * @return 返回 name。
	 */
	public String getName() {

		return name;
	}

	/**
	 * @return 返回 telephone。
	 */
	public String getTelephone() {

		return telephone;
	}

	/**
	 * @return 返回 idCard。
	 */
	public String getIdCard() {

		return idCard;
	}

	/**
	 * @return 返回 uscc。
	 */
	public String getUscc() {

		return uscc;
	}

	/**
	 * @return 返回 areaCodes。
	 */
	public String getAreaCodes() {

		return areaCodes;
	}

	/**
	 * @return 返回 areaNames。
	 */
	public String getAreaNames() {

		return areaNames;
	}

	/**
	 * @return 返回 address。
	 */
	public String getAddress() {

		return address;
	}

	/**
	 * @return 返回 cardPositive。
	 */
	public String getCardPositive() {

		return cardPositive;
	}

	/**
	 * @return 返回 cardNegative。
	 */
	public String getCardNegative() {

		return cardNegative;
	}

	/**
	 * @return 返回 businessLicense。
	 */
	public String getBusinessLicense() {

		return businessLicense;
	}

	/**
	 * @return 返回 industryDesc。
	 */
	public String getIndustryDesc() {

		return industryDesc;
	}

	/**
	 * @return 返回 tenantNonExpired。
	 */
	public boolean isTenantNonExpired() {

		return tenantNonExpired;
	}

	/**
	 * @return 返回 credentialsNonExpired。
	 */
	public boolean isCredentialsNonExpired() {

		return credentialsNonExpired;
	}

	/**
	 * @return 返回 enabled。
	 */
	public boolean isEnabled() {

		return enabled;
	}
}
