import cn.mrdear.entity.QTCity;
import cn.mrdear.entity.QUser;
import cn.mrdear.entity.User;
import cn.mrdear.utils.DateUtilsExt;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.dml.SQLInsertClause;
import com.sun.xml.internal.bind.v2.TODO;

import static com.querydsl.core.types.Ops.DateTimeOps.CURRENT_DATE;
import static org.assertj.core.api.Assertions.*;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.assertj.core.util.DateUtil;
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
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        log.error("-----------------------{}",a.get(0));

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

    /*    *
     * 删选最大值方法*/

    @Test
    public void maxinTT()
    {
        QUser qUser=QUser.user;
        Integer ages=new JPAQueryFactory(em).select(qUser.age.max()).from(qUser).fetchFirst();
        assertThat(ages).isNull();

    }

    /*    *
     * 测试日期比较
     * jpa querydsl中格式化日期*/

    @Test
    public void dateTT()
    {
        QUser qUser=QUser.user;
        //转日期
        String dateString="2019-06-06";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date= LocalDate.parse(dateString, formatter);
        Date ds = DateUtilsExt.localDateToDate(date);
        String dateString2="2019-06-06 23:59:59";
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate date2= LocalDate.parse(dateString2, formatter2);
        Date ds2 = DateUtilsExt.localDateToDate(date2);
        //方法1：StringTemplate DATE_FORMAT为mysql中用法
        // ,oracle中 to_date('2004-05-07 13:23:44','yyyy-mm-dd hh24:mi:ss') to_char(sysdate,'dd')
        String queryDate = DateFormatUtils.format(new Date(),"yyyy-MM-dd");

        //建立格式化模板，这里相当于oracle语句DATE_FORMAT( ,'%Y-%m-%d')
        StringTemplate dateExprOracle = Expressions
                .stringTemplate("TO_CHAR({0},'yyyy-mm-dd')",qUser.birthday);

        //建立格式化模板，这里相当于mysql语句DATE_FORMAT( ,'%Y-%m-%d')
        StringTemplate dateExpr = Expressions
                .stringTemplate("DATE_FORMAT({0},'%Y-%m-%d')",qUser.birthday);
        List<Tuple> s = new JPAQueryFactory(em)
                .select(qUser.id,qUser.birthday )
                .from(qUser)
                .where(dateExpr.eq(dateString))
                .orderBy(qUser.id.asc())
                .fetch();
        //方法2：普通eq
        //当日期为时间搓时，普通方法无法准确使用eq查询某天，
        List<Tuple> s2 = new JPAQueryFactory(em)
                .select(qUser.id.shortValue(),qUser.birthday,qUser.name )
                .from(qUser)
                .where(qUser.birthday.eq(ds))
                .orderBy(qUser.id.asc())
                .fetch();
        //方法3：between
        List<Tuple> s3 = new JPAQueryFactory(em)
                .select(qUser.id,qUser.birthday )
                .from(qUser)
                .where(qUser.birthday.between(ds,ds2))
                .orderBy(qUser.id.asc())
                .fetch();
        assertThat(s).isNull();
    }

    /*   *
     * 字符转Number
     * qUser.name.castToNum(Integer.class)
     * 万物皆可Template模板化*/

    @Test
    public void stringToNumber()
    {
        QUser qUser=QUser.user;
        //方法1：NumberTemplate
        NumberTemplate numberExpr =Expressions
                .numberTemplate(Integer.class,"CONVERT({0},UNSIGNED)",qUser.name);
        List<Tuple> s1 = new JPAQueryFactory(em)
                .select(qUser.name,qUser.birthday )
                .from(qUser)
                .orderBy(numberExpr.desc())
                .fetch();

        //方法2：castToNum
        List<Tuple> s2 = new JPAQueryFactory(em)
                .select(qUser.name,qUser.birthday )
                .from(qUser)
                .orderBy(qUser.name.castToNum(Integer.class).desc())
                .fetch();
        assertThat(s1).isNull();

    }

    /**
     * 测试java的日期LocalDate等映射数据库类型
     * 事实证明jpa还是不能正常映射LocalDate
     *  JPA底层还是Hibernate，要使用Java8的LocalDate得引入hibernate-java8依赖。
     *参考https://blog.csdn.net/mn960mn/article/details/53141366
     * 应用场景
     * 现在希望把createDate映射成表的date类型->LocalDate，createTime映射成datetime类型->LocalDateTime
     */
    @Test
    public void dateTT2()
    {
        QUser qUser=QUser.user;
        Date s1 = new JPAQueryFactory(em)
                .select(qUser.birthday )
                .from(qUser)
                .where(qUser.name.eq("4"))
                .fetchFirst();
        System.out.println(s1);

    }

    @Test
    @Transactional
    public void ddtt()
    {
        QUser qUser=QUser.user;
        User s1 = new JPAQueryFactory(em)
                .select(qUser )
                .from(qUser)
                .orderBy(qUser.birthday.desc())
                .fetchFirst();
        System.out.println(s1);
    }

    /**
     * 测试sum函数会不会使多条查询结果变成一条结果
     * 结果：变成一条了
     */
    @Test
    @Transactional
    public void ss1()
    {
        //
        QUser qUser=QUser.user;
        List<User> s1 = new JPAQueryFactory(em)
                .select(qUser)
                .from(qUser)
                .where(qUser.name.eq("3dde"))
                .fetch();
        //测试查不到结果返回的是null还是空list->>>>结果是空list
        List<Tuple> s2 = new JPAQueryFactory(em)
                .select(qUser.age,qUser.name)
                .from(qUser)
                .where(qUser.name.eq("3dde"))
                .fetch();
        for (Tuple e : s2)
        {

        }

        List<Tuple> s3 = new JPAQueryFactory(em)
                .select(qUser.age,qUser.name)
                .from(qUser)
                .groupBy(qUser.name)
                .fetch();
        System.out.println(String.format("INN_GLP_FOR_CPS_P_%s.dat","dddd"));
    }

    /**
     * JPA中的eq方法不能为null
     * 大于小于判断一律不包含空null的数据
     * //小于等于 loe
     * //大于等于 goe
     */
    @Test
    public void test()  {
        Date s=null;
        if (s==null)
        {
            try {
                String s1= "2019-07-19";
                s= DateUtils.parseDate(s1,"yyyy-MM-dd");
            }catch (ParseException e)
            {
            }
        }
        QUser qUser=QUser.user;
        String s333=null;
        List<Tuple> s1 = new JPAQueryFactory(em)
                .select(qUser.age,qUser.name)
                .from(qUser)
                //小于等于 loe
                //大于等于 goe
                .where(qUser.birthday.loe(s)/*,
                        qUser.name.eq(s333)*/,
                        qUser.age.goe(3))
                .fetch();
        System.out.println();
    }

    /**
     * 测试.isEmpty() isNull()
     * oracle中空的字段默认为null,mysql中空的字段默认为 ””
     * 所以oracle中jpa判断为空为isnull,mysql中jpa判断为空为isEmpty
     */
    @Test
    public void isEmpty()
    {
        QUser qUser =QUser.user;
        //测试查不到结果返回的是null还是空list->>>>结果是空list
        List<Tuple> s2 = new JPAQueryFactory(em)
                .select(qUser.age,qUser.name)
                .from(qUser)
                .where(qUser.name.isEmpty())
                .fetch();
        org.springframework.util.Assert.notNull(s2,"查到数据了");
    }

    @Test
    public void is2Empty()
    {
        QUser qUser =QUser.user;
        //测试groupby会不会过滤空值,结果：不会忽略
        List<Integer> s2 = new JPAQueryFactory(em)
                .select(qUser.age)
                .from(qUser)
                .groupBy(qUser.age)
                .fetch();
        System.out.println(s2);
    }

}

