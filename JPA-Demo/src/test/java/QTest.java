import cn.mrdear.entity.QTCity;
import cn.mrdear.entity.QUser;
import cn.mrdear.entity.User;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.dml.SQLInsertClause;
import com.sun.xml.internal.bind.v2.TODO;
import static org.assertj.core.api.Assertions.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Field;
import java.util.*;

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
    private static final Logger log = LoggerFactory.getLogger(QTest.class);
    @PersistenceContext
    private EntityManager em;

      /*@Autowired
        private SQLQueryFactory queryFactory;*/


    @Test
    @Transactional
    public  void tt() throws NoSuchFieldException, IllegalAccessException {
        QUser qUser=QUser.user;
        //添加
        User user=new User();
        user.setId(77);
        user.setName("lijiahui");
        user.setBirthday(new Date());
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

    @Test
    public void isnullTest()
    {
        QUser qUser = QUser.user;
        Tuple da=new  JPAQueryFactory(em).select(qUser.name,qUser.age).from(qUser).where(qUser.id.eq(34)).fetchOne();
        String ss=da.get(qUser.name);
        Integer aa=da.get(qUser.age);
        if (da.get(qUser.name)==null)
        {
            System.out.println();
        }
    }

    @Test
    @Transactional
    public void dateString()
    {
        QUser qUser=QUser.user;
        //添加
        User user=new User();
        user.setId(77);
        user.setName("lijiahui");
        user.setBirthday(new Date());
        em.persist(user);
        String dat=new  JPAQueryFactory(em).select(qUser.birthday.stringValue()).from(qUser).where(qUser.id.eq(77)).fetchOne();
        System.out.println(dat);
    }

    @Test
    public  void groupB()
    {
        //分组只能查出一条数据，所以常和聚合函数一起用
        QUser qUser=QUser.user;
        List<Tuple> a = new JPAQueryFactory(em).select(qUser.age.add(qUser.id).sum(),qUser.name,qUser.birthday).from(qUser)
                .where(qUser.name.eq("s"))
                //.groupBy(qUser.name)
                .fetch();
        log.error("-----------------------{}"/*,a.get(qUser.age.add(qUser.id).sum())*/);

    }
    @Test
    public void queryFactoryInsert()
    {
        //去重 distinct
        Date dd=new Date();
        System.out.println(dd.toString());
        QUser u=QUser.user;
        // SQLInsertClause insert = queryFactory.insert(u);
        List<User> s = new JPAQueryFactory(em).select(u).from(u).where(u.age.isNotNull())
                .distinct().fetch();
        System.out.println();

    }

    @Test
    @Rollback(false)
    @Transactional
    //是因为在junit下，插入数据会自动回滚，所以测试显示成功但实际上不能插入。若要插入到数据库，只需在测试方法上添加@Rollback(false)注解即可。
    public void prTT()
    {
        QUser qUser=QUser.user;
        User u=new  JPAQueryFactory(em).select(qUser).from(qUser).where(qUser.id.eq(34)).fetchFirst();
        u.setName("yangxing");
        List<User> u2=new  JPAQueryFactory(em).select(qUser).from(qUser).fetch();
        User s=em.find(User.class,1);
        s.setAge(1000);
        em.remove(s);
        //merge包括保存和更新 persist只是保存持久化
        // em.merge(s);
        em.persist(s);
        System.out.println(u2.toString());
        Assert.assertNotNull(u2);
    }

    @Test
    public void nqTT()
    {
        //测试空的数据会不会被进行条件删选，jap返回的值一律为对象，所以申明对象时，属性要为对象，如Interge
        QUser qUser=QUser.user;
        //ne eq等条件判断中不会包含删选字段为空的数据，如想包含需另加or判断
        List<User> u2=new  JPAQueryFactory(em).select(qUser).from(qUser)
                .where(qUser.age.ne(3).or( qUser.age.isNull()).or(qUser.name.isNotEmpty()))
                .fetch();
        Assert.assertNotNull(u2);
    }

    @Test
    public void normalTT()
    {
        QUser qUser=QUser.user;
        em.flush();
    }

    @Test
    @Transactional
    @Rollback(false)//设置结束后不回滚
    public void updateTT()
    {
        QUser qUser=QUser.user;
        new JPAQueryFactory(em).update(qUser).set(qUser.age,22).set(qUser.name,"22").where(qUser.id.eq(444)).execute();

    }

    @Test
    public void fetchfirst()
    {
        QUser qUser=QUser.user;
        Tuple r = new JPAQueryFactory(em).select(qUser.name,qUser.id).from(qUser).where(qUser.id.eq(431))
                .fetchFirst();
        assertThat(r).isNull();
    }

    /**
     * 删选最大值方法
     */
    @Test
    public void maxinTT()
    {
        QUser qUser=QUser.user;
        Integer ages=new JPAQueryFactory(em).select(qUser.age.max()).from(qUser).fetchFirst();
        assertThat(ages).isNull();

    }
}

