package sh.calaba.driver.model;

public class By {
	private String identifier;

	private By() {
	}

	protected By(String identifier) {
		this.identifier = identifier;
	}

	public String getIndentifier() {
		return identifier;
	}

	public class Index extends By {
		public Index(Integer identifier) {
			super(String.valueOf(identifier));
		}
		public Index(String identifier) {
			super(identifier);
		}
	}

	public class Id extends By {
		public Id(String id) {
			super(id);
		}
	}

	public class ContentDescription extends By {
		public ContentDescription(String text) {
			super(text);
		}
	}

	public static By index(Integer index) {
		return new By().new Index(index);
	}
	
	public static By index(String index) {
		return new By().new Index(index);
	}

	public static By text(String text) {
		return new By().new ContentDescription(text);
	}

	public static By id(String id) {
		return new By().new Id(id);
	}
}
