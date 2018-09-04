package dao;

import com.chuyu.awj.service.sys.SysMenuService;
import org.junit.Test;

import javax.annotation.Resource;
import java.sql.SQLException;

/**
 * Created by Administrator on 2018/9/3.
 */
public class SysMenuTest extends Tester{

    @Resource
    private SysMenuService sysMenuService;

    @Test
    public void test() throws SQLException {
        System.out.println(null instanceof Number);
        sysMenuService.queryListParentId();
    }


}
