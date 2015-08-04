package com.giants3.hd.utils.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name="T_Xiankang")
public class Xiankang implements Serializable {


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;


    private long productId;




    /**
     * 镜子宽
     */
    private String jingzi_kuan;
    /**
     * 備註
     */
    private String beizhu;
    /**
     * 槽宽
     */
    private String caokuan;
    /**
     * 槽深
     */
    private String caoshen;
    /**
     * 挂距
     */
    private String guaju;
    /**
     * 画规  高
     */
    private String huangui_gao;
    /**
     * 画规  宽
     */
    private String huangui_kuan;

    /**
     * 玻璃规格  高
     */
    private String boliguige_gao;
    /**
     * 玻璃规格 宽
     */
    private String boliguige_kuan;

    /**
     * 材质
     */
    private String caizhi;
    /**
     * 边框
     */
    private String biankuang;
    /**
     * 磨边
     */
    private String mobian;
    /**
     * 其他同产品货号
     */
    private String qitahuohao;
    /**
     * 材质百分比
     */
    private String caizhibaifenbi;
    /**
     * 甲醛标记
     */
    private String jiaquan="NO";
    /**
     * 画芯编号
     */
    private String huaxinbianhao;
    /**
     * 画芯厂商
     */
    private String huaxinchangshang;
    /**
     * 画芯效果
     */
    private String huaxinxiaoguo;
    /**
     * 镜子规格	高
     */
    private String jingzi_gao;


    /**
     * 包装描述额外信息 会参与计算
     *
     * 前
     *
     */
    public float pack_front;

    /**
     * 包装描述额外信息 会参与计算
     *
     * 前后
     *
     */
    public float pack_front_back;

    /**
     * 包装描述额外信息 会参与计算
     *
     * 中间
     *
     */
    public float pack_middle;

    /**
     * 包装描述额外信息 会参与计算
     *
     * 六面
     *
     */
    public float pack_cube;

    /**
     * 包装描述额外信息 会参与计算
     *
     * 四周
     *
     */
    public float pack_perimeter;
    /**
     * 包装描述额外信息 会参与计算
     *
     * 描述
     *
     */
    public String pack_memo;




    public Xiankang() {
    }


    public String getJingzi_kuan() {
        return jingzi_kuan;
    }

    public void setJingzi_kuan(final String jingzi_kuan) {
        this.jingzi_kuan = jingzi_kuan;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(final String beizhu) {
        this.beizhu = beizhu;
    }

    public String getCaokuan() {
        return caokuan;
    }

    public void setCaokuan(final String caokuan) {
        this.caokuan = caokuan;
    }

    public String getCaoshen() {
        return caoshen;
    }

    public void setCaoshen(final String caoshen) {
        this.caoshen = caoshen;
    }

    public String getGuaju() {
        return guaju;
    }

    public void setGuaju(final String guaju) {
        this.guaju = guaju;
    }

    public String getHuangui_gao() {
        return huangui_gao;
    }

    public void setHuangui_gao(final String huangui_gao) {
        this.huangui_gao = huangui_gao;
    }

    public String getHuangui_kuan() {
        return huangui_kuan;
    }

    public void setHuangui_kuan(final String huangui_kuan) {
        this.huangui_kuan = huangui_kuan;
    }

    public String getBoliguige_gao() {
        return boliguige_gao;
    }

    public void setBoliguige_gao(final String boliguige_gao) {
        this.boliguige_gao = boliguige_gao;
    }

    public String getBoliguige_kuan() {
        return boliguige_kuan;
    }

    public void setBoliguige_kuan(final String boliguige_kuan) {
        this.boliguige_kuan = boliguige_kuan;
    }

    public String getCaizhi() {
        return caizhi;
    }

    public void setCaizhi(final String caizhi) {
        this.caizhi = caizhi;
    }

    public String getBiankuang() {
        return biankuang;
    }

    public void setBiankuang(final String biankuang) {
        this.biankuang = biankuang;
    }

    public String getMobian() {
        return mobian;
    }

    public void setMobian(final String mobian) {
        this.mobian = mobian;
    }

    public String getQitahuohao() {
        return qitahuohao;
    }

    public void setQitahuohao(final String qitahuohao) {
        this.qitahuohao = qitahuohao;
    }

    public String getCaizhibaifenbi() {
        return caizhibaifenbi;
    }

    public void setCaizhibaifenbi(final String caizhibaifenbi) {
        this.caizhibaifenbi = caizhibaifenbi;
    }

    public String getJiaquan() {
        return jiaquan;
    }

    public void setJiaquan(final String jiaquan) {
        this.jiaquan = jiaquan;
    }

    public String getHuaxinbianhao() {
        return huaxinbianhao;
    }

    public void setHuaxinbianhao(final String huaxinbianhao) {
        this.huaxinbianhao = huaxinbianhao;
    }

    public String getHuaxinchangshang() {
        return huaxinchangshang;
    }

    public void setHuaxinchangshang(final String huaxinchangshang) {
        this.huaxinchangshang = huaxinchangshang;
    }

    public String getHuaxinxiaoguo() {
        return huaxinxiaoguo;
    }

    public void setHuaxinxiaoguo(final String huaxinxiaoguo) {
        this.huaxinxiaoguo = huaxinxiaoguo;
    }

    public String getJingzi_gao() {
        return jingzi_gao;
    }

    public void setJingzi_gao(final String jingzi_gao) {
        this.jingzi_gao = jingzi_gao;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Xiankang)) return false;

        Xiankang xiankang = (Xiankang) o;

