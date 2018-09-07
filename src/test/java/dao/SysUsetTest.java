package dao;

import com.chuyu.awj.dao.sys.SysMenuDao;
import com.chuyu.awj.model.sys.SysUser;
import com.chuyu.awj.service.sys.SysUserService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.sql.SQLException;

/**
 * Created by Administrator on 2018/8/30.
 */
public class SysUsetTest extends Tester {

    private Logger logger = LoggerFactory.getLogger(SysUsetTest.class);

    @Resource
    private SysUserService sysUserService;

    @Test
    public void testQuery() throws SQLException {
        SysUser sysUser = sysUserService.queryByUserCode("admin");
        logger.info("朱玉平");
        System.out.println(sysUser);
    }

    @Resource
    private  SysMenuDao sysMenuDao;

    @Test
    public void testMap() throws SQLException {
        sysMenuDao.queryListByParentId(3);
    }
}
