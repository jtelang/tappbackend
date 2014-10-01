dataSource {
    pooled = true
    driverClassName = "com.mysql.jdbc.Driver"
    username = "cptjvmh_tapp"
    password = "jaishriram108*"
//    username = "root"
//    password = "dino"
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory' // Hibernate 3
//    cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory' // Hibernate 4
}

// environment specific settings
environments {
    development {
        dataSource {
            dbCreate = "none" // one of 'create', 'create-drop', 'update', 'validate', ''
  //         url = "jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
	     url = "jdbc:mysql://127.0.0.1/cptjvmh_tapp?useUnicode=yes&characterEncoding=UTF-8&?autoReconnect=true"

        }
    }
    test {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
        }
    }
    production {
        dataSource {
            dbCreate = "none"
            //url = "jdbc:h2:prodDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
            url = "jdbc:mysql://127.0.0.1/cptjvmh_tapp?useUnicode=yes&characterEncoding=UTF-8&?autoReconnect=true" 
	       properties {
               maxActive = -1
               minEvictableIdleTimeMillis=1800000
               timeBetweenEvictionRunsMillis=60000
               numTestsPerEvictionRun=3
               testOnBorrow=true
               testWhileIdle=true
               testOnReturn=false
               validationQuery="SELECT 1"
               jdbcInterceptors="ConnectionState"
            }
        }
    }
}
