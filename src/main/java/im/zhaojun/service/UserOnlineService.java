package im.zhaojun.service;

import im.zhaojun.model.User;
import im.zhaojun.model.UserOnline;
import im.zhaojun.util.IPUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserOnlineService {

    @Resource
    private SessionDAO sessionDAO;

    public List<UserOnline> list() {
        List<UserOnline> list = new ArrayList<>();
        Collection<Session> sessions = sessionDAO.getActiveSessions();
        for (Session session : sessions) {
            UserOnline userOnline = new UserOnline();
            if (session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) == null) {
                continue;
            } else {
                SimplePrincipalCollection principalCollection = (SimplePrincipalCollection) session
                        .getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                User user = (User) principalCollection.getPrimaryPrincipal();
                userOnline.setUsername(user.getUsername());
                userOnline.setUserId(user.getUserId());
            }
            userOnline.setId((String) session.getId());
            userOnline.setIp(IPUtils.getIpAddr());
            userOnline.setStartTimestamp(session.getStartTimestamp());
            userOnline.setLastAccessTime(session.getLastAccessTime());
            Long timeout = session.getTimeout();
            if (timeout == 0L) {
                userOnline.setStatus("离线");
            } else {
                userOnline.setStatus("在线");
            }
            userOnline.setTimeout(timeout);
            list.add(userOnline);
        }
        return list;
    }

    public void forceLogout(String sessionId) {
        Session session = sessionDAO.readSession(sessionId);
        if (session != null) {
            session.stop();
            session.stop();
            sessionDAO.delete(session);
        }
    }

    public int count() {
        int count = 0;
        Collection<Session> sessions = sessionDAO.getActiveSessions();
        for (Session session : sessions) {
            if (session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) != null) {
                count++;
            }
        }
        return count;
    }
}
