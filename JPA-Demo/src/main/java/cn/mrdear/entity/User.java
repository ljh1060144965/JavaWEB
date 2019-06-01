package cn.mrdear.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/****
 * <pre>类名: User</pre>
 * <pre>描述: </pre>
 * <pre>版权: lijiahui</pre>
 * <pre>日期: 2019/5/22 10:37</pre>
 * @author lijiahui
 */

@Entity
@Table(name = "user", schema = "test", catalog = "")
public class User implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "name")
    private  String name;

    @Column(name = "age")
    private int age;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
