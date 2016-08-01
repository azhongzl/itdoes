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

/**
 * @author Jalen Zhong
 */
@Entity
@Table(name = "invcompany")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class InvCompany extends BaseEntity {
	private static final long serialVersionUID = 7675920774289107945L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer inventoryId;

	private Integer skuNo;

	private Integer companyId;

	private Integer onHandQty;

	private Integer onOrderQty;

	private Integer transOutQty;

	private Integer transInQty;

	private Integer commQty;

	@Column(name = "avgty")
	private Integer avgQty;

	@Column(name = "maxgty")
	private Integer maxQty;

	@Column(name = "mingty")
	private Integer minQty;

	private Date eta;

	private String comment;

	private String invType;

	private String status;

	private Date etaConfirm;

	private Integer onOrderQtyGift;

	private Integer reserveQty;

	public Integer getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(Integer inventoryId) {
		this.inventoryId = inventoryId;
	}

	public Integer getSkuNo() {
		return skuNo;
	}

	public void setSkuNo(Integer skuNo) {
		this.skuNo = skuNo;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getOnHandQty() {
		return onHandQty;
	}

	public void setOnHandQty(Integer onHandQty) {
		this.onHandQty = onHandQty;
	}

	public Integer getOnOrderQty() {
		return onOrderQty;
	}

	public void setOnOrderQty(Integer onOrderQty) {
		this.onOrderQty = onOrderQty;
	}

	public Integer getTransOutQty() {
		return transOutQty;
	}

	public void setTransOutQty(Integer transOutQty) {
		this.transOutQty = transOutQty;
	}

	public Integer getTransInQty() {
		return transInQty;
	}

	public void setTransInQty(Integer transInQty) {
		this.transInQty = transInQty;
	}

	public Integer getCommQty() {
		return commQty;
	}

	public void setCommQty(Integer commQty) {
		this.commQty = commQty;
	}

	public Integer getAvgQty() {
		return avgQty;
	}

	public void setAvgQty(Integer avgQty) {
		this.avgQty = avgQty;
	}

	public Integer getMaxQty() {
		return maxQty;
	}

	public void setMaxQty(Integer maxQty) {
		this.maxQty = maxQty;
	}

	public Integer getMinQty() {
		return minQty;
	}

	public void setMinQty(Integer minQty) {
		this.minQty = minQty;
	}

	public Date getEta() {
		return eta;
	}

	public void setEta(Date eta) {
		this.eta = eta;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getInvType() {
		return invType;
	}

	public void setInvType(String invType) {
		this.invType = invType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getEtaConfirm() {
		return etaConfirm;
	}

	public void setEtaConfirm(Date etaConfirm) {
		this.etaConfirm = etaConfirm;
	}

	public Integer getOnOrderQtyGift() {
		return onOrderQtyGift;
	}

	public void setOnOrderQtyGift(Integer onOrderQtyGift) {
		this.onOrderQtyGift = onOrderQtyGift;
	}

	public Integer getReserveQty() {
		return reserveQty;
	}

	public void setReserveQty(Integer reserveQty) {
		this.reserveQty = reserveQty;
	}
}
