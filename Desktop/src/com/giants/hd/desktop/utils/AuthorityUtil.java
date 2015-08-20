package com.giants.hd.desktop.utils;

import com.giants.hd.desktop.api.CacheManager;
import com.giants3.hd.utils.entity.Authority;
import com.giants3.hd.utils.entity.Module;
import com.giants3.hd.utils.entity.User;

import java.util.List;

/** 权限判断
 * Created by davidleen29 on 2015/8/8.
 */
public class AuthorityUtil {



    private static AuthorityUtil authorityUtil=new AuthorityUtil();
    public static AuthorityUtil getInstance()
    {
        return authorityUtil;
    }



    /**
     * 查看模块是否有查看权限
     * @param moduleName
     * @return
     */
    private boolean isViewable(String moduleName)
    {
        if(CacheManager.getInstance().bufferData.loginUser.name.equals(User.ADMIN))
            return true;
        for(Authority authority:getAuthority())
        {
            if(moduleName.equals(authority.module.name)&&authority.viewable)
            {
                return true;
            }

        }


        return false;
    }


    /**
     * 查看模块是否有添加的权限
     * @param moduleName
     * @return
     */
    private boolean isAddable(String moduleName)
    {
        if(CacheManager.getInstance().bufferData.loginUser.name.equals(User.ADMIN))
            return true;
        for(Authority authority:getAuthority())
        {
            if(moduleName.equals(authority.module.name)&&authority.addable)
            {
                return true;
            }

        }
        return false;
    }


    /**
     * 查看模块是否有查看权限
     * @param moduleName
     * @return
     */
    private boolean editable(String moduleName)
    {
        if(CacheManager.getInstance().bufferData.loginUser.name.equals(User.ADMIN))
            return true;
        for(Authority authority:getAuthority())
        {
            if(moduleName.equals(authority.module.name)&&authority.editable)
            {
                return true;
            }

        }


        return false;
    }


    /**
     * 查看模块是否有查看权限
     * @param moduleName
     * @return
     */
    private boolean deletable(String moduleName)
    {
        if(CacheManager.getInstance().bufferData.loginUser.name.equals(User.ADMIN))
            return true;
        for(Authority authority:getAuthority())
        {
            if(moduleName.equals(authority.module.name)&&authority.deletable)
            {
                return true;
            }

        }


        return false;
    }

    /**
     * 查看模块是否有查看权限
     * @param moduleName
     * @return
     */
    private boolean exportable(String moduleName)
    {
        if(CacheManager.getInstance().bufferData.loginUser.name.equals(User.ADMIN))
            return true;
        for(Authority authority:getAuthority())
        {
            if(moduleName.equals(authority.module.name)&&authority.exportable)
            {
                return true;
            }

        }


        return false;
    }


    public List<Authority> getAuthority()
    {
        return CacheManager.getInstance().bufferData.authorities;
    }

    public boolean viewProductModule()
    {


        return viewProductList()||viewMaterialList();
    }

    public boolean viewProductList()
    {



        return isViewable(Module.NAME_PRODUCT);

    }


        public boolean viewMaterialList()
        {



            return isViewable(Module.NAME_MATERIAL);
        }






    /**
     * 查看报价模块
     * @return
     */
    public boolean viewQuotationModule() {



        return isViewable(Module.NAME_QUOTATION);
    }


    /**
     * 查看基础数据模块
     * @return
     */
    public boolean viewBaseDataModule() {
        return viewProcessList()||viewMaterialClassList()||viewCustomerList();

    }


    /**
     * 查看工序列表
     * @return
     */
    public boolean viewProcessList()
    {

        return isViewable(Module.NAME_PROCESS);
    }



    /**
     * 查看工序列表
     * @return
     */
    public boolean viewCustomerList()
    {


        return isViewable(Module.NAME_CUSTOMER);

    }


    /**
     * 查看材料类型列表
     * @return
     */
    public boolean viewMaterialClassList()
    {

        return isViewable(Module.NAME_MATERIAL_CLASS);


    }


    /**
     * 查看权限模块
     */
    public boolean viewAuthorityModule()
    {


        return viewUserList()||viewModuleList()||viewAuthorityList();

    }


    /**
     * 查看用户列表
     */

    public boolean viewUserList()
    {

      return   isViewable(Module.NAME_USER);
    }


    /**
     * 查看模块列表
     */

    public boolean viewModuleList()
    {

        return   isViewable(Module.NAME_MODULE);
    }

    /**
     * 查看模块列表
     */

    public boolean viewAuthorityList()
    {

        return   isViewable(Module.NAME_AUTHORITY);
    }


    /**
     * 系统模块是否可视
     * @return
     */
    public boolean viewSystemModule() {

        return viewSyncData()||viewPictureUpload();
    }

    /**
     * 同步数据 菜单
     * @return
     */
    public boolean viewSyncData()
    {

        return isViewable(Module.NAME_SYNC_DATA);
    }


    /**
     * 图片上传菜单
     * @return
     */
    public boolean viewPictureUpload()
    {



        return isViewable(Module.NAME_PICTURE_UPLOAD);
    }


    /**
     * 是否有添加产品的权限
     * @return
     */
    public boolean addProduct() {


        return isAddable(Module.NAME_PRODUCT);


    }

    /**
     * 添加材料的权限
     * @return
     */
    public boolean addMaterial() {
        return isAddable(Module.NAME_MATERIAL);

    }


    /**
     * 添加报价单的权限
     * @return
     */
    public boolean addQuotation() {


        return isAddable(Module.NAME_QUOTATION);


    }

    public boolean editProduct() {

        return editable(Module.NAME_PRODUCT);
    }


    public boolean deleteProduct() {

        return deletable(Module.NAME_PRODUCT);
    }

    public boolean exportProduct() {
        return exportable(Module.NAME_PRODUCT);
    }

    public boolean editQuotation() {

        return editable(Module.NAME_QUOTATION);
    }

    public boolean exportQuotation() {

        return exportable(Module.NAME_QUOTATION);
    }

    public boolean deleteQuotation() {

        return deletable(Module.NAME_QUOTATION);
    }

    public boolean editMaterial() {

        return editable(Module.NAME_MATERIAL);
    }

    public boolean deleteMaterial() {

        return deletable(Module.NAME_MATERIAL);
    }
}