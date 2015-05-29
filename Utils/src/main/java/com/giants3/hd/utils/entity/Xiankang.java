package com.giants3.hd.utils.entity;

import javax.persistence.*;

@Entity(name="T_Xiankang")
public class Xiankang {


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;


    private long productId;

    /**
     * 镜子规格	高
     */

    private String jingzi_gap;

    private String jingzi_kuan;
    private String beizhu;
    private String caokuan;
    private String caoshen;
    private String guaju;
    private String huangui_gao;
    private String huangui_kuan;
    private String boliguige_gao;
    private String boliguige_kuan;
    private String caizhi;
    private String biankuang;
    private String mobian;
    private String qitahuohao;
    private String caizhibaifenbi;
    private String jiaquan;
    private String huaxinbianhao;
    private String huaxinchangshang;
    private String huaxinxiaoguo;
    private String jingzi_gao;

    public Xiankang() {
    }

    public String getJingzi_gap() {
        return jingzi_gap;
    }

    public void setJingzi_gap(final String jingzi_gap) {
        this.jingzi_gap = jingzi_gap;
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
}