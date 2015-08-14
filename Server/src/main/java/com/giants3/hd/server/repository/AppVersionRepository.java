package com.giants3.hd.server.repository;
//

import com.giants3.hd.utils.entity.AppVersion;
import com.giants3.hd.utils.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * 应用版本信息
* Created by davidleen29 on 2014/9/17.
*/
public interface AppVersionRepository extends JpaRepository<AppVersion,Long> {




    public AppVersion findFirstByAppNameEqualsOrderByVersionCodeDesc(String appName);



    public AppVersion findFirstByAppNameLikeOrderByVersionCodeDesc(String appName);

}
