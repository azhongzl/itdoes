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
@Table(name = "orderheader")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrderHeader extends BaseEntity {
	@Id
	@Column(name = "orderid")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer orderId;
	@Column(name = "orderno", length = 15, nullable = false)
	private String orderNo;
	@Column(name = "pono", length = 50)
	private String pono;
	@Column(name = "companyid")
	private Integer companyId;
	@Column(name = "carrierid")
	private Integer carrierId;
	@Column(name = "status")
	private String status;
	@Column(name = "dateflag")
	private Date dateFlag;
	@Column(name = "entrydate")
	private Date entryDate;
	@Column(name = "entryid")
	private Integer entryId;
	@Column(name = "componentdate")
	private Date componentDate;
	@Column(name = "componentid")
	private Integer componentId;
	@Column(name = "partpodate")
	private Date partPoDate;
	@Column(name = "partpoid")
	private Integer partPoId;
	@Column(name = "partpono", length = 40)
	private String partPoNo;
	@Column(name = "linecnt")
	private Integer lineCnt;
	@Column(name = "menufestdate")
	private Date menufestDate;
	@Column(name = "menufestid")
	private Integer menufestId;
	@Column(name = "pickdate")
	private Date pickDate;
	@Column(name = "pickid")
	private Integer pickId;
	@Column(name = "pickby", length = 40)
	private String pickBy;
	@Column(name = "reviewby", length = 40)
	private String reviewBy;
	@Column(name = "labeldate")
	private Date labelDate;
	@Column(name = "labelid")
	private Integer labelId;
	@Column(name = "shipdate")
	private Date shipDate;
	@Column(name = "shipdatecarrier")
	private Date shipDateCarrier;
	@Column(name = "shipid")
	private Integer shipId;
	@Column(name = "invoicedate")
	private Date invoiceDate;
	@Column(name = "invoiceid")
	private Integer invoiceId;
	@Column(name = "ordernote", length = 200)
	private String orderNote;
	@Column(name = "completedate")
	private Date completeDate;
	@Column(name = "completeid")
	private Integer completeId;
	@Column(name = "orderstatus", length = 10)
	private String orderStatus;
	@Column(name = "trackno", length = 100)
	private String trackNo;
	@Column(name = "havepart")
	private Boolean havePart;
	@Column(name = "mtldate")
	private Date mtlDate;
	@Column(name = "mtlid")
	private Integer mtlId;
	@Column(name = "comment", length = 255)
	private String comment;
	@Column(name = "commentdate")
	private Date commentDate;
	@Column(name = "ismtl")
	private Boolean isMtl;
	@Column(name = "havepackingslip")
	private Boolean havePackingSlip;
	@Column(name = "needinspect")
	private Boolean needInspect;
	@Column(name = "inspectdate")
	private Date inspectDate;
	@Column(name = "inspectid")
	private Integer inspectId;
	@Column(name = "inspectby", length = 30)
	private String inspectBy;
	@Column(name = "freightcost")
	private Double freightCost;
	@Column(name = "mycompanyid")
	private Integer myCompanyId;
	@Column(name = "isrush")
	private Boolean isRush;
	@Column(name = "iscod")
	private Boolean isCod;
	@Column(name = "signed")
	private Boolean signed;
	@Column(name = "skidtype", length = 30)
	private String skidType;
	@Column(name = "officecomment", length = 255)
	private String officeComment;
	@Column(name = "warehousecomment", length = 255)
	private String warehouseComment;
	@Column(name = "invtypename", length = 20)
	private String invTypeName;
	@Column(name = "ptcomment", length = 255)
	private String ptComment;
	@Column(name = "pttype", length = 50)
	private String ptType;
	@Column(name = "currencyid")
	private Integer currencyId;
	@Column(name = "allowedpickdate")
	private Date allowedPickDate;
	@Column(name = "priority")
	private Integer priority;
	@Column(name = "salesid")
	private Integer salesId;
	@Column(name = "companyidbill")
	private Integer companyIdBill;

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getPono() {
		return pono;
	}

	public void setPono(String pono) {
		this.pono = pono;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(Integer carrierId) {
		this.carrierId = carrierId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDateFlag() {
		return dateFlag;
	}

	public void setDateFlag(Date dateFlag) {
		this.dateFlag = dateFlag;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public Integer getEntryId() {
		return entryId;
	}

	public void setEntryId(Integer entryId) {
		this.entryId = entryId;
	}

	public Date getComponentDate() {
		return componentDate;
	}

	public void setComponentDate(Date componentDate) {
		this.componentDate = componentDate;
	}

	public Integer getComponentId() {
		return componentId;
	}

	public void setComponentId(Integer componentId) {
		this.componentId = componentId;
	}

	public Date getPartPoDate() {
		return partPoDate;
	}

	public void setPartPoDate(Date partPoDate) {
		this.partPoDate = partPoDate;
	}

	public Integer getPartPoId() {
		return partPoId;
	}

	public void setPartPoId(Integer partPoId) {
		this.partPoId = partPoId;
	}

	public String getPartPoNo() {
		return partPoNo;
	}

	public void setPartPoNo(String partPoNo) {
		this.partPoNo = partPoNo;
	}

	public Integer getLineCnt() {
		return lineCnt;
	}

	public void setLineCnt(Integer lineCnt) {
		this.lineCnt = lineCnt;
	}

	public Date getMenufestDate() {
		return menufestDate;
	}

	public void setMenufestDate(Date menufestDate) {
		this.menufestDate = menufestDate;
	}

	public Integer getMenufestId() {
		return menufestId;
	}

	public void setMenufestId(Integer menufestId) {
		this.menufestId = menufestId;
	}

	public Date getPickDate() {
		return pickDate;
	}

	public void setPickDate(Date pickDate) {
		this.pickDate = pickDate;
	}

	public Integer getPickId() {
		return pickId;
	}

	public void setPickId(Integer pickId) {
		this.pickId = pickId;
	}

	public String getPickBy() {
		return pickBy;
	}

	public void setPickBy(String pickBy) {
		this.pickBy = pickBy;
	}

	public String getReviewBy() {
		return reviewBy;
	}

	public void setReviewBy(String reviewBy) {
		this.reviewBy = reviewBy;
	}

	public Date getLabelDate() {
		return labelDate;
	}

	public void setLabelDate(Date labelDate) {
		this.labelDate = labelDate;
	}

	public Integer getLabelId() {
		return labelId;
	}

	public void setLabelId(Integer labelId) {
		this.labelId = labelId;
	}

	public Date getShipDate() {
		return shipDate;
	}

	public void setShipDate(Date shipDate) {
		this.shipDate = shipDate;
	}

	public Date getShipDateCarrier() {
		return shipDateCarrier;
	}

	public void setShipDateCarrier(Date shipDateCarrier) {
		this.shipDateCarrier = shipDateCarrier;
	}

	public Integer getShipId() {
		return shipId;
	}

	public void setShipId(Integer shipId) {
		this.shipId = shipId;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Integer getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Integer invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getOrderNote() {
		return orderNote;
	}

	public void setOrderNote(String orderNote) {
		this.orderNote = orderNote;
	}

	public Date getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}

	public Integer getCompleteId() {
		return completeId;
	}

	public void setCompleteId(Integer completeId) {
		this.completeId = completeId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getTrackNo() {
		return trackNo;
	}

	public void setTrackNo(String trackNo) {
		this.trackNo = trackNo;
	}

	public Boolean getHavePart() {
		return havePart;
	}

	public void setHavePart(Boolean havePart) {
		this.havePart = havePart;
	}

	public Date getMtlDate() {
		return mtlDate;
	}

	public void setMtlDate(Date mtlDate) {
		this.mtlDate = mtlDate;
	}

	public Integer getMtlId() {
		return mtlId;
	}

	public void setMtlId(Integer mtlId) {
		this.mtlId = mtlId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(Date commentDate) {
		this.commentDate = commentDate;
	}

	public Boolean getIsMtl() {
		return isMtl;
	}

	public void setIsMtl(Boolean isMtl) {
		this.isMtl = isMtl;
	}

	public Boolean getHavePackingSlip() {
		return havePackingSlip;
	}

	public void setHavePackingSlip(Boolean havePackingSlip) {
		this.havePackingSlip = havePackingSlip;
	}

	public Boolean getNeedInspect() {
		return needInspect;
	}

	public void setNeedInspect(Boolean needInspect) {
		this.needInspect = needInspect;
	}

	public Date getInspectDate() {
		return inspectDate;
	}

	public void setInspectDate(Date inspectDate) {
		this.inspectDate = inspectDate;
	}

	public Integer getInspectId() {
		return inspectId;
	}

	public void setInspectId(Integer inspectId) {
		this.inspectId = inspectId;
	}

	public String getInspectBy() {
		return inspectBy;
	}

	public void setInspectBy(String inspectBy) {
		this.inspectBy = inspectBy;
	}

	public Double getFreightCost() {
		return freightCost;
	}

	public void setFreightCost(Double freightCost) {
		this.freightCost = freightCost;
	}

	public Integer getMyCompanyId() {
		return myCompanyId;
	}

	public void setMyCompanyId(Integer myCompanyId) {
		this.myCompanyId = myCompanyId;
	}

	public Boolean getIsRush() {
		return isRush;
	}

	public void setIsRush(Boolean isRush) {
		this.isRush = isRush;
	}

	public Boolean getIsCod() {
		return isCod;
	}

	public void setIsCod(Boolean isCod) {
		this.isCod = isCod;
	}

	public Boolean getSigned() {
		return signed;
	}

	public void setSigned(Boolean signed) {
		this.signed = signed;
	}

	public String getSkidType() {
		return skidType;
	}

	public void setSkidType(String skidType) {
		this.skidType = skidType;
	}

	public String getOfficeComment() {
		return officeComment;
	}

	public void setOfficeComment(String officeComment) {
		this.officeComment = officeComment;
	}

	public String getWarehouseComment() {
		return warehouseComment;
	}

	public void setWarehouseComment(String warehouseComment) {
		this.warehouseComment = warehouseComment;
	}

	public String getInvTypeName() {
		return invTypeName;
	}

	public void setInvTypeName(String invTypeName) {
		this.invTypeName = invTypeName;
	}

	public String getPtComment() {
		return ptComment;
	}

	public void setPtComment(String ptComment) {
		this.ptComment = ptComment;
	}

	public String getPtType() {
		return ptType;
	}

	public void setPtType(String ptType) {
		this.ptType = ptType;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public Date getAllowedPickDate() {
		return allowedPickDate;
	}

	public void setAllowedPickDate(Date allowedPickDate) {
		this.allowedPickDate = allowedPickDate;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Integer getSalesId() {
		return salesId;
	}

	public void setSalesId(Integer salesId) {
		this.salesId = salesId;
	}

	public Integer getCompanyIdBill() {
		return companyIdBill;
	}

	public void setCompanyIdBill(Integer companyIdBill) {
		this.companyIdBill = companyIdBill;
	}
}
