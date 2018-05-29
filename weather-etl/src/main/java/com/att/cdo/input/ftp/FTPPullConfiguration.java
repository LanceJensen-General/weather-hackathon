package com.att.cdo.input.ftp;

import java.io.File;
import java.net.URL;

public class FTPPullConfiguration {

	private URL pullFile;
	private File pullFilesFromUrlList;
	private File outputDirectory;
	
	public URL getPullFile() {
		return pullFile;
	}
	public void setPullFile(URL pullFile) {
		this.pullFile = pullFile;
	}
	public File getPullFilesFromUrlList() {
		return pullFilesFromUrlList;
	}
	public void setPullFilesFromUrlList(File pullFilesFromUrlList) {
		this.pullFilesFromUrlList = pullFilesFromUrlList;
	}
	public File getOutputDirectory() {
		return outputDirectory;
	}
	public void setOutputDirectory(File outputDirectory) {
		this.outputDirectory = outputDirectory;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((outputDirectory == null) ? 0 : outputDirectory.hashCode());
		result = prime * result + ((pullFile == null) ? 0 : pullFile.hashCode());
		result = prime * result + ((pullFilesFromUrlList == null) ? 0 : pullFilesFromUrlList.hashCode());
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
		FTPPullConfiguration other = (FTPPullConfiguration) obj;
		if (outputDirectory == null) {
			if (other.outputDirectory != null)
				return false;
		} else if (!outputDirectory.equals(other.outputDirectory))
			return false;
		if (pullFile == null) {
			if (other.pullFile != null)
				return false;
		} else if (!pullFile.equals(other.pullFile))
			return false;
		if (pullFilesFromUrlList == null) {
			if (other.pullFilesFromUrlList != null)
				return false;
		} else if (!pullFilesFromUrlList.equals(other.pullFilesFromUrlList))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "WeatherPullConfiguration [pullFile=" + pullFile + ", pullFilesFromUrlList=" + pullFilesFromUrlList
				+ ", outputDirectory=" + outputDirectory + "]";
	}
	
	
	
	
}
