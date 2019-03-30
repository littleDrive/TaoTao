import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.junit.Test;

public class FTP_Test {

	@Test
	public void test() throws Exception{
		
		String host = "192.168.159.200";
		String username = "ftpuser";
		String password = "ftpuser";
		
		//1.连接服务器
		FTPClient ftpClient = new FTPClient();
		ftpClient.connect(host);
		
		//2.登陆
		ftpClient.login(username, password);
		
		//3.读取本地文件
		FileInputStream fileInputStream = new FileInputStream(new File("D:/Picture/ykls10.jpg"));
		
		//4.上传文件
		
		//指定上传目录
		ftpClient.changeWorkingDirectory("/home/ftpuser/www/images");
		//指定文件类型
		ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
		//第一个参数：文件在远程服务器的名字
		//第二个参数：文件流
		ftpClient.storeFile("test.jpg", fileInputStream);
		//退出登录
		ftpClient.logout();
	}
}
