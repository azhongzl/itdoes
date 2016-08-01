package com.itdoes.business.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.itdoes.common.business.BaseEntity;
import com.itdoes.common.business.SecureColumn;

/**
 * @author Jalen Zhong
 */
@Entity
@Table(name = "ss_temp_part")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TempPart extends BaseEntity {
	private static final long serialVersionUID = 2033649964045071367L;

	@Id
	@Column(name = "partid")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer partId;

	@SecureColumn
	@Column(name = "barCode")
	private String barCode;

	@Column(name = "registerdate")
	private Date registerDate;

	public Integer getPartId() {
		return partId;
	}

	public void setPartId(Integer partId) {
		this.partId = partId;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
}
