package com.project.service.common.impl;

import com.project.core.proxy.ProxyFactory;
import com.project.entity.common.ImageLib;
import com.project.service.common.IImageLibService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("imageLibService")
public class ImageLibServiceImpl implements IImageLibService {

    @SuppressWarnings("unchecked")
	public List<ImageLib> list(Integer accountId, Integer entityType,
                               Integer dataId, boolean DESC, int max) {
        List<Object> params = new ArrayList<Object>();
        String hql = "from ImageLib where accountId = ? ";
        params.add(accountId);
        if (null != entityType) {
            hql += " and type = ? ";
            params.add(entityType);
        }
        if (null != dataId) {
            hql += " and foreignKey = ? ";
            params.add(dataId);
        }
        if (DESC) {
            hql += " order by id DESC ";
        }
        List<ImageLib> list = ProxyFactory.baseService.findByHql(
                ImageLib.class, hql, params, 0, max);
        return list;
    }

    public int addReturnID(ImageLib entity) {
        return ProxyFactory.baseService.addReturnID(ImageLib.class.getName(),
                entity);
    }

    @SuppressWarnings("unchecked")
	public List<ImageLib> queryByIdAndType(Integer id, Integer entiyType) {
        try {
            String hql = "from ImageLib where foreignKey=? and type = ? ";
            return ProxyFactory.baseService.findByHql(ImageLib.class, hql, 0,
                    0, id, entiyType);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
	public List<ImageLib> queryByFkAndType(Integer foreignKey, Integer type, Integer start, Integer max) {
        try {
            String hql = "from ImageLib where foreignKey=? and type = ?";
            return ProxyFactory.baseService.findByHql(ImageLib.class, hql,
                    start, max, foreignKey, type);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
	public List<ImageLib> queryByType(Integer entiyType) {
        try {
            String hql = "from ImageLib where type=? order by level";
            return ProxyFactory.baseService.findByHql(ImageLib.class, hql, 0,
                    0, entiyType);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    public boolean delete(Integer type) {
        String sql = "delete from image_lib where type = " + type;
        return ProxyFactory.baseService.delete(sql);
    }

    public boolean saveOrUpdate(ImageLib imageLib) {
        return ProxyFactory.baseService.saveOrUpdate(ImageLib.class.getName(),
                imageLib);
    }

    public ImageLib queryById(Integer id) {
        return (ImageLib) ProxyFactory.baseService.findById(
                ImageLib.class.getName(), id);
    }

    public boolean deleteById(Integer id) {
        String sql = "delete from image_lib where id = " + id;
        return ProxyFactory.baseService.delete(sql);
    }

    public ImageLib queryOneByFkAndType(Integer foreignKey, Integer type) {
        String hql = "from ImageLib where foreignKey=? and type=?";
        return (ImageLib) ProxyFactory.baseService.findObjectByHql(ImageLib.class, hql, foreignKey, type);
    }

    public void deleteByFkIdAndType(Integer fkId, Integer type) {
        String hql = "delete from ImageLib where foreignKey=? and type=?";
        ProxyFactory.baseService.deleteByHql(hql, fkId, type);
    }

    @Override
	public boolean addByPaths(String imgPaths, int fkid, int type)  throws RuntimeException{
		try {
			//删除
			deleteByFkIdAndType(fkid, type);
			if(StringUtils.isBlank(imgPaths)){
				return false;
			}
			//添加
			String[] arr = imgPaths.split(",");
			List<ImageLib> list = new ArrayList<ImageLib>();
			for(String path : arr){
				ImageLib image = new ImageLib();
				image.setForeignKey(fkid);
				image.setType(type);
				image.setCreateDate(new Date());
				image.setImgPath(path);
				image.setMediumImgPath(path);
				image.setMinImgPath(path);
				list.add(image);
			}
			return ProxyFactory.baseService.addByList(ImageLib.class.getName(), list);
		} catch (RuntimeException e) {
			throw e;
		}
	}

    @SuppressWarnings("unchecked")
	@Override
	public List<ImageLib> queryByFkAndType(String foreignKeyIds, Integer type,
			Integer start, Integer max) {
		try {
            String hql = "from ImageLib where foreignKey in ("+foreignKeyIds+") and type = ?";
            return ProxyFactory.baseService.findByHql(ImageLib.class, hql,
                    start, max, type);
        } catch (RuntimeException e) {
            throw e;
        }
	}
}
