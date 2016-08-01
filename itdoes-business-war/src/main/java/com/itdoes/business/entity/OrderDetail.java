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
@Table(name = "orderdetail")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrderDetail extends BaseEntity {
	private static final long serialVersionUID = -1402060459558822752L;

	@Id
	@Column(name = "orderdetailid")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer orderDetailId;
	@Column(name = "orderno", length = 50, nullable = false)
	private String orderNo;
	@Column(name = "skuno", nullable = false)
	private Integer skuNo;
	@Column(name = "comment", length = 155)
	private String comment;
	@Column(name = "orderqty")
	private Integer orderQty;
	@Column(name = "shipqty")
	private Integer shipQty;
	@Column(name = "partno", length = 30)
	private String partNO;
	@Column(name = "shipped")
	private Boolean shipped;
	@Column(name = "dateflag")
	private Date dateFlag;
	@Column(name = "eta")
	private Date eta;
	@Column(name = "etacomment", length = 200)
	private String etaComment;
	@Column(name = "stock", length = 1)
	private String stock;
	@Column(name = "reserveqty")
	private Integer reserveQty;
	@Column(name = "printqty")
	private Integer printQty;

	public Integer getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(Integer orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getSkuNo() {
		return skuNo;
	}

	public void setSkuNo(Integer skuNo) {
		this.skuNo = skuNo;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(Integer orderQty) {
		this.orderQty = orderQty;
	}

	public Integer getShipQty() {
		return shipQty;
	}

	public void setShipQty(Integer shipQty) {
		this.shipQty = shipQty;
	}

	public String getPartNO() {
		return partNO;
	}

	public void setPartNO(String partNO) {
		this.partNO = partNO;
	}

	public Boolean getShipped() {
		return shipped;
	}

	public void setShipped(Boolean shipped) {
		this.shipped = shipped;
	}

	public Date getDateFlag() {
		return dateFlag;
	}

	public void setDateFlag(Date dateFlag) {
		this.dateFlag = dateFlag;
	}

	public Date getEta() {
		return eta;
	}

	public void setEta(Date eta) {
		this.eta = eta;
	}

	public String getEtaComment() {
		return etaComment;
	}

	public void setEtaComment(String etaComment) {
		this.etaComment = etaComment;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	public Integer getReserveQty() {
		return reserveQty;
	}

	public void setReserveQty(Integer reserveQty) {
		this.reserveQty = reserveQty;
	}

	public Integer getPrintQty() {
		return printQty;
	}

	public void setPrintQty(Integer printQty) {
		this.printQty = printQty;
	}
}
