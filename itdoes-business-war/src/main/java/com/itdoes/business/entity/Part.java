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
@Table(name = "part")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Part extends BaseEntity {
	private static final long serialVersionUID = 4012160670257552155L;

	@Id
	@Column(name = "skuno")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer skuNo;

	@Column(name = "partno", length = 50, nullable = false)
	private String partNo;

	@Column(name = "companyid")
	private Integer companyId;

	@Column(name = "vendpartno", length = 80)
	private String vendPartNo;

	@Column(name = "partdesc", length = 80)
	private String partDesc;

	@Column(name = "parttype", length = 15)
	private String partType;

	@Column(name = "productlineid")
	private Integer productLineId;

	@Column(name = "partdesclong", length = 250)
	private String partDescLong;

	@Column(name = "barcode", length = 20)
	private String barCode;

	@Column(name = "upccode", length = 50)
	private String upcCode;

	@Column(name = "abccode", length = 20)
	private String abcCode;

	@Column(name = "img", length = 50)
	private String img;

	@Column(name = "imgbig", length = 50)
	private String imgBig;

	@Column(name = "unit", length = 10)
	private String unit;

	@Column(name = "length")
	private Integer length;

	@Column(name = "width")
	private Integer width;

	@Column(name = "height")
	private Integer height;

	@Column(name = "grossweight")
	private Double grossWeight;

	@Column(name = "netweight")
	private Double netWeight;

	@Column(name = "status", length = 5)
	private String status;

	@Column(name = "avecost")
	private Double aveCost;

	@Column(name = "unitprice")
	private Double unitPrice;

	@Column(name = "avgorderqty")
	private Integer avgOrderQty;

	@Column(name = "minqty")
	private Integer minQty;

	@Column(name = "maxqty")
	private Integer maxQty;

	@Column(name = "minorderqty")
	private Integer minOrderQty;

	@Column(name = "maxorderqty")
	private Integer maxOrderQty;

	@Column(name = "gst")
	private Double gst;

	@Column(name = "pst")
	private Double pst;

	@Column(name = "leadtime")
	private Integer leadTime;

	@Column(name = "htscode", length = 20)
	private String htsCode;

	@Column(name = "entrydate")
	private Date entryDate;

	@Column(name = "entryid")
	private Integer entryID;

	@Column(name = "stock", length = 1)
	private String stock;

	@Column(name = "unitqty")
	private Integer unitQty;

	@Column(name = "param", length = 1)
	private String param;

	@Column(name = "prodtype", length = 2)
	private String prodType;

	@Column(name = "skidqty")
	private Integer skidQty;

	@Column(name = "specpdf", length = 50)
	private String specPdf;

	@Column(name = "barcodesuffix", length = 4)
	private String barCodeSuffix;

	@Column(name = "lastcost")
	private Double lastCost;

	@Column(name = "warrantydays")
	private Integer warrantyDays;

	@Column(name = "lastpoprice")
	private Double lastPOPrice;

	@Column(name = "cube")
	private Double cube;

	public Integer getSkuNo() {
		return skuNo;
	}

	public void setSkuNo(Integer skuNo) {
		this.skuNo = skuNo;
	}

	public String getPartNo() {
		return partNo;
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getVendPartNo() {
		return vendPartNo;
	}

	public void setVendPartNo(String vendPartNo) {
		this.vendPartNo = vendPartNo;
	}

	public String getPartDesc() {
		return partDesc;
	}

	public void setPartDesc(String partDesc) {
		this.partDesc = partDesc;
	}

	public String getPartType() {
		return partType;
	}

	public void setPartType(String partType) {
		this.partType = partType;
	}

	public Integer getProductLineId() {
		return productLineId;
	}

	public void setProductLineId(Integer productLineId) {
		this.productLineId = productLineId;
	}

	public String getPartDescLong() {
		return partDescLong;
	}

	public void setPartDescLong(String partDescLong) {
		this.partDescLong = partDescLong;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getUpcCode() {
		return upcCode;
	}

	public void setUpcCode(String upcCode) {
		this.upcCode = upcCode;
	}

	public String getAbcCode() {
		return abcCode;
	}

	public void setAbcCode(String abcCode) {
		this.abcCode = abcCode;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getImgBig() {
		return imgBig;
	}

	public void setImgBig(String imgBig) {
		this.imgBig = imgBig;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Double getGrossWeight() {
		return grossWeight;
	}

	public void setGrossWeight(Double grossWeight) {
		this.grossWeight = grossWeight;
	}

	public Double getNetWeight() {
		return netWeight;
	}

	public void setNetWeight(Double netWeight) {
		this.netWeight = netWeight;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getAveCost() {
		return aveCost;
	}

	public void setAveCost(Double aveCost) {
		this.aveCost = aveCost;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Integer getAvgOrderQty() {
		return avgOrderQty;
	}

	public void setAvgOrderQty(Integer avgOrderQty) {
		this.avgOrderQty = avgOrderQty;
	}

	public Integer getMinQty() {
		return minQty;
	}

	public void setMinQty(Integer minQty) {
		this.minQty = minQty;
	}

	public Integer getMaxQty() {
		return maxQty;
	}

	public void setMaxQty(Integer maxQty) {
		this.maxQty = maxQty;
	}

	public Integer getMinOrderQty() {
		return minOrderQty;
	}

	public void setMinOrderQty(Integer minOrderQty) {
		this.minOrderQty = minOrderQty;
	}

	public Integer getMaxOrderQty() {
		return maxOrderQty;
	}

	public void setMaxOrderQty(Integer maxOrderQty) {
		this.maxOrderQty = maxOrderQty;
	}

	public Double getGst() {
		return gst;
	}

	public void setGst(Double gst) {
		this.gst = gst;
	}

	public Double getPst() {
		return pst;
	}

	public void setPst(Double pst) {
		this.pst = pst;
	}

	public Integer getLeadTime() {
		return leadTime;
	}

	public void setLeadTime(Integer leadTime) {
		this.leadTime = leadTime;
	}

	public String getHtsCode() {
		return htsCode;
	}

	public void setHtsCode(String htsCode) {
		this.htsCode = htsCode;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public Integer getEntryID() {
		return entryID;
	}

	public void setEntryID(Integer entryID) {
		this.entryID = entryID;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	public Integer getUnitQty() {
		return unitQty;
	}

	public void setUnitQty(Integer unitQty) {
		this.unitQty = unitQty;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getProdType() {
		return prodType;
	}

	public void setProdType(String prodType) {
		this.prodType = prodType;
	}

	public Integer getSkidQty() {
		return skidQty;
	}

	public void setSkidQty(Integer skidQty) {
		this.skidQty = skidQty;
	}

	public String getSpecPdf() {
		return specPdf;
	}

	public void setSpecPdf(String specPdf) {
		this.specPdf = specPdf;
	}

	public String getBarCodeSuffix() {
		return barCodeSuffix;
	}

	public void setBarCodeSuffix(String barCodeSuffix) {
		this.barCodeSuffix = barCodeSuffix;
	}

	public Double getLastCost() {
		return lastCost;
	}

	public void setLastCost(Double lastCost) {
		this.lastCost = lastCost;
	}

	public Integer getWarrantyDays() {
		return warrantyDays;
	}

	public void setWarrantyDays(Integer warrantyDays) {
		this.warrantyDays = warrantyDays;
	}

	public Double getLastPOPrice() {
		return lastPOPrice;
	}

	public void setLastPOPrice(Double lastPOPrice) {
		this.lastPOPrice = lastPOPrice;
	}

	public Double getCube() {
		return cube;
	}

	public void setCube(Double cube) {
		this.cube = cube;
	}
}
