package com.giants3.hd.server.repository;

import com.giants3.hd.utils.entity.ProductPaint;
import com.giants3.hd.utils.entity.User;
import com.giants3.hd.utils.entity.Xiankang;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by david on 2015/11/5.
 */
public interface XiankangRepository  extends JpaRepository<Xiankang,Long> {


//    public List<Xiankang> findByProductIdEqualsOrderByItemIndexAsc(long productId);
}
