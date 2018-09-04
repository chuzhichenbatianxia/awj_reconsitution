package com.chuyu.awj.model.sys;

import java.io.Serializable;
import java.util.List;

public class SysMenu implements Serializable {
    /**
     * 自增主键
     */
    private Integer menuId;

    /**
     * 上级菜单ID
     */
    private Integer parentId;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单地址
     */
    private String url;

    /**
     * 打开方式
     */
    private String target;

    /**
     * 权限代码
     */
    private String perms;

    /**
     * 菜单类型，0：目录，1：页面，2：按钮
     */
    private Integer type;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 数据版本号
     */
    private Integer version;

    /**
     * 排序
     */
    private Integer orderNo;

    private List<SysMenu> subMenu;
    /*
    父菜单名称
     */
    private String parentName;

    /**
     * 自增主键
     * @return menuId 自增主键
     */
    public Integer getMenuId() {
        return menuId;
    }

    /**
     * 自增主键
     * @param menuId 自增主键
     */
    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    /**
     * 上级菜单ID
     * @return parentId 上级菜单ID
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * 上级菜单ID
     * @param parentId 上级菜单ID
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * 菜单名称
     * @return name 菜单名称
     */
    public String getName() {
        return name;
    }

    /**
     * 菜单名称
     * @param name 菜单名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 菜单地址
     * @return url 菜单地址
     */
    public String getUrl() {
        return url;
    }

    /**
     * 菜单地址
     * @param url 菜单地址
     */
    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    /**
     * 权限代码
     * @return perms 权限代码
     */
    public String getPerms() {
        return perms;
    }

    /**
     * 权限代码
     * @param perms 权限代码
     */
    public void setPerms(String perms) {
        this.perms = perms == null ? null : perms.trim();
    }

    /**
     * 菜单类型，0：目录，1：页面，2：按钮
     * @return type 菜单类型，0：目录，1：页面，2：按钮
     */
    public Integer getType() {
        return type;
    }

    /**
     * 菜单类型，0：目录，1：页面，2：按钮
     * @param type 菜单类型，0：目录，1：页面，2：按钮
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 菜单图标
     * @return icon 菜单图标
     */
    public String getIcon() {
        return icon;
    }

    /**
     * 菜单图标
     * @param icon 菜单图标
     */
    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

    /**
     * 数据版本号
     * @return version 数据版本号
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * 数据版本号
     * @param version 数据版本号
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * 排序
     * @return orderNo 排序
     */
    public Integer getOrderNo() {
        return orderNo;
    }

    /**
     * 排序
     * @param orderNo 排序
     */
    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public List<SysMenu> getSubMenu()
    {
        return subMenu;
    }

    public void setSubMenu(List<SysMenu> subMenu)
    {
        this.subMenu = subMenu;
    }

    public String getTarget()
    {
        return target;
    }

    public void setTarget(String target)
    {
        this.target = target;
    }

    public String getParentName()
    {
        return parentName;
    }

    public void setParentName(String parentName)
    {
        this.parentName = parentName;
    }
}