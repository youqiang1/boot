package com.yq.mybatis.mapper;

import com.yq.mybatis.entity.User;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * <p> 用户Mapper</p>
 * @author youq  2019/4/11 14:08
 */
@Repository
public interface UserMapper extends Mapper<User> {

    /**
     * <p> 根据id查询数据</p>
     * @param id id
     * @return com.yq.mybatis.entity.User
     * @author youq  2019/4/11 14:08
     */
    User findOne(Integer id);

    /**
     * <p> 保存</p>
     * @param user 用户对象
     * @author youq  2019/4/11 15:12
     */
    void save(User user);

    /**
     * <p> 删除</p>
     * @param id id
     * @author youq  2019/4/11 17:00
     */
    void deleteById(Integer id);

    /**
     * <p> 修改用户邮箱信息</p>
     * @param user id，email
     * @author youq  2019/4/11 17:10
     */
    void updateUserEmail(User user);

    /**
     * <p> 软删</p>
     * @param id id
     * @author youq  2019/4/11 17:12
     */
    void softDelete(Integer id);
    
}
