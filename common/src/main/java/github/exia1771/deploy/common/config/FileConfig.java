package github.exia1771.deploy.common.config;

import github.exia1771.deploy.common.props.FileProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableConfigurationProperties(FileProperties.class)
public class FileConfig {

}
