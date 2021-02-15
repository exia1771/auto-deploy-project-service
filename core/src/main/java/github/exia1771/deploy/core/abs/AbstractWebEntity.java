package github.exia1771.deploy.core.abs;

import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import github.exia1771.deploy.common.util.Dates;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@JSONType(naming = PropertyNamingStrategy.PascalCase)
public abstract class AbstractWebEntity {

    @JSONField(name = "created")
    private Long createdTimestamp;

    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    public LocalDateTime getCreatedTime() {
        return Dates.toLocalDateTime(createdTimestamp * 1000);
    }

}
