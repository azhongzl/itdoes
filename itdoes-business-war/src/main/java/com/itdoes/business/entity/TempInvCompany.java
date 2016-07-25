package com.itdoes.business.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.itdoes.common.business.BaseEntity;

/**
 * @author Jalen Zhong
 */
@Entity
@Table(name = "ss_temp_ivncompany")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TempInvCompany extends BaseEntity {
	@Id
	@Column(name = "invcompanyid")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer invCompanyId;

	@Column(name = "comment")
	private String comment;

	@ManyToOne
	@JoinColumn(name = "partid")
	private TempPart part;

	public Integer getInvCompanyId() {
		return invCompanyId;
	}

	public void setInvCompanyId(Integer invCompanyId) {
		this.invCompanyId = invCompanyId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public TempPart getPart() {
		return part;
	}

	public void setPart(TempPart part) {
		this.part = part;
	}
}