        if (id != xiankang.id) return false;
        if (productId != xiankang.productId) return false;
        if (Float.compare(xiankang.pack_front, pack_front) != 0) return false;
        if (Float.compare(xiankang.pack_front_back, pack_front_back) != 0) return false;
        if (Float.compare(xiankang.pack_middle, pack_middle) != 0) return false;
        if (Float.compare(xiankang.pack_cube, pack_cube) != 0) return false;
        if (Float.compare(xiankang.pack_perimeter, pack_perimeter) != 0) return false;
        if (jingzi_kuan != null ? !jingzi_kuan.equals(xiankang.jingzi_kuan) : xiankang.jingzi_kuan != null)
            return false;
        if (beizhu != null ? !beizhu.equals(xiankang.beizhu) : xiankang.beizhu != null) return false;
        if (caokuan != null ? !caokuan.equals(xiankang.caokuan) : xiankang.caokuan != null) return false;
        if (caoshen != null ? !caoshen.equals(xiankang.caoshen) : xiankang.caoshen != null) return false;
        if (guaju != null ? !guaju.equals(xiankang.guaju) : xiankang.guaju != null) return false;
        if (huangui_gao != null ? !huangui_gao.equals(xiankang.huangui_gao) : xiankang.huangui_gao != null)
            return false;
        if (huangui_kuan != null ? !huangui_kuan.equals(xiankang.huangui_kuan) : xiankang.huangui_kuan != null)
            return false;
        if (boliguige_gao != null ? !boliguige_gao.equals(xiankang.boliguige_gao) : xiankang.boliguige_gao != null)
            return false;
        if (boliguige_kuan != null ? !boliguige_kuan.equals(xiankang.boliguige_kuan) : xiankang.boliguige_kuan != null)
            return false;
        if (caizhi != null ? !caizhi.equals(xiankang.caizhi) : xiankang.caizhi != null) return false;
        if (biankuang != null ? !biankuang.equals(xiankang.biankuang) : xiankang.biankuang != null) return false;
        if (mobian != null ? !mobian.equals(xiankang.mobian) : xiankang.mobian != null) return false;
        if (qitahuohao != null ? !qitahuohao.equals(xiankang.qitahuohao) : xiankang.qitahuohao != null) return false;
        if (caizhibaifenbi != null ? !caizhibaifenbi.equals(xiankang.caizhibaifenbi) : xiankang.caizhibaifenbi != null)
            return false;
        if (jiaquan != null ? !jiaquan.equals(xiankang.jiaquan) : xiankang.jiaquan != null) return false;
        if (huaxinbianhao != null ? !huaxinbianhao.equals(xiankang.huaxinbianhao) : xiankang.huaxinbianhao != null)
            return false;
        if (huaxinchangshang != null ? !huaxinchangshang.equals(xiankang.huaxinchangshang) : xiankang.huaxinchangshang != null)
            return false;
        if (huaxinxiaoguo != null ? !huaxinxiaoguo.equals(xiankang.huaxinxiaoguo) : xiankang.huaxinxiaoguo != null)
            return false;
        if (jingzi_gao != null ? !jingzi_gao.equals(xiankang.jingzi_gao) : xiankang.jingzi_gao != null) return false;
        return !(pack_memo != null ? !pack_memo.equals(xiankang.pack_memo) : xiankang.pack_memo != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (productId ^ (productId >>> 32));
        result = 31 * result + (jingzi_kuan != null ? jingzi_kuan.hashCode() : 0);
        result = 31 * result + (beizhu != null ? beizhu.hashCode() : 0);
        result = 31 * result + (caokuan != null ? caokuan.hashCode() : 0);
        result = 31 * result + (caoshen != null ? caoshen.hashCode() : 0);
        result = 31 * result + (guaju != null ? guaju.hashCode() : 0);
        result = 31 * result + (huangui_gao != null ? huangui_gao.hashCode() : 0);
        result = 31 * result + (huangui_kuan != null ? huangui_kuan.hashCode() : 0);
        result = 31 * result + (boliguige_gao != null ? boliguige_gao.hashCode() : 0);
        result = 31 * result + (boliguige_kuan != null ? boliguige_kuan.hashCode() : 0);
        result = 31 * result + (caizhi != null ? caizhi.hashCode() : 0);
        result = 31 * result + (biankuang != null ? biankuang.hashCode() : 0);
        result = 31 * result + (mobian != null ? mobian.hashCode() : 0);
        result = 31 * result + (qitahuohao != null ? qitahuohao.hashCode() : 0);
        result = 31 * result + (caizhibaifenbi != null ? caizhibaifenbi.hashCode() : 0);
        result = 31 * result + (jiaquan != null ? jiaquan.hashCode() : 0);
        result = 31 * result + (huaxinbianhao != null ? huaxinbianhao.hashCode() : 0);
        result = 31 * result + (huaxinchangshang != null ? huaxinchangshang.hashCode() : 0);
        result = 31 * result + (huaxinxiaoguo != null ? huaxinxiaoguo.hashCode() : 0);
        result = 31 * result + (jingzi_gao != null ? jingzi_gao.hashCode() : 0);
        result = 31 * result + (pack_front != +0.0f ? Float.floatToIntBits(pack_front) : 0);
        result = 31 * result + (pack_front_back != +0.0f ? Float.floatToIntBits(pack_front_back) : 0);
        result = 31 * result + (pack_middle != +0.0f ? Float.floatToIntBits(pack_middle) : 0);
        result = 31 * result + (pack_cube != +0.0f ? Float.floatToIntBits(pack_cube) : 0);
        result = 31 * result + (pack_perimeter != +0.0f ? Float.floatToIntBits(pack_perimeter) : 0);
        result = 31 * result + (pack_memo != null ? pack_memo.hashCode() : 0);
        return result;
    }
}