package github.exia1771.deploy.core.entity;

import lombok.Data;

@Data
public class Port {

    private String ip;
    private String privatePort;
    private String publicPort;
    private String type;

}
