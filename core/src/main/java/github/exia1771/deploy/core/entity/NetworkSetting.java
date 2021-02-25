package github.exia1771.deploy.core.entity;

import github.exia1771.deploy.core.abs.AbstractWebEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class NetworkSetting extends AbstractWebEntity {

    private Network networks;

    @Data
    static class Network {
        private Bridge bridge;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    static class Bridge extends AbstractWebEntity {
        private String gateway;
        private String iPAddress;
        private String macAddress;
    }

}
