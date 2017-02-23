package com.project.entity.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * 实体通用图片库
 *
 * @author LiuDing 2014-5-18-下午02:27:48
 */
@Entity
@Table(name = "image_lib")
public class ImageLib implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 11)
    private Integer type;//类型(1-商品规格)
    @Column(length = 11)
    private Integer foreignKey; // 外键
    @Column(length = 250)
    private String imgPath;// 原图片路径
    @Column(length = 250)
    private String minImgPath;//小图(宽180)
    @Column(length = 250)
    private String mediumImgPath;//中等图片(宽600)
    @Column(length = 3)
    private Integer level;//排序
    @Column(length = 150)
    private String linkUrl;//链接地址
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getForeignKey() {
        return foreignKey;
    }

    public void setForeignKey(Integer foreignKey) {
        this.foreignKey = foreignKey;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getMinImgPath() {
        return minImgPath;
    }

    public void setMinImgPath(String minImgPath) {
        this.minImgPath = minImgPath;
    }

    public String getMediumImgPath() {
        return mediumImgPath;
    }

    public void setMediumImgPath(String mediumImgPath) {
        this.mediumImgPath = mediumImgPath;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
