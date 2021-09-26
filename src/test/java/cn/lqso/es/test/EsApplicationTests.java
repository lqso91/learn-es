package cn.lqso.es.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import cn.lqso.es.model.AccountInfo;
import cn.lqso.es.service.EsService;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EsApplicationTests {
    @Autowired
    private EsService esService;

    @Test
    public void test() {
        List<AccountInfo> list = esService.queryAccountInfo("安桂华", "", "040100", true, 20);
        for (AccountInfo ai : list) {
            System.out.println(ai);
        }
    }
}
