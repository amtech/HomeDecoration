package com.giants3.hd.utils.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 报价权限
 */
@Entity(name="T_QuoteAuth")
public class QuoteAuth  implements Serializable{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public long id;

}
