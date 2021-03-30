package github.exia1771.deploy.core.service.docker;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public abstract class AbstractRemoteApiParam {


    public static final Set<String> EXCLUDED_FIELDS = new HashSet<>();

    static {
		EXCLUDED_FIELDS.add("filters");
    }

	private Integer limit;
	private String filters;
	private Boolean all;


}
