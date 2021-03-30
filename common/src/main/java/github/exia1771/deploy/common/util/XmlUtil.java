package github.exia1771.deploy.common.util;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

public abstract class XmlUtil {

	private static final Configuration CONFIGURATION = new Configuration(Configuration.VERSION_2_3_30);


	public static Template getTemplate(File file) throws IOException {
		CONFIGURATION.setTemplateLoader(new FileTemplateLoader(file.getParentFile()));
		return getTemplate(file.getName());
	}

	public static Template getTemplate(Resource resource) throws IOException {
		File file = resource.getFile();
		if (!file.isDirectory()) {
			file = file.getParentFile();
		}
		CONFIGURATION.setTemplateLoader(new FileTemplateLoader(file));
		return getTemplate(resource.getFile().getName());
	}

	public static Template getTemplate(String s) throws IOException {
		return CONFIGURATION.getTemplate(s);
	}

	public static String getResult(Resource resource, Map<String, Object> variable) throws IOException, TemplateException {
		Template template = getTemplate(resource);
		StringWriter out = new StringWriter();
		template.process(variable, out);
		return out.toString();
	}

}
