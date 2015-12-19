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
	// ��Ӧ�ñ��滺�����ݵ��ļ���
	private final static String APP_CACHE_DIR = "ToucanNews";

	// ����json���ݵ��ļ�������
	private final static String TAG_JSON_CACHE = "JsonCache";
	
	private final static String TAG_DOWNLOAD="Download";
	
	// ����ͼƬ���ļ�������
	private final static String TAG_IMG_CACHE="ImgCache";

	
	/**
	 * ��ȡͼƬ����·��
	 * @return
	 */
	public static String getImgCacheDir()
	{
		return getFileDir(TAG_IMG_CACHE).getAbsolutePath();
	}
	
	/**
	 * ��ȡ�ļ�����·��
	 * @return
	 */
	public static String getDownloadDir()
	{
		return getFileDir(TAG_DOWNLOAD).getAbsolutePath();
	}
	
	/**
	 * ����SD���Ŀ���״̬
	 * 
	 * @return
	 */
	private static boolean isSdcardCanUse()
	{
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED) ? true : false;
	}

	/**
	 * ���ݱ�ǣ�����������Ҫ�Ŀ��õĻ����ļ���
	 * 
	 * @param tag
	 *            ����TAG_JSON_CACHE������jsoncache�Ļ���Ŀ¼,������
	 * @return
	 */
	private static File getFileDir(String tag)
	{
		StringBuilder sb = new StringBuilder();
		// sd�����ã��ͱ�����SD��
		if (isSdcardCanUse())
		{
			String sdcardDir = Environment.getExternalStorageDirectory()
					.getAbsolutePath();
			sb.append(sdcardDir);
		} else
		// SD�������ã��ͱ�����cacheĿ¼
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
	 * ��json���ݱ���Ϊ���ػ����ļ�
	 * @param fileName json�ļ���
	 * @param fileData json����
	 */
	public static void writeJsonToCache(String fileName, String fileData)
	{
		File file = new File(getFileDir(TAG_JSON_CACHE), fileName);
		BufferedWriter bw = null;
		try
		{
			bw = new BufferedWriter(new FileWriter(file));
			//��д�����ʱ���ڻ����ļ��ĵ�һ�У���Ϊ�����Ƿ��ʱ������(��Сʱ�Ӻ󣬻����ʱ)
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
	 * ��ȡjson�����ļ�
	 * @param fileName
	 * @return ���ļ������ڣ������ļ����ڣ�����Null
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
			//��һ��
			line=br.readLine();
			long outDate=Long.parseLong(line);
			//������
			if(System.currentTimeMillis()>outDate)
			{
				return null;
			}else{//û����
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
	 * ��ȡjson�����ļ�����ʹ��readJsonFromCache����
	 * @param fileName
	 * @return ���ļ������ڣ������ļ����ڣ�����Null
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
				//��һ�У�����ʱ�䴦��
				if(isReadFirstLine)
				{
					isReadFirstLine=false;
					//��ȡ����ʱ��
					long outdateTime=Long.parseLong(line);
					//�Ѿ������ˣ�����null��Ҫ���������������
					if(System.currentTimeMillis()>outdateTime)
					{
						retResult=null;
						break;
					}else//��û���ڣ����в�Ҫ�ˣ���ȡ��������ݷ���
					{
						isCacheCanUse=true;
						continue;
					}
				}
				
				//�����ж�ȡ����
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
	
	/** ���������ж�ȡ�ı����� �˷�����ر�������
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
