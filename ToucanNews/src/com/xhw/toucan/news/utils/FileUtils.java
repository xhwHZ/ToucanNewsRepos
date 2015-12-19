package com.xhw.toucan.news.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import android.os.Environment;

import com.lidroid.xutils.util.IOUtils;
import com.xhw.toucan.news.application.BaseApplication;

public class FileUtils
{
	// 该应用保存缓存数据的文件夹
	private final static String APP_CACHE_DIR = "ToucanNews";

	// 缓存json数据的文件夹名称
	private final static String TAG_JSON_CACHE = "JsonCache";
	
	private final static String TAG_DOWNLOAD="Download";
	
	// 缓存图片的文件夹名称
	private final static String TAG_IMG_CACHE="ImgCache";

	
	/**
	 * 获取图片缓存路径
	 * @return
	 */
	public static String getImgCacheDir()
	{
		return getFileDir(TAG_IMG_CACHE).getAbsolutePath();
	}
	
	/**
	 * 获取文件下载路径
	 * @return
	 */
	public static String getDownloadDir()
	{
		return getFileDir(TAG_DOWNLOAD).getAbsolutePath();
	}
	
	/**
	 * 返回SD卡的可用状态
	 * 
	 * @return
	 */
	private static boolean isSdcardCanUse()
	{
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED) ? true : false;
	}

	/**
	 * 根据标记，返回你所需要的可用的缓存文件夹
	 * 
	 * @param tag
	 *            传入TAG_JSON_CACHE，返回jsoncache的缓存目录,待补充
	 * @return
	 */
	private static File getFileDir(String tag)
	{
		StringBuilder sb = new StringBuilder();
		// sd卡可用，就保存在SD卡
		if (isSdcardCanUse())
		{
			String sdcardDir = Environment.getExternalStorageDirectory()
					.getAbsolutePath();
			sb.append(sdcardDir);
		} else
		// SD卡不可用，就保存在cache目录
		{
			String cacheDir = BaseApplication.getAppliction().getCacheDir()
					.getAbsolutePath();
			sb.append(cacheDir);
		}
		sb.append(File.separator);
		sb.append(APP_CACHE_DIR);
		sb.append(File.separator);
		sb.append(tag);
		File fileDir = new File(sb.toString());
		if (!fileDir.exists())
		{
			fileDir.mkdirs();
		}
		return fileDir;

	}

	
	/**
	 * 将json数据保存为本地缓存文件
	 * @param fileName json文件名
	 * @param fileData json内容
	 */
	public static void writeJsonToCache(String fileName, String fileData)
	{
		File file = new File(getFileDir(TAG_JSON_CACHE), fileName);
		BufferedWriter bw = null;
		try
		{
			bw = new BufferedWriter(new FileWriter(file));
			//先写入过期时间在缓存文件的第一行，作为缓存是否过时的依据(半小时钟后，缓存过时)
			bw.write(System.currentTimeMillis()+1000*30*60+"");
			bw.newLine();
			bw.write(fileData);
			bw.flush();
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			IOUtils.closeQuietly(bw);
		}
	}
	
	/**
	 * 读取json缓存文件
	 * @param fileName
	 * @return 若文件不存在，或者文件过期，返回Null
	 */
	public static String readJsonFromCache(String fileName)
	{
		File file=new File(getFileDir(TAG_JSON_CACHE), fileName);
		if(!file.exists())
		{
			return null;
		}
		StringWriter sw = new StringWriter();
		BufferedReader br=null;
		String line=null;
		try
		{
			br=new BufferedReader(new FileReader(file));
			//第一行
			line=br.readLine();
			long outDate=Long.parseLong(line);
			//过期了
			if(System.currentTimeMillis()>outDate)
			{
				return null;
			}else{//没过期
				while((line=br.readLine())!=null)
				{
					sw.write(line);
				}
				return sw.toString();
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}finally{
			IOUtils.closeQuietly(br);
			IOUtils.closeQuietly(sw);
		}
	}
	
	/**
	 * 读取json缓存文件，请使用readJsonFromCache方法
	 * @param fileName
	 * @return 若文件不存在，或者文件过期，返回Null
	 */
	@Deprecated
	public static String readJsonFromCache2(String fileName)
	{
		File file=new File(getFileDir(TAG_JSON_CACHE), fileName);
		String retResult=null;
		if(!file.exists())
		{
			return retResult;
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		BufferedReader br=null;
		boolean isReadFirstLine=true;
		boolean isCacheCanUse=false;
		try
		{
			br=new BufferedReader(new FileReader(file));
			String line=null;
			while((line=br.readLine())!=null)
			{
				//第一行，过期时间处理
				if(isReadFirstLine)
				{
					isReadFirstLine=false;
					//获取过期时间
					long outdateTime=Long.parseLong(line);
					//已经过期了，返回null，要求重新请求服务器
					if(System.currentTimeMillis()>outdateTime)
					{
						retResult=null;
						break;
					}else//还没过期，这行不要了，读取后面的数据返回
					{
						isCacheCanUse=true;
						continue;
					}
				}
				
				//其余行读取返回
				bos.write(line.getBytes("utf-8"));
			}
			if(isCacheCanUse)
			{
				retResult=bos.toString();
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}finally{
			IOUtils.closeQuietly(br);
			IOUtils.closeQuietly(bos);
		}
		return retResult;
	}
	
	/** 从输入流中读取文本返回 此方法会关闭输入流
	 * 
	 * @param in
	 * @return
	 */
	public static String in2String(InputStream in)
	{

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		int len = 0;
		byte[] buffer = new byte[1024];

		try
		{
			while ((len = in.read(buffer)) != -1)
			{
				bos.write(buffer, 0, len);
			}
			String text = bos.toString();
			return text;
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally
		{
			if (bos != null)
			{
				try
				{
					bos.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				} finally
				{
					bos = null;
				}
			}

			if (in != null)
			{
				try
				{
					in.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				} finally
				{
					in = null;
				}
			}
		}

	}
}
