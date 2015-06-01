package com.giants3.hd.server.repository;
//

import com.giants3.hd.utils.entity.Material;
import com.giants3.hd.utils.entity.Product;
import com.giants3.hd.utils.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
* 材料
 *
*/
public interface MaterialRepository extends JpaRepository<Material,Long> {


    public Page<Material> findByCodeLikeOrNameLike(String code,String name ,Pageable pageable);

    public Page<Material> findByNameLike( String codeOrName,Pageable pageable);

    public Material findByCodeEquals( String code);


}
