package bug.squashers.RestAPI.manager;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class CacheConfig {

    @Bean
    @Primary
    public CacheManager cacheManagerActivities(){
        return new ConcurrentMapCacheManager("community-activities");
    }
    @Bean
    public CacheManager cacheManagerChildren() {
        return new ConcurrentMapCacheManager("children");
    }

}
