package cn.lqso.es.model;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用电账户
 * 
 * @author luojie
 * 2018年9月7日
 */
public class AccountInfo implements Serializable{
	private static final long serialVersionUID = -1997536760448132723L;
	
	/** 用户编号 */
	private String yhbh;
	
	/** 用户名称 */
	private String yhmc;
	
	/** 用点地址 */
	private String yddz;
	
	/** 抄表电量 ZYGCJDL*/
	private BigDecimal cbdl;
	
	/** 抄表行度 ZYGZM*/
	private String cbhd;
	
	/** 抄表日期 */
	private Date cbrq;
	
	/** 地区编码 */
	private String dqbm;
	
	/** 供电单位编码 */
	private String gddwbm;
	
	/** 用电类别代码 */
	private String ydlbdm;

	/** 用电容量 */
	private BigDecimal ydrl;

	/** 是否居民 */
	private Boolean isResident;

	private static final String JMYDLBDM = "500";

	public String getYdlbdm() {
		return ydlbdm;
	}

	public void setYdlbdm(String ydlbdm) {
		this.ydlbdm = ydlbdm;
	}
	
	public Boolean getIsResident() {
		if(StringUtils.isNotEmpty(ydlbdm)) {
			if(JMYDLBDM.equals(ydlbdm)) {
				isResident = Boolean.TRUE;
			}else {
				isResident = Boolean.FALSE;
			}
		}
		return isResident;
	}

	public BigDecimal getYdrl() {
		return ydrl;
	}

	public void setYdrl(BigDecimal ydrl) {
		this.ydrl = ydrl;
	}

	public void setIsResident(Boolean isResident) {
		this.isResident = isResident;
	}

	public String getDqbm() {
		return dqbm;
	}

	public void setDqbm(String dqbm) {
		this.dqbm = dqbm;
	}

	public String getGddwbm() {
		return gddwbm;
	}

	public void setGddwbm(String gddwbm) {
		this.gddwbm = gddwbm;
	}

	public Date getCbrq() {
		return cbrq;
	}
	
	public void setCbrq(Date cbrq) {
		this.cbrq = cbrq;
	}

	public String getYhbh() {
		return yhbh;
	}

	public void setYhbh(String yhbh) {
		this.yhbh = yhbh;
	}

	public String getYhmc() {
		return yhmc;
	}

	public void setYhmc(String yhmc) {
		this.yhmc = yhmc;
	}

	public String getYddz() {
		return yddz;
	}

	public void setYddz(String yddz) {
		this.yddz = yddz;
	}

	public BigDecimal getCbdl() {
		return cbdl;
	}

	public void setCbdl(BigDecimal cbdl) {
		this.cbdl = cbdl;
	}

	public String getCbhd() {
		return cbhd;
	}

	public void setCbhd(String cbhd) {
		this.cbhd = cbhd;
	}

	@Override
	public String toString() {
		return "AccountInfo{" +
				"yhbh='" + yhbh + '\'' +
				", yhmc='" + yhmc + '\'' +
				", yddz='" + yddz + '\'' +
				", cbdl=" + cbdl +
				", cbhd='" + cbhd + '\'' +
				", cbrq=" + cbrq +
				", dqbm='" + dqbm + '\'' +
				", gddwbm='" + gddwbm + '\'' +
				", ydlbdm='" + ydlbdm + '\'' +
				", ydrl=" + ydrl +
				", isResident=" + isResident +
				'}';
	}
}