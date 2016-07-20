package com.itdoes.business.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "employeemaster")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EmployeeMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer employeeId;

	private String loginId;

	private Integer deptId;

	private Integer managerId;

	private String password;

	private String status;

	private String lastName;

	private String firstName;

	private String middleName;

	private Date hireDate;

	private Date termDate;

	private String sex;

	private String email;

	private String address;

	private String city;

	private String province;

	private String postalCode;

	private String phone;

	private String cellular;

	private String sin;

	private String jobTitle;

	private Integer entryId;

	private Date entryDate;

	private Double salary;

	private String img;

	private Integer vacationDay;

	private Integer sickDay;

	private Integer currencyId;

	private String countryCode;

	private String phoneOffice;

	private String phoneExt;

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public Integer getManagerId() {
		return managerId;
	}

	public void setManagerId(Integer managerId) {
		this.managerId = managerId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public Date getHireDate() {
		return hireDate;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}

	public Date getTermDate() {
		return termDate;
	}

	public void setTermDate(Date termDate) {
		this.termDate = termDate;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCellular() {
		return cellular;
	}

	public void setCellular(String cellular) {
		this.cellular = cellular;
	}

	public String getSin() {
		return sin;
	}

	public void setSin(String sin) {
		this.sin = sin;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public Integer getEntryId() {
		return entryId;
	}

	public void setEntryId(Integer entryId) {
		this.entryId = entryId;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public Integer getVacationDay() {
		return vacationDay;
	}

	public void setVacationDay(Integer vacationDay) {
		this.vacationDay = vacationDay;
	}

	public Integer getSickDay() {
		return sickDay;
	}

	public void setSickDay(Integer sickDay) {
		this.sickDay = sickDay;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getPhoneOffice() {
		return phoneOffice;
	}

	public void setPhoneOffice(String phoneOffice) {
		this.phoneOffice = phoneOffice;
	}

	public String getPhoneExt() {
		return phoneExt;
	}

	public void setPhoneExt(String phoneExt) {
		this.phoneExt = phoneExt;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
