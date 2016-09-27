package mx.uaemex

import grails.transaction.Transactional
import com.jcraft.jsch.*

@Transactional
class DocumentService {
	
	def grailsApplication

	Session session
	String username
	String password
	String host
	Integer port

  void connectSFTP(String username, String password, String host, int port) throws JSchException, IllegalAccessException {
		if (!this.session  || !this.session.isConnected()) {
			JSch jsch = new JSch()
			this.session = jsch.getSession(username, host, port)
			this.session.setPassword(password)
			this.session.setConfig("StrictHostKeyChecking", "no")
			this.session.connect()
		} else {
			throw new IllegalAccessException("Sesion ya iniciada.")
		}
	}

	void uploadFile(String localFilePath,String remoteFilePath){
		getProperties()
		connectSFTP(username, password, host, port)
		ChannelSftp sftpChannel
		try {
			sftpChannel = (ChannelSftp) session.openChannel("sftp")
			sftpChannel.connect()
			sftpChannel.put(localFilePath,remoteFilePath)
		} catch (SftpException | JSchException ex) {
			throw new IOException(ex)
		} finally {
			if (sftpChannel != null) {
				sftpChannel.disconnect()
				disconnectSFTP()
			}
		}
	}
	
	void downloadFile(String remoteFilePath,String localFilePath){
		getProperties()
		connectSFTP(username, password, host, port)
		ChannelSftp sftpChannel
		try {
			sftpChannel = (ChannelSftp) session.openChannel("sftp")
			sftpChannel.connect()
			sftpChannel.get(remoteFilePath,localFilePath)
		} catch (SftpException | JSchException ex) {
			throw new IOException(ex)
		} finally {
			if (sftpChannel != null) {
				sftpChannel.disconnect()
				disconnectSFTP()
			}
		}
	}

	void getProperties(){
		username = grailsApplication.config.sftp.username
		password = grailsApplication.config.sftp.password
		host = grailsApplication.config.sftp.host
		port = new Integer(grailsApplication.config.sftp.port)
	}

	void disconnectSFTP(){
		this.session.disconnect()
	}

}