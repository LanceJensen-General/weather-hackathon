package com.att.cdo.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Level;

import com.att.cdo.input.ftp.FTPPullConfiguration;
import com.att.cdo.trait.Logging;

public class FTPService implements Logging {

	private static final int BUFFER_SIZE = 1024;
	
	public void pull(FTPPullConfiguration weatherPullConfiguration) {
		
		getLogger().info("Begining data pull with the following configuration:" + weatherPullConfiguration.toString());
		
		File outputDirectory = weatherPullConfiguration.getOutputDirectory();
		if( outputDirectory == null) {
			outputDirectory = new File(".");
		}
		
		
		if(weatherPullConfiguration.getPullFile() != null) {
			URL fileToDownload = weatherPullConfiguration.getPullFile();
			try {
				downlodFile(fileToDownload, outputDirectory);
			} catch (IOException e) {
				getLogger().log(Level.SEVERE, "An error occured while downloading " + fileToDownload.toString() + ".", e);
				System.exit(1);
			}
		}
		
		if(weatherPullConfiguration.getPullFilesFromUrlList() != null) {
			List<String> urls = null;
			File urlInputFile = weatherPullConfiguration.getPullFilesFromUrlList();
			try {
				urls = Files.readAllLines(urlInputFile.toPath());
			} catch (IOException e) {
				getLogger().log(Level.SEVERE, "An issue occured while reading the url list " + urlInputFile.getAbsolutePath() + ".", e);
				System.exit(1);
			}
			
			for(String urlString : urls) {
				try {
					URL urlToDownload = new URL(urlString);
					downlodFile(urlToDownload, outputDirectory);
				} catch (IOException e) {
					getLogger().log(Level.SEVERE, "An error occured while downloading " + urlString + ".", e);
					e.printStackTrace();
					System.exit(1);
				}
			}
		}
		
		getLogger().info("Data pull completed successfully.");
		
	}
	
	private void downlodFile(URL fileToDownload, File outputDirectory) throws IOException {
		URLConnection connection = getConnection(fileToDownload);
		getLogger().info("Connection to " + fileToDownload + " established.");
		String fileName = new File(fileToDownload.getFile()).getName();
		File outputFile = new File(outputDirectory, fileName);
		if(!outputFile.exists()) {
			outputFile.createNewFile();
		}
		
		BufferedOutputStream outputFileStream = new BufferedOutputStream(new FileOutputStream(outputFile));
		InputStream ftpInputStream = connection.getInputStream();
		byte[] inputBuffer = new byte[BUFFER_SIZE];
		int bytesRead = 0;
		while( (bytesRead = ftpInputStream.read(inputBuffer)) != -1 ) {
			outputFileStream.write(inputBuffer, 0, bytesRead);
		}
		
		outputFileStream.close();
		ftpInputStream.close();
		getLogger().info("Data successfully written to " + outputFile.getAbsolutePath() + ".");
		
	}
	

	private URLConnection getConnection(URL fileToDownload) throws IOException {
		if(System.getProperty("ftp.proxyHost") == null) {
			System.setProperty("ftp.proxyHost", "one.proxy.att.com");
		}
		if(System.getProperty("ftp.proxyPort") == null) {
			System.setProperty("ftp.proxyPort", "8080");
		}
		return fileToDownload.openConnection();
	}

}
