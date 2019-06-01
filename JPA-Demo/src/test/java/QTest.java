import cn.mrdear.entity.QTCity;
import cn.mrdear.entity.QUser;
import cn.mrdear.entity.User;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sun.xml.internal.bind.v2.TODO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/****
 * <pre>类名: QTest</pre>
 * <pre>描述: </pre>
 * <pre>版权: lijiahui</pre>
 * <pre>日期: 2019/5/22 10:33</pre>
 * @author lijiahui
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext.xml")
public class QTest {

    @PersistenceContext
    private EntityManager em;

    @Test
    @Transactional
    public  void tt() throws NoSuchFieldException, IllegalAccessException {
        QUser qUser=QUser.user;
        //添加
        User user=new User();
        user.setId(77);
        user.setName("lijiahui");
        em.persist(user);
        //删除execute()只是返回个结果，不加这个也能生效
        Long idD=new JPAQueryFactory(em).delete(qUser).where(qUser.age.lt(18)).execute();
        //更新
        Long idU=new JPAQueryFactory(em).update(qUser).set(qUser.age,2).where(qUser.name.eq("lijiahui")).execute();
        User ss=em.find(User.class,user.getId());
        System.out.println(ss.getName());
        //查询
        //fetchone只能用于返回单个结果，如果多个里返回一个只能用fetchfirst
        List<Tuple> tuples=new JPAQueryFactory(em).select(qUser.name,qUser.age).from(qUser).where(qUser.age.gt(18)).fetch();
        Tuple da=new  JPAQueryFactory(em).select(qUser.name,qUser.age).from(qUser).where(qUser.name.eq("li")).fetchOne();
        for(Tuple tuple:tuples)
        {
            String name=tuple.get(qUser.name);
            System.out.println();
        }
        if(da.get(qUser.age)!=null)
        {
            System.out.println();
        }
        Map<String, Integer> items = new HashMap<>();
        items.put("A", 10);
        items.put("B", 20);
        Field[] fields = user.getClass().getDeclaredFields();
        String f=fields[0].getName();
        Field mapField = user.getClass().getDeclaredField("name");
        mapField.setAccessible(true);
        Object na=mapField.get(user);
        System.out.println(na);
    }


}
