package im.zhaojun.service;

import com.github.pagehelper.PageHelper;
import im.zhaojun.mapper.UserMapper;
import im.zhaojun.mapper.UserRoleMapper;
import im.zhaojun.model.Menu;
import im.zhaojun.model.User;
import im.zhaojun.util.TreeUtil;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Resource
    private UserMapper userMapper;

    @Resource
    private MenuService menuService;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private SessionDAO sessionDAO;


    public List<User> selectAllWithDept(int page, int rows) {
        PageHelper.startPage(page, rows);
        return userMapper.selectAllWithDept();
    }

    public Integer[] selectRoleIdsById(Integer userId) {
        return userMapper.selectRoleIdsByUserId(userId);
    }

    @Transactional
    public Integer add(User user, Integer[] roleIds) {
        String salt = generateSalt();
        String encryptPassword = new Md5Hash(user.getPassword(), salt).toString();

        user.setSalt(salt);
        user.setPassword(encryptPassword);
        userMapper.insert(user);

        grantRole(user.getUserId(), roleIds);

        return user.getUserId();
    }

    public void updateLastLoginTimeByUsername(String username) {
        userMapper.updateLastLoginTimeByUsername(username);
    }

    public boolean disableUserByID(Integer id) {
//        offlineByUserId(id); // 加上这段代码, 禁用用户后, 会将当前在线的用户立即踢出.
        return userMapper.updateStatusByPrimaryKey(id, 0) == 1;
    }

    public boolean enableUserByID(Integer id) {
        return userMapper.updateStatusByPrimaryKey(id, 1) == 1;
    }

    public boolean update(User user) {
        return userMapper.updateByPrimaryKeySelective(user) == 1;
    }

    @Transactional
    public boolean update(User user, Integer[] roleIds) {
        grantRole(user.getUserId(), roleIds);
        return userMapper.updateByPrimaryKeySelective(user) == 1;
    }

    public User selectOne(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    public boolean checkUserNameExist(String username) {
        return userMapper.countByUserName(username) > 0;
    }

    public void offlineBySessionId(String sessionId) {
        Session session = sessionDAO.readSession(sessionId);
        if (session != null) {
            log.debug("成功踢出 sessionId 为 :" + sessionId + "的用户.");
            session.stop();
            sessionDAO.delete(session);
        }
    }

    /**
     * 删除所有此用户的在线用户
     */
    public void offlineByUserId(Integer userId) {
        Collection<Session> activeSessions = sessionDAO.getActiveSessions();
        for (Session session : activeSessions) {
            SimplePrincipalCollection simplePrincipalCollection = (SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if (simplePrincipalCollection != null) {
                User user = (User) simplePrincipalCollection.getPrimaryPrincipal();
                if (user != null && userId.equals(user.getUserId())) {
                    offlineBySessionId(String.valueOf(session.getId()));
                }
            }
        }
    }

    @Transactional
    public void grantRole(Integer userId, Integer[] roleIds) {
        if (roleIds == null || roleIds.length == 0) {
            throw new IllegalArgumentException("赋予的角色数组不能为空.");
        }
        // 清空原有的角色, 赋予新角色.
        userRoleMapper.deleteUserRoleByUserId(userId);
        userRoleMapper.insertList(userId, roleIds);
    }

    public User selectByActiveCode(String activeCode) {
        return userMapper.selectByActiveCode(activeCode);
    }

    public int count() {
        return userMapper.count();
    }

    @Transactional
    public void delete(Integer userId) {
        userMapper.deleteByPrimaryKey(userId);
        userRoleMapper.deleteUserRoleByUserId(userId);
    }

    /**
     * 获取用户拥有的所有菜单权限和操作权限.
     * @param username      用户名
     * @return              权限标识符号列表
     */
    public Set<String> selectPermsByUsername(String username) {
        Set<String> perms = new HashSet<>();
        List<Menu> menuTreeVOS = menuService.selectMenuTreeVOByUsername(username);
        List<Menu> leafNodeMenuList = TreeUtil.getAllLeafNode(menuTreeVOS);
        for (Menu menu : leafNodeMenuList) {
            perms.add(menu.getPerms());
        }
        perms.addAll(userMapper.selectOperatorPermsByUserName(username));
        return perms;
    }

    public Set<String> selectRoleNameByUserName(String username) {
        return userMapper.selectRoleNameByUserName(username);
    }

    public User selectOneByUserName(String username) {
        return userMapper.selectOneByUserName(username);
    }

    public void updatePasswordByUserId(Integer userId, String password) {
        String salt = generateSalt();
        String encryptPassword = new Md5Hash(password, salt).toString();
        userMapper.updatePasswordByUserId(userId, encryptPassword, salt);
    }


    private String generateSalt() {
        return String.valueOf(System.currentTimeMillis());
    }
}