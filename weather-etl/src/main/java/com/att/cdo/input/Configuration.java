package com.att.cdo.input;

import com.att.cdo.input.etl.ETLConfiguration;
import com.att.cdo.input.ftp.FTPPullConfiguration;
import com.att.cdo.input.join.JoinConfiguration;

public class Configuration {

	private FTPPullConfiguration weatherPullConfiguration;
	private ETLConfiguration etlConfiguration;
	private JoinConfiguration tableJoinConfiguration;
	
	public JoinConfiguration getTableJoinConfiguration() {
		return tableJoinConfiguration;
	}
	public void setTableJoinConfiguration(JoinConfiguration tableJoinConfiguration) {
		this.tableJoinConfiguration = tableJoinConfiguration;
	}
	public FTPPullConfiguration getWeatherPullConfiguration() {
		return weatherPullConfiguration;
	}
	public void setWeatherPullConfiguration(FTPPullConfiguration weatherPullConfiguration) {
		this.weatherPullConfiguration = weatherPullConfiguration;
	}
	public ETLConfiguration getEtlConfiguration() {
		return etlConfiguration;
	}
	public void setEtlConfiguration(ETLConfiguration etlConfiguration) {
		this.etlConfiguration = etlConfiguration;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((etlConfiguration == null) ? 0 : etlConfiguration.hashCode());
		result = prime * result + ((tableJoinConfiguration == null) ? 0 : tableJoinConfiguration.hashCode());
		result = prime * result + ((weatherPullConfiguration == null) ? 0 : weatherPullConfiguration.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Configuration other = (Configuration) obj;
		if (etlConfiguration == null) {
			if (other.etlConfiguration != null)
				return false;
		} else if (!etlConfiguration.equals(other.etlConfiguration))
			return false;
		if (tableJoinConfiguration == null) {
			if (other.tableJoinConfiguration != null)
				return false;
		} else if (!tableJoinConfiguration.equals(other.tableJoinConfiguration))
			return false;
		if (weatherPullConfiguration == null) {
			if (other.weatherPullConfiguration != null)
				return false;
		} else if (!weatherPullConfiguration.equals(other.weatherPullConfiguration))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Configuration [weatherPullConfiguration=" + weatherPullConfiguration + ", etlConfiguration="
				+ etlConfiguration + ", tableJoin=" + tableJoinConfiguration + "]";
	}
}
