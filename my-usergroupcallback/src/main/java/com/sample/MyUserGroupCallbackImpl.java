package com.sample;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.jbpm.services.task.identity.AbstractUserGroupInfo;
import org.kie.api.task.UserGroupCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MyUserGroupCallbackImpl extends AbstractUserGroupInfo implements UserGroupCallback {

	private static final Logger logger = LoggerFactory.getLogger(MyUserGroupCallbackImpl.class);
    
    protected static final String DEFAULT_PROPERTIES_NAME = "classpath:/jbpm.usergroup.callback.properties";
    
    public static final String DS_JNDI_NAME = "db.ds.jndi.name";
    public static final String PRINCIPAL_QUERY = "db.user.query";
    public static final String USER_ROLES_QUERY = "db.user.roles.query";
    public static final String ROLES_QUERY = "db.roles.query";
	
    private Properties config;
    private DataSource ds; 
    
    public MyUserGroupCallbackImpl() {
		super();
		String propertiesLocation = System.getProperty("jbpm.usergroup.callback.properties");        
        config = readProperties(propertiesLocation, DEFAULT_PROPERTIES_NAME);
        init();
		
	}
    
    //no no-arg constructor to prevent cdi from auto deploy
    public MyUserGroupCallbackImpl(boolean activate) {
    	logger.info("construcotr called!!!!!!!!");
        String propertiesLocation = System.getProperty("jbpm.usergroup.callback.properties");        
        config = readProperties(propertiesLocation, DEFAULT_PROPERTIES_NAME);
        init();

    }
    
    public MyUserGroupCallbackImpl(Properties config) {
    	logger.info("construcotr called properties config!!!!!!!!");
        this.config = config;        
        init();

    }  	

	public boolean existsUser(String userId) {
		logger.info("exits user!!!!!!!!"+userId);
		if (userId == null) {
			throw new IllegalArgumentException("UserId cannot be null");
		}
		return checkExistence(this.config.getProperty(PRINCIPAL_QUERY), userId);
	}

	public boolean existsGroup(String groupId) {
		logger.info("exists group called!!!!!!!!"+groupId);
		if (groupId == null) {
			throw new IllegalArgumentException("GroupId cannot be null");
		}
		return checkExistence(this.config.getProperty(ROLES_QUERY), groupId);
	}

	public List<String> getGroupsForUser(String userId) {
		logger.info("groups for user!!!!!!!!"+userId);
		if (userId == null) {
			throw new IllegalArgumentException("UserId cannot be null");
		}
		
		List<String> roles = new ArrayList<String>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			logger.info("connection created!!!!!!!!"+conn);
			conn = ds.getConnection();

			ps = conn.prepareStatement(this.config.getProperty(USER_ROLES_QUERY));
			try {
				ps.setString(1, userId);
			} catch (ArrayIndexOutOfBoundsException ignore) {

			}
			rs = ps.executeQuery();
			while (rs.next()) {
				roles.add(rs.getString(1));
			}
		} catch (Exception e) {
			logger.error("Error when checking roles in db, parameter: " + userId, e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception ex) {
				}
			}
		}
		
		return roles;
	}
	
	protected Connection getConnection() throws SQLException {

		return ds.getConnection();
	}
	
	private void init() {
		logger.info("init method!!!!!!!!");
		if (this.config == null || !this.config.containsKey(DS_JNDI_NAME) || 
				!this.config.containsKey(PRINCIPAL_QUERY) || !this.config.containsKey(ROLES_QUERY) 
				|| !this.config.containsKey(USER_ROLES_QUERY)) {
			throw new IllegalArgumentException("All properties must be given ("+ DS_JNDI_NAME + ","
					+ PRINCIPAL_QUERY +"," + ROLES_QUERY +"," +USER_ROLES_QUERY +")");
		}
		String jndiName = this.config.getProperty(DS_JNDI_NAME, "java:/DefaultDS");
		try {
			InitialContext ctx = new InitialContext();
			
			ds = (DataSource) ctx.lookup(jndiName);

		} catch (Exception e) {
			throw new IllegalStateException("Can get data source for DB usergroup callback, JNDI name: " + jndiName, e);
		}
	}
	
	protected boolean checkExistence(String querySql, String parameter) {
		logger.info("check existence***********!!!!!!!!");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean result = false;
		try {
			conn = ds.getConnection();

			ps = conn.prepareStatement(querySql);
			
			ps.setString(1, parameter);

			rs = ps.executeQuery();
			if (rs.next()) {
				result = true;
			}
		} catch (Exception e) {
			logger.error("Error when checking user/group in db, parameter: " + parameter, e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception ex) {
				}
			}
		}
		
		
		return result;
	}
}
