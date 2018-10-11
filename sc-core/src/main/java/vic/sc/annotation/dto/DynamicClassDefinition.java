package vic.sc.annotation.dto;

public class DynamicClassDefinition {
	
	private String pkg;
	
	private String[] imports;
	
	private String body;

	public String getPkg() {
		return pkg;
	}

	public void setPkg(String pkg) {
		this.pkg = pkg;
	}

	public String[] getImports() {
		return imports;
	}

	public void setImports(String[] imports) {
		this.imports = imports;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(pkg + ";");
		if (imports != null && imports.length > 0) {
			for (String s: imports) {
				buffer.append(s + ";");
			}
		}
		buffer.append(body);
		return buffer.toString();
	}
	
}
