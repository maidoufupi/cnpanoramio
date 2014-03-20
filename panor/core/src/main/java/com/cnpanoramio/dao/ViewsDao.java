package com.cnpanoramio.dao;

import java.util.Date;
import java.util.List;

import org.appfuse.dao.GenericDao;

import com.cnpanoramio.domain.Views;

public interface ViewsDao extends GenericDao<Views, Views.ViewsPK> {
	
	/**
	 * 通过appId的应用程序进行了查看photoId记录计数,以每天为单位计数
	 * 
	 * @param photoId
	 * @param appId
	 */
	public void view(Long photoId, String appId);
	
	/**
	 * 获取图片被appId查看过的次数
	 * 
	 * @param photoId
	 * @param appId
	 * @return
	 */
	public int getViewsCount(Long photoId, String appId);
	
	/**
	 * 获取图片被查看过的次数
	 * 
	 * @param photoId
	 * @return
	 */
	public int getViewsCount(Long photoId);
	
	/**
	 * 获取图片被查看次数的以日期为单位的列表
	 * 
	 * @param photoId
	 * @param appId
	 * @return
	 */
	public List<Views> getViewsList(Long photoId, String appId);
	
	/**
	 * 获取图片被查看次数的以日期为单位的列表
	 * 
	 * @param photoId
	 * @return
	 */
	public List<Views> getViewsList(Long photoId);
	
	/**
	 * 获取图片某日的访问量列表
	 * 
	 * @param photoId
	 * @return
	 */
	public List<Views> getViewsList(Long photoId, Date date);
	
	/**
	 * 获取图片某天的某应用程序的访问量
	 * 
	 * @param photoId
	 * @param appId
	 * @param date
	 * @return
	 */
	public Views getViews(Long photoId, String appId, Date date);
	
	/**
	 * 获取图片某天的访问总量
	 * 
	 * @param photoId
	 * @param date
	 * @return
	 */
	public int getViewsCount(Long photoId, Date date);
}
