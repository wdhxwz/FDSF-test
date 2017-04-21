package org.csource.fastdfs.test;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

public class TestApp {

	/**
	 * 测试FDFS java客户端
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		 * 配置文件路径，需要是键值对，内部是使用Properties进行解析的 connect_timeout = 2
		 * network_timeout = 30 charset = UTF-8 http.tracker_http_port = 80
		 * http.anti_steal_token = no http.secret_key = FastDFS1234567890
		 * tracker_server = 192.168.77.132:22122 tracker_server =
		 * 192.168.77.133:22122
		 * 
		 * 多个tracker_server通过上面的配置只能取到最后一个配置
		 * 因为Properties是一个map，不会有重复的键值
		 */
		String config_file = "fdfs_client.properties";

		// 待上传的文件路径
		//String local_filename = "C:\\Users\\Administrator\\Desktop\\bbb.jpg";

		// 上传的文件的元数据
		NameValuePair[] meta_list;

		// 上传后文件的完整路径
		//String fileUrl = "";

		try {

			// 初始化配置
			ClientGlobal.init(config_file);

			meta_list = new NameValuePair[1];
			meta_list[0] = new NameValuePair("author", "wangdh");

			TrackerClient tracker = new TrackerClient();
			TrackerServer trackerServer = tracker.getConnection();
			StorageServer storageServer = null;
			StorageClient client = new StorageClient(trackerServer, storageServer);

			// 通过文件本地路径上传
			// 返回结果，第一个结果是group，第二个是文件保存的路径
			// group1
			// M00/00/00/wKhNhFj5sYmAMVVSAAI1agfYPTI560.jpg
//			String[] results = client.upload_file(local_filename, null, meta_list);
//			if (results != null) {
//				for (String result : results) {
//					System.out.println(result);
//				}
//			}

			// 通过文件流上传文件
//			String file_ext_name = "";
//			File f;
//			f = new File(local_filename);
//			int nPos = local_filename.lastIndexOf('.');
//			if (nPos > 0 && local_filename.length() - nPos <= ProtoCommon.FDFS_FILE_EXT_NAME_MAX_LEN + 1) {
//				file_ext_name = local_filename.substring(nPos + 1);
//			} else {
//				file_ext_name = null;
//			}
//			results = client.upload_file(null, f.length(), new UploadLocalFileSender(local_filename), file_ext_name,
//					meta_list);
//
			String groupName = "fos";
			String remoteFileName = "M00/00/83/CgEVg1jvXyeAeGYeAAA9HE8BoN0414.jpg";
//			if (results != null) {
//				for (String result : results) {
//					System.out.println(result);
//				}
//				groupName = results[0];
//				remoteFileName = results[1];
//				System.err.println("group_name: " + groupName + ", remote_filename: " + remoteFileName);
//			}
//
//			// 获取上传后文件的完整路径
//			fileUrl = "http://" + trackerServer.getInetSocketAddress().getAddress().getHostAddress();
//			if (ClientGlobal.g_tracker_http_port != 80) {
//				fileUrl += ":" + ClientGlobal.g_tracker_http_port;
//			}
//			fileUrl += "/" + groupName + StorageClient1.SPLIT_GROUP_NAME_AND_FILENAME_SEPERATOR + remoteFileName;
//			System.err.println("file url: " + fileUrl);
//
//			// 获取文件的信息
//			// 通过指定groupName和remote_file_name获取
//			// 输出结果如下：fileInfo=source_ip_addr = 192.168.77.132, file_size =
//			// 144746, create_timestamp = 2017-04-21 15:30:22, crc32 = 131611954
//			FileInfo fileInfo = client.get_file_info(groupName, remoteFileName);
//			System.err.println("fileInfo=" + fileInfo);

			// 下载文件
			// 通过指定groupName和remote_file_name下载并保存到指定目录
			// 返回0表示下载成功，否则失败
			// http://10.1.21.131/fos/M00/00/83/CgEVg1jvXyeAeGYeAAA9HE8BoN0414.jpg
			int errorCode = client.download_file(groupName, remoteFileName, 0, 0,
					"C:\\Users\\Administrator\\Desktop\\" + remoteFileName.replaceAll("/", "_"));
			if (errorCode == 0) {
				System.err.println("Download file success");
			} else {
				System.err.println("Download file fail, error no: " + errorCode);
			}

			// 删除文件
			// 通过指定groupName和remote_file_name进行文件删除
			// client.delete_file("group1","M00/00/00/wKhNhFj5sYmAMVVSAAI1agfYPTI560.jpg");

			// 获取文件元数据
			// 通过指定groupName和remote_file_name获取
//			NameValuePair[] metadatas = client.get_metadata(groupName, remoteFileName);
//			for (NameValuePair metadata : metadatas) {
//				System.err.println("metadata name:" + metadata.getName() + ",metadata value:" + metadata.getValue());
//			}

			// 关闭资源
			storageServer = tracker.getFetchStorage(trackerServer, groupName, remoteFileName);
			if (storageServer == null) {
				System.out.println("getFetchStorage fail, errno code: " + tracker.getErrorCode());
				return;
			}
			storageServer.close();
			trackerServer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
