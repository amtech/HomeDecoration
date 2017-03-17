package com.giants3.hd.server.repository;
//

import com.giants3.hd.server.entity.ProductEquationUpdateTemp;
import com.giants3.hd.server.entity.ProductPaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 产品公式更新临时表
 *
*/
public interface ProductEquationUpdateTempRepository extends JpaRepository<ProductEquationUpdateTemp,Long> {



}
