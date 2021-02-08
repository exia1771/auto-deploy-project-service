package github.exia1771.deploy.core.entity;

import github.exia1771.deploy.core.abs.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class NetworkSetting extends AbstractEntity {

    private Network networks;

    @Data
    static class Network {
        private Bridge bridge;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    static class Bridge extends AbstractEntity {
        private String gateway;
        private String iPAddress;
        private String macAddress;
    }

}
